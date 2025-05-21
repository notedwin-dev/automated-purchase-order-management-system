/*
 * MainContainer.java - Main container frame for the application
 * Holds the Navbar and feature panels in a tabbed-like interface
 */
package dashboard;

import auth.Login;
import auth.Session;
import auth.User;
import components.Navbar;
import roles.Feature;

import javax.swing.*;

import PurchaseRequisition.PR_Management;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Main container for the application that holds the Navbar and content panels.
 * Supports switching between different feature panels without closing the application.
 * @author notedwin-dev
 */
public class MainContainer extends javax.swing.JFrame {

    // User data
    private User currentUser;
    
    // Components
    private Navbar navbar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Active panels cache
    private Map<String, Component> activePanels;
    
    /**
     * Creates new MainContainer (requires login)
     */
    public MainContainer() {
        // Check if a user is logged in first
        User sessionUser = Session.getInstance().getCurrentUser();
        if (sessionUser == null) {
            // No user logged in, show login
            SwingUtilities.invokeLater(() -> {
                Login login = new Login();
                login.setVisible(true);
            });
            // Close this container
            dispose();
            return;
        } else {
            this.currentUser = sessionUser;
            initialize();
        }
    }
    
    /**
     * Creates new MainContainer with a specified user
     * 
     * @param user The logged-in user
     */
    public MainContainer(User user) {
        this.currentUser = user;
        
        // Store user in session
        Session.getInstance().setCurrentUser(user);
        initialize();
    }
    
    /**
     * Initialize the container components
     */
    private void initialize() {
        // Initialize cache for active panels
        activePanels = new HashMap<>();
        
        // Setup frame properties
        setTitle("OMEGA WHOLESALE SDN BHD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        // Setup the navbar
        setupNavbar();
        mainPanel.add(navbar, BorderLayout.WEST);
        
        // Create content panel with card layout
        cardLayout = new CardLayout();        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add the Dashboard panel as default view
        DashboardPanel dashboardPanel = new DashboardPanel(currentUser);
        contentPanel.add(dashboardPanel, "dashboard.DashboardPanel");
        activePanels.put("dashboard.DashboardPanel", dashboardPanel);
        cardLayout.show(contentPanel, "dashboard.DashboardPanel");
        
        // Add main panel to frame
        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Sets up the navbar
     */
    private void setupNavbar() {
        // Create the navbar with user
        navbar = new Navbar(currentUser);
        
        // Set up navigation listener
        navbar.setNavigationListener(new Navbar.NavigationListener() {
            @Override
            public void onNavigate(String className) {
                // Show the selected feature
                showFeature(className);
            }
        });
    }
      /**
     * Shows a feature panel in the content area or opens a new JFrame
     * 
     * @param className The fully qualified class name of the feature
     */    
    public void showFeature(String className) {
        try {
            // Special handling for logout
            if (className.equals("auth.Login")) {
                logout();
                return;
            }
            
            // Special handling for Dashboard
            if (className.equals("dashboard.Dashboard")) {
                // Show the dashboard panel instead
                cardLayout.show(contentPanel, "dashboard.DashboardPanel");
                return;
            }
            
            // Special handling for PurchaseOrder.Main_PO
            if (className.equals("PurchaseOrder.Main_PO")) {
                try {
                    // Check if already in cache
                    if (activePanels.containsKey(className)) {
                        cardLayout.show(contentPanel, className);
                        return;
                    }
                    
                    // Create PR_Management instance
                    PR_Management prmanagement = new PR_Management();
                    
                    // Create the PO_Panel with the PR_Management instance
                    JFrame poPanel = new PurchaseOrder.PO_Panel(prmanagement);
                    
                    // Extract content from the frame
                    Container frameContent = poPanel.getContentPane();
                    JPanel poContentPanel = new JPanel(new BorderLayout());
                    
                    // Get all components from the frame content and add to our panel
                    Component[] components = frameContent.getComponents();
                    for (Component component : components) {
                        poContentPanel.add(component, BorderLayout.CENTER);
                    }
                    
                    // Add to content panel
                    this.contentPanel.add(poContentPanel, className);
                    activePanels.put(className, poContentPanel);
                    cardLayout.show(this.contentPanel, className);
                    
                    // Dispose of the temporary frame
                    poPanel.dispose();
                    
                    return;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error showing Purchase Order view: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }
            }
            
            // Load the feature class
            Class<?> featureClass = Class.forName(className);
            
            // Check if the class is a JFrame or JPanel
            if (JPanel.class.isAssignableFrom(featureClass)) {
                // Check if panel is already in the cache
                if (activePanels.containsKey(className)) {
                    // Show existing panel
                    cardLayout.show(contentPanel, className);
                    return;
                }
                
                // It's a JPanel, create it directly
                JPanel featurePanel;
                try {
                    // Try to find constructor that takes a User parameter
                    Constructor<?> constructor = featureClass.getConstructor(User.class);
                    featurePanel = (JPanel) constructor.newInstance(currentUser);
                } catch (NoSuchMethodException e) {
                    // Fall back to default constructor
                    featurePanel = (JPanel) featureClass.getDeclaredConstructor().newInstance();
                }
                
                // Add the feature panel to the content panel
                contentPanel.add(featurePanel, className);
                
                // Cache the panel for future use
                activePanels.put(className, featurePanel);
                
                // Show the panel
                cardLayout.show(contentPanel, className);
            } else if (JFrame.class.isAssignableFrom(featureClass)) {
                // Check if frame content is already in the cache
                if (activePanels.containsKey(className)) {
                    // Show existing panel
                    cardLayout.show(contentPanel, className);
                    return;
                }
                
                // It's a JFrame, but we'll extract its content panel instead of opening a new window
                JFrame tempFrame;
                try {
                    // Try to find constructor that takes a User parameter
                    Constructor<?> constructor = featureClass.getConstructor(User.class);
                    tempFrame = (JFrame) constructor.newInstance(currentUser);
                } catch (NoSuchMethodException e) {
                    try {
                        // Fall back to default constructor
                        tempFrame = (JFrame) featureClass.getDeclaredConstructor().newInstance();
                        
                        // If there's a setUser method, call it
                        try {
                            java.lang.reflect.Method setUserMethod = featureClass.getMethod("setUser", User.class);
                            setUserMethod.invoke(tempFrame, currentUser);
                        } catch (NoSuchMethodException ex) {
                            // No setUser method, ignore
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to instantiate " + className, ex);
                    }
                }
                
                // Ensure the session is updated
                Session.getInstance().setCurrentUser(currentUser);
                
                // Extract the content pane from the JFrame
                Container frameContent = tempFrame.getContentPane();
                JPanel framePanel = new JPanel(new BorderLayout());
                
                // Get all components from the JFrame's content pane and add them to our panel
                Component[] components = frameContent.getComponents();
                for (Component component : components) {
                    framePanel.add(component, BorderLayout.CENTER);
                }
                
                // Add the extracted panel to our content panel
                contentPanel.add(framePanel, className);
                
                // Cache the panel for future use
                activePanels.put(className, framePanel);
                
                // Show the panel
                cardLayout.show(contentPanel, className);
                
                // We don't need the temporary frame anymore
                tempFrame.dispose();
            } else {
                // Not a supported type
                throw new IllegalArgumentException("Feature class must be JPanel or JFrame");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error showing feature: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            // Clear the session
            Session.getInstance().logout();
            
            // Show login screen
            Login login = new Login();
            login.setVisible(true);
            
            // Close this container
            this.dispose();
        }
    }
    
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainContainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainContainer().setVisible(true);
        });
    }
}
