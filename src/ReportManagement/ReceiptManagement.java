/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

import TextFile_Handler.TextFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author user
 */
public class ReceiptManagement {
    private static final String paymentFile = "src/Reportmanagement/Payment.txt";
    private static final String supplierFile = "src/SupplierManagement/Suppliers.txt";
    
    public String generateReceiptID(String poid){
        return "R"+poid;
    }
    
    public List<Payment_Encapsulation> getPaymentsByPO(String poID) {
        List<Payment_Encapsulation> paymentList = new ArrayList<>();
        List<String> lines = TextFile.readFile(paymentFile);

        for (String line : lines) {
            // Use improved splitting method here if your data fields contain commas inside braces
            String[] data = splitOutsideBraces(line);

            // Check if line has at least 11 parts and POID matches
            if (data.length >= 11 && data[1].trim().equalsIgnoreCase(poID.trim())) {
                // Parse multi-value fields by removing braces and splitting
                String[] supplierNames = data[2].replaceAll("[{}]", "").split("\\s*,\\s*");
                String[] supplierIDs = data[3].replaceAll("[{}]", "").split("\\s*,\\s*");
                String[] itemNames = data[4].replaceAll("[{}]", "").split("\\s*,\\s*");
                String[] itemCodes = data[5].replaceAll("[{}]", "").split("\\s*,\\s*");
                String[] quantities = data[6].replaceAll("[{}]", "").split("\\s*,\\s*");
                String[] unitPrices = data[8].replaceAll("[{}]", "").split("\\s*,\\s*");

                String joinedSupplierNames = String.join(", ", supplierNames);
                String joinedSupplierIDs = String.join(", ", supplierIDs);
                String joinedItemNames = String.join(", ", itemNames);
                String joinedItemCodes = String.join(", ", itemCodes);
                String joinedQuantities = String.join(", ", quantities);
                String joinedUnitPrices = String.join(", ", unitPrices);

                int itemCount = supplierNames.length;
                for (int i = 0; i < itemCount; i++) {
                    paymentList.add(new Payment_Encapsulation(
                        data[0].trim(),    // paymentID
                        data[1].trim(),    // poID
                        i < supplierNames.length ? supplierNames[i].trim() : "",
                        i < supplierIDs.length ? supplierIDs[i].trim() : "",
                        i < itemNames.length ? itemNames[i].trim() : "",
                        i < itemCodes.length ? itemCodes[i].trim() : "",
                        i < quantities.length ? quantities[i].trim() : "0",
                        i < unitPrices.length ? unitPrices[i].trim() : "0",
                        data[9].trim(),    // totalAmount (same for all items)
                        data[7].trim(),    // paymentStatus
                        data[10].trim()    // paymentDate
                    ));
                }
            }
        }
        return paymentList;
    }

    // Helper function to split CSV line on commas not inside braces, if needed:
    private String[] splitOutsideBraces(String line) {
        List<String> parts = new ArrayList<>();
        int braceLevel = 0;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '{') braceLevel++;
            else if (c == '}') braceLevel--;

            if (c == ',' && braceLevel == 0) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());
        return parts.toArray(new String[0]);
    }



    public List<String> getSuppliersInPO(String POID){
        List<String> result = new ArrayList<>();
        List<Payment_Encapsulation> payments = getPaymentsByPO(POID);

        System.out.println("--- Suppliers in payments for PO " + POID + " ---");
        for(Payment_Encapsulation p : payments){
            System.out.println("Supplier ID: " + p.getSupplierID() + ", Supplier Name: " + p.getSupplierName());
            String supplierID = p.getSupplierID().trim();
            if(!result.contains(supplierID)){
                result.add(supplierID);
            }
        }

        return result;
    }



    public String getSupplierNameByID(String id){
        for(String line : TextFile.readFile(supplierFile)){
            String[] parts = line.split(",");
            if(parts.length > 1 && parts[0].trim().equals(id.trim())){
                return parts[1].trim(); // supplier name
            }
        }
        return "";
    }

    public String getSupplierAddressByID(String id){
        for(String line : TextFile.readFile(supplierFile)){
            String[] parts = line.split(",");
            if(parts.length > 4 && parts[0].trim().equals(id.trim())){
                return parts[4].trim(); // supplier address
            }
        }
        return "";
    }

    
    public List<Object[]> getReceiptItemRows(String poID, String id) {
        List<Object[]> rows = new ArrayList<>();

        for (Payment_Encapsulation p : getPaymentsByPO(poID)) {
            if (p.getSupplierID().equals(id)) {
                String item = p.getItemName().trim();
                String qtyStr = p.getQuantities().trim();
                String priceStr = p.getUnitPrice().trim();
                System.out.println("Processing item: " + item + ", qty: " + qtyStr + ", price: " + priceStr); // DEBUG
                try {
                    int qty = Integer.parseInt(qtyStr);
                    double price = Double.parseDouble(priceStr);
                    double amount = qty * price;

                    rows.add(new Object[] { item, qty, price, amount });
                } catch (NumberFormatException ex) {
                    System.err.println("Error parsing quantity or price for item: " + item + ", qty: " + qtyStr + ", price: " + priceStr);
                    rows.add(new Object[] { item, 0, 0.0, "RM 0.00" });
                }
            }
        }

        return rows;
    }
    
    public double calculateTotal(List<Object[]> rows){
        double totalAmount = 0.0;
        for(Object[] row : rows){
            try {
                int qty = Integer.parseInt(row[1].toString());
                double unitPrice = Double.parseDouble(row[2].toString());
                totalAmount += qty * unitPrice;
            } catch (NumberFormatException e) {
                System.err.println("Error calculating total: " + e.getMessage());
            }
        }
        return totalAmount;
    }

}
