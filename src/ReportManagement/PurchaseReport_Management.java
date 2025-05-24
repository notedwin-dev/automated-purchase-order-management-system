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

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import javax.swing.table.TableModel;

import java.io.File;
import java.io.IOException;     
import javax.swing.JOptionPane;
/**
 *
 * @author nixon
 */
public class PurchaseReport_Management {
    private final String paymentFile = "src/ReportManagement/payment.txt";

    public class PurchaseData {
        public String paymentID, poID, totalAmount, paymentDate;
        public List<PurchaseReportItem> items = new ArrayList<>();
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
}
