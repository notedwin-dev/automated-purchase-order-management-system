/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ReportManagement;

import InventoryManagement.InventoryManager;
import PurchaseOrder.PurchaseOrder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Payment_UI extends javax.swing.JFrame {

    private DefaultTableModel model;
    public Payment_UI() {
        initComponents();
        model = new DefaultTableModel(
                new String[]{"No", "PO ID", "PO Status", "Supplier Name",
                    "Supplier ID",  "Item Name", "Item Code", "Quantity", "Payment Status", "Total Payment"}, 
                0 );
        
        PaymentTable.setModel(model);
        displayPOTable();
    }
    
    private void displayPOTable() {
        model.setRowCount(0);
        List<PurchaseOrder> poList = Payment.getAllPurchaseOrders(); 
        int no = 1;

        for (PurchaseOrder po : poList) {
            if ("Unpaid".equalsIgnoreCase(po.getPaymentStatus()) && "Complete".equalsIgnoreCase(po.getStatus())) {
                String supplierName = po.getSP_Name().replace("{", "").replace("}", "");
                String supplierID = po.getSP_ID().replace("{", "").replace("}", "");
                String itemNames = po.getItemName().replace("{", "").replace("}", "");
                String itemCodesStr = Payment.getItemCodes(po.getPO_ID()); 
                String quantityStr = Payment.getOriginalQuantity(po.getPO_ID()); 

                String[] itemCodes = itemCodesStr.split(",");
                String[] quantities = quantityStr.split(",");
                String[] itemNameArr = itemNames.split(",");

                StringBuilder combinedItemNames = new StringBuilder();
                StringBuilder combinedItemCodes = new StringBuilder();
                StringBuilder combinedQuantities = new StringBuilder();

                double totalPayment = 0.0;

                for (int i = 0; i < itemCodes.length; i++) {
                    String code = itemCodes[i].trim();
                    String qtyStr = (i < quantities.length) ? quantities[i].trim() : "0";
                    String itemName = (i < itemNameArr.length) ? itemNameArr[i].trim() : "Unknown";

                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(qtyStr);
                    } catch (NumberFormatException e) {
                        quantity = 0;
                    }

                    double unitPrice = Payment.getUnitPrice(code);
                    totalPayment += quantity * unitPrice;

                    // Build combined strings
                    if (i > 0) {
                        combinedItemNames.append(",");
                        combinedItemCodes.append(",");
                        combinedQuantities.append(",");
                    }
                    combinedItemNames.append(itemName);
                    combinedItemCodes.append(code);
                    combinedQuantities.append(quantity);
                }

                String totalPaymentFormatted = "RM " + String.format("%.2f", totalPayment);

                Object[] rowData = {
                    no++,
                    po.getPO_ID(),           
                    po.getStatus(),
                    supplierName,
                    supplierID,
                    combinedItemNames.toString(),
                    combinedItemCodes.toString(),
                    combinedQuantities.toString(),
                    po.getPaymentStatus(),
                    totalPaymentFormatted
                };
                model.addRow(rowData);
            }      
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
        PaymentTable = new javax.swing.JTable();
        PayButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        PaymentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "PO ID", "PR ID", "Date", "PM Name", "SM Name", "Expected Delivery Date", "Supplier Name", "Item Name", "Item Code", "Quantity", "Purchase Order Status", "Approved By", "Payment Status", "Total Payment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(PaymentTable);

        PayButton.setText("Paid");
        PayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Process Payment");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1268, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PayButton)
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(PayButton)
                .addGap(126, 126, 126))
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

    private void PayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayButtonActionPerformed
        // Update
        int selectedRow = PaymentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row first.");
            return;
        }

        String poID = PaymentTable.getValueAt(selectedRow, 1).toString(); 

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to mark " + poID + " as Paid?", 
            "Confirm Payment", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            List<String> rowData = new ArrayList<>();
            for (int i = 1; i < PaymentTable.getColumnCount(); i++) {
                rowData.add(PaymentTable.getValueAt(selectedRow, i).toString());
            }

            boolean success = Payment.appendPayment(poID, rowData); 

            if (success) {
                JOptionPane.showMessageDialog(this, poID + " Paid Successfully.");
                displayPOTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed Payment.");
            }
        }
        InventoryManager.resetDeliveryStatusWhenPOsPaid();
        displayPOTable();
    }//GEN-LAST:event_PayButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Payment_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Payment_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Payment_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Payment_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Payment_UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PayButton;
    private javax.swing.JTable PaymentTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
