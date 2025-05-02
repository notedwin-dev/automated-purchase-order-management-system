/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DailySalesManagement;

import InventoryManagement.InventoryUI;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class DailySalesMain {
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            DailySalesUI dailySalesUI = new DailySalesUI();
            dailySalesUI.setVisible(true);
        });
    }
}
