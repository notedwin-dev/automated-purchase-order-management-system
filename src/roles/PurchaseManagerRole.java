/*
 * PurchaseManagerRole.java - Represents the Purchase Manager role
 */
package roles;

/**
 * Represents the Purchase Manager role in the system.
 * Purchase Managers can view items and suppliers, and generate purchase orders.
 * @author notedwin-dev
 */
public class PurchaseManagerRole extends Role {
    
    /**
     * Constructor
     */
    public PurchaseManagerRole() {
        super(RoleFactory.PURCHASE_MANAGER);
    }
    
    /**
     * Sets up permissions for the Purchase Manager role.
     */
    @Override
    protected void setupPermissions() {
        // Item and Supplier Management - View only
        addPermission(Feature.ITEM_ENTRY, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.SUPPLIER_ENTRY, PermissionLevel.VIEW_ONLY);
        
        // Sales Management - No access
        addPermission(Feature.SALES_ENTRY, PermissionLevel.NO_ACCESS);
        
        // Purchase Requisition - View only
        addPermission(Feature.PURCHASE_REQUISITION, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.DISPLAY_REQUISITION, PermissionLevel.VIEW_ONLY);
        
        // Purchase Orders - Full access for generate, view only for list
        addPermission(Feature.PURCHASE_ORDERS_LIST, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.GENERATE_PURCHASE_ORDER, PermissionLevel.ADD_EDIT_DELETE_VIEW);
        
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
