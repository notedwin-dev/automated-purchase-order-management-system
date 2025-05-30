/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

import PurchaseOrder.PurchaseOrder;
import InventoryManagement.Inventory;
import TextFile_Handler.TextFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;

public class Payment {
    private static final String ItemFile = "src/itemmanagement/Items.txt";
    private static final String POfile = "src/PurchaseOrder/PO.txt";
    private static final String PaymentFile = "src/ReportManagement/Payment.txt";
    private static Map<String, String> quantityMap = new HashMap<>();

    
    private static PurchaseOrder parseLineToPO(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int braceCount = 0;

        for (char c : line.toCharArray()) {
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;

            if (c == ',' && braceCount == 0) {
                parts.add(currentPart.toString().trim());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }

        parts.add(currentPart.toString().trim());

        if (parts.size() < 17) {
            System.out.println("Invalid line (missing fields): " + line);
            return null;
        }

        String PO_ID = parts.get(0);
        String PR_ID = parts.get(1);
        String date = parts.get(2);
        String PM_Name = parts.get(3);
        String PM_ID = parts.get(4);
        String SM_Name = parts.get(5);
        String SM_ID = parts.get(6);
        String expectedDeliveryDate = parts.get(7);
        String SP_Name = parts.get(8);
        String SP_ID = parts.get(9);
        String itemName = parts.get(10);
        String itemCode = parts.get(11);
        String quantityField = parts.get(12);
        String status = parts.get(13);

        String quantityStr = quantityField.replace("{", "").replace("}", "");
        quantityMap.put(PO_ID, quantityStr);
        
        String FM_Name = parts.get(14);
        String FM_ID = parts.get(15);
        String paymentStatus = parts.get(16);

        int quantity = 0;
        try {
            if (quantityStr.contains(",")) {
                quantity = Integer.parseInt(quantityStr.split(",")[0].trim());
            } else {
                quantity = Integer.parseInt(quantityStr.trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity in line: " + line);
        }

        return new PurchaseOrder(PO_ID, PR_ID, date, PM_Name, PM_ID, SM_Name, SM_ID, expectedDeliveryDate,
                                 SP_ID, SP_Name, itemCode, itemName, status, quantity, FM_Name, FM_ID, paymentStatus);
    }
    
    public static List<PurchaseOrder> getAllPurchaseOrders() {
        List<String> lines = TextFile.readFile(POfile);
        List<PurchaseOrder> poList = new ArrayList<>();

        for (String line : lines) {
            PurchaseOrder po = parseLineToPO(line);
            if (po != null) {
                poList.add(po);
            }
        }
        return poList;
    }
    
    public static String getOriginalQuantity(String poId) {
        return quantityMap.getOrDefault(poId, "");
    }
    
    public static double getUnitPrice(String itemCode) {
        List<String> lines = TextFile.readFile(ItemFile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                String code = parts[1].trim();
                String priceStr = parts[5].trim();

                if (code.equals(itemCode)) {
                    try {
                        return Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid unit price: " + priceStr);
                    }
                }
            }
        }
        System.out.println("Item code not found in Items.txt: " + itemCode);
        return 0.0;
    }
    
    public static String getItemCodes(String poId) {
        List<PurchaseOrder> poList = getAllPurchaseOrders();
        for (PurchaseOrder po : poList) {
            if (po.getPO_ID().equals(poId)) {
                return po.getItemCode().replace("{", "").replace("}", "");
            }
        }
        return "";
    }

    
    public static int getQuantityForItem(String poId, String itemCode) {
        String itemCodesStr = getItemCodes(poId);  
        String quantityStr = getOriginalQuantity(poId);
        if (itemCodesStr == null || quantityStr == null) return 0;
        String[] itemCodes = itemCodesStr.split(",");
        String[] quantities = quantityStr.split(",");

        for (int i = 0; i < itemCodes.length; i++) {
            if (itemCodes[i].trim().equals(itemCode)) {
                if (i < quantities.length) {
                    try {
                        return Integer.parseInt(quantities[i].trim());
                    } catch (NumberFormatException e) {
                        return 0;  // invalid number, fallback to 0
                    }
                }
            }
        }
        return 0;  // item code not found
    }
    
    public static boolean updatePaymentStatusToPaid(String poID) {
        List<String> lines = TextFile.readFile(POfile);
        boolean updated = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> fields = smartSplit(line);
            if (fields.size() != 17) {
                continue;
            }
            // Match POID and update payment status
            if (fields.get(0).equals(poID)) {
                fields.set(16, "Paid"); 
                String newLine = String.join(",", fields);
                lines.set(i, newLine);
                updated = true;
                break;
            }
        }
        if (updated) {
            TextFile.overwriteFile(POfile, lines);
            return true;
        }
        return false;
    }
    
    public static boolean updatePaymentFileStatusToPaid(String poID) {
        List<String> lines = TextFile.readFile(PaymentFile);
        boolean updated = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> fields = smartSplit(line);

            if (fields.size() >= 11 && fields.get(1).equals(poID)) {
                fields.set(7, "paid");
                String updatedLine = String.join(",", fields);
                lines.set(i, updatedLine);
                updated = true;
                break;
            }
        }

        if (updated) {
            TextFile.overwriteFile(PaymentFile, lines);
        }

        return updated;
    }
    
