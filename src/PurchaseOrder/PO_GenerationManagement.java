/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
/**
 *
 * @author user
 */
import InventoryManagement.Inventory;
import TextFile_Handler.TextFile;
import java.util.HashMap;
import java.util.Map;
public class PO_GenerationManagement {
    private static final String ItemFile = "src/itemManagement/Items.txt"; 
    private static final String PRfile = "src/PurchaseRequisition/PR.txt" ;
    public static final String POfile = "src/PurchaseOrder/PO.txt";
    private static Map<String, String> quantityMap = new HashMap<>();

    
    public class PRData{
        public String PR_ID, date, SM_Name, SM_ID, expectedDeliveryDate;
        public List<PurchaseOrderItem> items = new ArrayList<>();
        private List<Object[]> tableData = new ArrayList<>();

        // ========== Read the PR_List text file ========== //
        public PRData() {
            readPR_List();
        }
        
        private void readPR_List() {
            List<String> lines = TextFile.readFile(PRfile);
            boolean isFirstLine = true;
            int rowNo = 1;

            for (String line : lines) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (data.length == 8) {
                    String prID = data[0].trim();
                    String date = data[1].trim();
                    String smName = data[2].trim();
                    String smID = data[3].trim();
                    String[] itemCodes = data[4].replace("{", "").replace("}", "").split(",");
                    String[] quantities = data[5].replace("{", "").replace("}", "").split(",");
                    String expectedDate = data[6].trim();
                    String status = data[7].trim();

                    String itemCodeText = Arrays.stream(itemCodes).map(String::trim).collect(Collectors.joining("\n"));
                    String quantityText = Arrays.stream(quantities).map(String::trim).collect(Collectors.joining("\n"));

                    tableData.add(new Object[]{
                        rowNo++, prID, date, smName, smID, itemCodeText, quantityText, expectedDate, status
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
        PRData prData = new PRData();
        return prData.getTableData();
    }
    
    public Inventory getItemDetailsByCode(String itemCode) {
        List<String> lines = TextFile.readFile(ItemFile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 8 && parts[1].trim().equals(itemCode)) {
                String itemName = parts[0].trim();
                String code = parts[1].trim();
                String supplierID = parts[2].trim();
                String supplierName = parts[3].trim();
                int quantity = Integer.parseInt(parts[4].trim());
                double unitPrice = Double.parseDouble(parts[5].trim());
                double retailPrice = Double.parseDouble(parts[6].trim());
                boolean deliveryStatus = Boolean.parseBoolean(parts[7].trim());

                return new Inventory(itemName, code, supplierID, supplierName, quantity, unitPrice, retailPrice, deliveryStatus);
            }
        }
        return null; 
    }

    public String generatePO_ID() {
        int maxID = 0;
        List<String> lines = TextFile.readFile(POfile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].startsWith("PO")) {
                try {
                    int id = Integer.parseInt(parts[0].substring(2)); // skip "PO"
                    if (id > maxID) maxID = id;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid PO ID: " + parts[0]);
                }
            }
        }
        return "PO" + String.format("%04d", maxID + 1);
    }

    
    public List<String> getSupplierID(){
        List<String> supplierID = new ArrayList<>();
        for(String line : TextFile.readFile(ItemFile)){
            String[] data = line.split(",\\s*");
            if(data.length >= 5 && !supplierID.contains(data[2])){
                supplierID.add(data[2]);
            }
        }
        return supplierID;
    }
    
    public List<String> getItemCodeFromSupplier(String supplierID){
        List<String> itemCode = new ArrayList<>();
        for(String line : TextFile.readFile(ItemFile)){
            String[] data = line.split(",\\s*");
            if(data.length >= 5 && data[2].equals(supplierID)){
                itemCode.add(data[1]);
            }
        }
        return itemCode;
    }
    
    public Inventory getInventoryFromItemCode(String itemCode){
        for(String line : TextFile.readFile(ItemFile)){
            String[] data = line.split(",\\s*");
            if(data.length >= 5 && data[1].equals(itemCode)){
                return new Inventory(data[0], data[1], data[2], data[3], 0, 0, 0, false);
            }
        }
        return null;
    }
    
    public void saveToPOtxt(String PO_ID, String PR_ID, String date, String PM_Name, String PM_ID,String SM_Name, String SM_ID, String expectedDeliveryDate, List<PurchaseOrderItem> items) {
        StringBuilder supplierName = new StringBuilder();
        StringBuilder supplierID = new StringBuilder();
        StringBuilder itemName = new StringBuilder();
        StringBuilder itemCode = new StringBuilder();
        StringBuilder quantity = new StringBuilder();
        
        for(int i = 0; i < items.size(); i++){
            PurchaseOrderItem item = items.get(i);
            supplierName.append(item.getSupplierName());
            supplierID.append(item.getSupplierID());
            itemName.append(item.getItemName());
            itemCode.append(item.getItemCode());
            quantity.append(item.getQuantity());
            
            if(i < items.size() -1 ){
                supplierName.append(",");
                supplierID.append(",");
                itemName.append(",");
                itemCode.append(",");
                quantity.append(",");
            }
        }
        
        String fmName = "FmName";
        String fmID = "FmID";
        String paymentStatus = "Unpaid";
        
        String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,{%s},{%s},{%s},{%s},{%s},Pending,%s,%s,%s",
                PO_ID,PR_ID,date,
                PM_Name,PM_ID,
                SM_Name,SM_ID,
                expectedDeliveryDate,
                supplierName.toString(),
                supplierID.toString(),
                itemName.toString(),
                itemCode.toString(),
                quantity.toString(),
                fmName,fmID,paymentStatus);
        
