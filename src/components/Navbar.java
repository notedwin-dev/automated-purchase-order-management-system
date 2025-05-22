/*
 * Navbar.java - Navigation panel for the application
 * Shows navigation options based on user's role permissions
 */
package components;

import auth.User;
import auth.Session;
import roles.Feature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Navigation panel component that displays navigation options based on the user's role
 * @author Edwin
 * @author notedwin-dev
 */
public class Navbar extends javax.swing.JPanel {
    // User session data
    private User currentUser;
    
    // Interface for handling navigation clicks
    public interface NavigationListener {
        void onNavigate(String className);
    }
    
    // Navigation listener
    private NavigationListener navigationListener;
    
    // Selected menu item
    private JPanel selectedMenu;
    private final Color SELECTED_COLOR = new Color(204, 204, 255);
    private final Color DEFAULT_COLOR = new Color(255, 255, 204);
    
    /**
     * Creates new form Navbar
     */
    public Navbar() {
        // Initialize user info panel first
        initUserInfoPanel();
        
        // Then initialize components
        initComponents();
        
        // - - - - - RESIZE ICON LOGO - - - - - //
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/icons/Logo.png"));
        Image scaled_logo = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(scaled_logo);
        Logo_lbl.setIcon(resizedLogo);
        
        // Check if there's already a logged-in user in the session
        User sessionUser = Session.getInstance().getCurrentUser();
        if (sessionUser != null) {
            this.currentUser = sessionUser;
            setupNavigationButtons();
        }
    }
    
    /**
     * Creates new Navbar with a specified user
     * 
     * @param user The logged-in user
     */
    public Navbar(User user) {
        // Initialize user info panel first
        initUserInfoPanel();
        
        // Then initialize components
        initComponents();
        
        this.currentUser = user;
        
        // Store user in the session for application-wide access
        Session.getInstance().setCurrentUser(user);
        
        // Set up navigation buttons
        setupNavigationButtons();
    }
    
