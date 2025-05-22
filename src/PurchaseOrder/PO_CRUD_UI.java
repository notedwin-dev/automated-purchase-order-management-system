/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import InventoryManagement.Inventory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class PO_CRUD_UI extends javax.swing.JFrame {

    private DefaultTableModel model;
    private PurchaseOrder selectedPO;
    private PO_GenerationManagement management;
    private PO_List_UI listUI;
    
    public PO_CRUD_UI(PurchaseOrder selectedRow) {
        initComponents();
        this.selectedPO = selectedRow;
        this.management = new PO_GenerationManagement();
        lockFields();

        model = new DefaultTableModel(
                new String[]{ "Supplier Name", "Supplier ID", "Item Code", "Item Name", "Quantity", "Expected Delivery Date"}, 
                0 );
        
        PurchaseOrderTableCRUD.setModel(model);  
        displayPOCRUDTable(selectedRow);
        fillTextFields(selectedRow);
        loadSupplierIDinComboBox();
        
        SupplierComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemsFromSupplier(); // populate ItemComboBox
            }
        });
         
        ItemComcoBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayItemDetailsTxt(); // update item/supplier name text fields
            }
        });
        
        PurchaseOrderTableCRUD.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && PurchaseOrderTableCRUD.getSelectedRow() != -1) {
                    int selectedRow = PurchaseOrderTableCRUD.getSelectedRow();

                    String supplierName = (String) model.getValueAt(selectedRow, 0);
                    String supplierID = (String) model.getValueAt(selectedRow, 1);
                    String itemCode = (String) model.getValueAt(selectedRow, 2);
                    String itemName = (String) model.getValueAt(selectedRow, 3);
                    String quantity = (String) model.getValueAt(selectedRow, 4);
                    

                    SupplierName_TextField.setText(supplierName);
                    ItemName_TextField.setText(itemName);

                    SupplierComboBox.setSelectedItem(supplierID);

                    itemsFromSupplier();
                    ItemComcoBox.setSelectedItem(itemCode);

                    Quantity_TextField.setText(quantity); 
                }
            }
        });
    }
    
    private void displayPOCRUDTable(PurchaseOrder selectedRow) {
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

        PurchaseOrderTableCRUD.setModel(model);
    }
    
    private void lockFields() {
        PurchaseOrderID_TextField.setEditable(false);
        PurchaseRequisitionID_TextField.setEditable(false);
        Date_TextField.setEditable(false);
        PurchaseManagerName_TextField.setEditable(false);
        PurchaseManagerID_TextField.setEditable(false);
        SalesManagerName_TextField.setEditable(false);
        SalesManagerID_TextField.setEditable(false);
    }

    private void fillTextFields(PurchaseOrder selectedRow) {
        PurchaseOrderID_TextField.setText(selectedRow.getPO_ID());
        PurchaseRequisitionID_TextField.setText(selectedRow.getPR_ID());
        PurchaseManagerName_TextField.setText(selectedRow.getPM_Name());
        PurchaseManagerID_TextField.setText(selectedRow.getPM_ID());
        SalesManagerName_TextField.setText(selectedRow.getSM_Name());
        SalesManagerID_TextField.setText(selectedRow.getSM_ID());
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
        SupplierComboBox.removeAllItems();
        for(String id : management.getSupplierID()){
            SupplierComboBox.addItem(id);
        }
    }
    
    private void itemsFromSupplier(){
        String supplierID = (String) SupplierComboBox.getSelectedItem();
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
        int selectedRow = PurchaseOrderTableCRUD.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }

        String supplierName = SupplierName_TextField.getText().trim();
        String supplierID = (String) SupplierComboBox.getSelectedItem();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        PurchaseOrderID_TextField = new javax.swing.JTextField();
        Date_TextField = new javax.swing.JTextField();
        PurchaseManagerName_TextField = new javax.swing.JTextField();
        PurchaseManagerID_TextField = new javax.swing.JTextField();
        PurchaseRequisitionID_TextField = new javax.swing.JTextField();
        SalesManagerName_TextField = new javax.swing.JTextField();
        SalesManagerID_TextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        SupplierName_TextField = new javax.swing.JTextField();
        ItemName_TextField = new javax.swing.JTextField();
        Quantity_TextField = new javax.swing.JTextField();
        SupplierComboBox = new javax.swing.JComboBox<>();
        ItemComcoBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        PurchaseOrderTableCRUD = new javax.swing.JTable();
        SaveButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        UpdateRowButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 720));

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("PO ID:");

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("PR ID:");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Date:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("SM Name:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("PM Name:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("PM ID:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("SM ID:");

        PurchaseOrderID_TextField.setBackground(new java.awt.Color(204, 204, 204));
        PurchaseOrderID_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Date_TextField.setBackground(new java.awt.Color(204, 204, 204));
        Date_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        PurchaseManagerName_TextField.setBackground(new java.awt.Color(204, 204, 204));
        PurchaseManagerName_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        PurchaseManagerID_TextField.setBackground(new java.awt.Color(204, 204, 204));
        PurchaseManagerID_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        PurchaseRequisitionID_TextField.setBackground(new java.awt.Color(204, 204, 204));
        PurchaseRequisitionID_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SalesManagerName_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SalesManagerName_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SalesManagerID_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SalesManagerID_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Supplier Name:");

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Supplier ID:");

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Item Name:");

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Item ID:");

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Quantity:");

        SupplierName_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierName_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ItemName_TextField.setBackground(new java.awt.Color(204, 204, 204));
        ItemName_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Quantity_TextField.setBackground(new java.awt.Color(204, 204, 204));
        Quantity_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SupplierComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ItemComcoBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        PurchaseOrderTableCRUD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PurchaseOrderTableCRUD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Supplier Name", "Supplier ID", "Item Name", "Item ID", "Quantity", "Expected Delivery Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(PurchaseOrderTableCRUD);

        SaveButton.setText("Save All");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Expected Delivery Date:");

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
                .addGap(197, 197, 197)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Date_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(28, 28, 28)
                                    .addComponent(PurchaseOrderID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(PurchaseManagerName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(32, 32, 32)
                                .addComponent(PurchaseManagerID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PurchaseRequisitionID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(SalesManagerName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(SalesManagerID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(401, 401, 401)
                                .addComponent(SaveButton)
                                .addGap(123, 123, 123)
                                .addComponent(CancelButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(UpdateRowButton)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(38, 38, 38)
                                    .addComponent(ItemName_TextField))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addGap(53, 53, 53)
                                    .addComponent(Quantity_TextField))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, 18)
                                    .addComponent(SupplierName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(66, 66, 66)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(SupplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ItemComcoBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(27, 27, 27)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(PurchaseOrderID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(Date_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(PurchaseManagerName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(PurchaseManagerID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(PurchaseRequisitionID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(54, 54, 54)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(SalesManagerName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addComponent(SalesManagerID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(SupplierName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(ItemName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(Quantity_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(ItemComcoBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 287, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveButton)
                    .addComponent(CancelButton)
                    .addComponent(UpdateRowButton))
                .addContainerGap())
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

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        // Cancel
        // Close this window
        this.dispose();

        // Open PO_List_UI window
        PO_List_UI poListUI = new PO_List_UI();
        poListUI.setVisible(true);
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // Save All
        saveNewData();
        this.dispose();
        PO_List_UI poListUI = new PO_List_UI();
        poListUI.setVisible(true);
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void UpdateRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateRowButtonActionPerformed
        // Update Row
        updateRowFromFields();
    }//GEN-LAST:event_UpdateRowButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JTextField Date_TextField;
    private javax.swing.JComboBox<String> ItemComcoBox;
    private javax.swing.JTextField ItemName_TextField;
    private javax.swing.JTextField PurchaseManagerID_TextField;
    private javax.swing.JTextField PurchaseManagerName_TextField;
    private javax.swing.JTextField PurchaseOrderID_TextField;
    private javax.swing.JTable PurchaseOrderTableCRUD;
    private javax.swing.JTextField PurchaseRequisitionID_TextField;
    private javax.swing.JTextField Quantity_TextField;
    private javax.swing.JTextField SalesManagerID_TextField;
    private javax.swing.JTextField SalesManagerName_TextField;
    private javax.swing.JButton SaveButton;
    private javax.swing.JComboBox<String> SupplierComboBox;
    private javax.swing.JTextField SupplierName_TextField;
    private javax.swing.JButton UpdateRowButton;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