        TextFile.appendTo(POfile, line);
    }

    
    public void updatePR(String PR_ID, List<PurchaseOrderItem> items, String expectedDeliveryDate){
        List<String> lines = TextFile.readFile(PRfile);
        
        for(String line : lines){
            if(line.startsWith(PR_ID + ",")){
                int itemStart = line.indexOf('{');
                int itemEnd = line.indexOf('}');
                int qtyStart = line.indexOf('{', itemEnd);
                int qtyEnd = line.indexOf('}', qtyStart);
                
                String beforeItems = line.substring(0, itemStart).trim();
                String afterQuantities = line.substring(qtyEnd + 1 ).trim();
                
                StringBuilder itemBuilder = new StringBuilder("{");
                StringBuilder quantityBuilder = new StringBuilder("{");
                
                for(int i = 0; i < items.size(); i++){
                    itemBuilder.append(items.get(i).getItemCode());
                    quantityBuilder.append(items.get(i).getQuantity());
                    if(i < items.size() - 1){
                        itemBuilder.append(", ");
                        quantityBuilder.append(", ");
                    }
                }
                itemBuilder.append('}');
                quantityBuilder.append('}');
                
                String updatedLine = beforeItems + itemBuilder.toString() + ", " + quantityBuilder.toString() + ", " + expectedDeliveryDate + ", APPROVED";
                
                TextFile.replaceLine(PRfile, line, updatedLine);
                break;
            }
        }
    }
    
    
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

        // Save full quantity string (remove curly braces only for display)
        String quantityStr = quantityField.replace("{", "").replace("}", "");
        quantityMap.put(PO_ID, quantityStr);
        
        String FM_Name = parts.get(14);
        String FM_ID = parts.get(15);
        String paymentStatus = parts.get(16);
        
        // Parse only the first quantity as int
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
    
    public boolean updatePO(PurchaseOrder updatedPO, String quantityList) {
        List<String> lines = TextFile.readFile(POfile);
        String oldLine = null;
        String newLine = convertPOToLine(updatedPO, quantityList); // use string

        for (String line : lines) {
            if (line.startsWith(updatedPO.getPO_ID() + ",")) {
                oldLine = line;
                break;
            }
        }

        if (oldLine == null) {
            return false;
        }

        TextFile.replaceLine(POfile, oldLine, newLine);
        return true;
    }
    
    private String convertPOToLine(PurchaseOrder po, String quantityList) {
        String spName = po.getSP_Name();
        String spID = po.getSP_ID();
        String itemName = po.getItemName();
        String itemCode = po.getItemCode();

        return String.join(",", 
            po.getPO_ID(),
            po.getPR_ID(),
            po.getDate(),
            po.getPM_Name(),
            po.getPM_ID(),
            po.getSM_Name(),
            po.getSM_ID(),
            po.getExpectedDeliveryDate(),
            spName,
            spID,
            itemName,
            itemCode,
            quantityList, // use the full quantity list string here
            po.getStatus(),
            po.getFM_Name(),
            po.getFM_ID(),
            po.getPaymentStatus()
        );
    }
    
    public static boolean deletePurchaseOrder(String poID) {
        List<String> lines = TextFile.readFile(POfile);
        String lineToDelete = null;

        for (String line : lines) {
            if (line.startsWith(poID + ",")) {
                lineToDelete = line;
                break;
            }
        }

        if (lineToDelete != null) {
            TextFile.deleteLine(POfile, lineToDelete);
            quantityMap.remove(poID); // Optional: clean up the cache if needed
            return true;
        }
        return false;
    }
    
    public void updatePOStatus(String poID, String newStatus) {
        List<String> allLines = TextFile.readFile(POfile);

        for (String line : allLines) {
            if (line.startsWith(poID + ",")) {
                List<String> parts = smartSplit(line);

                if (parts.size() > 13) {
                    parts.set(13, newStatus); // status is column 14 (index 13)
                    String updatedLine = String.join(",", parts);
                    TextFile.replaceLine(POfile, line, updatedLine);
                }
                return;
            }
        }

        // If not found
        TextFile.appendTo(POfile, poID + "," + newStatus);
    }
    
    private List<String> smartSplit(String line) {
    List<String> result = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean insideBraces = false;

    for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
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

    // Add last field
    result.add(current.toString());

    return result;
}
    
}
