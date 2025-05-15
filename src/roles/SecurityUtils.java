/*
 * SecurityUtils.java - Helper class for security operations
 */
package roles;

import auth.User;

/**
 * Utility class for security-related operations.
 * @author notedwin-dev
 * @see User
 */
public class SecurityUtils {
    
    // Current logged-in user (session)
    private static User currentUser;
    
    /**
     * Sets the current logged-in user
     * 
     * @param user The user to set as current
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * Gets the current logged-in user
     * 
     * @return The current user, or null if no user is logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user has access to a specific feature
     * 
     * @param user The user to check
     * @param featureName The name of the feature
     * @return True if the user has access, false otherwise
     */
    public static boolean hasAccess(User user, String featureName) {
        if (user == null || featureName == null) {
            return false;
        }
        return user.hasAccess(featureName);
    }
    
    /**
     * Checks if the current user has access to a specific feature
     * 
     * @param featureName The name of the feature
     * @return True if the current user has access, false otherwise
     */
    public static boolean currentUserHasAccess(String featureName) {
        return hasAccess(currentUser, featureName);
    }
    
    /**
     * Checks if the current user can modify a specific feature
     * 
     * @param featureName The name of the feature
     * @return True if the current user can modify, false otherwise
     */
    public static boolean currentUserCanModify(String featureName) {
        return currentUser != null && currentUser.canModify(featureName);
    }
}
