/*
 * Main.java - Entry point for the application
 */
package dashboard;

import auth.Login;
import auth.Session;
import auth.User;

import javax.swing.*;

/**
 * Main entry point for the application.
 * @author notedwin-dev
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        // Start the application with SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            // Check if a user is already logged in (e.g., from a previous session)
            User currentUser = Session.getInstance().getCurrentUser();
            
            if (currentUser != null) {
                // A user is already logged in, show the main container
                MainContainer mainContainer = new MainContainer(currentUser);
                mainContainer.setVisible(true);
            } else {
                // No user is logged in, show the login screen
                Login login = new Login();
                login.setVisible(true);
            }
        });
    }
}
