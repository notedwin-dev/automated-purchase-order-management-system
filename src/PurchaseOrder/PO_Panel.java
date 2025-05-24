/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import PurchaseRequisition.PROperation;
import PurchaseRequisition.PR_Management;
import java.awt.*;
import java.util.Arrays;
import javax.swing.table.*;
import javax.swing.*;
import java.util.List;


public class PO_Panel extends javax.swing.JFrame {

    private DefaultTableModel tmodel;
    private PO_GenerationManagement manage;
    private PR_Management prmanagement;
    
    public PO_Panel(PR_Management prmanagement) {
        initComponents();
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
        
        //----- Link model to table -----//
//        prTable.setModel(tmodel); 

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
    
    private PROperation getSelectedPR(){
        int selectedRow = prTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(null, "Please select a row" , "Selection Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String prid = prTable.getValueAt(selectedRow, 1).toString(); // PR ID is at col 1
        return prmanagement.findprid(prid);
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
                        .addGap(442, 442, 442)
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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generatePO_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rejectPO_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        // TODO add your handling code here:
        PROperation selectedPR = getSelectedPR();
        if(selectedPR != null){
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
            String prID = prTable.getValueAt(selectedRow, 0).toString(); 
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to reject PR " + prID + "?",
                    "Confirm Rejection",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                PO_GenerationManagement manager = new PO_GenerationManagement();
                manager.rejectPR(prID);
                JOptionPane.showMessageDialog(this, "PR " + prID + " has been rejected.");
                refreshTable(); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a PR to reject.");
        }
    }//GEN-LAST:event_rejectPO_btnActionPerformed

    
 
    
    
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
    private javax.swing.JButton rejectPO_btn;
    // End of variables declaration//GEN-END:variables
}
