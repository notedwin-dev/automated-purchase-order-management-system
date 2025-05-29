/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author User
 */
public class Payment_Encapsulation {
    private String paymentID, poID, supplierName, supplierID, itemName, itemCodes, quantities, paymentStatus, unitPrice, totalAmount, paymentDate;
    
    public Payment_Encapsulation(String paymentID, String poID, String supplierName, String supplierID,
                   String itemName, String itemCodes, String quantities, String unitPrice,
                   String totalAmount, String paymentStatus, String paymentDate) {
        this.paymentID = paymentID;
        this.poID = poID;
        this.supplierName = supplierName;
        this.supplierID = supplierID;
        this.itemName = itemName;
        this.itemCodes = itemCodes;
        this.quantities = quantities;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getPoID() {
        return poID;
    }

    public void setPoID(String poID) {
        this.poID = poID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(String itemCodes) {
        this.itemCodes = itemCodes;
    }

    public String getQuantities() {
        return quantities;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
    
}
