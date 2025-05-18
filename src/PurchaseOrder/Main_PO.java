/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import PurchaseRequisition.PR_Management;

/**
 *
 * @author nixon
 */
public class Main_PO {
    private PR_Management prmanagement;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PR_Management prmanagement = new PR_Management();
                new PO_Panel(prmanagement).setVisible(true);
            }
        });
    }
}
