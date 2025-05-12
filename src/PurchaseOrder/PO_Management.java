/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;

public class PO_Management implements PO_Operation{
    private JTable prTable;
    
    public PO_Management(JTable PRTable) {
        this.prTable = PRTable;
    }
 
        //================ REFRESH FUNCTION ================//
    @Override
    public void refresh() {
        DefaultTableModel tmodel = (DefaultTableModel) prTable.getModel();
        tmodel.setRowCount(0); 

        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseRequisition/PR.txt"))) {
            String line;
            boolean isFirstLine = true;
            int rowNo = 1;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                //----- Split line safely even with commas inside {} -----//
                String[] data = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (data.length == 7) {
                    String prID = data[0].trim();
                    String date = data[1].trim();
                    String smName = data[2].trim();
                    String smID = data[3].trim();
                    String[] itemCodes = data[4].replace("{","").replace("}","").split(",");
                    String[] quantities = data[5].replace("{","").replace("}","").split(",");
                    String status = data[6].trim();

                    //----- Join with newline -----//
                    String itemCodeText = String.join("\n", itemCodes).trim();
                    String quantityText = String.join("\n", quantities).trim();

                    // Add row to table
                    tmodel.addRow(new Object[]{
                        rowNo++,
                        prID,
                        date,
                        smName,
                        smID,
                        itemCodeText,
                        quantityText,
                        status
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
        }

        // Apply custom renderer after loading data
        applyCustomRenderer();
    }

    // - - - - - Create a custom TableCellRenderer for multi-line text - - - - - //
    public class MultiLineRenderer extends JTextArea implements TableCellRenderer {

        public MultiLineRenderer() {
            setWrapStyleWord(true);
            setLineWrap(true);
            setOpaque(true); 
            setFont(new Font("Arial", Font.PLAIN, 12)); 
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());

            //----- Dynamically adjust row height based on number of lines -----//
            int numLines = getLineCount();
            int rowHeight = numLines * getFontMetrics(getFont()).getHeight();
            table.setRowHeight(row, rowHeight);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }

    //----- Call this method to apply the renderer to Item Code and Quantity columns -----//
    public void applyCustomRenderer() {
        //----- Apply custom renderer to the Item Code and Quantity columns -----//
        prTable.getColumnModel().getColumn(5).setCellRenderer(new MultiLineRenderer()); // Item Code column
        prTable.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer()); // Quantity column
    }

    //----- Method to initialize the table and setup the default renderer -----//
    public void initTable() {
        prTable = new JTable();
        DefaultTableModel tmodel = new DefaultTableModel(new Object[]{
            "No.", "PR ID", "Date", "SM Name", "SM ID", "Item Code", "Quantity", "Status"
        }, 0);
        prTable.setModel(tmodel);

        //----- Apply custom renderer to display multi-line data in Item Code and Quantity columns -----//
        applyCustomRenderer();
    }


}
