/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itemmanagement;

/**
 *
 * @author nixon
 */    
//----- Implements with ItemOperations, handles item logic and file saving -----//
import TextFile_Handler.TextFile;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import auth.User;
import java.util.ArrayList;
import java.util.List;


public class ItemManagement implements ItemOperations {
    //----- Basically these are passed from the MainPanel.java -----//
    private JTable itemTable;
    private JTextField itemName_textbox, itemCode_textbox, unitPrice_textbox, retailPrice_textbox, supplierName_textbox;
    private JComboBox supplierID_comboBox;
    private JTextArea itemDesc_textarea;
    private User currentUser;
    private static final String itemFile = "src/itemmanagement/items.txt";
//    private TableRowSorter<DefaultTableModel> sorter;

    //----- As you can see this is a constructor -----//
    public ItemManagement( JTable table, 
                                            JTextField name, 
                                            JTextField code, 
                                            JTextArea itemDesc,
                                            JComboBox supID, 
                                            JTextField supName, 
                                            JTextField price, 
                                            JTextField rprice ) {
        this.itemTable = table;
        this.itemName_textbox = name;
        this.itemCode_textbox = code;
        this.itemDesc_textarea = itemDesc;
        this.supplierID_comboBox = supID;
        this.supplierName_textbox = supName;
        this.unitPrice_textbox = price;
        this.retailPrice_textbox = rprice;
    }
    
    // Constructor for view-only mode (just need the table)
    public ItemManagement(JTable table, User user) {
        this.itemTable = table;
        this.currentUser = user;

        if (this.currentUser == null) {
            JOptionPane.showMessageDialog(null, "Error: User session not found. Please log in again.");
            throw new IllegalArgumentException("User object is required for role-based filtering.");
        }
    }

    // - - - - - - - - - - ADD FUNCTION - - - - - - - - - - //
    @Override //Acts like a "kastam" where it checks whether the method name, return type, and parameters are correctly used.
    public void add() {
        //----- Reads the data from the textbox -----//
        String name = itemName_textbox.getText().trim();
        String code = itemCode_textbox.getText().trim();
        String supID = (String) supplierID_comboBox.getSelectedItem();
        String supName = supplierName_textbox.getText().trim();
        String price = unitPrice_textbox.getText().trim();
        String rprice = retailPrice_textbox.getText().trim();
        
        //----- Check for errors before adding to the table -----//
        if (name.isEmpty() || code.isEmpty()  || supID == null || supName.isEmpty() ||  price.isEmpty() || rprice.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill up everything before adding to table");
            return;
        }
        
        //----- Check if any data is duplicated -----//
        for (int i = 0; i < itemTable.getRowCount(); i++) {
            if (
                name.equalsIgnoreCase(itemTable.getValueAt(i, 1).toString()) && 
                code.equalsIgnoreCase(itemTable.getValueAt(i, 2).toString()) &&  
                supID.equals(itemTable.getValueAt(i, 3).toString()) &&          
                supName.equals(itemTable.getValueAt(i, 4).toString()) &&        
                price.equals(itemTable.getValueAt(i, 6).toString()) &&
                rprice.equals(itemTable.getValueAt(i, 7).toString()) 
            ) {
            JOptionPane.showMessageDialog(null, "This item already exists in the table.");
            return;
            }
        }
        
        //----- Now add the data to the table -----//
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        int rowCount = model.getRowCount() + 1;
        model.addRow(new Object[] {rowCount, name, code, supID, supName, "0", price, rprice, false});
         saveTableToFile();
        JOptionPane.showMessageDialog(null, "Item added");
        
        //----- Clear the textbox after add_Button is click -----//
        itemName_textbox.setText("");
        itemCode_textbox.setText("");
        supplierID_comboBox.setSelectedIndex(-1);
        supplierName_textbox.setText("");
        unitPrice_textbox.setText("");
        retailPrice_textbox.setText("");
    }
    
    
    // - - - - - - - - - - UPDATE FUNCTION - - - - - - - - - - //
    @Override 
    public void update() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        int selectedRow = itemTable.getSelectedRow();

