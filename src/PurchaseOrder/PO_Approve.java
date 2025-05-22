/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import InventoryManagement.Inventory;
import auth.Session;
import auth.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class PO_Approve extends javax.swing.JFrame {

    private DefaultTableModel model;
    private PurchaseOrder selectedPO;
    private PO_GenerationManagement management;
    private PO_Approval PoApprovalUi;
            
    public PO_Approve(PurchaseOrder selectedRow) {
        initComponents();
        this.selectedPO = selectedRow;
        this.management = new PO_GenerationManagement();
        displayLockedFields();
        
        model = new DefaultTableModel(
                new String[]{ "Supplier Name", "Supplier ID", "Item Code", "Item Name", "Quantity", "Expected Delivery Date"}, 
                0 );
        PurchaseOrderApproveTable.setModel(model); 
        displayTable(selectedRow);
        fillTextFields(selectedRow);
        loadSupplierIDinComboBox();
        
        jComboBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemsFromSupplier(); // populate ItemComboBox
            }
        });
         
        ItemComcoBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayItemDetailsTxt(); // update item/supplier name text fields
            }
        });
        
        PurchaseOrderApproveTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && PurchaseOrderApproveTable.getSelectedRow() != -1) {
                    int selectedRow = PurchaseOrderApproveTable.getSelectedRow();

                    String supplierName = (String) model.getValueAt(selectedRow, 0);
                    String supplierID = (String) model.getValueAt(selectedRow, 1);
                    String itemCode = (String) model.getValueAt(selectedRow, 2);
                    String itemName = (String) model.getValueAt(selectedRow, 3);
                    String quantity = (String) model.getValueAt(selectedRow, 4);
                    

                    SupplierName_TextField.setText(supplierName);
                    ItemName_TextField.setText(itemName);

                    jComboBox1.setSelectedItem(supplierID);

                    itemsFromSupplier();
                    ItemComcoBox.setSelectedItem(itemCode);

                    Quantity_TextField.setText(quantity); 
                }
            }
        });
    }

    private void displayTable(PurchaseOrder selectedRow){
        model.setRowCount(0);
        String[] supplierNames = cleanBracket(selectedRow.getSP_Name()).split(",");
        String[] supplierIDs = cleanBracket(selectedRow.getSP_ID()).split(",");
        String[] itemCodes = cleanBracket(selectedRow.getItemCode()).split(",");
        String[] itemNames = cleanBracket(selectedRow.getItemName()).split(",");

        String quantityStr = PO_GenerationManagement.getOriginalQuantity(selectedRow.getPO_ID());
        String[] quantities = cleanBracket(quantityStr).split(",");
        String expectedDeliveryDate = selectedRow.getExpectedDeliveryDate();

        for (int i = 0; i < supplierNames.length; i++) {
            model.addRow(new Object[]{
                supplierNames[i].trim(),
                supplierIDs[i].trim(),
                itemCodes[i].trim(),
                itemNames[i].trim(),
                quantities[i].trim(),
                expectedDeliveryDate.trim()
            });
        }

        PurchaseOrderApproveTable.setModel(model);
    }
    
    private void displayLockedFields() {
        POID_TextField.setEditable(false);
        PRID_TextField.setEditable(false);
        Date_TextField.setEditable(false);
        SMName_TextField.setEditable(false);
        SMID_TextField.setEditable(false);
        PMName_TextField.setEditable(false);
        PMID_TextField.setEditable(false);
        jDateChooser1.setEnabled(false);
    }
    
    private void fillTextFields(PurchaseOrder selectedRow) {
        POID_TextField.setText(selectedRow.getPO_ID());
        PRID_TextField.setText(selectedRow.getPR_ID());
        PMName_TextField.setText(selectedRow.getPM_Name());
        PMID_TextField.setText(selectedRow.getPM_ID());
        SMName_TextField.setText(selectedRow.getSM_Name());
        SMID_TextField.setText(selectedRow.getSM_ID());
        User currentUser = Session.getInstance().getCurrentUser();
            if(currentUser != null){
                FMName_TextField.setText(currentUser.getUsername());
                FMID_TextField.setText(currentUser.getID());
            }
        Date_TextField.setText(selectedRow.getDate());
        String dateStr = selectedRow.getExpectedDeliveryDate(); // e.g. "27-7-2025"
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        
        try {
            Date date = formatter.parse(dateStr);
            jDateChooser1.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            jDateChooser1.setDate(null);
        }
    }

    private String cleanBracket(String input) {
        return input == null ? "" : input.replace("{", "").replace("}", "");
    }
    
    private void loadSupplierIDinComboBox(){
        jComboBox1.removeAllItems();
        for(String id : management.getSupplierID()){
            jComboBox1.addItem(id);
        }
    }
    
    private void itemsFromSupplier(){
        String supplierID = (String) jComboBox1.getSelectedItem();
        if(supplierID == null){
            return;
        }
        
        ItemComcoBox.removeAllItems();
        for(String itemCode : management.getItemCodeFromSupplier(supplierID)){
            ItemComcoBox.addItem(itemCode);
        }
    }
    
    private void displayItemDetailsTxt(){
        String itemCode = (String) ItemComcoBox.getSelectedItem();
        if(itemCode == null){
            return;
        }
        
        Inventory inv = management.getInventoryFromItemCode(itemCode);
        if(inv != null){
            ItemName_TextField.setText(inv.getItemName());
            SupplierName_TextField.setText(inv.getSupplierName());
        }
    }
    
    private void updateRowFromFields() {
        int selectedRow = PurchaseOrderApproveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }

        String supplierName = SupplierName_TextField.getText().trim();
        String supplierID = (String) jComboBox1.getSelectedItem();
        String itemCode = (String) ItemComcoBox.getSelectedItem();
        String itemName = ItemName_TextField.getText().trim();
        String quantity = Quantity_TextField.getText().trim();
        String expectedDate = new SimpleDateFormat("d-M-yyyy").format(jDateChooser1.getDate());

        model.setValueAt(supplierName, selectedRow, 0);
        model.setValueAt(supplierID, selectedRow, 1);
        model.setValueAt(itemCode, selectedRow, 2);
        model.setValueAt(itemName, selectedRow, 3);
        model.setValueAt(quantity, selectedRow, 4);
        model.setValueAt(expectedDate, selectedRow, 5);
    }
    
    private void saveNewData(){
        int rowCount = model.getRowCount();

        StringBuilder supplierNames = new StringBuilder("{");
        StringBuilder supplierIDs = new StringBuilder("{");
        StringBuilder itemCodes = new StringBuilder("{");
        StringBuilder itemNames = new StringBuilder("{");
        StringBuilder quantitiesStr = new StringBuilder("{"); 

        int totalQuantity = 0;

        for (int i = 0; i < rowCount; i++) {
            supplierNames.append(model.getValueAt(i, 0));
            supplierIDs.append(model.getValueAt(i, 1));
            itemCodes.append(model.getValueAt(i, 2));
            itemNames.append(model.getValueAt(i, 3));

            Object val = model.getValueAt(i, 4);
            if (val != null) {
                try {
                    int q = Integer.parseInt(val.toString());
                    totalQuantity += q;
                    quantitiesStr.append(q);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity at row " + (i+1) + ": " + val, 
                                                 "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                quantitiesStr.append("0"); // fallback
            }

            if (i != rowCount - 1) {
                supplierNames.append(",");
                supplierIDs.append(",");
                itemCodes.append(",");
                itemNames.append(",");
                quantitiesStr.append(",");
            }
        }

        supplierNames.append("}");
        supplierIDs.append("}");
        itemCodes.append("}");
        itemNames.append("}");
        quantitiesStr.append("}");

        selectedPO.setSP_Name(supplierNames.toString());
        selectedPO.setSP_ID(supplierIDs.toString());
        selectedPO.setItemCode(itemCodes.toString());
        selectedPO.setItemName(itemNames.toString());
        selectedPO.setQuantity(totalQuantity);

        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        String expectedDate = formatter.format(jDateChooser1.getDate());
        selectedPO.setExpectedDeliveryDate(expectedDate);

        management.updatePO(selectedPO, quantitiesStr.toString());

        JOptionPane.showMessageDialog(this, "Purchase order saved successfully!");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        PurchaseOrderApproveTable = new javax.swing.JTable();
        POID_TextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        PRID_TextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Date_TextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        SMName_TextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        SMID_TextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        PMName_TextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        PMID_TextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        SupplierName_TextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        ItemName_TextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        Quantity_TextField = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        CancelButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        ItemComcoBox = new javax.swing.JComboBox<>();
        UpdateRowButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        ApproveButton = new javax.swing.JButton();
        RejectButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        FMName_TextField = new javax.swing.JTextField();
        FMID_TextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        PurchaseOrderApproveTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PurchaseOrderApproveTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Item Code", "Item Name", "Supplier ID", "Supplier Name", "Quantity", "Expected Delivery Date", "Purchase Order Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(PurchaseOrderApproveTable);

        POID_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                POID_TextFieldActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("PO ID:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("PR ID:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Date:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("SM Name:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("SM ID:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("PM Name:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("PM ID:");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Supplier Name:");

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Item Name:");

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Quantity:");

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Expected Delivery Date:");

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Supplier ID:");

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Item ID:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ItemComcoBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        UpdateRowButton.setText("Update Row");
        UpdateRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateRowButtonActionPerformed(evt);
            }
        });

        SaveButton.setText("Save All");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        ApproveButton.setText("Approve");
        ApproveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApproveButtonActionPerformed(evt);
            }
        });

        RejectButton.setText("Reject");
        RejectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RejectButtonActionPerformed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Finance Manager Name:");

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Finance Manager ID:");

        FMName_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FMName_TextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1089, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(Date_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(POID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(SMID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addGap(18, 18, 18)
                                            .addComponent(SMName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(78, 78, 78)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(PRID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(PMID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addGap(18, 18, 18)
                                            .addComponent(PMName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ApproveButton)
                                .addGap(65, 65, 65)
                                .addComponent(RejectButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FMID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(FMName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(93, 93, 93)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(SupplierName_TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(ItemName_TextField))
                                .addGap(46, 46, 46)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ItemComcoBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(53, 53, 53)
                                .addComponent(Quantity_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(UpdateRowButton)
                                .addGap(53, 53, 53)
                                .addComponent(SaveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CancelButton)
                                .addGap(8, 8, 8)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(POID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(PRID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(Date_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(SMName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(SMID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(PMName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(PMID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(SupplierName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(ItemName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(ItemComcoBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(Quantity_TextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(FMName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(FMID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelButton)
                    .addComponent(UpdateRowButton)
                    .addComponent(SaveButton)
                    .addComponent(ApproveButton)
                    .addComponent(RejectButton))
                .addGap(23, 23, 23))
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

    private void POID_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_POID_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_POID_TextFieldActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        // Cancel
        this.dispose();

        PO_Approval poApprovalUI = new PO_Approval();
        poApprovalUI.setVisible(true);
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void UpdateRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateRowButtonActionPerformed
        // Update
        updateRowFromFields();
    }//GEN-LAST:event_UpdateRowButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // Save All
        saveNewData();
        this.dispose();
        PO_Approval poApprovalUi = new PO_Approval();
        poApprovalUi.setVisible(true);
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void ApproveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApproveButtonActionPerformed
        // Approve
        String poID = POID_TextField.getText().trim(); // <-- you implement this to get PO ID from UI
        if (!poID.isEmpty()) {
            management.updatePOStatus(poID, "Complete");
            JOptionPane.showMessageDialog(this, "PO " + poID + " approved successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a PO ID.");
        }
        this.dispose();
        PO_Approval poApprovalUi = new PO_Approval();
        poApprovalUi.setVisible(true);
    }//GEN-LAST:event_ApproveButtonActionPerformed

    private void RejectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RejectButtonActionPerformed
        // Reject
        String poID = POID_TextField.getText().trim();  // <- get POID from text field
        if (!poID.isEmpty()) {
            management.updatePOStatus(poID, "Rejected");
            JOptionPane.showMessageDialog(this, "PO " + poID + " rejected.");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a PO ID.");
        }
        this.dispose();
        PO_Approval poApprovalUi = new PO_Approval();
        poApprovalUi.setVisible(true);
    }//GEN-LAST:event_RejectButtonActionPerformed

    private void FMName_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FMName_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FMName_TextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ApproveButton;
    private javax.swing.JButton CancelButton;
    private javax.swing.JTextField Date_TextField;
    private javax.swing.JTextField FMID_TextField;
    private javax.swing.JTextField FMName_TextField;
    private javax.swing.JComboBox<String> ItemComcoBox;
    private javax.swing.JTextField ItemName_TextField;
    private javax.swing.JTextField PMID_TextField;
    private javax.swing.JTextField PMName_TextField;
    private javax.swing.JTextField POID_TextField;
    private javax.swing.JTextField PRID_TextField;
    private javax.swing.JTable PurchaseOrderApproveTable;
    private javax.swing.JTextField Quantity_TextField;
    private javax.swing.JButton RejectButton;
    private javax.swing.JTextField SMID_TextField;
    private javax.swing.JTextField SMName_TextField;
    private javax.swing.JButton SaveButton;
    private javax.swing.JTextField SupplierName_TextField;
    private javax.swing.JButton UpdateRowButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
