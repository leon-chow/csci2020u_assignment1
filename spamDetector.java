package sample;

        import java.io.*;
        import java.util.*;

public class spamDetector {
    double Accuracy;
    double Precision;
    public double numberOfSpamFiles = 0;
    public double numberOfHamFiles = 0;
    private Map<String,Integer> TrainHamFreq;
    private Map<String,Integer> TrainSpamFreq;

    public spamDetector() {
        TrainHamFreq = new TreeMap <>();
        TrainSpamFreq = new TreeMap <>();
    }

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

    public void spamProbability() {
        Map<String, Double> spamProb = new TreeMap<>();
        Map<String, Double> hamProb = new TreeMap<>();
        Map<String, Double> fileIsSpamProb = new TreeMap<>();

        for (Map.Entry<String, Integer> entry : TrainSpamFreq.entrySet()) {
            spamProb.put(entry.getKey(), (double)entry.getValue()/numberOfSpamFiles);
        }

        for (Map.Entry<String, Integer> entry: TrainHamFreq.entrySet()) {
            hamProb.put(entry.getKey(), (double)entry.getValue()/numberOfHamFiles);
        }

        for (Map.Entry<String, Double> entry: spamProb.entrySet()) {
            if (hamProb.containsKey(entry.getKey())) {
                fileIsSpamProb.put(entry.getKey(), entry.getValue()/(entry.getValue() + hamProb.get(entry.getKey())));
            } else {
                fileIsSpamProb.put(entry.getKey(), entry.getValue()/(entry.getValue()));
            }
        }
    }
            //public Map word_spam_probability(){ return SpamFreq ;}

    private boolean isWord(String word) {
        String pattern = "^[a-zA-Z]+$";
        if (word.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    private void PrecisionAndAccuracy(){
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


    public void Test(){
   /*
    int value;
    for(File current: contents){
    value +=(Math.log(//p(wsw) - math.log(pr(s|w))))
    }
    */
        double psf;
        double x = 0;
        psf = 1/ (1+ Math.pow(Math.E,x));
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
        } else {
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
                Iterator<String> hamKeyIterator = hamKeys.iterator();
                Set<String> spamKeys = TrainSpamFreq.keySet();
                Iterator<String> spamKeyIterators = spamKeys.iterator();

                while (hamKeyIterator.hasNext()) {

                    String hamIterator = hamKeyIterator.next();
                    String spamIterator = spamKeyIterators.next();
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
            spamDetector.trainSpamDetector(dataDir);
            spamDetector.outputWordCounts(1, outFile);
            spamDetector.spamProbability();
        } catch (FileNotFoundException e) {
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
