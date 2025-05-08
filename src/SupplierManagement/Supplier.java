/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SupplierManagement;

/**
 *
 * @author Jengsiang
 */
public class Supplier {
    private String supplierID, supplierName, contact, email, address, itemDescription;
    
    public Supplier(String supplierID,String supplierName,String contact,String email,String address,String itemDescription) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.itemDescription = itemDescription;
    
    }
    
    //Getters & Setters
    public String getSupplierID(){
        return supplierID;
    }
    public void setSupplierID(String supplierID){
        this.supplierID = supplierID;
    }
    
    public String getSupplierName(){
        return supplierName;
    }
    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }
    
    public String getContact(){
        return contact;
    }
    public void setContact(String contact){
        this.contact = contact;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getItemDescription(){
        return itemDescription;
    }
    public void setItemDescription(String itemDescription){
        this.itemDescription = itemDescription;
    }
    
}
