/*
 * InventoryManagerRole.java - Represents the Inventory Manager role
 */
package roles;

/**
 * Represents the Inventory Manager role in the system.
 * Inventory Managers manage stock levels, update inventory, and generate stock reports.
 * @author notedwin-dev
 */
public class InventoryManagerRole extends Role {
    
    /**
     * Constructor
     */
    public InventoryManagerRole() {
        super(RoleFactory.INVENTORY_MANAGER);
    }
    
    /**
     * Sets up permissions for the Inventory Manager role.
     */
    @Override
    protected void setupPermissions() {
        // Item and Supplier Management - View only
        addPermission(Feature.ITEM_LIST, PermissionLevel.VIEW_ONLY);
        
        // Purchase Orders - View only
        addPermission(Feature.PURCHASE_ORDERS_LIST, PermissionLevel.VIEW_ONLY);

        // User Management - No access
        addPermission(Feature.USER_MANAGEMENT, PermissionLevel.NO_ACCESS);
        
        // Inventory Management - Full access
        addPermission(Feature.INVENTORY_MANAGEMENT, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        addPermission(Feature.STOCK_REPORTS, PermissionLevel.GENERATE_REPORTS);
        
        // Financial Management - No access
        addPermission(Feature.SUPPLIER_PAYMENTS, PermissionLevel.NO_ACCESS);
    }
}
