/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import InventoryManagement.Inventory;
import PurchaseRequisition.PROperation;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */


public class PO_GenerationUI extends javax.swing.JFrame {

    /**
     * Creates new form PO_GenerationUI
     */
    private PO_GenerationManagement management;
    private DefaultTableModel tableModel;
    private PROperation existingPR;
    
    public PO_GenerationUI(PROperation existingPR) {
        initComponents();
        this.existingPR = existingPR;
        management = new PO_GenerationManagement();
        tableModel = new DefaultTableModel(
            new Object[]{"No.", "Item Name", "Item Code", "Supplier Name", "Supplier ID", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);

        displayPRDetails();
        loadSupplierIDinComboBox();
        itemsFromSupplier();
        doubleClickRow();
        displayItemDetailsTxt();
        
        SupplierIDComboBox.addActionListener(e -> itemsFromSupplier());
        ItemCodeComboBox.addActionListener(e -> displayItemDetailsTxt());
        
        if (SupplierIDComboBox.getItemCount() > 0)
        SupplierIDComboBox.setSelectedIndex(0);

        if (ItemCodeComboBox.getItemCount() > 0)
        ItemCodeComboBox.setSelectedIndex(0);

        ItemNametxt.setText(""); 
        SupplierNametxt.setText("");
        quantitytxt.setText("");
    }
    
    
 
    
    private void displayPRDetails(){
        POIDtxt.setText(management.generatePO_ID());
        PRIDtxt.setText(existingPR.getPRID());
        Datetxt.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        SMNAMEtxt.setText(existingPR.getSMName());
        SMIDtxt.setText(existingPR.getSMID());
        PMNAMEtxt.setText("Michael");
        PMIDtxt.setText("PM001");
        
        try {
            if (existingPR.getExDate() != null && !existingPR.getExDate().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date expectedDate = sdf.parse(existingPR.getExDate());
                expectedDatetxt.setDate(expectedDate);
            } else {
                System.out.println("Expected delivery date is empty!");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        

        String[] itemCodes = existingPR.getItemCode().split("\\r?\\n");
        String[] quantities = existingPR.getQuantity().split("\\r?\\n");

        int no = 1;
        for (int i = 0; i < itemCodes.length; i++) {
            String code = itemCodes[i].trim();
            String qty = (i < quantities.length) ? quantities[i].trim() : "";
            
            System.out.println("Looking up item: [" + code + "]");

            Inventory item = management.getItemDetailsByCode(code);
            if (item != null) {
                tableModel.addRow(new Object[]{
                    no++,
                    item.getItemName(),
                    item.getItemCode(),
                    item.getSupplierName(),
                    item.getSupplierID(),
                    qty
                });
            } else {
                System.out.println("Item not found: " + code);
            }
        }
    }
    
    private void loadSupplierIDinComboBox(){
        SupplierIDComboBox.removeAllItems();
        for(String id : management.getSupplierID()){
            SupplierIDComboBox.addItem(id);
        }
    }
    
    private void itemsFromSupplier(){
        String supplierID = (String) SupplierIDComboBox.getSelectedItem();
        if(supplierID == null){
            return;
        }
        
        ItemCodeComboBox.removeAllItems();
        for(String itemCode : management.getItemCodeFromSupplier(supplierID)){
            ItemCodeComboBox.addItem(itemCode);
        }
    }
    
    private void displayItemDetailsTxt(){
        String itemCode = (String) ItemCodeComboBox.getSelectedItem();
        if(itemCode == null){
            return;
        }
        
        Inventory inv = management.getInventoryFromItemCode(itemCode);
        if(inv != null){
            ItemNametxt.setText(inv.getItemName());
            SupplierNametxt.setText(inv.getSupplierName());
        }
    }
    
    private void dateChooserValidation() {
        Date expectedDate = expectedDatetxt.getDate();
        if (expectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get today with time stripped
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (expectedDate.before(today.getTime())) {
            JOptionPane.showMessageDialog(this, "Date cannot be before today!", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            expectedDatetxt.setDate(null); // Clear invalid date
        }
    }

    
    private void addItemByRow(){
        String itemCode = (String) ItemCodeComboBox.getSelectedItem();
        String itemName = ItemNametxt.getText();
        String supplierID = (String) SupplierIDComboBox.getSelectedItem();
        String supplierName = SupplierNametxt.getText();
        String quantity = quantitytxt.getText();
        
        if(itemCode == null || itemName.isEmpty() || supplierID == null || supplierName.isEmpty() || quantity.isEmpty()){
            JOptionPane.showMessageDialog(this, "There are empty fields.", "ERROR MESSAGE", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        for(int i = 0; i < tableModel.getRowCount(); i++){
            String itemCodeinTable = (String) tableModel.getValueAt(i, 2);
            String supplierIDinTable = (String) tableModel.getValueAt(i, 4);
            
            if(itemCode.equals(itemCodeinTable) && supplierID.equals(supplierIDinTable)){
                JOptionPane.showMessageDialog(this, "Cannot add same item in table, select the item to update it" , "ERROR MESSAGE", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        
        try{
            int rowNo = tableModel.getRowCount() + 1;
            int qty = Integer.parseInt(quantity);
            tableModel.addRow(new Object[]{
                rowNo,
                itemName,
                itemCode,
                supplierName,
                supplierID,
                qty
            });
            JOptionPane.showMessageDialog(this, "Item successfully added");
            clearFieldAfterAction();
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Quantity must be a number", "ERROR MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateItemByRow(){
        int selected = jTable1.getSelectedRow();
        if(selected == -1){
            JOptionPane.showMessageDialog(this, "Select a row first to update item.", "INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String itemCode = (String) ItemCodeComboBox.getSelectedItem();
        String itemName = ItemNametxt.getText();
        String supplierID = (String) SupplierIDComboBox.getSelectedItem();
        String supplierName = SupplierNametxt.getText();
        String quantity = quantitytxt.getText();
        
        if(itemCode == null || itemName.isEmpty() || supplierID == null || supplierName.isEmpty() || quantity.isEmpty()){
            JOptionPane.showMessageDialog(this, "There are empty fields.", "ERROR MESSAGE", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        try{
            int qty = Integer.parseInt(quantity);
            tableModel.setValueAt(itemName, selected, 1);
            tableModel.setValueAt(itemCode, selected, 2);
            tableModel.setValueAt(supplierName, selected, 3);
            tableModel.setValueAt(supplierID, selected, 4);
            tableModel.setValueAt(qty, selected, 5);
            JOptionPane.showMessageDialog(this, "Item successfully updated.");
            clearFieldAfterAction();
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Quantity must be a number", "ERROR MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteItemByRow(){
        int selected = jTable1.getSelectedRow();
        if(selected == -1){
            JOptionPane.showMessageDialog(this, "Select a row first to delete item.", "INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(selected != -1){
            tableModel.removeRow(selected);
        }
        JOptionPane.showMessageDialog(this, "Item successfully deleted.");
        clearFieldAfterAction();
    }
    
    private void clearTextField(){
        SupplierIDComboBox.setSelectedIndex(0);
        ItemCodeComboBox.setSelectedIndex(0);
        ItemNametxt.setText(""); 
        SupplierNametxt.setText("");
        quantitytxt.setText("");
        jTable1.clearSelection();
    }
    
    private void clearFieldAfterAction(){
        SupplierIDComboBox.setSelectedIndex(0);
        ItemCodeComboBox.setSelectedIndex(0);
        ItemNametxt.setText(""); 
        SupplierNametxt.setText("");
        quantitytxt.setText("");
        jTable1.clearSelection();
    }
    
    private void refreshRowNumbers() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0); // Column 0 = "No."
        }
    }
    
    private void generatePO(){
        String PO_ID = management.generatePO_ID();
        String PR_ID = PRIDtxt.getText();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Date expectedDate = expectedDatetxt.getDate();
        if (expectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select an expected delivery date.", "INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String expectedDeliveryDate = new SimpleDateFormat("dd-MM-yyyy").format(expectedDate);
        String PM_Name = PMNAMEtxt.getText();
        String PM_ID = PMIDtxt.getText();
        String SM_Name = SMNAMEtxt.getText();
        String SM_ID = SMIDtxt.getText();
        
        if(expectedDeliveryDate.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter an expected delivery date.", "INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        List<PurchaseOrderItem> items = new ArrayList<>();
        for(int i = 0; i < tableModel.getRowCount(); i++){
            String itemName = (String) tableModel.getValueAt(i, 1);
            String itemCode = (String) tableModel.getValueAt(i, 2);
            String supplierName = (String) tableModel.getValueAt(i, 3);
            String supplierID = (String) tableModel.getValueAt(i, 4);
            int quantity = Integer.parseInt(tableModel.getValueAt(i, 5).toString());
            
            items.add(new PurchaseOrderItem(itemCode, itemName, supplierID, supplierName, quantity));
        }
        
        if(items.isEmpty()){
            JOptionPane.showMessageDialog(this, "You must at least have one item in table only can generate PO.", "INFORMATION MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        management.saveToPOtxt(PO_ID, PR_ID, date, PM_Name, PM_ID, SM_Name, SM_ID, expectedDeliveryDate, items);
        management.updatePR(PR_ID, items, expectedDeliveryDate);
        JOptionPane.showMessageDialog(this, "Purchase Order: " + PO_ID + " generated and PR updated successfully!", "SUCCESS INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
    
     //----- Double click the table row to add the data to the textBox / comboBox -----//
    public final void doubleClickRow() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        String itemName = jTable1.getValueAt(selectedRow, 1).toString();
                        String itemCode = jTable1.getValueAt(selectedRow, 2).toString();
                        String supplierName = jTable1.getValueAt(selectedRow, 3).toString();
                        String supplierID = jTable1.getValueAt(selectedRow, 4).toString();
                        String quantity = jTable1.getValueAt(selectedRow, 5).toString();                      
                        SupplierIDComboBox.setSelectedItem(supplierID);
                        SwingUtilities.invokeLater(() -> {
                            ItemCodeComboBox.setSelectedItem(itemCode);
                        });
                        ItemNametxt.setText(itemName);
                        SupplierNametxt.setText(supplierName);
                        quantitytxt.setText(quantity);
                    }
                }
            }
        });
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
        POIDtxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        PRIDtxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        Datetxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        PMNAMEtxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        PMIDtxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        SMNAMEtxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        SMIDtxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        SupplierNametxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        SupplierIDComboBox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        ItemNametxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        ItemCodeComboBox = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        quantitytxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        expectedDatetxt = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(210, 100));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("OWSB");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel1)
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Arial", 2, 24)); // NOI18N
        jLabel2.setText("Generate Purchase Order");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("PO ID:");

        POIDtxt.setEditable(false);
        POIDtxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        POIDtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                POIDtxtActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("PR ID:");

        PRIDtxt.setEditable(false);
        PRIDtxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        PRIDtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PRIDtxtActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Date:");

        Datetxt.setEditable(false);
        Datetxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("PM Name:");

        PMNAMEtxt.setEditable(false);
        PMNAMEtxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("PM ID:");

        PMIDtxt.setEditable(false);
        PMIDtxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText("SM Name:");

        SMNAMEtxt.setEditable(false);
        SMNAMEtxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel9.setText("SM ID:");

        SMIDtxt.setEditable(false);
        SMIDtxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText("Supplier Name: ");

        SupplierNametxt.setEditable(false);
        SupplierNametxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        SupplierNametxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupplierNametxtActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText("Supplier ID:");

        SupplierIDComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        SupplierIDComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "                    " }));
        SupplierIDComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupplierIDComboBoxActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setText("Item Name:");

        ItemNametxt.setEditable(false);
        ItemNametxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ItemNametxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemNametxtActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText("Item Code:");

        ItemCodeComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ItemCodeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "                    " }));
        ItemCodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemCodeComboBoxActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setText("Quantity:");

        quantitytxt.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Item Name", "Item Code", "Supplier Name", "Supplier ID", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton1.setText("Add Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setText("Update Item");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton3.setText("Generate Purchase Order");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton4.setText("Delete Item");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton5.setText("Clear Field");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel15.setText("Expected Delivery Date:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(267, 267, 267))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(231, 231, 231)
                                                .addComponent(jButton5))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2)))
                                        .addGap(40, 40, 40)
                                        .addComponent(jButton4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addComponent(jLabel7)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(PMIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addComponent(jLabel6)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(PMNAMEtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(jLabel3)
                                                                    .addGap(54, 54, 54))
                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                    .addComponent(jLabel5)
                                                                    .addGap(60, 60, 60)))
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(Datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(POIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel10)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGap(13, 13, 13)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(jLabel12)
                                                                    .addComponent(jLabel14))))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(SupplierNametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(quantitytxt)
                                                                .addComponent(ItemNametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                                .addGap(27, 27, 27)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel4)
                                                            .addComponent(jLabel8)
                                                            .addComponent(jLabel9))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(PRIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(SMNAMEtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(SMIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel13)
                                                            .addComponent(jLabel11))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(SupplierIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(ItemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(expectedDatetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(124, 124, 124))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(POIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(PRIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(Datetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(SMNAMEtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(PMNAMEtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(SMIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(PMIDtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(SupplierNametxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11)
                                    .addComponent(SupplierIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(ItemNametxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(ItemCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(quantitytxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(expectedDatetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(55, 55, 55))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(78, 78, 78))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ItemNametxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemNametxtActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ItemNametxtActionPerformed

    private void SupplierIDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupplierIDComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SupplierIDComboBoxActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        clearTextField();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void POIDtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_POIDtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_POIDtxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dateChooserValidation();
        addItemByRow();
        refreshRowNumbers();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        dateChooserValidation();
        updateItemByRow();
        refreshRowNumbers();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        deleteItemByRow();
        refreshRowNumbers();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void PRIDtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PRIDtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PRIDtxtActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        generatePO();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void ItemCodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemCodeComboBoxActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ItemCodeComboBoxActionPerformed

    private void SupplierNametxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupplierNametxtActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_SupplierNametxtActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(PO_GenerationUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PO_GenerationUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PO_GenerationUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PO_GenerationUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PO_GenerationUI().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Datetxt;
    private javax.swing.JComboBox<String> ItemCodeComboBox;
    private javax.swing.JTextField ItemNametxt;
    private javax.swing.JTextField PMIDtxt;
    private javax.swing.JTextField PMNAMEtxt;
    private javax.swing.JTextField POIDtxt;
    private javax.swing.JTextField PRIDtxt;
    private javax.swing.JTextField SMIDtxt;
    private javax.swing.JTextField SMNAMEtxt;
    private javax.swing.JComboBox<String> SupplierIDComboBox;
    private javax.swing.JTextField SupplierNametxt;
    private com.toedter.calendar.JDateChooser expectedDatetxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField quantitytxt;
    // End of variables declaration//GEN-END:variables
}
