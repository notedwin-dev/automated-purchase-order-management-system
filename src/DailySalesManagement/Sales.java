/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DailySalesManagement;

/**
 *
 * @author user
 */
public class Sales {
    private String salesDate, itemCode, itemName;
    private int quantitySold;
    private double retailPrice, totalAmount;
    
    public Sales(String salesDate, String itemCode, String itemName, int quantitySold, double retailPrice, double totalAmount){
        this.salesDate = salesDate;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantitySold = quantitySold;
        this.retailPrice = retailPrice;
        this.totalAmount = totalAmount;
    }

    /**
     * @return the salesDate
     */
    public String getSalesDate() {
        return salesDate;
    }

    /**
     * @param salesDate the salesDate to set
     */
    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the quantitySold
     */
    public int getQuantitySold() {
        return quantitySold;
    }

    /**
     * @param quantitySold the quantitySold to set
     */
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    /**
     * @return the retailPrice
     */
    public double getRetailPrice() {
        return retailPrice;
    }

    /**
     * @param retailPrice the retailPrice to set
     */
    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
