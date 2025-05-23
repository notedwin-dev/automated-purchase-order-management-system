/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author nixon
 */

/**
 * The main class that launches the Purchase Report UI.
 */
public class PurchaseReportMain {
    /**
     * The main method that starts the application.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PurchaseReportUI().setVisible(true); 
            }
        });
    }
}

