package sample;

import java.io.*;
import java.util.*;

public class spamDetector {

    private Map<String,Integer> TrainHamFreq;
    private Map<String,Integer> TrainSpamFreq;

    public spamDetector() {

        TrainHamFreq = new TreeMap<>();
        TrainSpamFreq = new TreeMap <>();
    }

    public void processFile(File file) throws IOException {
        System.out.println("Processing " + file.getAbsolutePath() + "...");
        if (file.isDirectory()) {
            // process all the files in that directory
            File[] contents = file.listFiles();
            for (File current: contents) {
                processFile(current);
            }
        } else if (file.exists()) {
            // count the words in this file
            Scanner scanner = new Scanner(file);
            Set<String> words = new TreeSet<>();
            scanner.useDelimiter("\\s");//"[\s\.;:\?\!,]");//" \t\n.;,!?-/\\");
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    if (!words.contains(word)) {
                        words.add(word);
                        countWord(word,file.getParent());
                    }
                }
            }
        }
    }

    //public boolean isSpam(){

    //}
    private boolean isWord(String word) {
        String pattern = "^[a-zA-Z]+$";
        if (word.matches(pattern)) {
            return true;
        } else {
            return false;
        }

        // also fine:
        //return word.matches(pattern);
    }

    private void countWord(String word,String filename) {
        String ham= "ham";
        if (filename.toLowerCase().indexOf(ham.toLowerCase()) != -1) {
            if (TrainHamFreq.containsKey(word)) {
                int oldCount = TrainHamFreq.get(word);
                TrainHamFreq.put(word, oldCount + 1);
            } else {
                TrainHamFreq.put(word, 1);
            }
        }
        else{
            if (TrainSpamFreq.containsKey(word)) {
                int oldCount = TrainSpamFreq.get(word);
                TrainSpamFreq.put(word, oldCount + 1);
            } else {
                TrainSpamFreq.put(word, 1);
            }

        }
    }

    public void outputWordCounts(int minCount, File outFile) throws IOException {
        System.out.println("Saving word counts to " + outFile.getAbsolutePath());
        System.out.println("# of words: " + TrainHamFreq.keySet().size());
        if (!outFile.exists()) {
            outFile.createNewFile();
            if (outFile.canWrite()) {
                PrintWriter fileOut = new PrintWriter(outFile);

                Set<String> hamKeys = TrainHamFreq.keySet();
                Iterator<String> keyIterator = hamKeys.iterator();
                Set<String> spamKeys = TrainSpamFreq.keySet();
                Iterator<String> keyIterators = spamKeys.iterator();

                while (keyIterator.hasNext()) {

                    String hamIterator = keyIterator.next();
                    String spamIterator = keyIterators.next();
                    int spamCounts = TrainSpamFreq.get(spamIterator);
                    int hamCounts = TrainHamFreq.get(hamIterator);

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

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java WordCounter <dir> <outfile>");
            System.exit(0);
        }

        spamDetector spamDetector = new spamDetector();
        File dataDir = new File(args[0]);
        File outFile = new File(args[1]);

        try {
            spamDetector.processFile(dataDir);
            spamDetector.outputWordCounts(2, outFile);

        } catch (FileNotFoundException e) {
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
