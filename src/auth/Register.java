/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package auth;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import roles.RoleFactory;

/**
 *
 * @author USER
 * @author notedwin-dev
 */
public class Register extends javax.swing.JFrame {

    public User user;
    User_Management userManager = new User_Management();
    String newUserID = userManager.generateUserID();

    int did;
    String id = "";
    String username = "";
    String password = "";
    String department = "";
    String role = "";

    /**
     * Creates new form Register
     */
    public Register() {
        this.user = new User(id, username, password, department, role);
        initComponents();
        tableLoad();

        ImageIcon addIcon = new ImageIcon(getClass().getResource("/resources/icons/Add.png"));
        Image scaled_add = addIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedAdd = new ImageIcon(scaled_add);
        add_Button.setIcon(resizedAdd);
        // - - - - - RESIZE ICON DELETE - - - - - //
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/resources/icons/Delete.png"));
        Image scaled_delete = deleteIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedDelete = new ImageIcon(scaled_delete);
        delete_Button.setIcon(resizedDelete);
        // - - - - - RESIZE ICON UPDATE - - - - - //
        ImageIcon updateIcon = new ImageIcon(getClass().getResource("/resources/icons/Update.png"));
        Image scaled_update = updateIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedUpdate = new ImageIcon(scaled_update);
        update_Button.setIcon(resizedUpdate);
        // - - - - - RESIZE ICON REFRESH - - - - - //
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/resources/icons/Refresh.png"));
        Image scaled_refresh = refreshIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedRefresh = new ImageIcon(scaled_refresh);
        refresh_Button.setIcon(resizedRefresh);
        // - - - - - RESIZE ICON CLEAN - - - - - //
        ImageIcon cleanIcon = new ImageIcon(getClass().getResource("/resources/icons/Clean.png"));
        Image scaled_clean = cleanIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedClean = new ImageIcon(scaled_clean);
        clean_Button.setIcon(resizedClean);

        // Pre-fill the User ID field in the registration form
        JTextField userIDField = new JTextField(newUserID);
        txtID.setText(newUserID);
        txtID.setEditable(false); // Make the ID field non-editable
    }

    private void getData() {
        user.setID(txtID.getText());
        user.setUsername(txtUsername.getText());
        user.setPassword(txtPassword.getText());
        user.setDepartment(txtDepartment.getText());
        user.setRole(cbRole.getSelectedItem().toString());
    }

