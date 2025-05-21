/*
 * DashboardPanel.java - Main dashboard panel for the application
 * Designed to be displayed inside the MainContainer
 */
package dashboard;

import auth.User;
import roles.Feature;
import roles.Role;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main dashboard panel for the application.
 * Displays appropriate buttons based on the user's role.
 * @author notedwin-dev
 */
public class DashboardPanel extends javax.swing.JPanel {

    private User currentUser;
    private JPanel pnlMain;
    private JPanel pnlHeader;
    private JLabel lblTitle;
    private JLabel lblWelcome;
    private JPanel pnlFeatures;
    
    /**
     * Creates new empty DashboardPanel
     */
    public DashboardPanel() {
        initializeComponents();
    }
    
    /**
     * Creates new DashboardPanel with a specified user
     * 
     * @param user The logged-in user
     */
    public DashboardPanel(User user) {
        this.currentUser = user;
        initializeComponents();
        
        // Set welcome message with username and role
        lblWelcome.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        
        // Set up available features based on user role
        setupFeatureButtons();
    }
    
    /**
     * Initialize the panel components
     */
    private void initializeComponents() {
        // Initialize main components
        pnlMain = new JPanel();
        pnlHeader = new JPanel();
        lblTitle = new JLabel();
        lblWelcome = new JLabel();
        pnlFeatures = new JPanel();
        
        // Set up the panel
        setLayout(new BorderLayout());
        
        // Configure main panel
        pnlMain.setLayout(new BorderLayout(10, 10));
        
        // Configure header panel
        pnlHeader.setBackground(new Color(153, 153, 255));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlHeader.setLayout(new BorderLayout());
        
        // Configure title label
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setText("OMEGA WHOLESALE SDN BHD - Dashboard");
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        // Configure welcome label
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setHorizontalAlignment(SwingConstants.RIGHT);
        lblWelcome.setText("Welcome, User");
        pnlHeader.add(lblWelcome, BorderLayout.EAST);
        
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        
        // Configure features panel
        pnlFeatures.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlFeatures.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnlMain.add(pnlFeatures, BorderLayout.CENTER);
        
        add(pnlMain, BorderLayout.CENTER);
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
        addFeatureButtonIfPermitted(Feature.GENERATE_PURCHASE_ORDER, "Generate Purchase Order", "PurchaseOrder.PO_GenerationUI");
        addFeatureButtonIfPermitted(Feature.INVENTORY_MANAGEMENT, "Inventory Management", "InventoryManagement.InventoryUI");
        addFeatureButtonIfPermitted(Feature.USER_MANAGEMENT, "User Management", "auth.Register");
        
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
        if (currentUser != null && currentUser.hasAccess(featureName)) {
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
                    // Find the MainContainer parent
                    MainContainer container = findMainContainerParent();
                    if (container != null) {
                        // Make sure the session has the current user
                        auth.Session.getInstance().setCurrentUser(currentUser);
                        container.showFeature(className);
                    } else {
                        JOptionPane.showMessageDialog(DashboardPanel.this, 
                                "Error: Could not find MainContainer parent", 
                                "Navigation Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            pnlFeatures.add(button);
        }
    }
    
    /**
     * Finds the MainContainer parent of this panel
     * 
     * @return The MainContainer instance or null if not found
     */
    private MainContainer findMainContainerParent() {
        Container parent = getParent();
        while (parent != null) {
            if (parent instanceof MainContainer) {
                return (MainContainer) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
