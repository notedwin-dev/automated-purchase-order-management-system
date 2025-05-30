/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseRequisition;

/**
 *
 * @author User
 */
public class PurchaseRequisition {
    private String prid, date, prCreatedByName, prCreatedByID, itemcode, exdate, status;
    private int quantity;

    public PurchaseRequisition(String prid, String date, String prCreatedByName, String prCreatedByID, String itemcode, int quantity, String exdate, String status) {
        this.prid = prid;
        this.date = date;
        this.prCreatedByName = prCreatedByName;
        this.prCreatedByID = prCreatedByID;
        this.itemcode = itemcode;
        this.quantity = quantity;
        this.exdate = exdate;
        this.status = status;
    }

    /**
     * @return the prid
     */
    public String getPrid() {
        return prid;
    }

    /**
     * @param prid the prid to set
     */
    public void setPrid(String prid) {
        this.prid = prid;
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
     * @return the prCreatedByName
     */
    public String getPrCreatedByName() {
        return prCreatedByName;
    }

    /**
     * @param prCreatedByName the prCreatedByName to set
     */
    public void setPrCreatedByName(String prCreatedByName) {
        this.prCreatedByName = prCreatedByName;
    }

    /**
     * @return the prCreatedByID
     */
    public String getPrCreatedByID() {
        return prCreatedByID;
    }

    /**
     * @param prCreatedByID the prCreatedByID to set
     */
    public void setPrCreatedByID(String prCreatedByID) {
        this.prCreatedByID = prCreatedByID;
    }

    /**
     * @return the itemcode
     */
    public String getItemcode() {
        return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    /**
     * @return the exdate
     */
    public String getExdate() {
        return exdate;
    }

    /**
     * @param exdate the exdate to set
     */
    public void setExdate(String exdate) {
        this.exdate = exdate;
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

}
