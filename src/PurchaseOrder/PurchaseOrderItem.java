/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

/**
 *
 * @author user
 */
public class PurchaseOrderItem {
    private String itemCode;
    private String itemName;
    private String supplierID;
    private String supplierName;
    private int quantity;
    
    public PurchaseOrderItem(String itemCode, String itemName, String supplierID, String supplierName, int quantity){
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.quantity = quantity;
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
}
