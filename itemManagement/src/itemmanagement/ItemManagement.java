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
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ItemManagement implements ItemOperations {
    //----- Basically these are passed from the UI.java -----//
    private JTable itemTable;
    private JTextField itemName_textbox, itemCode_textbox, category_textbox, unitPrice_textbox, description_textbox;
    private JComboBox supplierID_comboBox, supplierName_comboBox;
    private TableRowSorter<DefaultTableModel> sorter;

    //----- As you can see this is a constructor -----//
    public ItemManagement( JTable table, 
                                            JTextField name, 
                                            JTextField code, 
                                            JComboBox supID, 
                                            JComboBox supName, 
                                            JTextField category, 
                                            JTextField price, 
                                            JTextField desc) {
        this.itemTable = table;
        this.itemName_textbox = name;
        this.itemCode_textbox = code;
        this.supplierID_comboBox = supID;
        this.supplierName_comboBox = supName;
        this.category_textbox = category;
        this.unitPrice_textbox = price;
        this.description_textbox = desc;
    }
    

    // - - - - - - - - - - ADD FUNCTION - - - - - - - - - - //
    @Override //Acts like a "kastam" where it checks whether the method name, return type, and parameters are correctly used.
    public void add() {
        //----- Reads the data from the textbox -----//
        String name = itemName_textbox.getText().trim();
        String code = itemCode_textbox.getText().trim();
        String supID = (String) supplierID_comboBox.getSelectedItem();
        String supName = (String) supplierName_comboBox.getSelectedItem();
        String category = category_textbox.getText().trim();
        String price = unitPrice_textbox.getText().trim();
        String desc = description_textbox.getText().trim();
        
        //----- Check for errors before adding to the table -----//
        if (name.isEmpty() || code.isEmpty()  || supID == null || supName == null || category.isEmpty() || price.isEmpty() || desc.isEmpty()) {
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
                category.equalsIgnoreCase(itemTable.getValueAt(i, 5).toString()) &&
                price.equals(itemTable.getValueAt(i, 6).toString()) &&
                desc.equalsIgnoreCase(itemTable.getValueAt(i, 7).toString())
            ) {
            JOptionPane.showMessageDialog(null, "This item already exists in the table.");
            return;
            }
        }
        
        //----- Now add the data to the table -----//
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        int rowCount = model.getRowCount() + 1;
        model.addRow(new Object[] {rowCount, name, code, supID, supName, category, price, desc});
         saveTableToFile();
        JOptionPane.showMessageDialog(null, "Item added");
        
        //----- Clear the textbox after add_Button is click -----//
        itemName_textbox.setText("");
        itemCode_textbox.setText("");
        supplierID_comboBox.setSelectedIndex(-1);
        supplierName_comboBox.setSelectedIndex(-1);
        category_textbox.setText("");
        unitPrice_textbox.setText("");
        description_textbox.setText("");
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
            String supName = (String) supplierName_comboBox.getSelectedItem();
            String category = category_textbox.getText().trim();
            String price = unitPrice_textbox.getText().trim();
            String desc = description_textbox.getText().trim();

            //----- Check if any fields are empty -----//
            if (name.isEmpty() || code.isEmpty() || supID == null || supName == null || category.isEmpty() || price.isEmpty() || desc.isEmpty()) {
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
                    category.equalsIgnoreCase(itemTable.getValueAt(i, 5).toString()) &&
                    price.equals(itemTable.getValueAt(i, 6).toString()) &&
                    desc.equalsIgnoreCase(itemTable.getValueAt(i, 7).toString())
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
            model.setValueAt(category, selectedRow, 5);
            model.setValueAt(price, selectedRow, 6);
            model.setValueAt(desc, selectedRow, 7);

            //----- Save to items.txt -----//
            saveTableToFile();
            JOptionPane.showMessageDialog(null, "Item edited and saved to file.");

            itemName_textbox.setText("");
            itemCode_textbox.setText("");
            supplierID_comboBox.setSelectedIndex(-1);
            supplierName_comboBox.setSelectedIndex(-1);
            category_textbox.setText("");
            unitPrice_textbox.setText("");
            description_textbox.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to update");
        }
    }
    
    
    // - - - - - Save the table to items.txt - - - - - //
        public void saveTableToFile() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        File file = new File("src/itemManagement/items.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 1; j < model.getColumnCount(); j++) { //----- Skip index 0 (row number) -----//
                    sb.append(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while saving file: " + e.getMessage());
        }
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
                        supplierName_comboBox.setSelectedItem(itemTable.getValueAt(selectedRow, 4).toString());
                        category_textbox.setText(itemTable.getValueAt(selectedRow, 5).toString());
                        unitPrice_textbox.setText(itemTable.getValueAt(selectedRow, 6).toString());
                        description_textbox.setText(itemTable.getValueAt(selectedRow, 7).toString());
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
        supplierID_comboBox.setSelectedIndex(-1);
        supplierName_comboBox.setSelectedIndex(-1);
        category_textbox.setText("");
        unitPrice_textbox.setText("");
        description_textbox.setText("");
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

    
    // - - - - - - - - - - FILTER FUNCTION - - - - - - - - - - //
    @Override 
    public void filter(String category) {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        sorter = new TableRowSorter<>(model);
        itemTable.setRowSorter(sorter);

        if (category.equals("All")) {
            sorter.setRowFilter(null); 
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)^" + Pattern.quote(category) + "$", 5)); 
        }
        
        System.out.println("Applied filter: " + category);
        System.out.println("Visible rows after filter: " + itemTable.getRowCount());
    }  
    
    
     // - - - - - - - - - - UPDATE FILTER MENU FUNCTION - - - - - - - - - - //
    @Override
    public List<String> updateFilter() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        Set<String> uniqueCategory = new HashSet<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            String category = model.getValueAt(i, 5).toString().trim();
            if (!category.isEmpty()) {
                uniqueCategory.add(category);
            }
        }

        List<String> categoryList = new ArrayList<>(uniqueCategory);
        Collections.sort(categoryList);
        return categoryList;
    }
    
    
    // - - - - - - - - - - REFRESH FUNCTION - - - - - - - - - - //
    @Override
    public void refresh() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        model.setRowCount(0); //----- Clears the row to avoid data repeatation -----//
        
        try (BufferedReader reader = new BufferedReader(new FileReader("src/itemManagement/items.txt"))) {
            String line;
            int no = 1;
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) { 
                    model.addRow(new Object[] {
                        no++,       //----- No. 
                        data[0],    
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        data[6]     //----- Description
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from this file" + e.getMessage());
        }
    }
    
}
