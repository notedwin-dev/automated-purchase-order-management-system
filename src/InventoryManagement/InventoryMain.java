/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package InventoryManagement;

import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class InventoryMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            InventoryUI inventoryUI = new InventoryUI();
            inventoryUI.setVisible(true);
        });
    }
    
}
