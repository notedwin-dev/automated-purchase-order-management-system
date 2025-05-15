/*
 * Role.java - Base class representing user roles in the system
 */
package roles;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all roles in the system.
 * Implements permission management functionality.
 * @see PermissionLevel
 * @see RoleFactory
 * @author notedwin-dev
 */
public abstract class Role {
    // The name of the role
    private final String roleName;
    
    // Map to store permissions (feature name -> permission level)
    private final Map<String, PermissionLevel> permissions;
    
    /**
     * Constructor
     * 
     * @param roleName The name of the role
     */
    public Role(String roleName) {
        this.roleName = roleName;
        this.permissions = new HashMap<>();
        setupPermissions();
    }
    
    /**
     * Abstract method to be implemented by each role to set up its specific permissions
     */
    protected abstract void setupPermissions();
    
    /**
     * Gets the name of the role
     * 
     * @return The role name
     */
    public String getRoleName() {
        return this.roleName;
    }
    
    /**
     * Add a permission for a specific feature
     * 
     * @param featureName The name of the feature
     * @param level The permission level for the feature
     */
    protected void addPermission(String featureName, PermissionLevel level) {
        permissions.put(featureName, level);
    }
    
    /**
     * Check if the role has access to a specific feature
     * 
     * @param featureName The name of the feature
     * @return True if the role has at least VIEW access to the feature, false otherwise
     */
    public boolean hasAccess(String featureName) {
        return permissions.containsKey(featureName) && 
               permissions.get(featureName) != PermissionLevel.NO_ACCESS;
    }
    
    /**
     * Check if the role has full access to a specific feature (can add, edit, delete)
     * 
     * @param featureName The name of the feature
     * @return True if the role has FULL_ACCESS to the feature, false otherwise
     */
    public boolean hasFullAccess(String featureName) {
        return permissions.containsKey(featureName) && 
               (permissions.get(featureName) == PermissionLevel.FULL_ACCESS ||
                permissions.get(featureName) == PermissionLevel.ADD_EDIT_DELETE_VIEW);
    }
    
    /**
     * Check if the role has view-only access to a specific feature
     * 
     * @param featureName The name of the feature
     * @return True if the role has VIEW_ONLY access to the feature, false otherwise
     */
    public boolean hasViewOnlyAccess(String featureName) {
        return permissions.containsKey(featureName) && 
               permissions.get(featureName) == PermissionLevel.VIEW_ONLY;
    }
    
    /**
     * Check if the role can modify a specific feature (add, edit, delete)
     * 
     * @param featureName The name of the feature
     * @return True if the role has ADD_EDIT_DELETE_VIEW access to the feature, false otherwise
     */
    public boolean canModify(String featureName) {
        return permissions.containsKey(featureName) && 
               (permissions.get(featureName) == PermissionLevel.ADD_EDIT_DELETE_VIEW ||
                permissions.get(featureName) == PermissionLevel.FULL_ACCESS);
    }
    
    /**
     * Get the permission level for a specific feature
     * 
     * @param featureName The name of the feature
     * @return The permission level for the feature, or NO_ACCESS if not defined
     */
    public PermissionLevel getPermissionLevel(String featureName) {
        return permissions.getOrDefault(featureName, PermissionLevel.NO_ACCESS);
    }
    
    /**
     * Get all permissions for this role
     * 
     * @return A map of all feature permissions
     */
    public Map<String, PermissionLevel> getAllPermissions() {
        return new HashMap<>(permissions);
    }
}
