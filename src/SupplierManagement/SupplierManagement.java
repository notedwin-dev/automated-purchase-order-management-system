/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SupplierManagement;

/**
 *
 * @author Jengsiang
 */
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class SupplierManagement implements SupplierOperations{
    
    private static final String filePath = "src/SupplierManagement/Suppliers.txt";
    
    private JTable SupplierTable;
    private JTextField SupplierID_TextField, SupplierName_TextField, SupplierContact_TextField, 
            SupplierEmail_TextField, SupplierAddress_TextField, SupplierItemDescription_TextField;
    
    
    private List<Supplier> supplierList = new ArrayList<>();
    private int selectedIndex = -1;
    
    public SupplierManagement(
        JTable SupplierTable,
        JTextField SupplierID_TextField,
        JTextField SupplierName_TextField,
        JTextField SupplierContact_TextField,
        JTextField SupplierEmail_TextField,
        JTextField SupplierAddress_TextField,
        JTextField SupplierItemDescription_TextField
    ) {
        this.SupplierTable = SupplierTable;
        this.SupplierID_TextField = SupplierID_TextField;
        this.SupplierName_TextField = SupplierName_TextField;
        this.SupplierContact_TextField = SupplierContact_TextField;
        this.SupplierEmail_TextField = SupplierEmail_TextField;
        this.SupplierAddress_TextField = SupplierAddress_TextField;
        this.SupplierItemDescription_TextField = SupplierItemDescription_TextField;
        
        this.SupplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        clear();
        refresh();
    }
    
    public SupplierManagement(JTable SupplierTable) {
    this.SupplierTable = SupplierTable;
    this.SupplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    refresh(); // Call this only if it doesn't depend on the text fields
}
    
    // Generate Next Supplier ID Function
    private String generateNextSupplierID(){
        List<String> lines = TextFile.readFile(filePath);
        Set<Integer> uniqueIDs = new HashSet<>();  // Set to store unique IDs

        // Iterate through each line in the file
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length > 0) {
                String supplierID = data[0].trim();  // Get the Supplier ID

                // Check if the supplier ID starts with "SP" and is a valid format
                if (supplierID.startsWith("SP")) {
                    try {
                        // Extract the numeric part after "SP" and add it to the set for uniqueness
                        int number = Integer.parseInt(supplierID.substring(2));  // Extract number after 'SP'
                        uniqueIDs.add(number);  // Only unique numbers will be added to the set
                    } catch (NumberFormatException ignored) {
                        // Ignore any invalid number format and continue
                    }
                }
            }
        }

        // Find the maximum number in the set and increment it
        int max = uniqueIDs.isEmpty() ? 0 : Collections.max(uniqueIDs);
        max++; // Increment to next ID

        // Return the next ID in the "SPxxxx" format
        return String.format("SP%04d", max);  
    }
    
    // Refresh Function
    @Override
    public void refresh(){
        DefaultTableModel model = (DefaultTableModel) SupplierTable.getModel();
        model.setRowCount(0); 
        supplierList.clear();
        
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to create Suppliers.txt: " + e.getMessage());
                return;
            }
        }
        
        int index = 1;
    
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",", -1);
            if (data.length == 6) {
                Supplier supplier = new Supplier(
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim(),
                    data[4].trim(),
                    data[5].trim()
                );

                supplierList.add(supplier);
                model.addRow(new Object[]{
                    index++,
                    supplier.getSupplierID(),
                    supplier.getSupplierName(),
                    supplier.getContact(),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    supplier.getItemDescription()
                });
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error reading Suppliers.txt: " + e.getMessage());
    }
    }
    
    
    // Add Function
    @Override
    public void add(){
        String id = generateNextSupplierID();
        SupplierID_TextField.setText(id);
        String name = SupplierName_TextField.getText().trim();
        String contact = SupplierContact_TextField.getText().trim();
        String email = SupplierEmail_TextField.getText().trim();
        String address = SupplierAddress_TextField.getText().trim();
        String itemDescription = SupplierItemDescription_TextField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || contact.isEmpty()  || email == null || address == null || itemDescription.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill up all fields before adding to table");
            return;
        }
        
        for (Supplier supplier : supplierList) {
            if (supplier.getSupplierName().equalsIgnoreCase(name) &&
                supplier.getContact().equalsIgnoreCase(contact) &&
                supplier.getEmail().equalsIgnoreCase(email) &&
                supplier.getAddress().equalsIgnoreCase(address) &&
                supplier.getItemDescription().equalsIgnoreCase(itemDescription)) {

                JOptionPane.showMessageDialog(null, "This supplier already exists in the table. Please modify the data or clear the form before adding.");
                return;
            }
        }
        
        String supplierData = String.join(",", id, name, contact, email, address, itemDescription);
        TextFile.appendTo(filePath, supplierData);
        JOptionPane.showMessageDialog(null, "Supplier added successfully.");

        refresh();
        clear();
    }
    
    
    
    // Update Function
    @Override
    public void update(){
        int selectedRow = SupplierTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a supplier to update.");
            return;
        }

        // Get the original values from the table (existing line)
        String originalID = SupplierTable.getValueAt(selectedRow, 1).toString();
        String originalName = SupplierTable.getValueAt(selectedRow, 2).toString();
        String originalContact = SupplierTable.getValueAt(selectedRow, 3).toString();
        String originalEmail = SupplierTable.getValueAt(selectedRow, 4).toString();
        String originalAddress = SupplierTable.getValueAt(selectedRow, 5).toString();
        String originalItem = SupplierTable.getValueAt(selectedRow, 6).toString();

        String originalLine = String.join(",", originalID, originalName, originalContact, originalEmail, originalAddress, originalItem);

        String updatedID = SupplierID_TextField.getText().trim();
        String updatedName = SupplierName_TextField.getText().trim();
        String updatedContact = SupplierContact_TextField.getText().trim();
        String updatedEmail = SupplierEmail_TextField.getText().trim();
        String updatedAddress = SupplierAddress_TextField.getText().trim();
        String updatedItem = SupplierItemDescription_TextField.getText().trim();

        String updatedLine = String.join(",", updatedID, updatedName, updatedContact, updatedEmail, updatedAddress, updatedItem);

        TextFile.replaceLine(filePath, originalLine, updatedLine);

        JOptionPane.showMessageDialog(null, "Supplier updated successfully.");

        refresh();
        clear();
    }
    
    
    // Clear Function
    @Override
    public void clear(){
        SupplierID_TextField.setText(generateNextSupplierID());
        SupplierName_TextField.setText("");
        SupplierContact_TextField.setText("");
        SupplierEmail_TextField.setText("");
        SupplierAddress_TextField.setText("");
        SupplierItemDescription_TextField.setText("");
    }
    
    
    // Delete Function
    @Override
    public void delete(){
        int selectedRow = SupplierTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a supplier to delete.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this supplier?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        String supplierID = SupplierTable.getValueAt(selectedRow, 1).toString();
        String name = SupplierTable.getValueAt(selectedRow, 2).toString();
        String contact = SupplierTable.getValueAt(selectedRow, 3).toString();
        String email = SupplierTable.getValueAt(selectedRow, 4).toString();
        String address = SupplierTable.getValueAt(selectedRow, 5).toString();
        String itemDescription = SupplierTable.getValueAt(selectedRow, 6).toString();

        String fullLine = String.join(",", supplierID, name, contact, email, address, itemDescription);

        TextFile.deleteLine(filePath, fullLine);

        JOptionPane.showMessageDialog(null, "Supplier deleted successfully.");

        refresh();
        clear();
    }
}
