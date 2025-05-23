/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseRequisition;

import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class PRMain extends javax.swing.JFrame {

    private boolean ignoreTableClick = false;

    public PROperation prop;
    int did;
    String prid = "PR";
    String date = "";
    String prCreatedByName = "";
    String prCreatedByID = "SM";
    String itemcode = "";
    String quantity = "";
    String exdate = "";
    String status = "";
    private DecimalFormat df = new DecimalFormat("00");

    // Add this to store items and quantities for mapping
    private String[] selectedItemCodes;
    private String[] selectedQuantities;
    private Map<String, String> itemQuantityMap = new HashMap<>();

    // Add this to track if we're adding a new record
    private boolean isAddingNewRecord = false;
    private boolean itemsLoaded = false;

    // Add this to store temporary modifications for new records
    private List<String> tempItemCodes = new ArrayList<>();
    private List<String> tempQuantities = new ArrayList<>();

    /**
     * Creates new form PRMain
     */
    public PRMain() {
        initComponents();
        RemoveItembtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        prop = new PROperation(prid, date, prCreatedByName, prCreatedByID, itemcode, quantity, exdate, status);
        tableLoad();
        try {
            // Load Add icon
            java.net.URL addIconURL = getClass().getResource("/resources/icons/Add.png");
            if (addIconURL != null) {
                ImageIcon addIcon = new ImageIcon(addIconURL);
                Image scaled_add = addIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                add_Button.setIcon(new ImageIcon(scaled_add));
            } else {
                System.err.println("Add icon not found. Using default icon.");
            }

            // Load Delete icon
            java.net.URL deleteIconURL = getClass().getResource("/resources/icons/Delete.png");
            if (deleteIconURL != null) {
                ImageIcon deleteIcon = new ImageIcon(deleteIconURL);
                Image scaled_delete = deleteIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                delete_Button.setIcon(new ImageIcon(scaled_delete));
            } else {
                System.err.println("Delete icon not found. Using default icon.");
            }

            // Load Update icon
            java.net.URL updateIconURL = getClass().getResource("/resources/icons/Update.png");
            if (updateIconURL != null) {
                ImageIcon updateIcon = new ImageIcon(updateIconURL);
                Image scaled_update = updateIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                update_Button.setIcon(new ImageIcon(scaled_update));
            } else {
                System.err.println("Update icon not found. Using default icon.");
            }

            // Load Refresh icon
            java.net.URL refreshIconURL = getClass().getResource("/resources/icons/Refresh.png");
            if (refreshIconURL != null) {
                ImageIcon refreshIcon = new ImageIcon(refreshIconURL);
                Image scaled_refresh = refreshIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                refresh_Button.setIcon(new ImageIcon(scaled_refresh));
            } else {
                System.err.println("Refresh icon not found. Using default icon.");
            }

            // Load Clean icon
            java.net.URL cleanIconURL = getClass().getResource("/resources/icons/Clean.png");
            if (cleanIconURL != null) {
                ImageIcon cleanIcon = new ImageIcon(cleanIconURL);
                Image scaled_clean = cleanIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                clean_Button.setIcon(new ImageIcon(scaled_clean));
            } else {
                System.err.println("Clean icon not found. Using default icon.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading icons. Please ensure the resource files are in the correct path.", "Resource Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Set isAddingNewRecord to true initially since we start with a blank form
        isAddingNewRecord = true;

        // Load items for new record
        loadItemsForNewRecord();

        PRTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                // This is crucial to prevent multiple calls during a single selection change
                // and to ensure the event is "finished" adjusting.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                // Check if we are currently ignoring table clicks due to a removal operation
                if (ignoreTableClick) {
                    return; // Ignore this selection event
                }

                int selectedRow = PRTable.getSelectedRow();
                if (selectedRow == -1) {
                    // No row is selected. This is the natural state after a row is removed,
                    // or if the user clicks outside a row.
                    // Clear the input fields. DO NOT show an error message here.
                    cbItemCode.setSelectedIndex(-1);
                    txtQuantity.setText("");
                    return; // Exit as nothing valid is selected
                }

                // Process the selected row and populate fields
                DefaultTableModel model = (DefaultTableModel) PRTable.getModel();
                if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
                    try {
                        String itemCode = model.getValueAt(selectedRow, 0).toString();
                        String quantity = model.getValueAt(selectedRow, 1).toString();

                        cbItemCode.setSelectedItem(itemCode);
                        txtQuantity.setText(quantity);

                        // Set isAddingNewRecord to false when an existing item is selected
                        isAddingNewRecord = false;
                        // Clear temporary lists if they were used for previous item entry
                        tempItemCodes.clear();
                        tempQuantities.clear();

                    } catch (Exception ex) {
                        // Log or handle the error, but avoid modal dialogs for selection changes
                        System.err.println("Error retrieving data from table selection: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void getData() {
        StringBuilder itemCodesBuilder = new StringBuilder("{");
        StringBuilder quantitiesBuilder = new StringBuilder("{");

        if (cbItemCode.getSelectedItem() != null && !txtQuantity.getText().isEmpty()) {
            String currentItemCode = cbItemCode.getSelectedItem().toString();
            String currentQuantity = txtQuantity.getText();

            if (isAddingNewRecord) {
                // Find and update in temp lists
                int existingIndex = tempItemCodes.indexOf(currentItemCode);
                if (existingIndex >= 0) {
                    tempQuantities.set(existingIndex, currentQuantity);
                } else if (!currentQuantity.equals("0")) {
                    tempItemCodes.add(currentItemCode);
                    tempQuantities.add(currentQuantity);
                }
            } else {
                // Update in itemQuantityMap for existing records
                itemQuantityMap.put(currentItemCode, currentQuantity);
            }
        }

        if (isAddingNewRecord) {
            // For new records, iterate through the items currently in PRTable
            DefaultTableModel tableModel = (DefaultTableModel) PRTable.getModel();
            boolean isFirst = true;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String itemCode = tableModel.getValueAt(i, 0).toString();
                String quantity = tableModel.getValueAt(i, 1).toString();

                if (!quantity.equals("0")) { // Only include non-zero quantities
                    if (!isFirst) {
                        itemCodesBuilder.append(", ");
                        quantitiesBuilder.append(", ");
                    } else {
                        isFirst = false;
                    }
                    itemCodesBuilder.append(itemCode);
                    quantitiesBuilder.append(quantity);
                }
            }
        } else {
            // For updating existing records, iterate through the items currently in PRTable
            // (as it should reflect the latest state including any additions/removals)
            DefaultTableModel tableModel = (DefaultTableModel) PRTable.getModel();
            boolean isFirst = true;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String itemCode = tableModel.getValueAt(i, 0).toString();
                String quantity = tableModel.getValueAt(i, 1).toString();

                if (!quantity.equals("0")) { // Only include non-zero quantities
                    if (!isFirst) {
                        itemCodesBuilder.append(", ");
                        quantitiesBuilder.append(", ");
                    } else {
                        isFirst = false;
                    }
                    itemCodesBuilder.append(itemCode);
                    quantitiesBuilder.append(quantity);
                }
            }
        }

        itemCodesBuilder.append("}");
        quantitiesBuilder.append("}");

        prop.setItemCode(itemCodesBuilder.toString());
        prop.setQuantity(quantitiesBuilder.toString());
        prop.setStatus(cbStatus.getSelectedItem().toString());
    }

    private void clear() {
        txtNo.setText("");
        txtPRID.setText("PR");
        txtDate.setDate(null); // This line clears the JDateChooser for Date
        txtSMName.setText("");
        txtSMID.setText("SM");
        txtQuantity.setText("");
        txtExDate.setDate(null); // This line clears the JDateChooser for Expected Delivery Date
        cbStatus.setSelectedIndex(0);

        // Set flag to indicate we're adding a new record
        isAddingNewRecord = true;

        // Clear temporary lists
        tempItemCodes.clear();
        tempQuantities.clear();

        // Load items for a new record
        loadItemsForNewRecord();

        // Clear the table
        DefaultTableModel model = (DefaultTableModel) PRTable.getModel();
        model.setRowCount(0);
    }

    // Add method to load items from items.txt for a new record
    private void loadItemsForNewRecord() {
        try {
            File itemsFile = new File("src/itemmanagement/items.txt");
            if (!itemsFile.exists()) {
                // Improved error message for file not found
                JOptionPane.showMessageDialog(this, "Items file not found at: " + itemsFile.getAbsolutePath(), "File Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(itemsFile));

            List<String> itemCodes = new ArrayList<>();
            itemQuantityMap.clear(); // Clear the map when loading new items for a new record

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String itemCode = parts[1].trim(); // code is in the second column
                    itemCodes.add(itemCode);
                    itemQuantityMap.put(itemCode, "0"); // Default quantity to 0 for new records
                }
            }
            br.close();

            // Set the array of item codes
            selectedItemCodes = itemCodes.toArray(new String[0]);

            // --- START OF CORRECTED COMBO BOX POPULATION AND LISTENER MANAGEMENT ---
            // 1. Remove all existing ItemListeners to prevent duplicate events.
            // This is crucial to ensure that the itemStateChanged logic only runs once per selection.
            // Do this BEFORE setting a new model or adding items, as changing the model might
            // trigger events or make previous listeners irrelevant.
            for (ItemListener listener : cbItemCode.getItemListeners()) {
                cbItemCode.removeItemListener(listener);
            }

            // 2. Populate the dropdown with item codes by setting a new model.
            // This is the correct and efficient way to load all items.
            // It also implicitly clears any existing items in the JComboBox.
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(selectedItemCodes);
            cbItemCode.setModel(model);

            // 3. Add the ItemListener back after setting the new model.
            // This listener will handle updating txtQuantity based on selection.
            cbItemCode.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedItem = (String) cbItemCode.getSelectedItem(); // Cast to String
                        if (selectedItem != null) { // Defensive check: ensure an item is actually selected
                            // Check if this item has a temporary quantity (if modified in the current session)
                            int tempIndex = tempItemCodes.indexOf(selectedItem);
                            if (tempIndex >= 0) {
                                // Use the temporary quantity if it exists
                                txtQuantity.setText(tempQuantities.get(tempIndex));
                            } else {
                                // Otherwise, fall back to the initial "0" from itemQuantityMap
                                String qty = itemQuantityMap.get(selectedItem);
                                if (qty != null) {
                                    txtQuantity.setText(qty);
                                } else {
                                    txtQuantity.setText("0"); // Fallback, should ideally always find a quantity
                                }
                            }
                        } else {
                            txtQuantity.setText(""); // Clear quantity if no item is selected (e.g., empty combo box)
                        }
                    }
                }
            });

            // --- END OF CORRECTED COMBO BOX POPULATION AND LISTENER MANAGEMENT ---
            // --- DocumentListener for txtQuantity ---
            // This part is correctly placed, but make sure it's not being added multiple times
            // if loadItemsForNewRecord() is called frequently.
            // A common pattern is to add this DocumentListener once in the constructor.
            // If you are only calling loadItemsForNewRecord() on initial load and for a "new record" clear operation,
            // then it might be fine to keep it here, assuming 'isAddingNewRecord' manages its behavior.
            // However, if it's called multiple times, you might need to remove previous DocumentListeners too.
            // For now, assuming it's okay as is, but be aware of potential duplicates.
            // Remove previous DocumentListeners to avoid duplicates if this method can be called multiple times
            // outside of initial setup. A cleaner approach for DocumentListener is often to add it ONCE
            // in the constructor. But if it MUST be here, this is how you'd manage it:
            // for (DocumentListener dl : ((AbstractDocument)txtQuantity.getDocument()).getDocumentListeners()) {
            //     txtQuantity.getDocument().removeDocumentListener(dl);
            // }
            // txtQuantity.getDocument().addDocumentListener(...); // Add it after removing old ones if needed
            txtQuantity.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {

                    // If we're adding a new record, update the temporary lists
                    if (isAddingNewRecord && cbItemCode.getSelectedItem() != null) {
                        String currentItem = cbItemCode.getSelectedItem().toString();
                        String currentQty = txtQuantity.getText();

                        int existingIndex = tempItemCodes.indexOf(currentItem);
                        if (existingIndex >= 0) {
                            // Update existing entry
                            tempQuantities.set(existingIndex, currentQty);
                        } else if (!currentQty.equals("0") && !currentQty.isEmpty()) { // Add !currentQty.isEmpty()
                            // Add new entry if not zero and not empty
                            tempItemCodes.add(currentItem);
                            tempQuantities.add(currentQty);
                        } else if (currentQty.equals("0") && existingIndex >= 0) {
                            // If it becomes "0", remove from temp lists
                            tempItemCodes.remove(existingIndex);
                            tempQuantities.remove(existingIndex);
                        }
                    }
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    insertUpdate(e);
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    insertUpdate(e);
                }
            });

            // Select first item and set quantity to 0
            if (itemCodes.size() > 0) {
                cbItemCode.setSelectedIndex(0);
                txtQuantity.setText("0"); // Always start with 0 quantity for a newly selected item in a new record
            } else {
                // If no items are loaded, clear the quantity field
                txtQuantity.setText("");
            }

            itemsLoaded = true; // Mark items as loaded

        } catch (IOException e) { // Catch IOException specifically for file errors
            // Improved error message for file read issues
            JOptionPane.showMessageDialog(this, "Error loading item codes from file: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) { // Catch any other unexpected exceptions
            JOptionPane.showMessageDialog(this, "An unexpected error occurred during item loading: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Modify populateItemCodeDropdown method to clear the isAddingNewRecord flag
    private void populateItemCodeDropdown() {
        if (selectedItemCodes != null && selectedItemCodes.length > 0) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(selectedItemCodes);
            cbItemCode.setModel(model);

            // Remove existing listeners before adding new one
            for (ItemListener listener : cbItemCode.getItemListeners()) {
                cbItemCode.removeItemListener(listener);
            }

            // Add item listener to update quantity when item is selected
            cbItemCode.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedItem = cbItemCode.getSelectedItem().toString();
                        String qty = itemQuantityMap.get(selectedItem);
                        if (qty != null) {
                            txtQuantity.setText(qty);
                        }
                    }
                }
            });

            // When populating from an existing record, we're not adding a new one
            isAddingNewRecord = false;

            // Clear temporary lists since we're working with an existing record now
            tempItemCodes.clear();
            tempQuantities.clear();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        PRTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPRID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSMName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSMID = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        refresh_Button = new javax.swing.JButton();
        add_Button = new javax.swing.JButton();
        delete_Button = new javax.swing.JButton();
        clean_Button = new javax.swing.JButton();
        update_Button = new javax.swing.JButton();
        txtDate = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtExDate = new com.toedter.calendar.JDateChooser();
        cbItemCode = new javax.swing.JComboBox<>();
        AddItembtn = new javax.swing.JButton();
        RemoveItembtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PRTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Quantity"
            }
        ));
        PRTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PRTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(PRTable);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("No.");

        txtNo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("PR ID");

        txtPRID.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtPRID.setText("PR");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Date");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("SM Name");

        txtSMName.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("SM ID");

        txtSMID.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtSMID.setText("SM");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText("Item Code");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setText("Quantity");

        txtQuantity.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel10.setText("Status");

        cbStatus.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PENDING", "APPROVED", "COMPLETED" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        refresh_Button.setBackground(new java.awt.Color(216, 212, 213));
        refresh_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Refresh.png"))); // NOI18N
        refresh_Button.setBorder(null);
        refresh_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_ButtonActionPerformed(evt);
            }
        });

        add_Button.setBackground(new java.awt.Color(120, 211, 77));
        add_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Add.png"))); // NOI18N
        add_Button.setBorder(null);
        add_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_ButtonActionPerformed(evt);
            }
        });

        delete_Button.setBackground(new java.awt.Color(251, 82, 35));
        delete_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Delete.png"))); // NOI18N
        delete_Button.setBorder(null);
        delete_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_ButtonActionPerformed(evt);
            }
        });

        clean_Button.setBackground(new java.awt.Color(240, 225, 0));
        clean_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Clean.png"))); // NOI18N
        clean_Button.setBorder(null);
        clean_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clean_ButtonActionPerformed(evt);
            }
        });

        update_Button.setBackground(new java.awt.Color(76, 134, 168));
        update_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Update.png"))); // NOI18N
        update_Button.setBorder(null);
        update_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_ButtonActionPerformed(evt);
            }
        });

        txtDate.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Expected Delivery");

        cbItemCode.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cbItemCode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbItemCodeActionPerformed(evt);
            }
        });

        AddItembtn.setText("Add Item");
        AddItembtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItembtnActionPerformed(evt);
            }
        });

        RemoveItembtn.setText("Remove Item");
        RemoveItembtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveItembtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbStatus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtNo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(update_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(delete_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clean_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtSMName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtSMID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtExDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbItemCode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RemoveItembtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddItembtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSMName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSMID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 31, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(AddItembtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RemoveItembtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtExDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(clean_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(delete_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(update_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(81, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1294, 727));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStatusActionPerformed

    private void refresh_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_ButtonActionPerformed
        tableLoad();
        JOptionPane.showMessageDialog(this, "Table Refreshed!");
    }//GEN-LAST:event_refresh_ButtonActionPerformed

    private void add_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_ButtonActionPerformed
        //Add Data Button
        try {
            // Collect item codes and quantities from the table
            DefaultTableModel tableModel = (DefaultTableModel) PRTable.getModel();
            StringBuilder itemCodesBuilder = new StringBuilder("{");
            StringBuilder quantitiesBuilder = new StringBuilder("{");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (i > 0) {
                    itemCodesBuilder.append(", ");
                    quantitiesBuilder.append(", ");
                }
                itemCodesBuilder.append(tableModel.getValueAt(i, 0).toString());
                quantitiesBuilder.append(tableModel.getValueAt(i, 1).toString());
            }

            itemCodesBuilder.append("}");
            quantitiesBuilder.append("}");

            // Set collected data into PROperation
            prop.setItemCode(itemCodesBuilder.toString());
            prop.setQuantity(quantitiesBuilder.toString());
            getData(); // Collect other details
            prop.add(); // Save to PR.txt

            tableLoad(); // Refresh table
            clear(); // Clear form
            JOptionPane.showMessageDialog(this, "Data added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding data: " + e.getMessage());
        }
    }//GEN-LAST:event_add_ButtonActionPerformed

    private void delete_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_ButtonActionPerformed
        //Delete Data Button
        prop.delete();
        tableLoad();
        clear();
    }//GEN-LAST:event_delete_ButtonActionPerformed

    private void clean_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clean_ButtonActionPerformed
        clear();
        JOptionPane.showMessageDialog(this, "TextBox Cleaned!");
    }//GEN-LAST:event_clean_ButtonActionPerformed

    private void update_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_ButtonActionPerformed
        // Make sure we're in "updating record" mode
        isAddingNewRecord = false;

        getData();
        prop.update();
        tableLoad();
        clear();
    }//GEN-LAST:event_update_ButtonActionPerformed

    private void AddItembtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItembtnActionPerformed
        // TODO add your handling code here:
        // Get the JTable's model
        DefaultTableModel model = (DefaultTableModel) PRTable.getModel();

        String selectedItemCode = (String) cbItemCode.getSelectedItem();
        String quantityText = txtQuantity.getText();

        if (selectedItemCode == null || selectedItemCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an Item Code.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Quantity.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a whole number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if the item already exists in the PRTable
        boolean itemExists = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(selectedItemCode)) {
                // Item exists, update its quantity
                int existingQuantity = Integer.parseInt(model.getValueAt(i, 1).toString());
                model.setValueAt(String.valueOf(existingQuantity + quantity), i, 1);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            // If item doesn't exist, add a new row
            model.addRow(new Object[]{selectedItemCode, quantityText});
        }

        // After adding/updating, you might want to clear the quantity field or reset for next entry
        txtQuantity.setText("0"); // Or "" if you prefer it completely empty
        cbItemCode.setSelectedIndex(0); // Reset to first item or keep current
    }//GEN-LAST:event_AddItembtnActionPerformed

    private void cbItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbItemCodeActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cbItemCodeActionPerformed

    private void RemoveItembtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveItembtnActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) PRTable.getModel();
        int selectedRow = PRTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to remove from the table.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove the selected item?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ignoreTableClick = true; // Set flag to ignore subsequent table selection events

                model.removeRow(selectedRow);

                JOptionPane.showMessageDialog(this, "Selected item removed successfully!", "Item Removed", JOptionPane.INFORMATION_MESSAGE);

                // Clear the input fields after removal
                cbItemCode.setSelectedIndex(0);
                txtQuantity.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error removing item: " + e.getMessage(), "Removal Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                // Always reset the flag, even if an error occurred during removal
                ignoreTableClick = false;
            }
        }
    }//GEN-LAST:event_RemoveItembtnActionPerformed

    private void PRTableMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = PRTable.getSelectedRow();
        if (selectedRow == -1) {
            // No row selected, or selection cleared. This might happen with mouse clicks.
            // You can simply return or clear relevant fields if needed.
            return;
        }

        // Get the table model
        DefaultTableModel model = (DefaultTableModel) PRTable.getModel();

        // Ensure the row index is valid before proceeding
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            try {
                // Retrieve data from the selected row using correct column indices
                // Column 0 is "Item Code"
                String itemCode = model.getValueAt(selectedRow, 0).toString();
                // Column 1 is "Quantity"
                String quantity = model.getValueAt(selectedRow, 1).toString();

                // Populate the input fields
                cbItemCode.setSelectedItem(itemCode); // Set the selected item in the combo box
                txtQuantity.setText(quantity); // Set the quantity in the text field

                // When an existing record is clicked, we are no longer "adding a new record"
                // You might want to adjust your isAddingNewRecord flag here
                isAddingNewRecord = false;

                // Clear temporary lists because we are now working with an existing record's data
                tempItemCodes.clear();
                tempQuantities.clear();

                // You might want to populate other fields here if they were part of the row data
                // For example, if your table contained 'PRID', 'SMName', etc.
                // However, your PRTable is specifically for "Item Code" and "Quantity".
                // The other fields (PRID, Date, SMName, SMID, ExDate, Status) are likely from the main PR record.
                // If you click a row in PRTable, it's just to edit that specific item/quantity, not change the whole PR.
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error retrieving data from table: " + e.getMessage(), "Table Data Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace(); // Print the stack trace for debugging
            }
        }
    }

    private void tableLoad() {
        DefaultTableModel model = (DefaultTableModel) PRTable.getModel();
        model.setRowCount(0);

        String filePath = "src/itemmanagement/items.txt"; // Ensure this path is correct based on your project structure
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine(); // Assuming there's a header line to skip
            String line;

            while ((line = br.readLine()) != null) {
                // Split the line by comma
                String[] dataRow = line.split(",");

                // Check if there are enough columns to get item code (index 1) and quantity (index 4)
                if (dataRow.length >= 5) {
                    String itemCode = dataRow[1].trim(); // Item Code is at index 1 [cite: 1]
                    String quantity = dataRow[4].trim(); // Quantity is at index 4 [cite: 1]

                    model.addRow(new Object[]{itemCode, quantity});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading table: " + e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PRMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PRMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddItembtn;
    private javax.swing.JTable PRTable;
    private javax.swing.JButton RemoveItembtn;
    private javax.swing.JButton add_Button;
    private javax.swing.JComboBox<String> cbItemCode;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JButton clean_Button;
    private javax.swing.JButton delete_Button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refresh_Button;
    private com.toedter.calendar.JDateChooser txtDate;
    private com.toedter.calendar.JDateChooser txtExDate;
    private javax.swing.JTextField txtNo;
    private javax.swing.JTextField txtPRID;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSMID;
    private javax.swing.JTextField txtSMName;
    private javax.swing.JButton update_Button;
    // End of variables declaration//GEN-END:variables
}
