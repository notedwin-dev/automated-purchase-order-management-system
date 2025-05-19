/*
 * FinanceManagerRole.java - Represents the Finance Manager role
 */
package roles;

/**
 * Represents the Finance Manager role in the system.
 * Finance Managers handle financial approvals, verify inventory updates, and process supplier payments.
 * @author notedwin-dev
 */
public class FinanceManagerRole extends Role {
    
    /**
     * Constructor
     */
    public FinanceManagerRole() {
        super(RoleFactory.FINANCE_MANAGER);
    }
    
    /**
     * Sets up permissions for the Finance Manager role.
     */
    @Override
    protected void setupPermissions() {
        // Item and Supplier Management - No access for items, view for suppliers
        addPermission(Feature.ITEM_ENTRY, PermissionLevel.NO_ACCESS);
        addPermission(Feature.SUPPLIER_ENTRY, PermissionLevel.NO_ACCESS);
        
        // Sales Management - No access
        addPermission(Feature.SALES_ENTRY, PermissionLevel.NO_ACCESS);
        
        // Purchase Requisition - View only
        addPermission(Feature.PURCHASE_REQUISITION, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.DISPLAY_REQUISITION, PermissionLevel.VIEW_ONLY);
        
        // Purchase Orders - Approve only for generate, view only for list
        addPermission(Feature.PURCHASE_ORDERS_LIST, PermissionLevel.VIEW_ONLY);
        addPermission(Feature.GENERATE_PURCHASE_ORDER, PermissionLevel.APPROVE_ONLY);
        
        // User Management - No access
        addPermission(Feature.USER_MANAGEMENT, PermissionLevel.NO_ACCESS);
        
        // Inventory Management - Verify updates only
        addPermission(Feature.INVENTORY_MANAGEMENT, PermissionLevel.VERIFY_ONLY);
        addPermission(Feature.STOCK_REPORTS, PermissionLevel.VIEW_ONLY);
        
        // Financial Management - Full access
        addPermission(Feature.FINANCIAL_REPORTS, PermissionLevel.GENERATE_REPORTS);
        addPermission(Feature.SUPPLIER_PAYMENTS, PermissionLevel.PROCESS_PAYMENTS);
    }
}
