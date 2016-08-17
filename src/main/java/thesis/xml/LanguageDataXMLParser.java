package thesis.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

/**
 * Created by mozt on 15.08.2016.
 */


public class LanguageDataXMLParser {

    public TranslationData translationData;
    public XMLEventReader xmlEventReader;




    public static void main(String[] args) {
        LanguageDataXMLParser ldxp = new LanguageDataXMLParser("D:\\Tez\\translationDataset\\en-tr.tmx");
        int[]maxNumbersOfTranslationData = ldxp.getMaximumWordNumbersOfTranslationData();
        System.out.println("Max EN Sentence Number of Word: " + maxNumbersOfTranslationData[0] + "\n"
                         + "Max TR Sentence Number of Word: " + maxNumbersOfTranslationData[1] + "\n");
        TranslationData tData = ldxp.getNextTranslationData();
        ldxp.getNextTranslationData();

    }

    public LanguageDataXMLParser(String filePath) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public int[] getMaximumWordNumbersOfTranslationData() {
        int INDEX_OF_NUMBER_OF_EN_SENTENCE= 0;
        int INDEX_OF_NUMBER_OF_TR_SENTENCE = 1;
        int[] maxNumbers = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};
        TranslationData tData = null;
        while((tData= getNextTranslationData()) != null){


            String[] enWords = tData.getEnSentence().split(" ");//split en sentence to words
            String[] trWords = tData.getTrSentence().split(" ");//split tr sentence to words

            if(enWords.length > maxNumbers[INDEX_OF_NUMBER_OF_EN_SENTENCE]){
                maxNumbers[INDEX_OF_NUMBER_OF_EN_SENTENCE] = enWords.length;
            }
            if(trWords.length > maxNumbers[INDEX_OF_NUMBER_OF_TR_SENTENCE]){
                maxNumbers[INDEX_OF_NUMBER_OF_TR_SENTENCE] = trWords.length;
            }
        }
        return maxNumbers;
    }

    public TranslationData getNextTranslationData() {
        TranslationData tData = null;
        try {
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();

                    if (startElement.getName().getLocalPart().equals("tu")) {
//                        <tu>
//                            <tuv thesis.xml:lang="en"><seg>Kosovo's privatisation process is under scrutiny</seg></tuv>
//                            <tuv thesis.xml:lang="tr"><seg>Kosova'nın özelleştirme süreci büyüteç altında</seg></tuv>
//                        </tu>
                        tData = getTranslationData(xmlEventReader);
                        break;
                    }
                }
                //if Employee end element is reached, add employee object to list
                else if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return tData;
    }


    private  TranslationData getTranslationData(XMLEventReader xmlEventReader) {
        TranslationData tData = new TranslationData();
        XMLEvent xmlEvent = null;
        byte flags = 0b00;
        try {
            while (xmlEventReader.hasNext()) {
                xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("tuv")) {
                        Iterator<Attribute> attIter = startElement.getAttributes();
                        Attribute langAttr = startElement.getAttributeByName(new QName("http://www.w3.org/XML/1998/namespace", "lang", "thesis/xml"));
                        if (langAttr != null) {
                            //get en sentence
                            if(langAttr.getValue().equals("en")){
                                tData.setEnSentence(getSentence(xmlEventReader));
                                flags = (byte)(flags | 1);
                            }
                            //get tr sentence
                            else if(langAttr.getValue().equals("tr")){
                                tData.setTrSentence(getSentence(xmlEventReader));
                                flags = (byte)(flags | 2);
                            }
                        }
                    }
                    if (flags == 0b11){
                        break;
                    }
                }
            }

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return tData;
    }

    private String getSentence(XMLEventReader xmlEventReader) {
        XMLEvent xmlEvent = null;
        while (xmlEventReader.hasNext()) {
            try {
                xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equals("seg")) {
                        xmlEvent = xmlEventReader.nextEvent();
                        return xmlEvent.asCharacters().getData();
                    }
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
                return "This sentence could not be read.";
            }

        }
        return "This sentence could not be read.";
    }
}
