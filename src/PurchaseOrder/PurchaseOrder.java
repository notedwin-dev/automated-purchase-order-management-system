/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

/**
 *
 * @author user
 */
public class PurchaseOrder {
    private String PO_ID, PR_ID, date, PM_Name, PM_ID, SM_Name, SM_ID, expectedDeliveryDate, SP_ID, SP_Name, itemCode, itemName, status = "PENDING", FM_Name, FM_ID, paymentStatus;
    private int quantity;
    
    public PurchaseOrder(String PO_ID, String PR_ID, String date, String PM_Name, String PM_ID, String SM_Name, String SM_ID, String expectedDeliveryDate, String SP_ID, String SP_Name, String itemCode, String itemName, String status, int quantity, String FM_Name, String FM_ID, String paymentStatus){
        this.PO_ID = PO_ID;
        this.PR_ID = PR_ID;
        this.date = date;
        this.PM_Name = PM_Name;
        this.PM_ID = PM_ID;
        this.SM_Name = SM_Name;
        this.SM_ID = SM_ID;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.SP_ID = SP_ID;
        this.SP_Name = SP_Name;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.status = status;
        this.quantity = quantity;
        this.FM_Name = FM_Name;
        this.FM_ID = FM_ID;
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return the PO_ID
     */
    public String getPO_ID() {
        return PO_ID;
    }

    /**
     * @param PO_ID the PO_ID to set
     */
    public void setPO_ID(String PO_ID) {
        this.PO_ID = PO_ID;
    }

    /**
     * @return the PR_ID
     */
    public String getPR_ID() {
        return PR_ID;
    }

    /**
     * @param PR_ID the PR_ID to set
     */
    public void setPR_ID(String PR_ID) {
        this.PR_ID = PR_ID;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the SM_Name
     */
    public String getSM_Name() {
        return SM_Name;
    }

    /**
     * @param SM_Name the SM_Name to set
     */
    public void setSM_Name(String SM_Name) {
        this.SM_Name = SM_Name;
    }

    /**
     * @return the SM_ID
     */
    public String getSM_ID() {
        return SM_ID;
    }

    /**
     * @param SM_ID the SM_ID to set
     */
    public void setSM_ID(String SM_ID) {
        this.SM_ID = SM_ID;
    }

    /**
     * @return the SP_ID
     */
    public String getSP_ID() {
        return SP_ID;
    }

    /**
     * @param SP_ID the SP_ID to set
     */
    public void setSP_ID(String SP_ID) {
        this.SP_ID = SP_ID;
    }

    /**
     * @return the SP_Name
     */
    public String getSP_Name() {
        return SP_Name;
    }

    /**
     * @param SP_Name the SP_Name to set
     */
    public void setSP_Name(String SP_Name) {
        this.SP_Name = SP_Name;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the PM_Name
     */
    public String getPM_Name() {
        return PM_Name;
    }

    /**
     * @param PM_Name the PM_Name to set
     */
    public void setPM_Name(String PM_Name) {
        this.PM_Name = PM_Name;
    }

    /**
     * @return the PM_ID
     */
    public String getPM_ID() {
        return PM_ID;
    }

    /**
     * @param PM_ID the PM_ID to set
     */
    public void setPM_ID(String PM_ID) {
        this.PM_ID = PM_ID;
    }

    /**
     * @return the expectedDeliveryDate
     */
    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * @param expectedDeliveryDate the expectedDeliveryDate to set
     */
    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    /**
     * @return the FM_Name
     */
    public String getFM_Name() {
        return FM_Name;
    }

    /**
     * @param FM_Name the FM_Name to set
     */
    public void setFM_Name(String FM_Name) {
        this.FM_Name = FM_Name;
    }

    /**
     * @return the FM_ID
     */
    public String getFM_ID() {
        return FM_ID;
    }

    /**
     * @param FM_ID the FM_ID to set
     */
    public void setFM_ID(String FM_ID) {
        this.FM_ID = FM_ID;
    }

    /**
     * @return the paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus the paymentStatus to set
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
}
