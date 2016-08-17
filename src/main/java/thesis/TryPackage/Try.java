package thesis.TryPackage;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import thesis.util.ThesisUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by mozt on 23.05.2016.
 */
public class Try {


    public static void main(String[] args) {
        //create work vector model.
        File gModel = new File(ThesisUtil.WORD_VECTORS_PATH);
        WordVectors wordVectors = null;
        try {
            wordVectors = WordVectorSerializer.loadGoogleModel(gModel, true);

            double[] vector = wordVectors.getWordVector("özelleştirilmesine");
            System.out.println("vector of özelliştirilmesine:");
            for (int i = 0; i < vector.length; i++) {
                System.out.print(vector[i] + ",");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}