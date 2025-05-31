/*
 * AdministratorRole.java - Represents the Administrator role
 */
package roles;

/**
 * Represents the Administrator role in the system.
 * Administrators have full access to all features.
 * @author notedwin-dev
 */
public class AdministratorRole extends Role {
    
    /**
     * Constructor
     */
    public AdministratorRole() {
        super(RoleFactory.ADMINISTRATOR);
    }
    
    /**
     * Sets up permissions for the Administrator role.
     * Administrators have full access to all features.
     */
    @Override
    protected void setupPermissions() {
        // Item and Supplier Management
        addPermission(Feature.ITEM_ENTRY, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.ITEM_LIST, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.SUPPLIER_LIST, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.SUPPLIER_ENTRY, PermissionLevel.FULL_ACCESS);
        
        // Sales Management
        addPermission(Feature.SALES_ENTRY, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.SALES_REPORT, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.DAILY_SALES, PermissionLevel.FULL_ACCESS);
        
        // Purchase Requisition
        addPermission(Feature.PURCHASE_REQUISITION, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.DISPLAY_REQUISITION, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.PURCHASING_REPORT, PermissionLevel.FULL_ACCESS);
        
        // Purchase Orders
        addPermission(Feature.PURCHASE_ORDERS_LIST, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.VIEW_PURCHASE_ORDER, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.GENERATE_PURCHASE_ORDER, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.APPROVE_PURCHASE_ORDER, PermissionLevel.FULL_ACCESS);
        
        // User Management
        addPermission(Feature.USER_MANAGEMENT, PermissionLevel.FULL_ACCESS);
        
        // Inventory Management
        addPermission(Feature.INVENTORY_MANAGEMENT, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.INVENTORY_LIST, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.STOCK_REPORTS, PermissionLevel.FULL_ACCESS);
        
        // Financial Management
        addPermission(Feature.SUPPLIER_PAYMENTS, PermissionLevel.FULL_ACCESS);
        addPermission(Feature.VIEW_PROCESSED_PAYMENTS, PermissionLevel.FULL_ACCESS);
    }
}
