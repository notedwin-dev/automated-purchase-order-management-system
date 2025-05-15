/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DailySalesManagement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class DailySalesManagement {
    private static final String ItemFile = "C:\\Users\\user\\OneDrive\\Documents\\NetBeansProjects\\automated-purchase-order-management-system\\src\\InventoryManagement\\Items.txt";
    private static final String SalesFile = "C:\\Users\\user\\OneDrive\\Documents\\NetBeansProjects\\automated-purchase-order-management-system\\src\\DailySalesManagement\\sales.txt";
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
            if(data.length >= 6 && data[1].equals(itemCode)){
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
    public void addSales(String salesDate, String itemCode, String itemName, int quantitySold){
        double retailPrice = getRetailPriceFromItemCode(itemCode);
        double totalAmount = retailPrice * quantitySold;
        Sales newSales = new Sales(salesDate, itemCode, itemName, quantitySold, retailPrice, totalAmount);
        salesData.add(newSales);
        String appendSale = salesDate + "," + itemCode + "," + itemName + "," + quantitySold + "," + retailPrice + "," + totalAmount;
        TextFile.appendTo(SalesFile, appendSale);
    }
    
    //update function
    public boolean updateSales(String salesDate, String itemCode, String itemName, int quantitySold){
        double retailPrice = getRetailPriceFromItemCode(itemCode);
        double totalAmount = retailPrice * quantitySold;
        for(Sales sale : salesData){
            if(sale.getSalesDate().equals(salesDate) && sale.getItemCode().equals(itemCode)){
                sale.setItemName(itemName);
                sale.setQuantitySold(quantitySold);
                sale.setRetailPrice(retailPrice);
                sale.setTotalAmount(totalAmount);
                saveSalesToTxtFile();
                return true;
            }
        }
        return false;
    }
    
    //delete function
    public boolean deleteSales(String salesDate, String itemCode){
        for(Sales sale : salesData){
            if(sale.getSalesDate().equals(salesDate) && sale.getItemCode().equals(itemCode)){
                salesData.remove(sale);
                saveSalesToTxtFile();
                return true;
            }
        }
        return false;
    }
    
    private void saveSalesToTxtFile(){
        List<String> lines = new ArrayList<>();
        for(Sales sale : salesData){
            lines.add(sale.getSalesDate() + "," + sale.getItemCode() + "," + sale.getItemName() + "," + sale.getQuantitySold() + "," + sale.getRetailPrice() + "," + sale.getTotalAmount());
        }
        TextFile.overwriteFile(SalesFile, lines);
    }
}