        if (selectedRow >= 0) {
            String name = itemName_textbox.getText().trim();
            String code = itemCode_textbox.getText().trim();
            String supID = (String) supplierID_comboBox.getSelectedItem();
            String supName = supplierName_textbox.getText().trim();
            String price = unitPrice_textbox.getText().trim();
            String rprice = retailPrice_textbox.getText().trim();

            //----- Check if any fields are empty -----//
            if (name.isEmpty() || code.isEmpty() || supID == null || supName.isEmpty() || price.isEmpty() || rprice.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill up everything before updating.");
                return;
            }

            //----- Check for duplicates (excluding the selected row) -----//
            for (int i = 0; i < itemTable.getRowCount(); i++) {
                if (i == selectedRow) continue;
                if (
                    name.equalsIgnoreCase(itemTable.getValueAt(i, 1).toString()) &&
                    code.equalsIgnoreCase(itemTable.getValueAt(i, 2).toString()) &&
                    supID.equals(itemTable.getValueAt(i, 3).toString()) &&
                    supName.equals(itemTable.getValueAt(i, 4).toString()) &&
                    price.equals(itemTable.getValueAt(i, 6).toString()) &&
                    rprice.equals(itemTable.getValueAt(i, 7).toString()) 
                ) {
                    JOptionPane.showMessageDialog(null, "This item already exists in the table.");
                    return;
                }
            }

            //----- Update the table row with new data -----//
            model.setValueAt(name, selectedRow, 1);
            model.setValueAt(code, selectedRow, 2);
            model.setValueAt(supID, selectedRow, 3);
            model.setValueAt(supName, selectedRow, 4);
            model.setValueAt(price, selectedRow, 6);
            model.setValueAt(rprice, selectedRow, 7);

            //----- Save to items.txt -----//
            saveTableToFile();
            JOptionPane.showMessageDialog(null, "Item edited and saved to file.");

            itemName_textbox.setText("");
            itemCode_textbox.setText("");
            supplierID_comboBox.setSelectedIndex(-1);
            supplierName_textbox.setText("");
            unitPrice_textbox.setText("");
            retailPrice_textbox.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to update");
        }
    }
    
    
    // - - - - - Save the table to items.txt - - - - - //
    public void saveTableToFile() {
      DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
      List<String> newLines = new ArrayList<>();

      for (int i = 0; i < model.getRowCount(); i++) {
          StringBuilder sb = new StringBuilder();
          for (int j = 1; j < model.getColumnCount(); j++) { // ----- Skip index 0 (row number) ----- //
              sb.append(model.getValueAt(i, j));
              if (j < model.getColumnCount() - 1) {
                  sb.append(",");
              }
          }
          newLines.add(sb.toString());
      }

      TextFile.overwriteFile(itemFile, newLines);
  }


    //----- Double click the table row to add the data to the textBox / comboBox -----//
    public void doubleClickRow() {
        itemTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = itemTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        itemName_textbox.setText(itemTable.getValueAt(selectedRow, 1).toString());
                        itemCode_textbox.setText(itemTable.getValueAt(selectedRow, 2).toString());
                        supplierID_comboBox.setSelectedItem(itemTable.getValueAt(selectedRow, 3).toString());
                        supplierName_textbox.setText(itemTable.getValueAt(selectedRow, 4).toString());
                        unitPrice_textbox.setText(itemTable.getValueAt(selectedRow, 6).toString());
                        retailPrice_textbox.setText(itemTable.getValueAt(selectedRow, 7).toString());
                    }
                }
            }
        });
    }


    // - - - - - - - - - - CLEAN FUNCTION - - - - - - - - - - //
    @Override 
    public void clean() {
        itemName_textbox.setText("");
        itemCode_textbox.setText("");
        itemDesc_textarea.setText("");
        supplierID_comboBox.setSelectedIndex(-1);
        supplierName_textbox.setText("");
        unitPrice_textbox.setText("");
        retailPrice_textbox.setText("");
    }
    
    
    // - - - - - - - - - - DELETE FUNCTION - - - - - - - - - - //
    @Override 
    public void delete() {
        try {
            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            int selectedRow = itemTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a row to delete");
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete?",
                    "Confirm delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    model.removeRow(selectedRow);
                     saveTableToFile();
                    JOptionPane.showMessageDialog(null, "Item deleted");
                    }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occured while deleting item" + e.getMessage());
        }
    }
    
    
    // - - - - - - - - - - REFRESH FUNCTION - - - - - - - - - - //
    @Override
    public void refresh() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        model.setRowCount(0); 

        List<String> lines = TextFile.readFile(itemFile);
        int no = 1;

        for (String line : lines) {
            String[] data = line.split(",");

            if (data.length == 8) { 
                model.addRow(new Object[]{
                    no++,       // No.
                    data[0],    // Item Code
                    data[1],    // Item Name
                    data[2],    // Category
                    data[3],    // Quantity
                    data[4],    // Supplier ID
                    data[5],    // Price
                    data[6],    // Delivery Date
                    data[7]     // Delivery Status
                });
            } else {
                System.out.println("Skipping unused line: " + line);
            }
        }
    }

    
}
