/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PurchaseOrder;

import auth.Session;
import auth.User;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author nixon
 */
public class View_PRList extends javax.swing.JFrame {
    
    private DefaultTableModel tablemodel;
    private JTable viewprtable;
    private PO_GenerationManagement POmanage;
    /**
     * Creates new form View_PRList
     */
    public View_PRList() {
        initComponents();
        tablemodel = new DefaultTableModel(
            new Object[]{"No.", "PR ID", "Date", "SM Name", "SM ID", "Item Code", "Quantity", "Expected Delivery Date", "Status"}, 0
        );
       
        viewPRTable.setModel(tablemodel);
        
        viewPRTable.getTableHeader().setReorderingAllowed(false);
        viewPRTable.getTableHeader().setResizingAllowed(false);
        viewPRTable.setRowSelectionAllowed(false);
        viewPRTable.setColumnSelectionAllowed(false);
        viewPRTable.setCellSelectionEnabled(false);
        
        POmanage = new PO_GenerationManagement(); 
        refreshTable(); // Same method from PO_Panel
        setColumnWidth(); // Same method
    }

    
    // ========== APPLY CUSTOM RENDERER ========== // 
    public void applyCustomRenderer() {
        viewPRTable.getColumnModel().getColumn(5).setCellRenderer(new MultiLineRenderer());
        viewPRTable.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer());
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
    public final void refreshTable() {
        tablemodel.setRowCount(0); // Clear existing rows

        User currentUser = Session.getInstance().getCurrentUser();

        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "Error: No user session found. ", "Session Error", JOptionPane.ERROR_MESSAGE);
        }
        
        String currentID = currentUser.getID(); 

        if (currentID == null || currentID.isEmpty() ) {
            JOptionPane.showMessageDialog(null, "Error: Incomplete user information. Please log in again.", "User Info Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Optional: Debugging printout (for development)
        System.out.println("User ID: " + currentID + ", User: " + currentUser);

        List<Object[]> allData = POmanage.getTableData(); 

        int no = 1;
        for (Object[] row : allData) {
            String prCreatedByID = row[4].toString(); 

            // If the current user is not Admin and didn't create the PR, skip it
            if (!currentUser.equals("Admin") && !prCreatedByID.equals(currentID)) {
                continue;
            }

            row[0] = no++;
            tablemodel.addRow(row);
        }

        applyCustomRenderer(); // Make sure multi-line columns are rendered properly
    }
    
//    public final void refreshTable() {
//        tablemodel.setRowCount(0); 
//        
//        List<Object[]> allData = POmanage.getTableData();
//        
//        System.out.println("Data rows count: " + allData.size());
//            for (Object[] row : POmanage.getTableData()) {
//                System.out.println(Arrays.toString(row));
//                tablemodel.addRow(row);
//            }
//        applyCustomRenderer(); // ----- Call method to ensure Item Code and Quantity are rendered as multiline -----//
//    }
    
    
    // ========== ADJUST TABLE COLUMN SIZE ========== //   
    public final void setColumnWidth() {
        viewPRTable.getColumnModel().getColumn(0).setPreferredWidth(40);   //-- "No."
        viewPRTable.getColumnModel().getColumn(1).setPreferredWidth(90);   //-- "PR ID"
        viewPRTable.getColumnModel().getColumn(2).setPreferredWidth(90);   //-- "Date"
        viewPRTable.getColumnModel().getColumn(3).setPreferredWidth(100); //-- "SM Name"
        viewPRTable.getColumnModel().getColumn(4).setPreferredWidth(90);   //-- "SM ID"
        viewPRTable.getColumnModel().getColumn(5).setPreferredWidth(90);   //-- "Item Code"
        viewPRTable.getColumnModel().getColumn(6).setPreferredWidth(90);   //-- "Quantity"
        viewPRTable.getColumnModel().getColumn(7).setPreferredWidth(130); //-- "Expected Delivery Date"
        viewPRTable.getColumnModel().getColumn(8).setPreferredWidth(90);   //-- "Status"
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
        Title_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        viewPRTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1080, 700));

        Title_lbl.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        Title_lbl.setForeground(new java.awt.Color(0, 0, 0));
        Title_lbl.setText("View Purchase Requisition");

        viewPRTable.setModel(new javax.swing.table.DefaultTableModel(
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
        viewPRTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewPRTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(viewPRTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(233, Short.MAX_VALUE)
                .addComponent(Title_lbl)
                .addGap(233, 233, 233))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(80, 80, 80)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(80, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(Title_lbl)
                .addContainerGap(620, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(100, 100, 100)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(100, Short.MAX_VALUE)))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewPRTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPRTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_viewPRTableMouseClicked

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
            java.util.logging.Logger.getLogger(View_PRList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View_PRList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View_PRList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View_PRList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> new View_PRList().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Title_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable viewPRTable;
    // End of variables declaration//GEN-END:variables
}
