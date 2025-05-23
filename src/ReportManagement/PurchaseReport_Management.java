/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author nixon
 */
import InventoryManagement.TextFile;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.*;
import java.util.Arrays;
import org.apache.pdfbox.pdmodel.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
        
/**
 *
 * @author nixon
 */
public class PurchaseReport_Management {

    private List<Object[]> tableData = new ArrayList<>();
    private static final String paymentFile = "src/ReportManagement/Payment.txt";

    public PurchaseReport_Management() {
        readPaymentFile();
    }

    private void readPaymentFile() {
        List<String> lines = TextFile.readFile(paymentFile);
        boolean isFirstLine = true;

        for (String line : lines) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            System.out.println("Reading line: " + line); // ✅ Debug

            String[] data = line.split(",(?![^\\{]*\\})");

            System.out.println("Parsed data length: " + data.length); // ✅ Debug

            if (data.length >= 11) {
                String paymentID = data[0].trim();
                String poID = data[1].trim();
                String[] companies = data[2].replace("{", "").replace("}", "").split(",");
                String[] supplierIDs = data[3].replace("{", "").replace("}", "").split(",");
                String[] itemNames = data[4].replace("{", "").replace("}", "").split(",");
                String[] itemCodes = data[5].replace("{", "").replace("}", "").split(",");
                String[] quantities = data[6].replace("{", "").replace("}", "").split(",");
                String status = data[7].trim();
                String[] prices = data[8].replace("{", "").replace("}", "").split(",");
                String totalAmount = data[9].trim();
                String paymentDate = data[10].trim();

                String companyText = String.join("\n", companies).trim();
                String supplierIDText = String.join("\n", supplierIDs).trim();
                String itemNameText = String.join("\n", itemNames).trim();
                String itemCodeText = String.join("\n", itemCodes).trim();
                String quantityText = String.join("\n", quantities).trim();

                tableData.add(new Object[]{
                    paymentID, poID, "", companyText, supplierIDText,
                    itemNameText, itemCodeText, quantityText,
                    status, "", totalAmount, paymentDate
                });

                System.out.println("✅ Added: " + paymentID); // ✅ Debug
            } else {
                System.out.println("❌ Skipped due to incorrect fields: " + data.length);
            }
        }
    }



    public List<Object[]> getTableData() {
        return tableData;
    }
  
    //==================================== GENERATE PDF ====================================//
    
}
