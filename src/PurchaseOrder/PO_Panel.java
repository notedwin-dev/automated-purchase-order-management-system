/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import PurchaseRequisition.PurchaseRequisition;
import PurchaseRequisition.PurchaseRequisitionManagement;
import java.awt.*;
import java.util.Arrays;
import javax.swing.table.*;
import javax.swing.*;
import java.util.List;


public class PO_Panel extends javax.swing.JFrame {

    private DefaultTableModel tmodel;
    private PO_GenerationManagement manage;
    private PurchaseRequisitionManagement prmanagement;
    
    public PO_Panel(PurchaseRequisitionManagement prmanagement) {
        initComponents();
        // - - - - - RESIZE ICON REFRESH - - - - - //
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/resources/icons/Refresh.png"));
        Image scaled_refresh = refreshIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedRefresh = new ImageIcon(scaled_refresh);
        refresh_Button.setIcon(resizedRefresh);
        
        this.prmanagement = prmanagement;
        this.prmanagement.getPRfromtxtfile();

        tmodel = new DefaultTableModel(
            new Object[]{"No.", "PR ID", "Date", "SM Name", "SM ID", "Item Code", "Quantity", "Expected Delivery Date", "Status"}, 0  ) {
            
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; //----- All column is non-editable -----//
            }
        
        };
        
        prTable.setModel(tmodel);
        
        prTable.getTableHeader().setReorderingAllowed(false);
        prTable.getTableHeader().setResizingAllowed(false);
        
        // - - - - - HIDES THE TABS - - - - - //
        PO_TabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0;
            } 
            @Override
            protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
                //----- Do nothing: skip painting tabs -----//
            }
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                //----- Remove the tab panel border -----//
            }
        });
        
         // - - - - - DISABLE CLICKING TABS - - - - - //
        PO_TabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                evt.consume(); // prevent clicking on tabs
            }
        });

        //----- Initialize manage -----//
        manage = new PO_GenerationManagement();

        refreshTable();
        setColumnWidth();
    }
    
    
    // ========== APPLY CUSTOM RENDERER ========== // 
    public void applyCustomRenderer() {
        prTable.getColumnModel().getColumn(5).setCellRenderer(new MultiLineRenderer());
        prTable.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer());
        }

            public class MultiLineRenderer extends JTextArea implements TableCellRenderer {
                public MultiLineRenderer() {
                    setLineWrap(true);
                    setWrapStyleWord(true);
                    setOpaque(true);
                }
                
                // ---------- Adjust the row height to fit the data ---------- //
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    setText(value == null ? "" : value.toString().trim());
                    setFont(table.getFont());

                    int lineHeight = getFontMetrics(getFont()).getHeight();
                    int lines = value.toString().split("\n").length;
                    int preferredHeight = Math.max(lineHeight * lines, 20); // ----- Set default height = 20

                    if (table.getRowHeight(row) != preferredHeight) {
                        table.setRowHeight(row, preferredHeight);
                    }

                    setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

                    return this;
             }
    }
    
            
    // ========== LOAD DATA INTO TABLE ========== //   
    public void refreshTable() {
        tmodel.setRowCount(0); // Clear table

        List<Object[]> data = manage.getTableData();
        System.out.println("Data rows count: " + data.size());
    
        for (Object[] row : manage.getTableData()) {
            System.out.println(Arrays.toString(row));
            tmodel.addRow(row);
        }

        applyCustomRenderer(); // ----- Call method to ensure Item Code and Quantity are rendered as multiline -----//
    }
    
    
    // ========== ADJUST TABLE COLUMN SIZE ========== //   
    public void setColumnWidth() {
        prTable.getColumnModel().getColumn(0).setPreferredWidth(40);   //-- "No."
        prTable.getColumnModel().getColumn(1).setPreferredWidth(90);   //-- "PR ID"
        prTable.getColumnModel().getColumn(2).setPreferredWidth(90);   //-- "Date"
        prTable.getColumnModel().getColumn(3).setPreferredWidth(100); //-- "SM Name"
        prTable.getColumnModel().getColumn(4).setPreferredWidth(90);   //-- "SM ID"
        prTable.getColumnModel().getColumn(5).setPreferredWidth(90);   //-- "Item Code"
        prTable.getColumnModel().getColumn(6).setPreferredWidth(90);   //-- "Quantity"
        prTable.getColumnModel().getColumn(7).setPreferredWidth(130); //-- "Expected Delivery Date"
        prTable.getColumnModel().getColumn(8).setPreferredWidth(90);   //-- "Status"
    }
    
    