    /**
     * Sets the navigation listener
     * 
     * @param listener The listener to handle navigation events
     */
    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }
    
    /**
     * Sets the current user and refreshes navigation
     * 
     * @param user The logged-in user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Store user in session
        Session.getInstance().setCurrentUser(user);
        setupNavigationButtons();
    }
    
    /**
     * Gets the current user
     * 
     * @return The current user or null if not logged in
     */
    public User getCurrentUser() {
        return this.currentUser;
    }
    
    /**
     * Sets up the navigation buttons based on the user's role permissions
     */
    private void setupNavigationButtons() {
        // Clear navigation panel except for the logo
        navButtonsPanel.removeAll();
        
        // Check if there's a user in the session
        if (currentUser == null) {
            currentUser = Session.getInstance().getCurrentUser();
        }
        
        // Update the user info label
        if (currentUser != null) {
            userInfoLabel.setText(currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        } else {
            userInfoLabel.setText("Not logged in");
        }
        
        // Add navigation buttons based on user permissions
        if (currentUser != null) {
            // Dashboard is always available
            addNavButton("Dashboard", "dashboard.Dashboard", true);
            
            // Add feature-specific navigation
            addNavButtonIfPermitted(Feature.ITEM_ENTRY, "Item Management", "itemmanagement.MainPanel");
            addNavButtonIfPermitted(Feature.SUPPLIER_ENTRY, "Supplier Management", "SupplierManagement.UI");
            addNavButtonIfPermitted(Feature.SALES_ENTRY, "Daily Sales", "DailySalesManagement.DailySalesUI");
            addNavButtonIfPermitted(Feature.PURCHASE_REQUISITION, "Create Purchase Requisition", "PurchaseRequisition.PRMain");
            addNavButtonIfPermitted(Feature.DISPLAY_REQUISITION, Feature.DISPLAY_REQUISITION, "PurchaseOrder.Main_PO");
            addNavButtonIfPermitted(Feature.PURCHASE_ORDERS_LIST, Feature.PURCHASE_ORDERS_LIST, "PurchaseOrder.PO_Panel");
            addNavButtonIfPermitted(Feature.GENERATE_PURCHASE_ORDER, Feature.GENERATE_PURCHASE_ORDER, "PurchaseOrder.Main_PO");
            addNavButtonIfPermitted(Feature.INVENTORY_MANAGEMENT, Feature.INVENTORY_MANAGEMENT, "InventoryManagement.InventoryUI");
            addNavButtonIfPermitted(Feature.INVENTORY_MANAGEMENT, "Inventory List", "InventoryManagement.View_Inventory_List");
            addNavButtonIfPermitted(Feature.USER_MANAGEMENT, Feature.USER_MANAGEMENT, "auth.Register");
            addNavButtonIfPermitted(Feature.STOCK_REPORTS, Feature.STOCK_REPORTS, "InventoryManagement.View_Inventory_List");
            addNavButtonIfPermitted(Feature.FINANCIAL_REPORTS, Feature.FINANCIAL_REPORTS, "FinancialManagement.ReportsUI");
            addNavButtonIfPermitted(Feature.SUPPLIER_PAYMENTS, Feature.SUPPLIER_PAYMENTS, "FinancialManagement.PaymentsUI");
            
            // Add logout button at the bottom
            addNavButton("Logout", "auth.Login", false);
        } else {
            // Not logged in, only show login
            addNavButton("Login", "auth.Login", true);
        }
        
        navButtonsPanel.revalidate();
        navButtonsPanel.repaint();
    }
    
    /**
     * Adds a navigation button if the user has permission to access it
     * 
     * @param featureName The name of the feature
     * @param buttonText The text to display on the button
     * @param className The fully qualified class name to open when clicked
     */
    private void addNavButtonIfPermitted(String featureName, String buttonText, String className) {
        if (currentUser != null && currentUser.hasAccess(featureName)) {
            JPanel menuItem = new JPanel();
            menuItem.setLayout(new BorderLayout());
            
            // Set different colors based on permission level
            Color buttonColor;
            if (currentUser.canModify(featureName)) {
                buttonColor = new Color(144, 238, 144); // Light green for full access
            } else {
                buttonColor = new Color(173, 216, 230); // Light blue for view only
            }
            menuItem.setBackground(buttonColor);
            
            // Store feature name in the component to restore color later
            menuItem.setName("feature_" + featureName);
            
            // Create label with text
            JLabel menuLabel = new JLabel(buttonText);
            menuLabel.setFont(new Font("Arial", Font.BOLD, 14));
            menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
            
            // Add hover effect
            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (menuItem != selectedMenu) {
                        menuItem.setBackground(buttonColor.darker());
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (menuItem != selectedMenu) {
                        menuItem.setBackground(buttonColor);
                    }
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Handle navigation
                    if (navigationListener != null) {
                        // Handle role-based redirections for Purchase Order features
                        if (className.equals("PurchaseOrder.Main_PO") || 
                            buttonText.equals(Feature.DISPLAY_REQUISITION)) {
                            String userRole = currentUser.getRole();
                            // All roles view requisitions but with different views
                            if ("Purchase Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.PM_View_PO");
                            } else if ("Finance Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.FM_View_PO_Approval");
                            } else if ("Administrator".equals(userRole) || 
                                      "Sales Manager".equals(userRole) || 
                                      "Inventory Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.View_All_PO_UI");
                            }
                        } 
                        // Handle Purchase Orders List with proper role-based redirections
                        else if (className.equals("PurchaseOrder.PO_Panel") || 
                                 buttonText.equals(Feature.PURCHASE_ORDERS_LIST)) {
                            String userRole = currentUser.getRole();
                            
                            // For viewing Purchase Order lists (all roles have View Only except Admin)
                            if ("Purchase Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.PM_View_PO");
                            } else if ("Finance Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.FM_View_PO_Approval");
                            } else if ("Administrator".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.PM_PO_List_UI"); // Admin gets full access
                            } else if ("Sales Manager".equals(userRole) || 
                                      "Inventory Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.View_All_PO_UI");
                            }
                        }
                        // Handle Purchase Order Generation
                        else if (buttonText.equals(Feature.GENERATE_PURCHASE_ORDER)) {
                            String userRole = currentUser.getRole();
                            
                            // Only Purchase Manager and Admin can generate POs
                            if ("Purchase Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.PO_GenerationUI");
                            } else if ("Administrator".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.PO_GenerationUI");
                            } else if ("Finance Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.PO_Approval"); // FM approves only
                            } else if ("Inventory Manager".equals(userRole)) {
                                navigationListener.onNavigate("PurchaseOrder.View_All_PO_UI"); // IM views only for verification
                            }
                        } else {
                            // Regular navigation for other screens
                            navigationListener.onNavigate(className);
                        }
                    }
                    
                    // Update selection
                    if (selectedMenu != null) {
                        // Reset previous selection color
                        if (selectedMenu.getName() != null && selectedMenu.getName().equals("Dashboard")) {
                            selectedMenu.setBackground(new Color(230, 230, 250));
                        } else if (selectedMenu.getName() != null && selectedMenu.getName().startsWith("feature_")) {
                            String prevFeatureName = selectedMenu.getName().substring(8);
                            if (currentUser.canModify(prevFeatureName)) {
                                selectedMenu.setBackground(new Color(144, 238, 144)); // Light green
                            } else {
                                selectedMenu.setBackground(new Color(173, 216, 230)); // Light blue
                            }
                        } else {
                            selectedMenu.setBackground(DEFAULT_COLOR);
                        }
                    }
                    selectedMenu = menuItem;
                    selectedMenu.setBackground(SELECTED_COLOR);
                }
            });
            
            // Add to panel
            menuItem.add(menuLabel);
            navButtonsPanel.add(menuItem);
            
            // Make menu item expand horizontally
            menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, menuItem.getPreferredSize().height));
        }
    }
    
    /**
     * Adds a navigation button to the panel
     * 
     * @param buttonText The text to display on the button
     * @param className The fully qualified class name to open when clicked
     * @param isMenuItem True if this is a regular menu item, false for special items like logout
     */
    private void addNavButton(String buttonText, String className, final boolean isMenuItem) {
        JPanel menuItem = new JPanel();
        menuItem.setLayout(new BorderLayout());
        
        // Set button colors based on type
        Color defaultButtonColor;
        if (buttonText.equals("Dashboard")) {
            defaultButtonColor = new Color(230, 230, 250); // Lavender for dashboard
        } else if (buttonText.equals("Logout")) {
            defaultButtonColor = new Color(255, 200, 200); // Light red for logout
        } else if (buttonText.equals("Login")) {
            defaultButtonColor = new Color(200, 255, 200); // Light green for login
        } else {
            defaultButtonColor = DEFAULT_COLOR;
        }
        
        menuItem.setBackground(defaultButtonColor);
        
        // Create label with text
        JLabel menuLabel = new JLabel(buttonText);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 14));
        menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        
        // Add hover effect
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (menuItem != selectedMenu) {
                    menuItem.setBackground(defaultButtonColor.darker());
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (menuItem != selectedMenu) {
                    menuItem.setBackground(defaultButtonColor);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle navigation
                if (navigationListener != null) {
                    navigationListener.onNavigate(className);
                }
                
                // Update selection (except for logout)
                if (isMenuItem && !buttonText.equals("Logout")) {
                    if (selectedMenu != null) {
                        // Reset the previously selected menu to its default color
                        if (selectedMenu.getName() != null && selectedMenu.getName().equals("Dashboard")) {
                            selectedMenu.setBackground(new Color(230, 230, 250));
                        } else if (selectedMenu.getName() != null && selectedMenu.getName().startsWith("feature_")) {
                            String featureName = selectedMenu.getName().substring(8);
                            if (currentUser != null && currentUser.canModify(featureName)) {
                                selectedMenu.setBackground(new Color(144, 238, 144)); // Light green
                            } else {
                                selectedMenu.setBackground(new Color(173, 216, 230)); // Light blue
                            }
                        } else {
                            selectedMenu.setBackground(DEFAULT_COLOR);
                        }
                    }
                    selectedMenu = menuItem;
                    selectedMenu.setBackground(SELECTED_COLOR);
                    
                    // Handle logout separately - logout clears the session
                    if (buttonText.equals("Logout")) {
                        Session.getInstance().logout();
                        currentUser = null;
                        userInfoLabel.setText("Not logged in");
                        setupNavigationButtons();
                    }
                }
            }
        });
        
        // Add to panel
        menuItem.add(menuLabel);
        navButtonsPanel.add(menuItem);
        
        // Set name to identify type of button
        if (buttonText.equals("Dashboard")) {
            menuItem.setName("Dashboard");
        }
        
        // Make menu item expand horizontally
        menuItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, menuItem.getPreferredSize().height));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Logo_lbl = new javax.swing.JLabel();
        navButtonsPanel = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Logo_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Logo_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        // Configure navigation buttons panel
        navButtonsPanel.setBackground(new java.awt.Color(255, 255, 204));
        navButtonsPanel.setLayout(new javax.swing.BoxLayout(navButtonsPanel, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(userInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(navButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(userInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(navButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void initUserInfoPanel() {
        // Initialize user info panel
        userInfoPanel = new javax.swing.JPanel();
        userInfoLabel = new javax.swing.JLabel();
        
        // Configure user info panel
        userInfoPanel.setBackground(new java.awt.Color(153, 153, 255));
        userInfoPanel.setLayout(new java.awt.BorderLayout());
        
        userInfoLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        userInfoLabel.setForeground(new java.awt.Color(255, 255, 255));
        userInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userInfoLabel.setText("Not logged in");
        userInfoLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        userInfoPanel.add(userInfoLabel, java.awt.BorderLayout.CENTER);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Logo_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel navButtonsPanel;
    // End of variables declaration//GEN-END:variables
    
    // Custom components
    private javax.swing.JPanel userInfoPanel;
    private javax.swing.JLabel userInfoLabel;
}
