/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PO_Management implements PO_Operation{
    private JTable prTable;
    
    public PO_Management(JTable PRTable) {
        this.prTable = PRTable;
    }
 
    // - - - - - - - - - - REFRESH FUNCTION - - - - - - - - - - //
    @Override
    public void refresh() {
        DefaultTableModel tmodel = (DefaultTableModel) prTable.getModel();
        tmodel.setRowCount(0); //----- Clears the row to avoid data repeatation -----//
        
        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseRequisition/PR.txt"))) {
            String line;
            boolean isColumnName = true;
            
            while ((line = reader.readLine()) != null) {
                if (isColumnName) {
                isColumnName = false;
                continue;
                }
                
                String[] data = line.split(",");
                if (data.length == 8) { 
                    tmodel.addRow(new Object[] {
                        data[0],    //----- No. 
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        data[6],
                        data[7]    //----- Status    
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while reading from this file: " + e.getMessage());
        }
    }
}
