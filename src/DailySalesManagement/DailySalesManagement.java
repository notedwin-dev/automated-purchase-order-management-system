/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DailySalesManagement;

import InventoryManagement.Inventory;
import InventoryManagement.InventoryManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class DailySalesManagement {
    private static final String ItemFile = "src/InventoryManagement/Items.txt";
    private static final String SalesFile = "src/DailySalesManagement/sales.txt";
    private List<Sales> salesData = new ArrayList<>();
    
    public List<String> getAllItemCode(){
        List<String> itemCode = new ArrayList<>();
        List<String> lines = TextFile.readFile(ItemFile);
        for(String line : lines){
            String[] data = line.split(",");
            if(data.length >= 2){
                itemCode.add(data[1].trim());
            }
        }
        return itemCode;
    }
    
    public String getItemNameByItemCode(String itemCode){
        List<String> lines = TextFile.readFile(ItemFile);
        for(String line : lines){
            String[] data = line.split(",");
            if(data.length >= 2 && data[1].equals(itemCode)){
                return data[0].trim();
            }
        }
        return null;
    }
    
    public void getAllSalesFromFile(){
        List<String> lines = TextFile.readFile(SalesFile);
        for(String line : lines){
            String[] data = line.split(",");
            if(data.length >= 6){
                String salesDate = data[0].trim();
                String itemCode = data[1].trim();
                String itemName = data[2].trim();
                int quantitySold = Integer.parseInt(data[3].trim());
                double retailPrice = Double.parseDouble(data[4].trim());
                double totalAmount = Double.parseDouble(data[5].trim());
                salesData.add(new Sales(salesDate, itemCode, itemName, quantitySold, retailPrice, totalAmount));
            }
        }
    }
    
    public List<Sales> getAllSales(){
        return salesData;
    }
    
    public double getRetailPriceFromItemCode(String itemCode){
        List<String> lines = TextFile.readFile(ItemFile);
        for(String line : lines){
            String[] data = line.split(",");
            if(data.length >= 7 && data[1].equals(itemCode)){
                try{
                    return Double.parseDouble(data[6].trim());
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
        return 0.0;
    }
    
    //Add function
    public Sales addSales(String salesDate, String itemCode, String itemName, int quantitySold){
        double retailPrice = getRetailPriceFromItemCode(itemCode);
        double totalAmount = retailPrice * quantitySold;
        Sales newSales = new Sales(salesDate, itemCode, itemName, quantitySold, retailPrice, totalAmount);
        salesData.add(newSales);
        String appendSale = salesDate + "," + itemCode + "," + itemName + "," + quantitySold + "," + retailPrice + "," + totalAmount;
        TextFile.appendTo(SalesFile, appendSale);
        return newSales;
    }
    
    //update function
    public Sales updateSales(String salesDate, String itemCode, String itemName, int quantitySold){
        double retailPrice = getRetailPriceFromItemCode(itemCode);
        double totalAmount = retailPrice * quantitySold;
        
        for(Sales sale : salesData){
            if(sale.getSalesDate().equals(salesDate) && sale.getItemCode().equals(itemCode)){
                Sales oldSale = new Sales(
                    sale.getSalesDate(),
                    sale.getItemCode(),
                    sale.getItemName(),
                    sale.getQuantitySold(),
                    sale.getRetailPrice(),
                    sale.getTotalAmount()
                );
                
                sale.setItemName(itemName);
                sale.setQuantitySold(quantitySold);
                sale.setRetailPrice(retailPrice);
                sale.setTotalAmount(totalAmount);

                saveSalesToTxtFile();
                return oldSale;
            }
        }
        return null;
    }
    
    public Sales deleteSales(String salesDate, String itemCode) {
        Iterator<Sales> iterator = salesData.iterator();

        while (iterator.hasNext()) {
            Sales sale = iterator.next();

            if (sale.getSalesDate().equals(salesDate) && sale.getItemCode().equals(itemCode)) {
                iterator.remove();
                saveSalesToTxtFile();
                return sale; // return the deleted sale
            }
        }
        return null;
    }
    
    public double calculateTotalSalesOfDay(String salesDate){
        double totalSales = 0.0;
        for(Sales sale : salesData){
            if(sale.getSalesDate().equals(salesDate)){
                totalSales += sale.getTotalAmount();
            }
        }
        return totalSales;
    }

    
    private void saveSalesToTxtFile(){
        List<String> lines = new ArrayList<>();
        for(Sales sale : salesData){
            lines.add(sale.getSalesDate() + "," + sale.getItemCode() + "," + sale.getItemName() + "," + sale.getQuantitySold() + "," + sale.getRetailPrice() + "," + sale.getTotalAmount());
        }
        TextFile.overwriteFile(SalesFile, lines);
    }
    
    public boolean updateStockWhenAdd(Sales newSale){
        Inventory inv = InventoryManager.getAllInventory().stream()
                .filter(i -> i.getItemCode().equals(newSale.getItemCode()))
                .findFirst().orElse(null);

        if(inv != null){
            int newStock = inv.getQuantity() - newSale.getQuantitySold();
            
            if(newStock < 0){
                JOptionPane.showMessageDialog(null, "Insufficient stock for item: " + newSale.getItemName());
                return false;
            }
            
            inv.setQuantity(newStock);
            InventoryManager.UpdateInventory(inv);
            return true;
        }
        return false;
    }
    
    public boolean updateStockWhenUpdate(Sales oldSale, Sales newSale){
        Inventory inv = InventoryManager.getAllInventory().stream()
                .filter(i -> i.getItemCode().equals(newSale.getItemCode()))
                .findFirst().orElse(null);
        
        if(inv != null){
            int revertedStock = inv.getQuantity() + oldSale.getQuantitySold();
            int newStock = revertedStock - newSale.getQuantitySold();
            
            if(newStock < 0){
                JOptionPane.showMessageDialog(null, "Insufficient stock for item: " + newSale.getItemName());
                return false;
            }
            
            inv.setQuantity(newStock);
            InventoryManager.UpdateInventory(inv);
            return true;
        }
        return false;
    }
    
    public boolean updateStockWhenDelete(Sales deletedSale) {
        Inventory inv = InventoryManager.getAllInventory().stream()
                .filter(i -> i.getItemCode().equals(deletedSale.getItemCode()))
                .findFirst().orElse(null);

        if (inv != null) {
            int newStock = inv.getQuantity() + deletedSale.getQuantitySold();
            inv.setQuantity(newStock);
            InventoryManager.UpdateInventory(inv);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Inventory record not found for item: " + deletedSale.getItemName());
        return false;
    }
}
