/*
 * RoleFactory.java - Factory class for creating role instances
 */
package roles;

/**
 * Factory class responsible for creating appropriate role instances.
 * Uses the Factory design pattern.
 * @author notedwin-dev
 * @see Role
 */
public class RoleFactory {
    // Role type constants
    public static final String ADMINISTRATOR = "Administrator";
    public static final String SALES_MANAGER = "Sales Manager";
    public static final String PURCHASE_MANAGER = "Purchase Manager";
    public static final String INVENTORY_MANAGER = "Inventory Manager";
    public static final String FINANCE_MANAGER = "Finance Manager";
    
    /**
     * Creates and returns a Role object based on the role name
     * 
     * @param roleName The name of the role to create
     * @return The appropriate Role object, or null if the role name is not recognized
     */
    public static Role createRole(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return null;
        }
        
        // Normalize the role name by trimming and converting to title case
        String normalizedRole = roleName.trim();
        
        switch (normalizedRole) {
            case ADMINISTRATOR:
                return new AdministratorRole();
            case SALES_MANAGER:
                return new SalesManagerRole();
            case PURCHASE_MANAGER:
                return new PurchaseManagerRole();
            case INVENTORY_MANAGER:
                return new InventoryManagerRole();
            case FINANCE_MANAGER:
                return new FinanceManagerRole();
            default:
                return null;
        }
    }
}