    private void clear() {
        txtID.setText("U");
        txtUsername.setText("");
        txtPassword.setText("");
        txtDepartment.setText("");
        cbRole.setSelectedIndex(-1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        cbRole = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        UserTable = new javax.swing.JTable();
        refresh_Button = new javax.swing.JButton();
        add_Button = new javax.swing.JButton();
        delete_Button = new javax.swing.JButton();
        clean_Button = new javax.swing.JButton();
        update_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jPanel2.setMinimumSize(getPreferredSize());
        jPanel2.setPreferredSize(new java.awt.Dimension(1280, 720));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel2.setText("ID:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel3.setText("Username:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel4.setText("Password:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel5.setText("Department:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel6.setText("Role:");

        txtDepartment.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        txtPassword.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        txtUsername.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        txtID.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtID.setText("U");

        cbRole.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            RoleFactory.ADMINISTRATOR,
            RoleFactory.SALES_MANAGER,
            RoleFactory.PURCHASE_MANAGER,
            RoleFactory.INVENTORY_MANAGER,
            RoleFactory.FINANCE_MANAGER
        }));

        UserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Username", "Password", "Department", "Role"
            }
        ));
        UserTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UserTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(UserTable);

        refresh_Button.setBackground(new java.awt.Color(216, 212, 213));
        refresh_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Refresh.png"))); // NOI18N
        refresh_Button.setBorder(null);
        refresh_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_ButtonActionPerformed(evt);
            }
        });

        add_Button.setBackground(new java.awt.Color(120, 211, 77));
        add_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Add.png"))); // NOI18N
        add_Button.setBorder(null);
        add_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_ButtonActionPerformed(evt);
            }
        });

        delete_Button.setBackground(new java.awt.Color(251, 82, 35));
        delete_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Delete.png"))); // NOI18N
        delete_Button.setBorder(null);
        delete_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_ButtonActionPerformed(evt);
            }
        });

        clean_Button.setBackground(new java.awt.Color(240, 225, 0));
        clean_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Clean.png"))); // NOI18N
        clean_Button.setBorder(null);
        clean_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clean_ButtonActionPerformed(evt);
            }
        });

        update_Button.setBackground(new java.awt.Color(76, 134, 168));
        update_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Update.png"))); // NOI18N
        update_Button.setBorder(null);
        update_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtDepartment)
                                        .addComponent(cbRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(update_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(delete_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clean_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(109, 109, 109)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(140, 140, 140)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(clean_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(delete_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(update_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 767, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void refresh_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_ButtonActionPerformed
        tableLoad();
        JOptionPane.showMessageDialog(this, "Table Refreshed!");
    }//GEN-LAST:event_refresh_ButtonActionPerformed

    private void add_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_ButtonActionPerformed
        //Add Data Button
        try {
            getData();
            userManager.add(user);
            tableLoad();
            clear();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_add_ButtonActionPerformed

    private void delete_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_ButtonActionPerformed
        //Delete Data Button
        userManager.delete(id);
        tableLoad();
        clear();
    }//GEN-LAST:event_delete_ButtonActionPerformed

    private void clean_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clean_ButtonActionPerformed
        clear();
        JOptionPane.showMessageDialog(this, "Table Cleaned!");
    }//GEN-LAST:event_clean_ButtonActionPerformed

    private void update_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_ButtonActionPerformed
        getData();
        userManager.update(user);
        tableLoad();
        clear();
    }//GEN-LAST:event_update_ButtonActionPerformed

    private void UserTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UserTableMouseClicked
        DefaultTableModel tmodel = (DefaultTableModel) UserTable.getModel();
        int selectrowindex = UserTable.getSelectedRow();

        if (selectrowindex >= 0) { // Check if a valid row is selected
            id = tmodel.getValueAt(selectrowindex, 0).toString();
            username = tmodel.getValueAt(selectrowindex, 1).toString();
            password = tmodel.getValueAt(selectrowindex, 2).toString();
            department = tmodel.getValueAt(selectrowindex, 3).toString();
            role = tmodel.getValueAt(selectrowindex, 4).toString();
            did = selectrowindex + 2;

            txtID.setText(id);
            txtUsername.setText(username);
            txtPassword.setText(password);
            txtDepartment.setText(department);

            // Set the selected item in the combo box
            cbRole.setSelectedItem(role);

            user.setDataID(did);
            user.setID(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setDepartment(department);
            user.setRole(role);
        }
    }//GEN-LAST:event_UserTableMouseClicked

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
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Register().setVisible(true);
            }
        });
    }

    private void tableLoad() {
        DefaultTableModel model = (DefaultTableModel) UserTable.getModel();
        model.setRowCount(0);
//        String columName[] = {"Id" , "Name"};
//        model = new DefaultTableModel(columName, 2);

        String filePath = "src/auth/txtlogin.txt";
        File file = new File(filePath);

        // Create the file if it doesn't exist
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); // Create directories if they don't exist
                file.createNewFile(); // Create the file
                // Create header row for the file
                FileWriter writer = new FileWriter(file);
                writer.write("ID,Username,Password,Department,Role");
                writer.write(System.getProperty("line.separator"));
                writer.close();
                JOptionPane.showMessageDialog(null, "Created new user database file.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error creating file: " + e.getMessage());
                return;
            }
        }

        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            String fline = br.readLine().trim();
            String[] columname = fline.split(",");
            //DefaultTableModel  model = (DefaultTableModel) jTable1.getModel();
            model.setColumnIdentifiers(columname);

            Object[] tableLines = br.lines().toArray();
            for (int i = 0; i < tableLines.length; i++) {
                String line = tableLines[i].toString().trim();
                String[] datarow = line.split(",");
                model.addRow(datarow);

            }

            br.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable UserTable;
    private javax.swing.JButton add_Button;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JButton clean_Button;
    private javax.swing.JButton delete_Button;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton refresh_Button;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JButton update_Button;
    // End of variables declaration//GEN-END:variables
}
