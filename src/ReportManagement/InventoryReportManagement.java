/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

import InventoryManagement.Inventory;
import InventoryManagement.TextFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class InventoryReportManagement {
    private static final String ItemFile = "src/InventoryManagement/Items.txt"; 
    
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
}
