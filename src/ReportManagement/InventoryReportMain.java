/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ReportManagement;


import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class InventoryReportMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            InventoryReportUI inventoryReportUI = new InventoryReportUI();
            inventoryReportUI.setVisible(true);
        });
    }
    
}
