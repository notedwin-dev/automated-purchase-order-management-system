/*
 * SalesManagerRole.java - Represents the Sales Manager role
 */
package roles;

/**
 * Represents the Sales Manager role in the system.
 * Sales Managers can manage items, suppliers, sales entries, and purchase requisitions.
 * @author notedwin-dev
 */
public class SalesManagerRole extends Role {
    
    /**
     * Constructor
     */
    public SalesManagerRole() {
        super(RoleFactory.SALES_MANAGER);
    }
    
    /**
     * Sets up permissions for the Sales Manager role.
     */
    @Override
    protected void setupPermissions() {
        // Item and Supplier Management - Full access
        addPermission(Feature.ITEM_ENTRY, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        addPermission(Feature.SUPPLIER_ENTRY, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        
        // Sales Management - Full access
        addPermission(Feature.SALES_ENTRY, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        
        // Purchase Requisition - Full access for create, view only for display
        addPermission(Feature.PURCHASE_REQUISITION, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        addPermission(Feature.DISPLAY_REQUISITION, PermissionLevel.VIEW_ONLY);
        
        // Purchase Orders - View only
        addPermission(Feature.PURCHASE_ORDERS_LIST, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.GENERATE_PURCHASE_ORDER, PermissionLevel.NO_ACCESS);
        
        // User Management - No access
        addPermission(Feature.USER_MANAGEMENT, PermissionLevel.NO_ACCESS);
        
        // Inventory Management - No access
        addPermission(Feature.INVENTORY_MANAGEMENT, PermissionLevel.NO_ACCESS);
        addPermission(Feature.STOCK_REPORTS, PermissionLevel.NO_ACCESS);
        
        // Financial Management - No access
        addPermission(Feature.FINANCIAL_REPORTS, PermissionLevel.NO_ACCESS);
        addPermission(Feature.SUPPLIER_PAYMENTS, PermissionLevel.NO_ACCESS);
    }
}
