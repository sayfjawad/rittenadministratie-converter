package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("No directory provided to scan!");
            return;
        }
        final String inputDirectoryPath = args[0];
        File directory = new File(inputDirectoryPath);

        if (!directory.isDirectory()) {
            System.out.println("The provided path is not a directory!");
            return;
        }

        File[] xmlFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        if (xmlFiles == null || xmlFiles.length == 0) {
            System.out.println("No XML files found in the directory!");
            return;
        }

        XmlMapper xmlMapper = new XmlMapper();

        for (File xmlFile : xmlFiles) {
            Envelop envelope = xmlMapper.readValue(xmlFile, Envelop.class);

            // Create Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Ritadministratie");

            // Define header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Rit ID");
            header.createCell(1).setCellValue("Datum Tijd Registratie");
            header.createCell(2).setCellValue("Type");
            header.createCell(3).setCellValue("Bestuurder ID");
            header.createCell(4).setCellValue("Kilometerstand Begin");
            header.createCell(5).setCellValue("Kilometerstand Eind");
            header.createCell(6).setCellValue("Prijs");
            header.createCell(7).setCellValue("Latitude Begin");
            header.createCell(8).setCellValue("Longitude Begin");
            header.createCell(9).setCellValue("Latitude Eind");
            header.createCell(10).setCellValue("Longitude Eind");

            int rowIndex = 1;
            for (Rit rit : envelope.getRitadministratie().getVervoerder().getOndernemerskaart().getRit()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rit.getData().getRtVgNr());
                row.createCell(1).setCellValue(rit.getData().getDatTdReg());
                row.createCell(2).setCellValue(rit.getData().getType());
                row.createCell(3).setCellValue(rit.getData().getBestuurder().getChIdNr());
                row.createCell(4).setCellValue(rit.getData().getKmStdBeg());
                row.createCell(5).setCellValue(rit.getData().getKmStdEnd());
                row.createCell(6).setCellValue(rit.getData().getPrijs());
                row.createCell(7).setCellValue(rit.getData().getLocBeg().getLat());
                row.createCell(8).setCellValue(rit.getData().getLocBeg().getLon());
                row.createCell(9).setCellValue(rit.getData().getLocEnd().getLat());
                row.createCell(10).setCellValue(rit.getData().getLocEnd().getLon());
            }

            // Write to Excel file
            String outputFilePath = xmlFile.getAbsolutePath().replace(".xml", "_output.xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
                workbook.write(outputStream);
            }

            workbook.close();
            System.out.println("Converted " + xmlFile.getName() + " to " + outputFilePath);
        }
    }
}