/*
 * UserAuthenticator.java - Handles user authentication
 */
package auth;

import dashboard.Dashboard;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import roles.SecurityUtils;

/**
 * Handles user authentication logic
 * @author notedwin-dev
 */
public class UserAuthenticator {
    
    /**
     * Authenticates a user with the given credentials
     * 
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return The authenticated User object, or null if authentication failed
     */
    public static User authenticate(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("txtlogin.txt"));
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String un = parts[1].trim();
                    String pw = parts[2].trim();
                    String department = parts[3].trim();
                    String role = parts[4].trim();
                    
                    if (username.equals(un) && password.equals(pw)) {
                        User user = new User(id, username, password, department, role);
                        user.setID(id);
                        user.setUsername(un);
                        user.setPassword(pw);
                        user.setDepartment(department);
                        user.setRole(role);
                        
                        reader.close();
                        return user;
                    }
                }
            }
            
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading login file: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Performs login and redirects to appropriate screen
     * 
     * @param loginScreen The login screen instance
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return true if login was successful, false otherwise
     */
    public static boolean login(Login loginScreen, String username, String password) {
        User user = authenticate(username, password);
        
        if (user != null) {
            // Set the current user in SecurityUtils
            SecurityUtils.setCurrentUser(user);
            
            // Open dashboard with the logged-in user
            Dashboard dashboard = new Dashboard(user);
            dashboard.setVisible(true);
            
            // Close the login screen
            if (loginScreen != null) {
                loginScreen.dispose();
            }
            
            return true;
        } else {
            JOptionPane.showMessageDialog(loginScreen, "Invalid username or password");
            return false;
        }
    }
}
