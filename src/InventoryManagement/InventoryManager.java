/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagement;

import TextFile_Handler.TextFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class InventoryManager {
    private static final String ItemFile = "src/itemmanagement/items.txt"; 
    private static final String POFile = "src/PurchaseOrder/PO.txt";
    
    public static List<Inventory> getAllInventory(){
        List<Inventory> inventoryList = new ArrayList<>();
        List<String> itemLines = TextFile.readFile(ItemFile);
        
        for(String line : itemLines){
            String[] data = line.split(",");
            if(data.length >= 7){
                try{
                    String itemName = data[0].trim();
                    String itemCode = data[1].trim();
                    String supplierID = data[2].trim();
                    String supplierName = data[3].trim();
                    int quantity = Integer.parseInt(data[4].trim());
                    double unitPrice = Double.parseDouble(data[5].trim());
                    double retailPrice = Double.parseDouble(data[6].trim());
                    
                    boolean deliveryStatus = false;
                    if(data.length >=8){
                        deliveryStatus = Boolean.parseBoolean(data[7].trim());
                    }
                    
                    Inventory inv = new Inventory(itemName, itemCode, supplierID, supplierName, quantity, unitPrice, retailPrice, deliveryStatus);
                    inventoryList.add(inv);
                }catch(NumberFormatException e){
                    System.err.println("Error parsing data: " + line);
                }
            }
        }
        return inventoryList;
    }
    
    //Update inventory into inventory.txt
    public static void UpdateInventory(Inventory inventory){
        List<String> itemLines = TextFile.readFile(ItemFile);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for(String line : itemLines){
            String[] data = line.split(",");
            if(data.length >= 7 && data[1].trim().equals(inventory.getItemCode())){
                updatedLines.add(convertizeInventory(inventory));
                found = true;
            } else {
                updatedLines.add(line);
            }
        }

        if(!found){
            updatedLines.add(convertizeInventory(inventory));
        }

        TextFile.overwriteFile(ItemFile, updatedLines); 
    }
    
    //convertize inventory object to string
    private static String convertizeInventory(Inventory inventory) {
        return String.join(",",
            inventory.getItemName(),
            inventory.getItemCode(),
            inventory.getSupplierID(),
            inventory.getSupplierName(),
            String.valueOf(inventory.getQuantity()),
            String.valueOf(inventory.getUnitPrice()),
            String.valueOf(inventory.getRetailPrice()),
            String.valueOf(inventory.isDeliveryStatus())
        );
    }
    
    public static List<Inventory> lowStockItems(){
        List<Inventory> lowstock = new ArrayList<>();
        for(Inventory inv : getAllInventory()){
            if(inv.isLowStock()){
                lowstock.add(inv);
            }
        }
        return lowstock;
    }
    
    public static List<Inventory> searchFunction(String keyword){
        keyword = keyword.toLowerCase();
        List<Inventory> filteredList = new ArrayList<>();
        for(Inventory inv : getAllInventory()){
            String line = inv.getItemName() + inv.getItemCode() + inv.getSupplierID() + inv.getSupplierName();
            if(line.toLowerCase().contains(keyword)){
                filteredList.add(inv);
            }
        }
        return filteredList;
    }
    
    public static void resetDeliveryStatusWhenPOsPaid(){
        List<String> poData = TextFile.readFile(POFile);
        List<String> poIDs = new ArrayList<>();
        List<List<String>> poItemCodes = new ArrayList<>();
        Set<String> paidItemCode = new HashSet<>();
        
        for(String line : poData){
            String[] data = line.split(",(?=(?:[^{}]*\\{[^}]*\\})*[^}]*$)");
            if(data.length >= 17){
                String paymentStatus = data[16].trim();
                if(paymentStatus.equalsIgnoreCase("Paid")){
                    String poID = data[0].trim();
                    String itemCodeArray = data[11].trim();
                    itemCodeArray = itemCodeArray.replaceAll("[{}]", "");
                    String[] itemCodes = itemCodeArray.split(",");
                    List<String> itemList = new ArrayList<>();
                    
                    for(String code : itemCodes){
                        String trimmed = code.trim();
                        paidItemCode.add(trimmed);
                        itemList.add(trimmed);
                    }
                    poIDs.add(poID);
                    poItemCodes.add(itemList);
                }
            }
        }
        List<String> itemData = TextFile.readFile(ItemFile);
        List<String> updatedItems = new ArrayList<>();
        for(String itemLine : itemData){
            String[] data = itemLine.split(",");
            if(data.length >= 8){
                String itemCode = data[1].trim();
                if(paidItemCode.contains(itemCode)){
                   data[7] = "false";
                }
                updatedItems.add(String.join(",", data));
            }else{
                updatedItems.add(itemLine);
            }
        }
        TextFile.overwriteFile(ItemFile, updatedItems);
        StringBuilder notification = new StringBuilder();
        for(int i = 0; i < poIDs.size(); i++){
            String poID = poIDs.get(i);
            List<String> itemCodes = poItemCodes.get(i);
            notification.append("PO ").append(poID).append(" has been paid.\nDelivery status reset for items: ")
                    .append(String.join(", ", itemCodes)).append("\n\n");
        }
        if(!notification.isEmpty()){
            JOptionPane.showMessageDialog(null, notification.toString(), "Paid PO Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
