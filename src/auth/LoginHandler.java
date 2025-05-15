/*
 * LoginHandler.java - Class to modify the Login behavior
 */
package auth;

import dashboard.Dashboard;
import javax.swing.JOptionPane;

/**
 * This class handles login logic from the Login form.
 * @author notedwin-dev
 */
public class LoginHandler {
    
    /**
     * Process the login attempt
     * 
     * @param loginForm The Login form
     * @param username The username entered
     * @param password The password entered
     */
    public static void processLogin(Login loginForm, String username, String password) {
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginForm, "Please enter both username and password");
            return;
        }
        
        // Authenticate user
        User loggedInUser = UserAuthenticator.authenticate(username, password);
        
        if (loggedInUser != null) {
            // Set the current user in SecurityUtils
            roles.SecurityUtils.setCurrentUser(loggedInUser);
            
            // Open dashboard with the logged-in user
            Dashboard dashboard = new Dashboard(loggedInUser);
            dashboard.setVisible(true);
            
            // Close the login form
            loginForm.dispose();
        } else {
            JOptionPane.showMessageDialog(loginForm, "Invalid username or password");
        }
    }
}
