package yandex.boyko;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFilesTest {

  @Test
  void zipTest() throws Exception {

    ZipFile zf = new ZipFile("src//test//resources//fromTests.zip");

    //PDF
    ZipEntry zipPdfEntry = zf.getEntry("ru.pdf");
    try (InputStream parsePdfFile = zf.getInputStream(zipPdfEntry)) {
      PDF parsed = new PDF(parsePdfFile);
      assertThat(parsed.title).isEqualTo("Microsoft Word - ППубличная лицензия с указанием авторства и с сохранением условий.docx");
      assertThat(parsed.numberOfPages).isEqualTo(6);

    }

    //CSV
    ZipEntry zipCsvEntry = zf.getEntry("elementalCSV.csv");
    try (InputStream parseCsvFile = zf.getInputStream(zipCsvEntry)) {
      CSVReader reader = new CSVReader(new InputStreamReader(parseCsvFile));
      List<String[]> list = reader.readAll();
      assertThat(list)
              .hasSize(3)
              .contains(new String[]{"water", "fire"},
                      new String[]{"sand", "wind"},
                      new String[]{"ocean", "mountains"});
    }

    //XLSX
    ZipEntry zipXlsxEntry = zf.getEntry("thisXlsxFile.xlsx");
    try (InputStream parseXlsxFile = zf.getInputStream(zipXlsxEntry)) {
      XLS parsed = new XLS(parseXlsxFile);
      assertThat(parsed.excel.getSheetAt(0)
              .getRow(2).getCell(1)
              .getStringCellValue()).isEqualTo("Mara");
    }
  }
}

