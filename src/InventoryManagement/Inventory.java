/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManagement;

/**
 *
 * @author user
 */
public class Inventory {
    private String itemName, itemCode, supplierID, supplierName;
    private int quantity;
    private double unitPrice, retailPrice;
    private boolean deliveryStatus;
    
    public Inventory(String itemName, String itemCode, String supplierID, String supplierName, int quantity, double unitPrice, double retailPrice, boolean deliveryStatus){
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.retailPrice = retailPrice;
        this.deliveryStatus = deliveryStatus;
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
     * @return the supplierID
     */
    public String getSupplierID() {
        return supplierID;
    }

    /**
     * @param supplierID the supplierID to set
     */
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the unitPrice
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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
     * @return the deliveryStatus
     */
    public boolean isDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * @param deliveryStatus the deliveryStatus to set
     */
    public void setDeliveryStatus(boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    
    public boolean isLowStock(){
        return getQuantity() < 5;
    }
}