    private static List<String> smartSplit(String line) {
        List<String> result = new ArrayList<>();    
        StringBuilder current = new StringBuilder();
        boolean insideBraces = false;

        for (char c : line.toCharArray()) {
            if (c == '{') {
                insideBraces = true;
            } else if (c == '}') {
                insideBraces = false;
            }

            if (c == ',' && !insideBraces) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString()); 
        return result;
    }
    
    public static boolean appendPayment(String poID, List<String> rowData) {
        try {
            String paymentID = generatePaymentID();
            String paymentDate = getCurrentDate();

            String itemCodesStr = getItemCodes(poID); 
            String[] itemCodes = itemCodesStr.split(",");

            List<String> unitPrices = new ArrayList<>();
            for (String code : itemCodes) {
                double price = getUnitPrice(code.trim());
                unitPrices.add(String.valueOf(price));
            }
            String unitPriceStr = "{" + String.join(",", unitPrices) + "}";

            List<String> filteredData = new ArrayList<>();
            filteredData.add(paymentID); 
            filteredData.add(poID);  

            filteredData.add(rowData.get(2));  
            filteredData.add(rowData.get(3));  
            filteredData.add(rowData.get(4)); 
            filteredData.add(rowData.get(5)); 
            filteredData.add(rowData.get(6)); 
            filteredData.add(rowData.get(7)); 
            filteredData.add(unitPriceStr);
            filteredData.add(rowData.get(8)); 
            filteredData.add(paymentDate);     

            // Format and write
            String formattedLine = formatRowForPaymentTxt(filteredData);
            TextFile.appendTo(PaymentFile, formattedLine);

            updatePaymentFileStatusToPaid(poID);
            updatePaymentStatusToPaid(poID);

            return true;
        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static String formatRowForPaymentTxt(List<String> data) {
        StringBuilder sb = new StringBuilder();
        // Grouped fields (wrap with {})
        int[] groupedIndices = {2, 3, 4, 5, 6};

        for (int i = 0; i < data.size(); i++) {
            String value = data.get(i);
            boolean isGrouped = false;

            for (int groupedIndex : groupedIndices) {
                if (i == groupedIndex) {
                    isGrouped = true;
                    break;
                }
            }
            if (isGrouped && !value.startsWith("{")) {
                value = "{" + value + "}";
            }
            sb.append(value);
            if (i < data.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    private static String generatePaymentID() {
        List<String> lines = TextFile.readFile(PaymentFile);
        if (lines.isEmpty()) {
            return "PAY001";
        }

        String lastLine = lines.get(lines.size() - 1);
        String[] parts = lastLine.split(",", -1);

        if (parts.length > 0 && parts[0].startsWith("PAY")) {
            String lastID = parts[0];
            try {
                int num = Integer.parseInt(lastID.substring(3));
                return String.format("PAY%03d", num + 1);
            } catch (NumberFormatException e) {
                // fallback if format is wrong
                return "PAY001";
            }
        }
        return "PAY001";
    }

    private static String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }
    
    public static List<Payment_Encapsulation> getAllPayments() {
        List<String> lines = TextFile.readFile(PaymentFile);
        List<Payment_Encapsulation> paymentList = new ArrayList<>();

        for (String line : lines) {
            Payment_Encapsulation payment = parseLineToPayment(line);
            if (payment != null) {
                paymentList.add(payment);
            }
        }
        return paymentList;
    }
    
    private static Payment_Encapsulation parseLineToPayment(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int braceCount = 0;

        for (char c : line.toCharArray()) {
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;

            if (c == ',' && braceCount == 0) {
                parts.add(currentPart.toString().trim());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }
        parts.add(currentPart.toString().trim());

        // Your payment.txt line has 11 fields expected (adjust if needed)
        if (parts.size() < 11) {
            System.out.println("Invalid line (missing fields): " + line);
            return null;
        }

        // Extract fields based on your payment.txt format:
        String paymentID = parts.get(0);
        String poID = parts.get(1);
        String supplierName = parts.get(2).replace("{", "").replace("}", "");
        String supplierID = parts.get(3).replace("{", "").replace("}", "");
        String itemName = parts.get(4).replace("{", "").replace("}", "");
        String itemCodes = parts.get(5).replace("{", "").replace("}", "");
        String quantities = parts.get(6).replace("{", "").replace("}", "");
        String paymentStatus = parts.get(7);
        String unitPrice = parts.get(8).replace("{", "").replace("}", "");
        String totalAmount = parts.get(9);
        String paymentDate = parts.get(10);

        return new Payment_Encapsulation(paymentID, poID, supplierName, supplierID, itemName,
                                         itemCodes, quantities, unitPrice, totalAmount, paymentStatus,
                                         paymentDate);
    }
}