//    private PurchaseRequisition getSelectedPR(){
//        int selectedRow = prTable.getSelectedRow();
//        if(selectedRow == -1){
//            JOptionPane.showMessageDialog(null, "Please select a row" , "Selection Error", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//        String prid = prTable.getValueAt(selectedRow, 1).toString(); // PR ID is at col 1
//        return prmanagement.findprid(prid);
//    }
    private PurchaseRequisition getSelectedPR() {
    int selectedRow = prTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select a row", "Selection Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    String prid = prTable.getValueAt(selectedRow, 1).toString(); // PR ID at column 1
    System.out.println("Selected PR ID from table: " + prid);  // Debug

    PurchaseRequisition pr = prmanagement.findprid(prid);
    if (pr == null) {
        System.out.println("PR not found for ID: " + prid);  // Debug
    }
    return pr;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PO_TabbedPane = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        Title_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        generatePO_button = new javax.swing.JButton();
        rejectPO_btn = new javax.swing.JButton();
        refresh_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        Title_lbl.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        Title_lbl.setText("Generate Purchase Order");

        prTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "PR ID", "Date", "SM Name", "SM ID", "Item Code", "Quantity", "Expected Delivery Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        prTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        prTable.getTableHeader().setReorderingAllowed(false);
        prTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(prTable);

        generatePO_button.setBackground(new java.awt.Color(255, 255, 204));
        generatePO_button.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        generatePO_button.setText("Generate");
        generatePO_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatePO_buttonActionPerformed(evt);
            }
        });

        rejectPO_btn.setBackground(new java.awt.Color(255, 255, 204));
        rejectPO_btn.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rejectPO_btn.setText("Reject");
        rejectPO_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejectPO_btnActionPerformed(evt);
            }
        });

        refresh_Button.setBackground(new java.awt.Color(216, 212, 213));
        refresh_Button.setBorder(null);
        refresh_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(245, 245, 245)
                        .addComponent(Title_lbl))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(389, 389, 389)
                        .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(generatePO_button, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rejectPO_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(254, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(81, 81, 81)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(81, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Title_lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 554, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(generatePO_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rejectPO_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(92, 92, 92)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(93, Short.MAX_VALUE)))
        );

        PO_TabbedPane.addTab("PR List", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PO_TabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1082, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PO_TabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generatePO_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatePO_buttonActionPerformed
        prmanagement.getPRfromtxtfile();
        PurchaseRequisition selectedPR = getSelectedPR();
        System.out.println("Selected PR: " + selectedPR);

        if (selectedPR != null) {
            String status = selectedPR.getStatus();  // Assuming you have a getStatus() method
            System.out.println("PR Status: " + status); // DEBUG


            if (status.equalsIgnoreCase("Approved") || status.equalsIgnoreCase("Rejected")) {
                JOptionPane.showMessageDialog(this, 
                    "PO cannot be generated for a PR that is already " + status + ".", 
                    "Invalid Operation", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            // ----- Proceed to generate PO ----- //
            System.out.println("Generating PO UI for PR: " + selectedPR.getPrid());
            new PO_GenerationUI(selectedPR).setVisible(true);
            this.dispose();
        } 
    }//GEN-LAST:event_generatePO_buttonActionPerformed

    private void prTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_prTableMouseClicked

    private void rejectPO_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectPO_btnActionPerformed
        // TODO add your handling code here:
        int selectedRow = prTable.getSelectedRow();
        if (selectedRow != -1) {
            String prID = prTable.getValueAt(selectedRow, 1).toString(); 
            String status = prTable.getValueAt(selectedRow, 8).toString();
            
            // ----- Cannot reject once status is APPROVED ----- //
             if (status.equalsIgnoreCase("APPROVED")) {
                JOptionPane.showMessageDialog(this, 
                    "You cannot reject PR " + prID + " because it is already APPROVED.",
                    "Action Denied",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
             
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to reject PR " + prID + "?",
                    "Confirm Rejection",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                PO_GenerationManagement manager = new PO_GenerationManagement();
                String result = manager.rejectPR(prID);

                switch (result) {
                    case "REJECTED":
                        JOptionPane.showMessageDialog(this, "PR " + prID + " has been rejected.");
                        refreshTable(); 
                        break;
                    case "APPROVED":
                        JOptionPane.showMessageDialog(this, "PR " + prID + " is already APPROVED and cannot be rejected.");
                        break;
                    case "NOT_FOUND":
                        JOptionPane.showMessageDialog(this, "PR " + prID + " not found.");
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Cannot reject PR " + prID + ". Current status does not allow rejection.");
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a PR to reject.");
        }
    }//GEN-LAST:event_rejectPO_btnActionPerformed

    private void refresh_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_ButtonActionPerformed
        refreshTable();
    }//GEN-LAST:event_refresh_ButtonActionPerformed

    
 
    
    
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ENDS HERE  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  //
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
//            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PO_Panel().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane PO_TabbedPane;
    private javax.swing.JLabel Title_lbl;
    private javax.swing.JButton generatePO_button;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable prTable;
    private javax.swing.JButton refresh_Button;
    private javax.swing.JButton rejectPO_btn;
    // End of variables declaration//GEN-END:variables
}
