package nl.multicode.rittenadministratie;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import nl.multicode.rittenadministratie.model.Data;
import nl.multicode.rittenadministratie.model.Envelop;
import nl.multicode.rittenadministratie.model.Rit;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("XML to Excel Converter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);

            JPanel panel = new JPanel();
            JButton selectFolderButton = new JButton("Select Folder");
            JButton startButton = new JButton("Start Conversion");
            JTextField folderPathField = new JTextField(20);
            folderPathField.setEditable(false);

            selectFolderButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    folderPathField.setText(selectedFolder.getAbsolutePath());
                }
            });

            startButton.addActionListener(e -> {
                String directoryPath = folderPathField.getText();
                if (directoryPath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please select a directory first.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    convertXmlFiles(directoryPath);
                    JOptionPane.showMessageDialog(frame, "Conversion completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error during conversion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(selectFolderButton);
            panel.add(folderPathField);
            panel.add(startButton);
            frame.add(panel);

            frame.setVisible(true);
        });
    }

    private static void convertXmlFiles(String inputDirectoryPath) throws IOException {

        final File directory = new File(inputDirectoryPath);
        if (!directory.isDirectory()) {
            throw new IOException("The provided path is not a directory!");
        }

        File[] xmlFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        if (xmlFiles == null || xmlFiles.length == 0) {
            throw new IOException("No XML files found in the directory!");
        }

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.setDefaultUseWrapper(false);

        xmlMapper.getFactory().setXMLTextElementName("");
        Arrays.stream(xmlFiles).forEach(processXmlFile(xmlMapper));
    }

    private static Consumer<File> processXmlFile(XmlMapper xmlMapper) {

        return xmlFile -> {
            try {
                Envelop envelope = xmlMapper.readValue(xmlFile, Envelop.class);
                Workbook workbook = createWorkbook(envelope);
                writeWorkbookToFile(workbook, xmlFile);
                System.out.println("Converted " + xmlFile.getName() + " to " + getOutputFilePath(xmlFile));
            } catch (IOException e) {
                System.err.println("Error processing file " + xmlFile.getName() + ": " + e.getMessage());
            }
        };
    }

    private static Workbook createWorkbook(Envelop envelope) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ritadministratie");
        createHeaderRow(sheet);
        addRitDataToSheet(sheet, envelope);
        return workbook;
    }

    private static void createHeaderRow(Sheet sheet) {

        Row header = sheet.createRow(0);
        Stream.of("Rit ID", "Datum Tijd Registratie", "Type", "Bestuurder ID", "Kilometerstand Begin", "Kilometerstand Eind", "Prijs",
                        "Latitude Begin", "Longitude Begin", "Latitude Eind", "Longitude Eind")
                .forEach(headerName -> header.createCell(header.getPhysicalNumberOfCells()).setCellValue(headerName));
    }

    private static void addRitDataToSheet(Sheet sheet, Envelop envelope) {

        int rowIndex = 1;
        final List<Rit> ritList = envelope.getRitadministratie().getVervoerder().getOndernemerskaart().getRit();
        if (ritList != null) {
            for (Rit rit : ritList) {
                final Data ritData = rit.getData();
                if (ritData != null) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(ritData.getRtVgNr());
                    row.createCell(1).setCellValue(ritData.getDatTdReg());
                    row.createCell(2).setCellValue(ritData.getType());
                    row.createCell(3).setCellValue(ritData.getBestuurder().getChIdNr());
                    row.createCell(4).setCellValue(ritData.getKmStdBeg());
                    row.createCell(5).setCellValue(ritData.getKmStdEnd());
                    row.createCell(6).setCellValue(ritData.getPrijs());
                    if (ritData.getLocBeg() != null) {
                        row.createCell(7).setCellValue(ritData.getLocBeg().getLat());
                        row.createCell(8).setCellValue(ritData.getLocBeg().getLon());
                    }
                    if (ritData.getLocEnd() != null) {
                        row.createCell(9).setCellValue(ritData.getLocEnd().getLat());
                        row.createCell(10).setCellValue(ritData.getLocEnd().getLon());
                    }
                }
            }
        }
    }

    private static void writeWorkbookToFile(Workbook workbook, File xmlFile) throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(getOutputFilePath(xmlFile))) {
            workbook.write(outputStream);
        }
        workbook.close();
    }

    private static String getOutputFilePath(File xmlFile) {

        return xmlFile.getAbsolutePath().replace(".xml", "_output.xlsx");
    }
}