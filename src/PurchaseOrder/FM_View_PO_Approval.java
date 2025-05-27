/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import auth.Session;
import auth.User;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class FM_View_PO_Approval extends javax.swing.JFrame {

    private PO_GenerationManagement PO;
    private DefaultTableModel model;
    public FM_View_PO_Approval() {
        initComponents();
        PO = new PO_GenerationManagement();
        model = new DefaultTableModel(
                new String[]{"No", "PO ID", "PR ID", "Date", "PO Created By (Name)", "PO Created By (ID)", 
                    "PR Created By (Name)", "PR Created By (ID)", "Expected Delivery Date", "Supplier Name",
                    "Supplier ID",  "Item Name", "Item Code", "Quantity", "Status", "PO Approved By (Name)", "PO Approved By (ID)", "Payment Status"}, 
                0 );
        
        POApprovalTable.setModel(model);

        displayPOTable();
        
        Search_TextField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                Search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Search();
            }
            
            private void Search(){
                String keyword = Search_TextField.getText().trim();
                if(keyword.isEmpty()){
                    displayPOTable();
                }else{
                    displayPOTable(keyword);
                }
            }
        });
    }
    
    private void displayPOTable() {
        model.setRowCount(0);
        User currentUser = Session.getInstance().getCurrentUser();
        String currentID = currentUser.getID();
        List<PurchaseOrder> poList = PO_GenerationManagement.getAllPurchaseOrders(); 
        int no = 1;
        for (PurchaseOrder po : poList) {
            if (!po.getPOApprovedByID().equals(currentID)) {
                continue;
            }
            String supplierName = po.getSP_Name().replace("{", "").replace("}", "");
            String supplierID = po.getSP_ID().replace("{", "").replace("}", "");
            String itemName = po.getItemName().replace("{", "").replace("}", "");
            String itemCode = po.getItemCode().replace("{", "").replace("}", "");

            String quantity = PO_GenerationManagement.getOriginalQuantity(po.getPO_ID());

            Object[] rowData = {
                no++,
                po.getPO_ID(),
                po.getPR_ID(),
                po.getDate(),
                po.getPOCreatedByName(),
                po.getPOCreatedByID(),
                po.getPRCreatedByName(),
                po.getPRCreatedByID(),
                po.getExpectedDeliveryDate(),
                supplierName,
                supplierID,
                itemName,
                itemCode,
                quantity,
                po.getStatus(),
                po.getPOApprovedByName(),
                po.getPOApprovedByID(),
                po.getPaymentStatus()
            };
            model.addRow(rowData);
        }
    }  
    
    private void displayPOTable(String keyword) {
        model.setRowCount(0);
        User currentUser = Session.getInstance().getCurrentUser();
        String currentID = currentUser.getID();
        List<PurchaseOrder> poList = PO_GenerationManagement.getAllPurchaseOrders(); 
        int no = 1;
        for (PurchaseOrder po : poList) {
            if (!po.getPOApprovedByID().equals(currentID)) {
                continue;
            }
            
            String combinedFields = (
            po.getPO_ID() + po.getPR_ID() + po.getDate() +
            po.getPOCreatedByName() + po.getPOCreatedByID() +
            po.getPRCreatedByName() + po.getPRCreatedByID() +
            po.getExpectedDeliveryDate() + po.getSP_Name() +
            po.getSP_ID() + po.getItemName() + po.getItemCode() +
            po.getStatus() + po.getPOApprovedByName() +
            po.getPOApprovedByID() + po.getPaymentStatus()
            ).toLowerCase();
            
            if (!combinedFields.contains(keyword.toLowerCase())) {
                continue;
            }
            
            String supplierName = po.getSP_Name().replace("{", "").replace("}", "");
            String supplierID = po.getSP_ID().replace("{", "").replace("}", "");
            String itemName = po.getItemName().replace("{", "").replace("}", "");
            String itemCode = po.getItemCode().replace("{", "").replace("}", "");

            String quantity = PO_GenerationManagement.getOriginalQuantity(po.getPO_ID());

            Object[] rowData = {
                no++,
                po.getPO_ID(),
                po.getPR_ID(),
                po.getDate(),
                po.getPOCreatedByName(),
                po.getPOCreatedByID(),
                po.getPRCreatedByName(),
                po.getPRCreatedByID(),
                po.getExpectedDeliveryDate(),
                supplierName,
                supplierID,
                itemName,
                itemCode,
                quantity,
                po.getStatus(),
                po.getPOApprovedByName(),
                po.getPOApprovedByID(),
                po.getPaymentStatus()
            };
            model.addRow(rowData);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        POApprovalTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        RefreshButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Search_TextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        POApprovalTable.getTableHeader().setReorderingAllowed(false);
        POApprovalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "PO ID", "PR ID", "Date", "SM Name", "SM ID", "PM Name", "PM ID", "Item Code", "Item Name", "Supplier ID", "Supplier Name", "Quantity", "Expected Delivery Date", "PO Status", "FM Name", "FM ID", "Payment Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(POApprovalTable);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        jLabel1.setText("My Puchase Order Approval List");

        RefreshButton.setText("Refresh");
        RefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshButtonActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Search:");

        Search_TextField.setBackground(new java.awt.Color(204, 204, 204));
        Search_TextField.setForeground(new java.awt.Color(0, 0, 0));
        Search_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Search_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Search_TextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(569, 569, 569)
                .addComponent(RefreshButton)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(Search_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Search_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(RefreshButton)
                .addGap(45, 45, 45))
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

    private void RefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshButtonActionPerformed
        // Refresh
        displayPOTable();
    }//GEN-LAST:event_RefreshButtonActionPerformed

    private void Search_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search_TextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(FM_View_PO_Approval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FM_View_PO_Approval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FM_View_PO_Approval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FM_View_PO_Approval.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FM_View_PO_Approval().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable POApprovalTable;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JTextField Search_TextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
