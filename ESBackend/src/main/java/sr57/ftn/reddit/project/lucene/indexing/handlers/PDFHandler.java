package sr57.ftn.reddit.project.lucene.indexing.handlers;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import sr57.ftn.reddit.project.elasticmodel.elasticentity.ElasticCommunity;

import java.io.File;
import java.io.IOException;

public class PDFHandler extends DocumentHandler {

    @Override
    public ElasticCommunity getIndexUnit(File file) {
        ElasticCommunity retVal = new ElasticCommunity();
        try {
            PDFParser parser = new PDFParser((RandomAccessRead) new RandomAccessFile(file, "r"));
            parser.parse();
            String text = getText(parser);
            retVal.setPdfDescription(text);

            PDDocument pdf = parser.getPDDocument();
            PDDocumentInformation info = pdf.getDocumentInformation();

            retVal.setFilename(file.getCanonicalPath());

            pdf.close();
        } catch (IOException e) {
            System.out.println("Error while converting document to PDF");
        }

        return retVal;
    }

    @Override
    public String getText(File file) {
        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(parser.getPDDocument());
        } catch (IOException e) {
            System.out.println("Error while converting document to PDF");
        }
        return null;
    }

    public String getText(PDFParser parser) {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(parser.getPDDocument());
        } catch (IOException e) {
            System.out.println("Error while converting document to PDF");
        }
        return null;
    }
}
