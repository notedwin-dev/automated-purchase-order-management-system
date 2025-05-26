/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author nixon
 */

import org.apache.pdfbox.pdmodel.*;
import TextFile_Handler.TextFile;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.File;
import java.io.IOException;     
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JTable;
/**
 *
 * @author nixon
 */
public class PurchaseReport_Management {
    private final String paymentFile = "src/ReportManagement/payment.txt";

    public class PurchaseData {
        public String paymentID, poID, totalAmount, paymentDate;
        private List<Object[]> tableData = new ArrayList<>();

        public PurchaseData() {
            readPaymentFile();
        }

        // ========== READ METHOD FOR PAYMENT FILE ========== //
        private void readPaymentFile() {
            List<String> lines = TextFile.readFile(paymentFile);
            boolean isFirstLine = true;

            for (String line : lines) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] data = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (data.length >= 11) {
                    String paymentID = data[0].trim();
                    String poID = data[1].trim();

                    String[] companies = data[2].replace("{", "").replace("}", "").split(",");
                    String[] supplierIDs = data[3].replace("{", "").replace("}", "").split(",");
                    String[] itemNames = data[4].replace("{", "").replace("}", "").split(",");
                    String[] itemCodes = data[5].replace("{", "").replace("}", "").split(",");
                    String[] quantities = data[6].replace("{", "").replace("}", "").split(",");
                    String[] unitPrices = data[8].replace("{", "").replace("}", "").split(",");

                    String totalAmount = data[9].trim();
                    String paymentDateStr = data[10].trim();
                    
                    String itemCodeText = String.join("\n", itemCodes).trim();
                    String itemNameText = String.join("\n", itemNames).trim();
                    String companyText = String.join("\n", companies).trim();
                    String supplierIDText = String.join("\n", supplierIDs).trim();
                    String quantityText = String.join("\n", quantities).trim();
                    String unitPriceText = String.join("\n", unitPrices).trim();

                    tableData.add( new Object[] {
                        paymentID,
                        poID,
                        itemCodeText,
                        itemNameText,
                        companyText,
                        supplierIDText,
                        quantityText,
                        unitPriceText,
                        totalAmount,
                        paymentDateStr
                    });
                }
            }
        }
        
        public List<Object[]> getTableData() {
            return tableData;  // where tableData is a List<Object[]> field
        }
    }
    
    // ========== PUBLIC METHOD TO ACCESS PR TABLE DATA ========== //
    public List<Object[]> getTableData() {
        PurchaseData prData = new PurchaseData();
        return prData.getTableData();
    }


    // ========== METHOD FOR EXPORTING AS PDF ========== //
    public void exportWeeklyReportToPDF(Date selectedDate, JTable table, String folderPath) {
        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No data available for the selected week to export.");
            return;
        }

        File fileToSave = new File(folderPath);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDFont font = PDType1Font.HELVETICA;
            float fontSize = 7.5f;
            float leading = 1.5f * fontSize;
            float margin = 50f;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

            String[] headers = {"Payment ID", "PO ID", "Item Code", "Item Name", "Company",
                                "Supplier ID", "Quantity", "Unit Price", "Total Amount", "Payment Date"};
            float[] colWidths = {50, 40, 50, 50, 90, 40, 40, 40, 50, 40};
            int numCols = colWidths.length;

            float yPosition = yStart;
            float rowHeight = 40f;
            float cellMargin = 3f;

            // ----- Title Section: Weekly Purchase Report Header ----- //
            contentStream.beginText();
            contentStream.setFont(font, 14);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Weekly Purchase Report");
            contentStream.endText();
            yPosition -= 18;

            // ----- Date Range ----- //
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(margin, yPosition);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            contentStream.showText("Week: " + sdf.format(selectedDate));
            contentStream.endText();
            yPosition -= 15;

            // ----- Generated On Date (current system date) ----- //
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated on: " + sdf.format(new Date()));
            contentStream.endText();
            yPosition -= 25;

            // Header
            contentStream.setFont(font, fontSize);
            for (int i = 0; i < numCols; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + getColumnX(colWidths, i) + cellMargin, yPosition);
                contentStream.showText(headers[i]);
                contentStream.endText();
            }

            // Draw line below header
            yPosition -= 2; // Slight adjustment to avoid overlapping text
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.setLineWidth(0.8f);
            contentStream.stroke();
            
            yPosition -= 18; 

            // Table rows
            for (int row = 0; row < table.getRowCount(); row++) {
                float rowY = yPosition;

                for (int col = 0; col < numCols; col++) {
                    Object valueObj = table.getValueAt(row, col);
                    String value = valueObj != null ? valueObj.toString() : "";

                    List<String> wrapped = wrapText(value, font, fontSize, colWidths[col] - 2 * cellMargin);

                    float textX = margin + getColumnX(colWidths, col) + cellMargin;
                    float textY = rowY;

                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(textX, textY);

                    for (String line : wrapped) {
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -leading);
                    }

                    contentStream.endText();
                }

                yPosition -= rowHeight;

                // Handle page break if needed
                if (yPosition < margin + rowHeight) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.LETTER);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = yStart;
                }
            }

            contentStream.close();
            document.save(fileToSave);
            JOptionPane.showMessageDialog(null, "PDF exported successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while exporting the PDF.");
        }
    }

    
    // ========== PUBLIC METHOD TO WRAP THE TEXT (to avoid text overlapping) ========== //
    public List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> wrappedLines = new ArrayList<>();
        String[] rawLines = text.split("\n");

        for (String rawLine : rawLines) {
            StringBuilder line = new StringBuilder();
            for (String word : rawLine.split(" ")) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                float size = font.getStringWidth(testLine) / 1000 * fontSize;
                if (size > maxWidth) {
                    wrappedLines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(testLine);
                }
            }
            wrappedLines.add(line.toString());
        }

        return wrappedLines;
    }

    
    // ========== GET COLUMN X METHOD ========== //
    private float getColumnX(float[] colWidths, int colIndex) {
        float x = 0;
        for (int i = 0; i < colIndex; i++) {
            x += colWidths[i];
        }
        return x;
    }


}
