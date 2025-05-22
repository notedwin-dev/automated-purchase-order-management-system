/*
 * Session.java - Manages the current user session
 */
package auth;

/**
 * Singleton class to manage the current user session.
 * Allows storing and retrieving the current user information across the application.
 * @author notedwin-dev
 */
public class Session {
    // Singleton instance
    private static Session instance;
    
    // Current logged in user
    private User currentUser;
    
    /**
     * Private constructor to prevent instantiation
     */
    private Session() {
        // Initialize with no user
        currentUser = null;
    }
    
    /**
     * Get the singleton instance
     * 
     * @return The Session instance
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    /**
     * Set the current user
     * 
     * @param user The user to set as current
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Get the current user
     * 
     * @return The current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if a user is logged in
     * 
     * @return True if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Clear the current user (logout)
     */
    public void logout() {
        currentUser = null;
    }
}
