package sample;

import java.io.File;

public class Spam {
    private String File;
    private String Ham;
    private float SpamProb;


    public Spam(String Ham, String File, float SpamProb) {} // Default constructor

    public Spam(String File, String Ham, int SpamProb ) {
        this.File = File;
        this.Ham = Ham;
        this.SpamProb = SpamProb;
    }

    // Constructor for button
    public Spam(String sid, String File, String Ham, float  SpamProb) {
        this.File = File;
        this.Ham = Ham;
        this. SpamProb =  SpamProb;

    }

    public String getFile() {
        return this.File;
    }

    public String getHam() {
        return this.Ham;
    }

    public float getSpamProb() {
        return this. SpamProb;
    }




}
