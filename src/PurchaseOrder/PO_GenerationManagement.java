/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
import InventoryManagement.Inventory;
public class PO_GenerationManagement {
    private static final String ItemFile = "src/InventoryManagement/Items.txt"; 
    private static final String PRfile = "src/PurchaseRequisition/PR.txt" ;
    private static final String POfile = "src/PurchaseOrder/PO.txt";

    
    public class PRData{
        public String PR_ID, date, SM_Name, SM_ID, expectedDeliveryDate;
        public List<PurchaseOrderItem> items = new ArrayList<>();
    }
    
    
    public PRData getFirstPR() {

        List<String> lines = TextFile.readFile(PRfile);
        if (lines.isEmpty())return null;

        String line = lines.get(0); // First PR only
        PRData prData = new PRData();

        // Extract fixed parts
        int firstBrace = line.indexOf('{');
        String beforeItems = line.substring(0, firstBrace).trim().replaceAll(",$", "");
        String[] firstParts = beforeItems.split(",", 4);

        prData.PR_ID = firstParts[0].trim();
        prData.date = firstParts[1].trim();
        prData.SM_Name = firstParts[2].trim();
        prData.SM_ID = firstParts[3].trim();  


        // Extract item codes
        int itemStart = line.indexOf('{');
        int itemEnd = line.indexOf('}');
        String itemCodeStr = line.substring(itemStart + 1, itemEnd).trim();

        // Extract quantities
        int qtyStart = line.indexOf('{', itemEnd);
        int qtyEnd = line.indexOf('}', qtyStart);
        String quantityStr = line.substring(qtyStart + 1, qtyEnd).trim();

        // Remaining string after quantity closing }
        String remaining = line.substring(qtyEnd + 1).trim();
        if (remaining.startsWith(",")) {
            remaining = remaining.substring(1).trim();  
        }

        String[] tailParts = remaining.split(",", 2);
        prData.expectedDeliveryDate = tailParts.length > 0 ? tailParts[0].trim() : "";
        String status = tailParts.length > 1 ? tailParts[1].trim() : "";

        // DEBUG PRINT
        System.out.println("Parsed expectedDeliveryDate: '" + prData.expectedDeliveryDate + "'");

        // Add items
        String[] itemCodes = itemCodeStr.split(",\\s*");
        String[] quantities = quantityStr.split(",\\s*");

        List<String> inventory = TextFile.readFile(ItemFile);
        for (int i = 0; i < itemCodes.length; i++) {
            String code = itemCodes[i];
            int qty = Integer.parseInt(quantities[i]);
            for (String inv : inventory) {
                String[] invData = inv.split(",\\s*");
                if (invData.length >= 5 && invData[1].equals(code)) {
                    prData.items.add(new PurchaseOrderItem(code, invData[0], invData[2], invData[3], qty));
                    break;
                }
            }
        }
        return prData;
    }


    public String generatePO_ID(){
        List<String> lines = TextFile.readFile(POfile);
        if(lines.isEmpty()){
            return "PO001";
        }
        
        String lastLine = lines.get(lines.size()-1);
        String[] data = lastLine.split(",", 2);
        if(data.length > 0 && data[0].startsWith("PO")){
            int id = Integer.parseInt(data[0].substring(2));
            return String.format("PO%03d", id + 1);
        }
        return "PO001";
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
                supplierName.append(", ");
                supplierID.append(", ");
                itemName.append(", ");
                itemCode.append(", ");
                quantity.append(", ");
            }
        }
        String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s, {%s}, {%s}, {%s}, {%s}, {%s}, Pending",
                PO_ID, PR_ID, date,
                PM_Name, PM_ID,
                SM_Name, SM_ID,
                expectedDeliveryDate,
                supplierName.toString(),
                supplierID.toString(),
                itemName.toString(),
                itemCode.toString(),
                quantity.toString());
        
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
    
    
}
