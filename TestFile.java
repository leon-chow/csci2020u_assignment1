package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.DecimalFormat;

public class TestFile {
    private SimpleStringProperty filename;
    private SimpleDoubleProperty spamProbability;
    private SimpleStringProperty actualClass;
    private SimpleStringProperty spamProbabilityRounded;

    public TestFile(String filename,
                    double spamProbability,
                    String actualClass){
        this.filename = new SimpleStringProperty(filename);
        this.spamProbability = new SimpleDoubleProperty(spamProbability);
        this.actualClass = new SimpleStringProperty(actualClass);
    }

    public String getFilename(){
        return this.filename.get();
    }

//    public double getSpamProbability(){
//        return this.spamProbability.get();
//    }

    //This function returns the rounded string version tableview to be readable.
    public String getSpamProbability(){
        //return this.spamProbability.get();
        return getSpamProbRounded();
    }

    public double getSpamProbabilityNotRounded(){
        return this.spamProbability.get();
    }

    public String getSpamProbRounded(){
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability.get());
    }

    public String getActualClass() {
        return actualClass.get();
    }

    public void setFilename(String value){
        this.filename = new SimpleStringProperty(value);
    }

    public void setSpamProbability(double val){
        this.spamProbability = new SimpleDoubleProperty(val);
    }

    public void setActualClass(String value){
        this.actualClass = new SimpleStringProperty(value);
    }
}