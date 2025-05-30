/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseRequisition;

import auth.Session;
import auth.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class PurchaseRequisitionCRUDUI extends javax.swing.JFrame {

    private DefaultTableModel model;
    private PurchaseRequisition selectedPR;
    private PurchaseRequisitionManagement management;
    public PurchaseRequisitionCRUDUI(PurchaseRequisition selectedPR) {
        initComponents();
        this.selectedPR = selectedPR;
        this.management = new PurchaseRequisitionManagement();
        
        model = new DefaultTableModel(
                new String[]{ "Item Code", "Quantity"}, 
                0 );
        jTable1.setModel(model); 
        populateTextFields();
        populateItemTable();
        populateItemComboBox();


        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow != -1) {
                    // Update QuantityTextField based on selected row
                    String quantity = model.getValueAt(selectedRow, 1).toString();
                    QuantityTextField.setText(quantity);

                    // Update ItemComboBox selected item based on selected row
                    String itemCode = model.getValueAt(selectedRow, 0).toString();
                    ItemComboBox.setSelectedItem(itemCode);
                }
            }
        });
        
        ItemComboBox.addActionListener(e -> {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                String newItemCode = (String) ItemComboBox.getSelectedItem();
                model.setValueAt(newItemCode, selectedRow, 0);
            }
        });
    }
    
    private void populateTextFields() {
        PRIDTextField.setText(selectedPR.getPrid());
        DateTextField.setText(selectedPR.getDate());
        User currentUser = Session.getInstance().getCurrentUser();
            if(currentUser != null){
                SMNameTextField.setText(currentUser.getUsername());
                SMIDTextField.setText(currentUser.getID());
            }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expectedDate = sdf.parse(selectedPR.getExdate());
            jDateChooser1.setDate(expectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // You might want to show a dialog or log this error
            jDateChooser1.setDate(null); // clear the date if there's a parsing issue
        }
    }
    
    private void populateItemTable() {
        model.setRowCount(0); 

        String[] itemCodes = cleanBracket(selectedPR.getItemcode()).split(",");
        String quantitiesStr = PurchaseRequisitionManagement.getOriginalQuantity(selectedPR.getPrid());
        String[] quantities = cleanBracket(quantitiesStr).split(",");            
        for (int i = 0; i < itemCodes.length; i++) {
            model.addRow(new Object[]{
                itemCodes[i].trim(),
                (i < quantities.length) ? quantities[i].trim() : "0"
            });
        }

        jTable1.setModel(model);
    }
    
    private String cleanBracket(String input) {
        if (input == null) return "";
        return input.replace("{", "").replace("}", "").trim();
    }
    
    private void populateItemComboBox() {
        ItemComboBox.removeAllItems(); // Clear existing items
        for (String itemCode : management.getItemCodeFromItem()) {
            ItemComboBox.addItem(itemCode); // Add each item code
        }
    }
    
    private void deleteRow() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected row?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }
    
    private void saveNewData() {
        int rowCount = model.getRowCount();

        StringBuilder itemCodes = new StringBuilder("{");
        StringBuilder quantitiesStr = new StringBuilder("{");

        int totalQuantity = 0;

        for (int i = 0; i < rowCount; i++) {
            // Assuming column 0 = Item Code, column 1 = Quantity
            String itemCode = model.getValueAt(i, 0).toString();
            itemCodes.append(itemCode);

            Object val = model.getValueAt(i, 1);
            int quantity = 0;
            if (val != null) {
                try {
                    quantity = Integer.parseInt(val.toString());
                    totalQuantity += quantity;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity at row " + (i+1) + ": " + val, 
                                                 "Input Error", JOptionPane.ERROR_MESSAGE);
                    return; // stop saving
                }
            }
            quantitiesStr.append(quantity);

            if (i != rowCount - 1) {
                itemCodes.append(",");
                quantitiesStr.append(",");
            }
        }

        itemCodes.append("}");
        quantitiesStr.append("}");

        // Update your selectedPR object
        selectedPR.setItemcode(itemCodes.toString());
        selectedPR.setQuantity(totalQuantity);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String expectedDate = "";
        if (jDateChooser1.getDate() != null) {
            expectedDate = formatter.format(jDateChooser1.getDate());
        }
        selectedPR.setExdate(expectedDate);

        // Update other fields if needed from your form
        selectedPR.setPrid(PRIDTextField.getText());
        selectedPR.setDate(DateTextField.getText());
        selectedPR.setPrCreatedByName(SMNameTextField.getText());
        selectedPR.setPrCreatedByID(SMIDTextField.getText());

        // Now update in your management class, pass quantities string as well if needed
        management.updatePR(selectedPR, quantitiesStr.toString());

        JOptionPane.showMessageDialog(this, "Purchase Requisition updated successfully.");
    }
    
    private void updateRowFromFields() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }

        String quantityStr = QuantityTextField.getText().trim();
        if (quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantity field cannot be empty.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.");
                return;
            }

            // Update quantity column (index 1)
            model.setValueAt(quantity, selectedRow, 1);

            // Clear quantity text field if you want
            QuantityTextField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.");
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        SaveButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        RefreshButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        PRIDTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        DateTextField = new javax.swing.JTextField();
        SMNameTextField = new javax.swing.JTextField();
        SMIDTextField = new javax.swing.JTextField();
        ItemComboBox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        QuantityTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        CencelButton = new javax.swing.JButton();
        UpdateRowButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Purchase Requisition Management");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Item Code", "Quantity"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        DeleteButton.setText("Delete");
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        RefreshButton.setText("Refresh");
        RefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshButtonActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("PR ID:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Date:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("PR Created By (Name):");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("PR Created By (ID):");

        ItemComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ItemComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemComboBoxActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Item Code:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Quantity:");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Expected Delivery Date:");

        CencelButton.setText("Cancel");
        CencelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CencelButtonActionPerformed(evt);
            }
        });

        UpdateRowButton.setText("Update Row");
        UpdateRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateRowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(SaveButton)
                                .addGap(41, 41, 41)
                                .addComponent(UpdateRowButton)
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel8))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                                    .addComponent(PRIDTextField)
                                    .addComponent(DateTextField)
                                    .addComponent(SMNameTextField)
                                    .addComponent(SMIDTextField)
                                    .addComponent(ItemComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(QuantityTextField))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 315, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(DeleteButton)
                                .addGap(50, 50, 50)
                                .addComponent(RefreshButton)
                                .addGap(150, 150, 150)
                                .addComponent(CencelButton)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(147, 147, 147))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(PRIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(DateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(SMNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(SMIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ItemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(QuantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveButton)
                    .addComponent(DeleteButton)
                    .addComponent(RefreshButton)
                    .addComponent(CencelButton)
                    .addComponent(UpdateRowButton))
                .addGap(82, 82, 82))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        // Delete selected row from table
        deleteRow();
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void ItemComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ItemComboBoxActionPerformed

    private void CencelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CencelButtonActionPerformed
        // cancel
        this.dispose();
    }//GEN-LAST:event_CencelButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // save to pr.txt
        saveNewData();
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void RefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshButtonActionPerformed
        // refresh
        populateTextFields();
        populateItemTable();
    }//GEN-LAST:event_RefreshButtonActionPerformed

    private void UpdateRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateRowButtonActionPerformed
        // Update
        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to update this row?","Confirm Update",JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            updateRowFromFields();
            JOptionPane.showMessageDialog(this, "Row updated successfully.");
        }
    }//GEN-LAST:event_UpdateRowButtonActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CencelButton;
    private javax.swing.JTextField DateTextField;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JComboBox<String> ItemComboBox;
    private javax.swing.JTextField PRIDTextField;
    private javax.swing.JTextField QuantityTextField;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JTextField SMIDTextField;
    private javax.swing.JTextField SMNameTextField;
    private javax.swing.JButton SaveButton;
    private javax.swing.JButton UpdateRowButton;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
