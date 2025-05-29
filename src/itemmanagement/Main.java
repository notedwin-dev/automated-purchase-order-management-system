/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itemmanagement;

/**
 *
 * @author nixon
 */
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                new MainPanel().setVisible(true); // It opens my MainPanel.java
                new ItemList().setVisible(true); // It opens my MainPanel.java
            }
        });
     }
}
