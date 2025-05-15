/*
 * PermissionLevel.java - Represents different levels of access in the system
 */
package roles;

/**
 * Enum representing different permission levels for features in the system.
 * @author notedwin-dev
 */
public enum PermissionLevel {
    /**
     * No access to the feature
     */
    NO_ACCESS,
    
    /**
     * Can only view the feature without making changes
     */
    VIEW_ONLY,
    
    /**
     * Can view, add, edit, and delete within the feature
     */
    ADD_EDIT_DELETE_VIEW,
    
    /**
     * Specialized access for verifying items
     */
    VERIFY_ONLY,
    
    /**
     * Specialized access for approving items
     */
    APPROVE_ONLY,
    
    /**
     * Specialized access for generating reports
     */
    GENERATE_REPORTS,
    
    /**
     * Specialized access for processing payments
     */
    PROCESS_PAYMENTS,
    
    /**
     * Complete access to all functionalities (for Administrator)
     */
    FULL_ACCESS
}
