/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itemmanagement;

/**
 *
 * @author nixon
 */
//----- ENCAPSULATION used -----//
public class Item {
    private String itemName, itemCode, supplierID, supplierName;
    private double unitPrice, retailPrice;
    
    public Item(String itemName, String itemCode, String supplierID, String supplierName, double unitPrice, double retailPrice) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.unitPrice = unitPrice;
        this.retailPrice = retailPrice;
    }
    
//----- Getters & Setters -----//
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
    
    
    
}

