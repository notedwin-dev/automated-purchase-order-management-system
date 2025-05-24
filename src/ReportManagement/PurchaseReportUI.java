/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author nixon
 */
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class PurchaseReportUI extends javax.swing.JFrame {

    private DefaultTableModel tmodel;
    private PurchaseReport_Management report_manage;

    public PurchaseReportUI() {
        initComponents();
        
        tmodel = new DefaultTableModel(new Object[]{
            "Payment ID", "PO ID", "Item Code", "Item Name",
            "Company", "Supplier ID", "Quantity", "Payment Status", "Unit Price", "Total Amount", "Payment Date"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make all cells non-editable
            }
        };

        PurchaseReportTable.setModel(tmodel);

        report_manage = new PurchaseReport_Management();

        refreshTable();
        System.out.println(PurchaseReportTable.getColumnCount());
        applyCustomRenderer();
        setColumnWidth();
        
    }

    // ========== APPLY CUSTOM RENDERER ========== //
    private void applyCustomRenderer() {
        // Apply multi-line renderer to item-related columns
        PurchaseReportTable.getColumnModel().getColumn(5).setCellRenderer(new MultiLineRenderer()); // Item Name
        PurchaseReportTable.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer()); // Item Code
        PurchaseReportTable.getColumnModel().getColumn(7).setCellRenderer(new MultiLineRenderer()); // Quantity
        PurchaseReportTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineRenderer()); // Company
        PurchaseReportTable.getColumnModel().getColumn(4).setCellRenderer(new MultiLineRenderer()); // Supplier ID

    }

    public class MultiLineRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString().trim());
            setFont(table.getFont());

            // Adjust row height based on content
            int lines = getLineCountForValue(value.toString());
            int lineHeight = getFontMetrics(getFont()).getHeight();
            int requiredHeight = lineHeight * lines;

            if (table.getRowHeight(row) < requiredHeight) {
                table.setRowHeight(row, requiredHeight);
            }

            // Maintain selection color behavior
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

            return this;
        }

        private int getLineCountForValue(String value) {
            return value.split("\n").length;
        }
    }

     // ========== DISPLAY DATA ON TABLE ========== //
    private void refreshTable() {
        tmodel.setRowCount(0); // Clear table
        List<Object[]> data = report_manage.getTableData();
        System.out.println("Data size: " + data.size());
        
        for (Object[] row : data) {
            tmodel.addRow(row);
        }
    }

    // ========== SET COLUMN WIDTH ========== //
    private void setColumnWidth() {
        PurchaseReportTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // Payment ID
        PurchaseReportTable.getColumnModel().getColumn(1).setPreferredWidth(60);   // PO ID
        PurchaseReportTable.getColumnModel().getColumn(2).setPreferredWidth(140);  // Company
        PurchaseReportTable.getColumnModel().getColumn(3).setPreferredWidth(60);   // Supplier ID
        PurchaseReportTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Item Name
        PurchaseReportTable.getColumnModel().getColumn(5).setPreferredWidth(60);   // Item Code
        PurchaseReportTable.getColumnModel().getColumn(6).setPreferredWidth(60);   // Quantity
        PurchaseReportTable.getColumnModel().getColumn(7).setPreferredWidth(60);  // Payment Status
        PurchaseReportTable.getColumnModel().getColumn(8).setPreferredWidth(90);  // Total Amount
        PurchaseReportTable.getColumnModel().getColumn(9).setPreferredWidth(100); // Payment Date
    }

    
// ========================================================================================//
    
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
        PurchaseReportTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Purchase Report");

        PurchaseReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Payment ID", "PO ID", "Item Code", "Item Name", "Company", "Payment Date", "Quantity", "Unit Price", "Total Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(PurchaseReportTable);

        jButton1.setText("jButton1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(340, 340, 340)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(85, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(484, 484, 484))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(39, 39, 39))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PurchaseReportUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable PurchaseReportTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
