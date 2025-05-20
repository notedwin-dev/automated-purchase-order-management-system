/*
 * Dashboard.java - Main dashboard for the application
 */
package dashboard;

import auth.Login;
import auth.User;
import roles.Feature;
import roles.Role;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main dashboard for the application.
 * Displays appropriate buttons based on the user's role.
 * @author notedwin-dev
 */
public class Dashboard extends javax.swing.JFrame {
    private User currentUser;
    
    /**
     * Creates new Dashboard
     */
    public Dashboard() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    /**
     * Creates new Dashboard with a specified user
     * 
     * @param user The logged-in user
     */
    public Dashboard(User user) {
        this.currentUser = user;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Set welcome message with username and role
        lblWelcome.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        
        // Set up available features based on user role
        setupFeatureButtons();
    }
    
    /**
     * Sets up the feature buttons based on the user's role permissions
     */
    private void setupFeatureButtons() {
        // Clear existing buttons
        pnlFeatures.removeAll();
        
        // Use a GridLayout for the buttons
        pnlFeatures.setLayout(new GridLayout(0, 3, 10, 10));
        
        Role userRole = currentUser.getUserRole();
        if (userRole == null) {
            JOptionPane.showMessageDialog(this, "Error: Invalid user role");
            return;
        }
        
        // Add buttons based on permissions
        addFeatureButtonIfPermitted(Feature.ITEM_ENTRY, "Item Management", "itemmanagement.MainPanel");
        addFeatureButtonIfPermitted(Feature.SUPPLIER_ENTRY, "Supplier Management", "SupplierManagement.UI");
        addFeatureButtonIfPermitted(Feature.SALES_ENTRY, "Daily Sales", "DailySalesManagement.DailySalesUI");
        addFeatureButtonIfPermitted(Feature.PURCHASE_REQUISITION, "Purchase Requisition", "PurchaseRequisition.PRMain");
        addFeatureButtonIfPermitted(Feature.DISPLAY_REQUISITION, "View Requisitions", "PurchaseRequisition.PRMain");
        addFeatureButtonIfPermitted(Feature.PURCHASE_ORDERS_LIST, "Purchase Orders", "PurchaseOrder.PO_Panel");
        addFeatureButtonIfPermitted(Feature.GENERATE_PURCHASE_ORDER, "Generate Purchase Order", "PurchaseOrder.PO_Panel");
        addFeatureButtonIfPermitted(Feature.INVENTORY_MANAGEMENT, "Inventory Management", "InventoryManagement.InventoryUI");
        addFeatureButtonIfPermitted(Feature.USER_MANAGEMENT, "User Management", "auth.Register");
        
        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(255, 99, 71)); // Tomato red color
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        pnlFeatures.add(logoutButton);
        pnlFeatures.revalidate();
        pnlFeatures.repaint();
    }
    
    /**
     * Adds a button for a feature if the user has permission to access it
     * 
     * @param featureName The name of the feature
     * @param buttonText The text to display on the button
     * @param className The fully qualified class name to open when clicked
     */
    private void addFeatureButtonIfPermitted(String featureName, String buttonText, String className) {
        if (currentUser.hasAccess(featureName)) {
            JButton button = new JButton(buttonText);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Set different colors based on permission level
            if (currentUser.canModify(featureName)) {
                button.setBackground(new Color(144, 238, 144)); // Light green for full access
            } else {
                button.setBackground(new Color(173, 216, 230)); // Light blue for view only
            }
            
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openFeature(className);
                }
            });
            
            pnlFeatures.add(button);
        }
    }
    
    /**
     * Opens a feature by class name
     * 
     * @param className The fully qualified class name to open
     */   
    private void openFeature(String className) {
        try {
            // Use reflection to create an instance of the class
            Class<?> featureClass = Class.forName(className);
            Object instance = featureClass.getDeclaredConstructor().newInstance();
            
            // If it's a JFrame, make it visible
            if (instance instanceof JFrame) {
                JFrame frame = (JFrame) instance;
                // Change the default close operation to DISPOSE_ON_CLOSE
                // This ensures only this window is closed, not the entire application
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                // Don't dispose the dashboard, keep it open in the background
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error opening feature: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Logs out the current user and returns to the login screen
     */
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            Login login = new Login();
            login.setVisible(true);
            this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlMain = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        pnlFeatures = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OWSB - Dashboard");
        setPreferredSize(new java.awt.Dimension(1200, 800));

        pnlMain.setLayout(new BorderLayout(10, 10));

        pnlHeader.setBackground(new Color(153, 153, 255));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlHeader.setLayout(new BorderLayout());

        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setText("OMEGA WHOLESALE SDN BHD - Dashboard");
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setText("Welcome, User");
        lblWelcome.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlHeader.add(lblWelcome, BorderLayout.EAST);

        pnlMain.add(pnlHeader, BorderLayout.NORTH);

        pnlFeatures.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlFeatures.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        pnlMain.add(pnlFeatures, BorderLayout.CENTER);

        getContentPane().add(pnlMain);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlFeatures;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
