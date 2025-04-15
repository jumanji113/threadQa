package tests.Junit;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.As;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$x;

public class SelenideTestFile {
    @Test
    public void readTest() throws IOException {
        File file = new File("src/test/resources/test.pdf");
        PDF pdf = new PDF(file);
        String textPdf = pdf.text;
        Assertions.assertTrue(textPdf.contains("AC-Tester"));
    }

    @Test
    public void readBrowserFile() throws IOException {
        Selenide.open("http://pdf995.com/samples/");
        File file = $x("//a[@href='/samples/pdf.pdf']").download();
        PDF pdf = new PDF(file);
        String textPdf = pdf.text;
        String author = pdf.author;
        Assertions.assertTrue(textPdf.contains("The pdf995"));
        Assertions.assertTrue(author.contains("Software 995"));
    }
}
