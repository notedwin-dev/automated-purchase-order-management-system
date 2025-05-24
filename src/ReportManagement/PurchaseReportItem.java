/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author nixon
 */
public class PurchaseReportItem {
    private String companies;
    private String supplierIDs;
    private String itemNames;
    private String itemCodes;
    private String quantities;
    private String unitPrices;
    
    public PurchaseReportItem(String companies, String supplierIDs, String itemNames, String itemCodes, String quantities, String unitPrices){
        this.companies = companies;
        this.supplierIDs = supplierIDs;
        this.itemNames = itemNames;
        this.itemCodes = itemCodes;
        this.quantities = quantities;
        this.unitPrices = unitPrices;  
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public String getSupplierIDs() {
        return supplierIDs;
    }

    public void setSupplierIDs(String supplierIDs) {
        this.supplierIDs = supplierIDs;
    }

    public String getItemNames() {
        return itemNames;
    }

    public void setItemNames(String itemNames) {
        this.itemNames = itemNames;
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

    public String getUnitPrices() {
        return unitPrices;
    }

    public void setUnitPrices(String unitPrices) {
        this.unitPrices = unitPrices;
    }
    
    
}
