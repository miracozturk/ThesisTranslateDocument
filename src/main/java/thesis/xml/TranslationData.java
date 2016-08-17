package thesis.xml;

/**
 * Created by mozt on 16.08.2016.
 */
public class TranslationData {
    String trSentence;
    String enSentence;

    public String getTrSentence() {
        return trSentence;
    }

    public void setTrSentence(String trSentence) {
        this.trSentence = trSentence;
    }

    public String getEnSentence() {
        return enSentence;
    }

    public void setEnSentence(String enSentence) {
        this.enSentence = enSentence;
    }

    public int getTrSentenceWordNumber(){
        return trSentence.split(" ").length;
    }

    public int getEnSentenceWordNumber(){
        return enSentence.split(" ").length;
    }
}
