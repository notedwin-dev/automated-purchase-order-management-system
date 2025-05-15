/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import java.awt.*;
import javax.swing.table.*;
import javax.swing.*;

public class PO_Panel extends javax.swing.JFrame {

    private DefaultTableModel tmodel;
    private PO_Management poManage;
    
    public PO_Panel() {
        initComponents();
        // - - - - - RESIZE ICON LOGO - - - - - //
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/icons/Logo.png"));
        Image scaled_logo = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(scaled_logo);
        Logo_lbl.setIcon(resizedLogo);

        tmodel = new DefaultTableModel(
            new Object[]{"No.", "PR ID", "Date", "SM Name", "SM ID", "Item Code", "Quantity", "Expected Delivery Date", "Status"}, 0  ) {
            
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; //----- All column is non-editable -----//
            }
        
        };
        
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
        prTable.setModel(tmodel); 

        poManage = new PO_Management();
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
        DefaultTableModel tmodel = (DefaultTableModel) prTable.getModel();
        tmodel.setRowCount(0); // Clear table

        for (Object[] row : poManage.getTableData()) {
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Logo_lbl = new javax.swing.JLabel();
        Home_Button = new javax.swing.JButton();
        PRList_Button = new javax.swing.JButton();
        PO_TabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        Title_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        generatePO_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        Home_Button.setBackground(new java.awt.Color(255, 255, 204));
        Home_Button.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        Home_Button.setForeground(new java.awt.Color(0, 0, 0));
        Home_Button.setText("Home");
        Home_Button.setBorder(null);
        Home_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Home_ButtonActionPerformed(evt);
            }
        });

        PRList_Button.setBackground(new java.awt.Color(255, 255, 204));
        PRList_Button.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        PRList_Button.setForeground(new java.awt.Color(0, 0, 0));
        PRList_Button.setText("PR List");
        PRList_Button.setBorder(null);
        PRList_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PRList_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Logo_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Home_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PRList_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Logo_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addComponent(Home_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(PRList_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1082, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 685, Short.MAX_VALUE)
        );

        PO_TabbedPane.addTab("Home", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        Title_lbl.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        Title_lbl.setForeground(new java.awt.Color(0, 0, 0));
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
        prTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(prTable);

        generatePO_button.setBackground(new java.awt.Color(255, 255, 204));
        generatePO_button.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        generatePO_button.setForeground(new java.awt.Color(0, 0, 0));
        generatePO_button.setText("Generate");

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
                        .addComponent(generatePO_button, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(generatePO_button, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PO_TabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1082, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PO_TabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Home_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Home_ButtonActionPerformed
        PO_TabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_Home_ButtonActionPerformed

    private void PRList_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PRList_ButtonActionPerformed
        PO_TabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_PRList_ButtonActionPerformed

    private void prTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_prTableMouseClicked

    
 
    
    
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ENDS HERE  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  //
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
            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PO_Panel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PO_Panel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Home_Button;
    private javax.swing.JLabel Logo_lbl;
    private javax.swing.JTabbedPane PO_TabbedPane;
    private javax.swing.JButton PRList_Button;
    private javax.swing.JLabel Title_lbl;
    private javax.swing.JButton generatePO_button;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable prTable;
    // End of variables declaration//GEN-END:variables
}
