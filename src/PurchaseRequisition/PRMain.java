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

    public PROperation prop;

    int did;
    String prid = "PR";
    String date = "";
    String smname = "";
    String smid = "SM";
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
    private boolean quantityModified = false;

    /**
     * Creates new form PRMain
     */
    public PRMain() {
        initComponents();
        prop = new PROperation(prid, date, smname, smid, itemcode, quantity, exdate, status);
        tableLoad();
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/resources/icons/Add.png"));
        Image scaled_add = addIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedAdd = new ImageIcon(scaled_add);
        add_Button.setIcon(resizedAdd);
        // - - - - - RESIZE ICON DELETE - - - - - //
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/resources/icons/Delete.png"));
        Image scaled_delete = deleteIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedDelete = new ImageIcon(scaled_delete);
        delete_Button.setIcon(resizedDelete);
        // - - - - - RESIZE ICON UPDATE - - - - - //
        ImageIcon updateIcon = new ImageIcon(getClass().getResource("/resources/icons/Update.png"));
        Image scaled_update = updateIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedUpdate = new ImageIcon(scaled_update);
        update_Button.setIcon(resizedUpdate);
        // - - - - - RESIZE ICON REFRESH - - - - - //
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/resources/icons/Refresh.png"));
        Image scaled_refresh = refreshIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedRefresh = new ImageIcon(scaled_refresh);
        refresh_Button.setIcon(resizedRefresh);
        // - - - - - RESIZE ICON CLEAN - - - - - //
        ImageIcon cleanIcon = new ImageIcon(getClass().getResource("/resources/icons/Clean.png"));
        Image scaled_clean = cleanIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedClean = new ImageIcon(scaled_clean);
        clean_Button.setIcon(resizedClean);
        
        // Set isAddingNewRecord to true initially since we start with a blank form
        isAddingNewRecord = true;
        
        // Load items for new record
        loadItemsForNewRecord();
    }

    private void getData() {
        String rawDate = ((com.toedter.calendar.JTextFieldDateEditor) txtDate.getDateEditor().getUiComponent()).getText();
        String rawExDate = ((com.toedter.calendar.JTextFieldDateEditor) txtExDate.getDateEditor().getUiComponent()).getText();
        try {
            java.util.Date date = new java.text.SimpleDateFormat("dd MMM yyyy").parse(rawDate);
            String formattedDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(date);
            prop.setDate(formattedDate);

            java.util.Date exDate = new java.text.SimpleDateFormat("dd MMM yyyy").parse(rawExDate);
            String formattedExDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(exDate);
            prop.setExDate(formattedExDate);
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing date: " + e.getMessage());
        }
        
        prop.setPRID(txtPRID.getText());
        prop.setSMName(txtSMName.getText());
        prop.setSMID(txtSMID.getText());
        
        // Modify the item code and quantity collection logic
        StringBuilder itemCodesBuilder = new StringBuilder("{");
        StringBuilder quantitiesBuilder = new StringBuilder("{");
        
        if (isAddingNewRecord) {
            // For new records, use the temporary lists of modified items
            boolean isFirst = true;
            
            // If the current selection has been modified, make sure it's in the temp lists
            if (quantityModified && cbItemCode.getSelectedItem() != null) {
                String currentItem = cbItemCode.getSelectedItem().toString();
                String currentQty = txtQuantity.getText();
                
                // Check if this item is already in our temp lists
                int existingIndex = tempItemCodes.indexOf(currentItem);
                if (existingIndex >= 0) {
                    // Update existing entry
                    tempQuantities.set(existingIndex, currentQty);
                } else if (!currentQty.equals("0")) {
                    // Add as new entry if quantity is not zero
                    tempItemCodes.add(currentItem);
                    tempQuantities.add(currentQty);
                }
                
                // Reset the modified flag
                quantityModified = false;
            }
            
            // Build the formatted strings from our temporary lists
            for (int i = 0; i < tempItemCodes.size(); i++) {
                // Only include items with non-zero quantities
                String qty = tempQuantities.get(i);
                if (!qty.equals("0")) {
                    if (!isFirst) {
                        itemCodesBuilder.append(", ");
                        quantitiesBuilder.append(", ");
                    } else {
                        isFirst = false;
                    }
                    itemCodesBuilder.append(tempItemCodes.get(i));
                    quantitiesBuilder.append(qty);
                }
            }
        } else {
            // For updating existing records, use the previous logic
            if (selectedItemCodes != null && selectedItemCodes.length > 0) {
                for (int i = 0; i < selectedItemCodes.length; i++) {
                    if (i > 0) {
                        itemCodesBuilder.append(", ");
                        quantitiesBuilder.append(", ");
                    }
                    String itemCode = selectedItemCodes[i];
                    itemCodesBuilder.append(itemCode);
                    
                    // If the quantity was edited, use the edited value for the selected item
                    if (cbItemCode.getSelectedItem() != null && 
                        cbItemCode.getSelectedItem().toString().equals(itemCode)) {
                        quantitiesBuilder.append(txtQuantity.getText());
                        // Update the map with the new quantity
                        itemQuantityMap.put(itemCode, txtQuantity.getText());
                    } else {
                        quantitiesBuilder.append(itemQuantityMap.get(itemCode));
                    }
                }
            } else {
                // Fallback to selected item only if no mapping exists
                if (cbItemCode.getSelectedItem() != null) {
                    String selectedItem = cbItemCode.getSelectedItem().toString();
                    itemCodesBuilder.append(selectedItem);
                    quantitiesBuilder.append(txtQuantity.getText());
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
    }
    
    // Add method to load items from items.txt for a new record
    private void loadItemsForNewRecord() {
        try {
            File itemsFile = new File("src/itemmanagement/items.txt");
            if (!itemsFile.exists()) {
                JOptionPane.showMessageDialog(this, "Items file not found.");
                return;
            }
            
            BufferedReader br = new BufferedReader(new FileReader(itemsFile));
            
            List<String> itemCodes = new ArrayList<>();
            itemQuantityMap.clear();
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String itemCode = parts[1].trim(); // code is in the second column
                    itemCodes.add(itemCode);
                    itemQuantityMap.put(itemCode, "0"); // Default quantity to 0
                }
            }
            br.close();
            
            // Set the array of item codes
            selectedItemCodes = itemCodes.toArray(new String[0]);
            
            // Populate the dropdown with item codes
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(selectedItemCodes);
            cbItemCode.setModel(model);
            
            // Add item listener to update quantity when item is selected
            cbItemCode.removeAllItems(); // Remove existing items
            for (String code : selectedItemCodes) {
                cbItemCode.addItem(code);
            }
            
            // Add item listener to update quantity when item is selected
            for (ItemListener listener : cbItemCode.getItemListeners()) {
                cbItemCode.removeItemListener(listener);
            }
            
            cbItemCode.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedItem = cbItemCode.getSelectedItem().toString();
                        
                        // Check if this item is in our temp lists first
                        int tempIndex = tempItemCodes.indexOf(selectedItem);
                        if (tempIndex >= 0) {
                            // Use the temp value
                            txtQuantity.setText(tempQuantities.get(tempIndex));
                        } else {
                            // Fall back to the default map
                            String qty = itemQuantityMap.get(selectedItem);
                            if (qty != null) {
                                txtQuantity.setText(qty);
                            } else {
                                txtQuantity.setText("0");
                            }
                        }
                    }
                }
            });
            
            // Add document listener to the quantity text field to track changes
            txtQuantity.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    quantityModified = true;
                    
                    // If we're adding a new record, update the temporary lists
                    if (isAddingNewRecord && cbItemCode.getSelectedItem() != null) {
                        String currentItem = cbItemCode.getSelectedItem().toString();
                        String currentQty = txtQuantity.getText();
                        
                        int existingIndex = tempItemCodes.indexOf(currentItem);
                        if (existingIndex >= 0) {
                            // Update existing entry
                            tempQuantities.set(existingIndex, currentQty);
                        } else if (!currentQty.equals("0")) {
                            // Add new entry if not zero
                            tempItemCodes.add(currentItem);
                            tempQuantities.add(currentQty);
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
            
            // Select first item
            if (itemCodes.size() > 0) {
                cbItemCode.setSelectedIndex(0);
                txtQuantity.setText("0");
            }
            
            itemsLoaded = true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading item codes: " + e.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 100));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        PRTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PR ID", "Date", "SM Name", "SM ID", "Item Code", "Quantity", "Expected Delivery", "Status"
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtQuantity)
                    .addComponent(cbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(cbItemCode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(txtNo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(txtPRID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel10)
                            .addGroup(jPanel2Layout.createSequentialGroup()
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
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtSMName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtSMID, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtExDate, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 814, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbItemCode)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
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
                            .addComponent(add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            // Make sure we're in "adding new record" mode
            isAddingNewRecord = true;
            
            getData();
            prop.add();
            tableLoad();
            clear();
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

    private void PRTableMouseClicked(java.awt.event.MouseEvent evt) {
        DefaultTableModel tmodel = (DefaultTableModel) PRTable.getModel();
        int selectrowindex = PRTable.getSelectedRow();

        if (selectrowindex >= 0) { // Check if a valid row is selected
            txtNo.setText(tmodel.getValueAt(selectrowindex, 0).toString());
            prid = tmodel.getValueAt(selectrowindex, 1).toString();
            date = tmodel.getValueAt(selectrowindex, 2).toString();
            smname = tmodel.getValueAt(selectrowindex, 3).toString();
            smid = tmodel.getValueAt(selectrowindex, 4).toString();
            itemcode = tmodel.getValueAt(selectrowindex, 5).toString();
            exdate = tmodel.getValueAt(selectrowindex, 6).toString();
            status = tmodel.getValueAt(selectrowindex, 7).toString();
            did = selectrowindex + 2;

            // Extract and store item codes and quantities for dropdown
            String itemsStr = tmodel.getValueAt(selectrowindex, 5).toString();
            itemQuantityMap.clear();
            
            // Parse multiple items like "SP2AP(10), SP2BN(5)"
            String[] itemParts = itemsStr.split(", ");
            selectedItemCodes = new String[itemParts.length];
            selectedQuantities = new String[itemParts.length];
            
            for (int i = 0; i < itemParts.length; i++) {
                String part = itemParts[i].trim();
                if (part.contains("(") && part.contains(")")) {
                    int startPos = part.indexOf("(") + 1;
                    int endPos = part.indexOf(")");
                    if (startPos < endPos) {
                        String code = part.substring(0, part.indexOf("(")).trim();
                        String qty = part.substring(startPos, endPos).trim();
                        
                        selectedItemCodes[i] = code;
                        selectedQuantities[i] = qty;
                        itemQuantityMap.put(code, qty);
                    }
                }
            }

            // Populate the dropdown
            populateItemCodeDropdown();
            
            // Select the first item by default
            if (selectedItemCodes != null && selectedItemCodes.length > 0) {
                cbItemCode.setSelectedItem(selectedItemCodes[0]);
                txtQuantity.setText(itemQuantityMap.get(selectedItemCodes[0]));
            }

            txtPRID.setText(prid);
            // Date
            try {
                java.util.Date parsedDate = new java.text.SimpleDateFormat("dd-MM-yyyy").parse(date);
                txtDate.setDate(parsedDate); // Use the parsed Date object
            } catch (java.text.ParseException e) {
                JOptionPane.showMessageDialog(this, "Error parsing date: " + e.getMessage());
            }

            txtSMName.setText(smname);
            txtSMID.setText(smid);

            // Expected Delivery Date
            try {
                java.util.Date parsedExDate = new java.text.SimpleDateFormat("dd-MM-yyyy").parse(exdate);
                txtExDate.setDate(parsedExDate);
            } catch (java.text.ParseException e) {
                JOptionPane.showMessageDialog(this, "Error parsing date: " + e.getMessage());
            }

            // Set the selected item in the combo box
            cbStatus.setSelectedItem(status);

            // Set properties for the PROperation
            prop.setDataID(did);
            prop.setPRID(prid);
            prop.setDate(date);
            prop.setSMName(smname);
            prop.setSMID(smid);
            prop.setItemCode(itemcode);
            prop.setQuantity(quantity);
            prop.setExDate(exdate);
            prop.setStatus(status);
        }
    }

    private void tableLoad() {
        DefaultTableModel model = (DefaultTableModel) PRTable.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"No.", "PR ID", "Date", "SM Name", "SM ID", "Items", "Expected Delivery", "Status"});

        String filePath = "src/PurchaseRequisition/PR.txt";
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine(); // Read and ignore the header line

            String line;
            int rowNum = 1;

            while ((line = br.readLine()) != null) {
                // Use regex to properly split by commas outside of curly braces
                String[] dataRow = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");

                if (dataRow.length >= 8) { // Check for required columns
                    String prID = dataRow[0].trim();
                    String date = dataRow[1].trim();
                    String smName = dataRow[2].trim();
                    String smID = dataRow[3].trim();

                    // Extract item codes and quantities - handle the curly braces
                    String itemCodesRaw = dataRow[4].trim().replace("{", "").replace("}", "");
                    String quantitiesRaw = dataRow[5].trim().replace("{", "").replace("}", "");

                    String[] itemCodes = itemCodesRaw.split(",");
                    String[] quantities = quantitiesRaw.split(",");

                    // Build formatted items string
                    StringBuilder itemsBuilder = new StringBuilder();
                    for (int i = 0; i < itemCodes.length && i < quantities.length; i++) {
                        if (i > 0) {
                            itemsBuilder.append(", ");
                        }
                        itemsBuilder.append(itemCodes[i].trim()).append("(").append(quantities[i].trim()).append(")");
                    }

                    String exDate = dataRow[6].trim();
                    String status = dataRow[7].trim();

                    // Add row with row number 
                    model.addRow(new Object[]{rowNum++, prID, date, smName, smID, itemsBuilder.toString(), exDate, status});
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading file for table population: " + e.getMessage());
            e.printStackTrace();
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
    private javax.swing.JTable PRTable;
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
    private javax.swing.JPanel jPanel1;
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
