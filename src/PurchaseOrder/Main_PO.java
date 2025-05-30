/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import PurchaseRequisition.PurchaseRequisitionManagement;

/**
 *
 * @author nixon
 */
public class Main_PO {
    private PurchaseRequisitionManagement prmanagement;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PurchaseRequisitionManagement prmanagement = new PurchaseRequisitionManagement();
                new PO_Panel(prmanagement).setVisible(true);
            }
        });
    }
}
