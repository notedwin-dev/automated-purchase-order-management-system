/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class InventoryManager {
    private static final String ItemFile = "src/InventoryManagement/Items.txt"; //<-Please tukar
    
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
}
