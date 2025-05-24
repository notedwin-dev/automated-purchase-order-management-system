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
    private String PO_ID, PR_ID, date, POCreatedByName, POCreatedByID, PRCreatedByName, PRCreatedByID, expectedDeliveryDate, SP_ID, SP_Name, itemCode, itemName, status = "PENDING", POApprovedByName, POApprovedByID, paymentStatus;
    private int quantity;
    
    public PurchaseOrder(String PO_ID, String PR_ID, String date, String POCreatedByName, String POCreatedByID, String PRCreatedByName, String PRCreatedByID, String expectedDeliveryDate, String SP_ID, String SP_Name, String itemCode, String itemName, String status, int quantity, String POApprovedByName, String POApprovedByID, String paymentStatus){
        this.PO_ID = PO_ID;
        this.PR_ID = PR_ID;
        this.date = date;
        this.POCreatedByName = POCreatedByName;
        this.POCreatedByID = POCreatedByID;
        this.PRCreatedByName = PRCreatedByName;
        this.PRCreatedByID = PRCreatedByID;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.SP_ID = SP_ID;
        this.SP_Name = SP_Name;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.status = status;
        this.quantity = quantity;
        this.POApprovedByName = POApprovedByName;
        this.POApprovedByID = POApprovedByID;
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
     * @return the POCreatedByName
     */
    public String getPOCreatedByName() {
        return POCreatedByName;
    }

    /**
     * @param POCreatedByName the POCreatedByName to set
     */
    public void setPOCreatedByName(String POCreatedByName) {
        this.POCreatedByName = POCreatedByName;
    }

    /**
     * @return the POCreatedByID
     */
    public String getPOCreatedByID() {
        return POCreatedByID;
    }

    /**
     * @param POCreatedByID the POCreatedByID to set
     */
    public void setPOCreatedByID(String POCreatedByID) {
        this.POCreatedByID = POCreatedByID;
    }

    /**
     * @return the PRCreatedByName
     */
    public String getPRCreatedByName() {
        return PRCreatedByName;
    }

    /**
     * @param PRCreatedByName the PRCreatedByName to set
     */
    public void setPRCreatedByName(String PRCreatedByName) {
        this.PRCreatedByName = PRCreatedByName;
    }

    /**
     * @return the PRCreatedByID
     */
    public String getPRCreatedByID() {
        return PRCreatedByID;
    }

    /**
     * @param PRCreatedByID the PRCreatedByID to set
     */
    public void setPRCreatedByID(String PRCreatedByID) {
        this.PRCreatedByID = PRCreatedByID;
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
     * @return the POApprovedByName
     */
    public String getPOApprovedByName() {
        return POApprovedByName;
    }

    /**
     * @param POApprovedByName the POApprovedByName to set
     */
    public void setPOApprovedByName(String POApprovedByName) {
        this.POApprovedByName = POApprovedByName;
    }

    /**
     * @return the POApprovedByID
     */
    public String getPOApprovedByID() {
        return POApprovedByID;
    }

    /**
     * @param POApprovedByID the POApprovedByID to set
     */
    public void setPOApprovedByID(String POApprovedByID) {
        this.POApprovedByID = POApprovedByID;
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
