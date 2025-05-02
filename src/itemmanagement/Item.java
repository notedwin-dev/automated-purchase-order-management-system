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
    private String itemName, itemCode, supplierID, supplierName, category, description;
    private double unitPrice, retailPrice;
    
    public Item(String itemName, String itemCode, String supplierID, String supplierName, String category, double unitPrice, double retailPrice, String description) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.category = category;
        this.unitPrice = unitPrice;
        this.retailPrice = retailPrice;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    
}

