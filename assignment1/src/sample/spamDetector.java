//Leon Chow 100617197, Bevan Donbosco(100618701), (100637553)
//we were able to  spam probabilities of each word, counting the words,
//counting the number of ham and spam files, we generated the table in the
//main.java
//However, we did not test the probabilities of each file and classify programs
//as ham or spam
package sample;

//import following libraries
import java.io.*;
import java.util.*;
import java.lang.Math;

public class spamDetector {
    //variables and maps definition
    double Accuracy;
    double Precision;
    public double numberOfSpamFiles = 0;
    public double numberOfHamFiles = 0;
    private Map<String,Integer> TrainHamFreq;
    private Map<String,Integer> TrainSpamFreq;
    private Map<String, Double> fileIsSpamProb;

    public spamDetector() {
        //initializing maps
        TrainHamFreq = new TreeMap <>();
        TrainSpamFreq = new TreeMap <>();
        fileIsSpamProb = new TreeMap <>();
    }

    //goes through each of the file in the folder, incrementing ham or spam file counter
    public void trainSpamDetector(File file) throws IOException {
        System.out.println("Processing " + file.getAbsolutePath() + "...");
        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            for (File current: contents) {
                if (current.getParentFile().getName().equals("spam")) {
                    numberOfSpamFiles += 1;
                } else if (current.getParentFile().getName().equals("ham")) {
                    numberOfHamFiles += 1;
                }
                trainSpamDetector(current);
            }
            //checks to see if the file exists
        } else if (file.exists()) {
            // count the words in this file
            Scanner scanner = new Scanner(file);
            Set<String> words = new TreeSet<>();
            scanner.useDelimiter("\\s");//"[\s\.;:\?\!,]");//" \t\n.;,!?-/\\");
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    //unique entries in the file and adds it to the map once
                    if (!words.contains(word)) {
                        words.add(word);
                        countWord(word,file.getParent());
                    }
                }
            }
        }
    }

    //checks to see if the combination of characters is a word
    private boolean isWord(String word) {
        String pattern = "^[a-zA-Z]+$";
        if (word.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    //counts the number of files the word appears in, seperating the words FileNotFoundException
    //ham or spam categories
    private void countWord(String word,String filename) {
        String ham= "ham";
        if (filename.toLowerCase().indexOf(ham.toLowerCase()) != -1) {
            if (TrainHamFreq.containsKey(word)) {
                int oldCount = TrainHamFreq.get(word);
                TrainHamFreq.put(word, oldCount + 1);
            } else {
                TrainHamFreq.put(word, 1);
            }
        } else {
            if (TrainSpamFreq.containsKey(word)) {
                int oldCount = TrainSpamFreq.get(word);
                TrainSpamFreq.put(word, oldCount + 1);
            } else {
                TrainSpamFreq.put(word, 1);
            }
        }
    }

    //displays the word count to the user and the outfile
    public void outputWordCounts(int minCount, File outFile) throws IOException {
        System.out.println("Saving word counts to " + outFile.getAbsolutePath());
        System.out.println("# of words: " + TrainHamFreq.keySet().size());
        if (!outFile.exists()) {
            outFile.createNewFile();
            if (outFile.canWrite()) {
                PrintWriter fileOut = new PrintWriter(outFile);

                //initializing the maps
                Set<String> hamKeys = TrainHamFreq.keySet();
                Iterator<String> hamKeyIterator = hamKeys.iterator();

                Set<String> spamKeys = TrainSpamFreq.keySet();
                Iterator<String> spamKeyIterators = spamKeys.iterator();

                //iterates through each word and displays the word count and word in another file
                while (hamKeyIterator.hasNext()) {
                    String hamIterator = hamKeyIterator.next();
                    String spamIterator = spamKeyIterators.next();
                    int spamCounts = TrainSpamFreq.get(spamIterator);
                    int hamCounts = TrainHamFreq.get(hamIterator);

                    //displays the word and the number of files it appears in
                    if (hamCounts >= minCount) {
                        fileOut.println("ham file "+ hamIterator + ": " + hamCounts);
                        fileOut.println("spam file "+ spamIterator + ": "+ spamCounts);
                    }
                }
                fileOut.close();
            } else {
                System.err.println("Error:  Cannot write to file: " + outFile.getAbsolutePath());
            }
        } else {
            System.err.println("Error:  File already exists: " + outFile.getAbsolutePath());
            System.out.println("outFile.exists(): " + outFile.exists());
            System.out.println("outFile.canWrite(): " + outFile.canWrite());
        }
    }

    //checks the probability of the file being spam if given the word
    public Map spamProbability() {
        //initializing the maps
        Map<String, Double> spamProb = new TreeMap<>();
        Map<String, Double> hamProb = new TreeMap<>();
        Map<String, Double> fileIsSpamProb = new TreeMap<>();

        //map to check the spam probability of the word
        for (Map.Entry<String, Integer> entry : TrainSpamFreq.entrySet()) {
            spamProb.put(entry.getKey(), (double)entry.getValue()/numberOfSpamFiles);
        }

        //map to check the ham probability of the word
        for (Map.Entry<String, Integer> entry: TrainHamFreq.entrySet()) {
            hamProb.put(entry.getKey(), (double)entry.getValue()/numberOfHamFiles);
        }

        //map to check if the file is probability given that it contains the word
        for (Map.Entry<String, Double> entry: spamProb.entrySet()) {
            if (hamProb.containsKey(entry.getKey())) {
                fileIsSpamProb.put(entry.getKey(), entry.getValue()/(entry.getValue() + hamProb.get(entry.getKey())));
            } else {
                fileIsSpamProb.put(entry.getKey(), entry.getValue()/(entry.getValue()));
            }
        }
        //returns the map to be used in another function
        return fileIsSpamProb;
    }

    //ths function is suposed to check the precision and accuracy of the program
    private void precisionAndAccuracy(){
        double numCorrectGuesses = 0;
        double numGuesses = 0;
        double numTrueNegatives= 0;
        double numFiles = TrainHamFreq.size() + TrainSpamFreq.size() ;
        double numTruePositives= 0;
        double numFalsePositives= 0;

    //Calculate for True Postivies

        this.Accuracy = (numTruePositives + numTrueNegatives)/ numFiles;
        this.Precision = numTruePositives / numFalsePositives + numTruePositives;
    }

    //this function is supposed to spam detect the test ham and spam files
   /*public void test(File file, Map<String, Double> fileIsSpamProb) throws IOException {
        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            for (File current: contents) {
                test(current, fileIsSpamProb);
            }
        } else if (file.exists()) {
            // count the words in this file
            Scanner scanner = new Scanner(file);
            //Map<String, Integer> wordProbability new TreeSet<>();
            scanner.useDelimiter("\\s");//"[\s\.;:\?\!,]");//" \t\n.;,!?-/\\");
            for (String currentWord: testWord)
                String testWord = scanner.next();
                Double n = 0.0;
                if (this.fileIsSpamProb.containsKey(testWord)) {
                    n += (Math.log(1.0 - this.fileIsSpamProb.get(testWord) - Math.log(this.fileIsSpamProb.get(testWord))));
                }
            }
        }
    } */

    //main function
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java WordCounter <dir> <outfile>");
            System.exit(0);
        }

        //new spamDetector object
        spamDetector spamDetector = new spamDetector();
        File dataDir = new File(args[0]);
        File outFile = new File(args[1]);

        try {
            //generates a map and trains the file to detect spam
            spamDetector.trainSpamDetector(dataDir);
            spamDetector.spamProbability();
            Map<String, Double> fileIsSpamProb = spamDetector.spamProbability();
            spamDetector.outputWordCounts(1, outFile);
            //spamDetector.test(testDir, fileIsSpamProb);
        //error detector
        } catch (FileNotFoundException e) {
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
