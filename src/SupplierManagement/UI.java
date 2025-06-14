/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SupplierManagement;

/**
 *
 * @author Jengsiang
 */
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
public class UI extends javax.swing.JFrame {
    
    private SupplierManagement sm;
    private DefaultTableModel model;
    
    public UI() {
        initComponents();
         // - - - - - RESIZE ICON ADD - - - - - //
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/resources/icons/Add.png"));
        Image scaled_add = addIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedAdd = new ImageIcon(scaled_add);
        Add.setIcon(resizedAdd);
         // - - - - - RESIZE ICON DELETE - - - - - //
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/resources/icons/Delete.png"));
        Image scaled_delete = deleteIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedDelete = new ImageIcon(scaled_delete);
        Delete.setIcon(resizedDelete);
         // - - - - - RESIZE ICON UPDATE - - - - - //
        ImageIcon updateIcon = new ImageIcon(getClass().getResource("/resources/icons/Update.png"));
        Image scaled_update = updateIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedUpdate = new ImageIcon(scaled_update);
        Update.setIcon(resizedUpdate);
         // - - - - - RESIZE ICON REFRESH - - - - - //
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/resources/icons/Refresh.png"));
        Image scaled_refresh = refreshIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedRefresh = new ImageIcon(scaled_refresh);
        Refresh.setIcon(resizedRefresh);
         // - - - - - RESIZE ICON CLEAN - - - - - //
        ImageIcon cleanIcon = new ImageIcon(getClass().getResource("/resources/icons/Clean.png"));
        Image scaled_clean = cleanIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedClean = new ImageIcon(scaled_clean);
        Clear.setIcon(resizedClean);
        
        
        SupplierID_TextField.setEditable(false);
        String[] columns = {"No.","Supplier ID", "Supplier Name", "Supplier Contact Number", "Supplier Email", "Supplier Address", "Item Description"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        SupplierTable.setModel(model);
        
        sm = new SupplierManagement(
            SupplierTable,
            SupplierID_TextField,
            SupplierName_TextField,
            SupplierContact_TextField,
            SupplierEmail_TextField,
            SupplierAddress_TextField,
            SupplierItemDescription_TextField
        );
        
        sm.refresh();
        
        SupplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = SupplierTable.getSelectedRow();
                if (selectedRow != -1) {
                    String supplierID = SupplierTable.getValueAt(selectedRow, 1).toString();
                    String supplierName = SupplierTable.getValueAt(selectedRow, 2).toString();
                    String supplierContact = SupplierTable.getValueAt(selectedRow, 3).toString();
                    String supplierEmail = SupplierTable.getValueAt(selectedRow, 4).toString();
                    String supplierAddress = SupplierTable.getValueAt(selectedRow, 5).toString();
                    String itemDescription = SupplierTable.getValueAt(selectedRow, 6).toString();

                    SupplierID_TextField.setText(supplierID);
                    SupplierName_TextField.setText(supplierName);
                    SupplierContact_TextField.setText(supplierContact);
                    SupplierEmail_TextField.setText(supplierEmail);
                    SupplierAddress_TextField.setText(supplierAddress);
                    SupplierItemDescription_TextField.setText(itemDescription);
                }
            }
        });
        
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
                    sm.refresh();
                }else{
                    sm.searchFunction(keyword);
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

        Background = new javax.swing.JPanel();
        SupplierID = new javax.swing.JLabel();
        SupplierID_TextField = new javax.swing.JTextField();
        SupplierName = new javax.swing.JLabel();
        SupplierName_TextField = new javax.swing.JTextField();
        SupplierContact = new javax.swing.JLabel();
        SupplierContact_TextField = new javax.swing.JTextField();
        SupplierEmail = new javax.swing.JLabel();
        SupplierEmail_TextField = new javax.swing.JTextField();
        SupplierAddress = new javax.swing.JLabel();
        SupplierAddress_TextField = new javax.swing.JTextField();
        SupplierItemDescription = new javax.swing.JLabel();
        SupplierItemDescription_TextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        SupplierTable = new javax.swing.JTable();
        Add = new javax.swing.JButton();
        Update = new javax.swing.JButton();
        Clear = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Refresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Search_TextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setPreferredSize(new java.awt.Dimension(1280, 720));

        SupplierID.setText("Supplier ID :");

        SupplierID_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierID_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SupplierName.setText("Supplier Name :");

        SupplierName_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierName_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        SupplierName_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupplierName_TextFieldActionPerformed(evt);
            }
        });

        SupplierContact.setText("Contact Number :");

        SupplierContact_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierContact_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SupplierEmail.setText("Email : ");

        SupplierEmail_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierEmail_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SupplierAddress.setText("Address :");

        SupplierAddress_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierAddress_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SupplierItemDescription.setText("Item Description : ");

        SupplierItemDescription_TextField.setBackground(new java.awt.Color(204, 204, 204));
        SupplierItemDescription_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        SupplierTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        SupplierTable.getTableHeader().setReorderingAllowed(false);
        SupplierTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Supplier ID", "Supplier Name", "Contact Number", "Email", "Address", "Item Description"
            }
        ));
        SupplierTable.setShowGrid(true);
        jScrollPane1.setViewportView(SupplierTable);
        SupplierTable.getAccessibleContext().setAccessibleDescription("");

        Add.setBackground(new java.awt.Color(120, 211, 77));
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        Update.setBackground(new java.awt.Color(76, 134, 168));
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });

        Clear.setBackground(new java.awt.Color(240, 225, 0));
        Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearActionPerformed(evt);
            }
        });

        Delete.setBackground(new java.awt.Color(251, 82, 35));
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        Refresh.setBackground(new java.awt.Color(153, 153, 153));
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Search  :");

        Search_TextField.setBackground(new java.awt.Color(204, 204, 204));
        Search_TextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Search_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Search_TextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BackgroundLayout = new javax.swing.GroupLayout(Background);
        Background.setLayout(BackgroundLayout);
        BackgroundLayout.setHorizontalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroundLayout.createSequentialGroup()
                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BackgroundLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SupplierContact)
                            .addComponent(SupplierEmail)
                            .addComponent(SupplierName)
                            .addComponent(SupplierID)
                            .addComponent(SupplierAddress)
                            .addComponent(SupplierItemDescription))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BackgroundLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(SupplierItemDescription_TextField)
                                    .addComponent(SupplierAddress_TextField)
                                    .addComponent(SupplierEmail_TextField)
                                    .addComponent(SupplierContact_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(SupplierID_TextField)
                                .addComponent(SupplierName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(BackgroundLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Update, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46)
                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BackgroundLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
                        .addContainerGap(77, Short.MAX_VALUE))
                    .addGroup(BackgroundLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(Search_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        BackgroundLayout.setVerticalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroundLayout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Search_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BackgroundLayout.createSequentialGroup()
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierID_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SupplierID))
                        .addGap(18, 18, 18)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SupplierName))
                        .addGap(18, 18, 18)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierContact_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SupplierContact))
                        .addGap(18, 18, 18)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierEmail_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SupplierEmail))
                        .addGap(21, 21, 21)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierAddress_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SupplierAddress))
                        .addGap(18, 18, 18)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SupplierItemDescription_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SupplierItemDescription))
                        .addGap(146, 146, 146)
                        .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Add, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Update, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        //ADD
        sm.add();
        sm.refresh();
    }//GEN-LAST:event_AddActionPerformed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        //Clear
        sm.clear();
    }//GEN-LAST:event_ClearActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        //Update
        sm.update();
        sm.refresh();
    }//GEN-LAST:event_UpdateActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        //Delete
        sm.delete();
        sm.refresh();
    }//GEN-LAST:event_DeleteActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        //Refresh
        sm.refresh();
    }//GEN-LAST:event_RefreshActionPerformed

    private void SupplierName_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupplierName_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SupplierName_TextFieldActionPerformed

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
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JPanel Background;
    private javax.swing.JButton Clear;
    private javax.swing.JButton Delete;
    private javax.swing.JButton Refresh;
    private javax.swing.JTextField Search_TextField;
    private javax.swing.JLabel SupplierAddress;
    private javax.swing.JTextField SupplierAddress_TextField;
    private javax.swing.JLabel SupplierContact;
    private javax.swing.JTextField SupplierContact_TextField;
    private javax.swing.JLabel SupplierEmail;
    private javax.swing.JTextField SupplierEmail_TextField;
    private javax.swing.JLabel SupplierID;
    private javax.swing.JTextField SupplierID_TextField;
    private javax.swing.JLabel SupplierItemDescription;
    private javax.swing.JTextField SupplierItemDescription_TextField;
    private javax.swing.JLabel SupplierName;
    private javax.swing.JTextField SupplierName_TextField;
    private javax.swing.JTable SupplierTable;
    private javax.swing.JButton Update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
