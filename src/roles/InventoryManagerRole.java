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
        addPermission(Feature.ITEM_ENTRY, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.SUPPLIER_ENTRY, PermissionLevel.VIEW_ONLY);
        
        // Sales Management - View only for reports, no access to daily operations
        addPermission(Feature.DAILY_SALES, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.SALES_ENTRY, PermissionLevel.NO_ACCESS);
        addPermission(Feature.SALES_REPORT, PermissionLevel.VIEW_ONLY);
        
        // Purchase Requisition - View only
        addPermission(Feature.PURCHASE_REQUISITION, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.DISPLAY_REQUISITION, PermissionLevel.VIEW_ONLY);
        
        // Purchase Orders - View only to verify received items
        addPermission(Feature.PURCHASE_ORDERS_LIST, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.GENERATE_PURCHASE_ORDER, PermissionLevel.VIEW_ONLY);
        
        // User Management - No access
        addPermission(Feature.USER_MANAGEMENT, PermissionLevel.NO_ACCESS);
        
        // Inventory Management - Full access
        addPermission(Feature.INVENTORY_MANAGEMENT, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        addPermission(Feature.STOCK_REPORTS, PermissionLevel.GENERATE_REPORTS);
        
        // Financial Management - No access
        addPermission(Feature.SUPPLIER_PAYMENTS, PermissionLevel.NO_ACCESS);
    }
}
