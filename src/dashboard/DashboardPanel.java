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
        
        // Configure header panel - replace BorderLayout with BoxLayout for better responsiveness
        pnlHeader.setBackground(new Color(153, 153, 255));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        
        // Create a separate panel for the header content
        JPanel headerContentPanel = new JPanel();
        headerContentPanel.setOpaque(false); // Make it transparent
        headerContentPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for more control
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Configure title label - allow text wrapping
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setText("OMEGA WHOLESALE SDN BHD - Dashboard");
        
        // Add title with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7; // Give title more space
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        headerContentPanel.add(lblTitle, gbc);
        
        // Add some space between components
        gbc.gridx = 1;
        gbc.weightx = 0.05;
        headerContentPanel.add(Box.createHorizontalStrut(20), gbc);
        
        // Configure welcome label
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setHorizontalAlignment(SwingConstants.RIGHT);
        lblWelcome.setText("Welcome, User");
        
        // Add welcome label with constraints
        gbc.gridx = 2;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.EAST;
        headerContentPanel.add(lblWelcome, gbc);
        
        // Add the header content panel to the header panel
        pnlHeader.add(headerContentPanel);
        
        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        
        // Create a scroll pane to handle overflow of buttons
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Make scrolling smoother
        
        // Configure features panel with proper layout and spacing
        pnlFeatures.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // We'll use GridLayout with 0 rows to allow flexible number of columns
        // but with enough horizontal gap to space out buttons nicely
        pnlFeatures.setLayout(new GridLayout(0, 3, 20, 20));
        
        // Add the features panel to the scroll pane
        scrollPane.setViewportView(pnlFeatures);
        
        // Add scroll pane to main panel
        pnlMain.add(scrollPane, BorderLayout.CENTER);
        
        // Add the main panel to this panel
        add(pnlMain, BorderLayout.CENTER);
        
        // Set minimum size to ensure proper display
        setPreferredSize(new Dimension(900, 600));
    }
    
    /**
     * Sets up the feature buttons based on the user's role permissions
     */
    private void setupFeatureButtons() {
        // Clear existing buttons
        pnlFeatures.removeAll();
        
        Role userRole = currentUser.getUserRole();
        if (userRole == null) {
            JOptionPane.showMessageDialog(this, "Error: Invalid user role");
            return;
        }
        
        // Add feature-specific navigation
            addFeatureButtonIfPermitted(Feature.ITEM_ENTRY, "Item Management", "itemmanagement.ItemList");
            addFeatureButtonIfPermitted(Feature.ITEM_LIST, "Item List", "itemmanagement.ViewItemList");
            addFeatureButtonIfPermitted(Feature.SUPPLIER_ENTRY, "Supplier Management", "SupplierManagement.UI");
            addFeatureButtonIfPermitted(Feature.SUPPLIER_LIST, "Supplier List", "SupplierManagement.View_Supplier_UI");
            addFeatureButtonIfPermitted(Feature.DAILY_SALES, Feature.DAILY_SALES, "DailySalesManagement.View_DailySales_List");
            addFeatureButtonIfPermitted(Feature.SALES_ENTRY, Feature.SALES_ENTRY, "DailySalesManagement.DailySalesUI");
            addFeatureButtonIfPermitted(Feature.PURCHASE_REQUISITION, "Create Purchase Requisition", "PurchaseRequisition.PurchaseRequisitionGenerationUI");
            addFeatureButtonIfPermitted(Feature.DISPLAY_REQUISITION, Feature.DISPLAY_REQUISITION, "PurchaseOrder.View_PRList"); //To be PR LIST
            addFeatureButtonIfPermitted(Feature.GENERATE_PURCHASE_ORDER, Feature.GENERATE_PURCHASE_ORDER, "PurchaseOrder.Main_PO");
            addFeatureButtonIfPermitted(Feature.PURCHASE_ORDERS_LIST, Feature.PURCHASE_ORDERS_LIST, "PurchaseOrder.PO_Panel");
            addFeatureButtonIfPermitted(Feature.APPROVE_PURCHASE_ORDER, Feature.APPROVE_PURCHASE_ORDER, "PurchaseOrder.PO_Approval");
            addFeatureButtonIfPermitted(Feature.INVENTORY_MANAGEMENT, Feature.INVENTORY_MANAGEMENT, "InventoryManagement.InventoryUI");
            addFeatureButtonIfPermitted(Feature.INVENTORY_LIST, "Inventory List", "InventoryManagement.View_Inventory_List");
            addFeatureButtonIfPermitted(Feature.USER_MANAGEMENT, Feature.USER_MANAGEMENT, "auth.Register");
            // Fix the typo in the package name - ReportManagerment should be ReportManagement
            addFeatureButtonIfPermitted(Feature.STOCK_REPORTS, Feature.STOCK_REPORTS, "ReportManagement.InventoryReportMain");
            addFeatureButtonIfPermitted(Feature.SALES_REPORT, Feature.SALES_REPORT, "ReportManagement.SalesReportMain");
            addFeatureButtonIfPermitted(Feature.SUPPLIER_PAYMENTS, Feature.SUPPLIER_PAYMENTS, "ReportManagement.Payment_UI");
            addFeatureButtonIfPermitted(Feature.VIEW_PROCESSED_PAYMENTS, Feature.VIEW_PROCESSED_PAYMENTS, "ReportManagement.Payment_List_UI");
            addFeatureButtonIfPermitted(Feature.PURCHASING_REPORT, Feature.PURCHASING_REPORT, "ReportManagement.PurchaseReportMain");
        
        // Configure button sizes to be consistent
        for (Component comp : pnlFeatures.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                // Set preferred size to make buttons more consistently sized
                button.setPreferredSize(new Dimension(240, 100));
                // Add some padding inside the button
                button.setMargin(new Insets(10, 10, 10, 10));
            }
        }
        
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
            
            // Set consistent size for all buttons
            button.setPreferredSize(new Dimension(240, 100));
            button.setMinimumSize(new Dimension(200, 80));
            
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
                        
                        // Role-based redirection
                        String redirectClass = className;
                        String userRole = currentUser.getRole();
                        
                        // Handle Display Requisitions based on role
                        if (featureName.equals(Feature.DISPLAY_REQUISITION)) {
                            
                            if ("Administrator".equals(userRole) || "Sales Manager".equals(userRole)) {
                                redirectClass = "PurchaseRequisition.PurchaseRequisitionList";
                            } else {
                                redirectClass = "PurchaseOrder.View_PRList";
                            }
                        } 
                        // Handle Purchase Orders List
                        else if (className.equals("PurchaseOrder.PO_Panel") || 
                                 featureName.equals(Feature.PURCHASE_ORDERS_LIST)) {
                            
                            
                            if ("Purchase Manager".equals(userRole)) {
                                redirectClass = "PurchaseOrder.PM_View_PO";
                            } else if ("Finance Manager".equals(userRole)) {
                                redirectClass = "PurchaseOrder.FM_View_PO_Approval";
                            } else if ("Administrator".equals(userRole)) {
                                redirectClass = "PurchaseOrder.PO_List_UI";
                            } else if ("Sales Manager".equals(userRole) || 
                                      "Inventory Manager".equals(userRole)) {
                                redirectClass = "PurchaseOrder.View_All_PO_UI";
                            }
                        }
                        // Handle Purchase Order Generation - Fixed to use Main_PO
                        else if (featureName.equals(Feature.GENERATE_PURCHASE_ORDER)) {
                            
                            // For Admin and Purchase Manager, use Main_PO which shows PR list
                            if ("Purchase Manager".equals(userRole) || "Administrator".equals(userRole)) {
                                redirectClass = "PurchaseOrder.Main_PO";
                            }
                            // For Inventory Manager, show view-only PO list
                            else if ("Inventory Manager".equals(userRole)) {
                                redirectClass = "PurchaseOrder.View_All_PO_UI"; 
                            }
                        }
                        
                        container.showFeature(redirectClass);
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
