/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseRequisition;

import TextFile_Handler.TextFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class PR_Management {

    private final List<PROperation> prlist;
    // Add a temporary list for unsaved PRs
    private final List<PROperation> tempPRList = new ArrayList<>();

    public PR_Management() {
        this.prlist = new ArrayList<>();
    }

    public void getPRfromtxtfile() {
        List<PROperation> Prlist = new ArrayList<>();
        List<String> lines = TextFile.readFile("src/PurchaseRequisition/PR.txt");

        for (String line : lines) {
            String[] parts = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)"); // handle {a,b} fields correctly

            if (parts.length == 8) {
                String prID = parts[0].trim();
                String date = parts[1].trim();
                String prCreatedByName = parts[2].trim();
                String prCreatedByID = parts[3].trim();

                String[] itemCodes = parts[4].replace("{", "").replace("}", "").split(",");
                String[] quantities = parts[5].replace("{", "").replace("}", "").split(",");

                String itemCodeText = Arrays.stream(itemCodes).map(String::trim).collect(Collectors.joining("\n"));
                String quantityText = Arrays.stream(quantities).map(String::trim).collect(Collectors.joining("\n"));

                String expectedDate = parts[6].trim();
                String status = parts[7].trim();

                PROperation pr = new PROperation(prID, date, prCreatedByName, prCreatedByID, itemCodeText, quantityText, expectedDate, status);
                prlist.add(pr);
            }
        }
    }

//    PR0001, 25-3-2025, Simon, SM0001, {DY0001, DY002}, {10, 5}, 10-04-2025, PENDING
    public PROperation findprid(String prid) {
        for (PROperation pr : prlist) {
            if (pr.getPRID().equals(prid)) {
                return pr;
            }
        }
        return null;
    }

    public List<PROperation> getPrlist() {
        return prlist;
    }

    public String generateNextPRID() {
        int maxID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseRequisition/PR.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length > 0) {
                    String currentPRID = parts[0].trim();

                    // Match PR IDs like PR0001, PR0002, etc.
                    if (currentPRID.matches("PR\\d{4}")) {
                        int numericPart = Integer.parseInt(currentPRID.substring(2));
                        if (numericPart > maxID) {
                            maxID = numericPart;
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading PR.txt: " + e.getMessage());
        }

        // Increment the max ID and return new PRID in format PR0001, PR0002, etc.
        return String.format("PR%04d", maxID + 1);
    }

    public void addRowToTable(javax.swing.JTable table, javax.swing.JComboBox<String> cbItemCode, javax.swing.JTextField txtQuantity, java.awt.Component parentComponent) {
    try {
        String selectedItemCode = cbItemCode.getSelectedItem().toString();
        String enteredQuantity = txtQuantity.getText();

        // Check if the item code already exists in the table
        for (int i = 0; i < table.getRowCount(); i++) {
            String codeInTable = table.getValueAt(i, 0).toString();
            if (codeInTable.equals(selectedItemCode)) {
                JOptionPane.showMessageDialog(parentComponent, "Item code already exists in the table. Please use the update function to modify its quantity.", "Duplicate Item", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        // Validate Quantity
        if (enteredQuantity.isEmpty() || !enteredQuantity.matches("\\d+") || enteredQuantity.startsWith("0") || Integer.parseInt(enteredQuantity) <= 0) {
            JOptionPane.showMessageDialog(parentComponent, "Quantity must be a numeric value greater than 0 and cannot start with 0.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
            return;
        }

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        model.addRow(new Object[]{selectedItemCode, enteredQuantity});
        table.setModel(model); // Ensure the table model is updated
        JOptionPane.showMessageDialog(parentComponent, "Item has been added to Items Table successfully!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(parentComponent, "Error adding data: " + e.getMessage());
    }
}

    public void deleteRowFromTable(javax.swing.JTable table, java.awt.Component parentComponent) {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentComponent, "Please select a row to delete.", "Delete Item", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(parentComponent, "Are you sure you want to delete the selected row?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(parentComponent, "Row deleted successfully.", "Delete Item", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentComponent, "Delete operation cancelled.", "Delete Item", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Error deleting row: " + e.getMessage());
        }
    }

    public void updateRowInTable(javax.swing.JTable table, javax.swing.JComboBox<String> cbItemCode, javax.swing.JTextField txtQuantity, java.awt.Component parentComponent) {
        try {
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentComponent, "Please select a row to update.", "Update Item", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String newItemCode = cbItemCode.getSelectedItem() != null ? cbItemCode.getSelectedItem().toString() : "";
            String newQuantity = txtQuantity.getText();

            if (newItemCode.isEmpty() || newQuantity.isEmpty()) {
                JOptionPane.showMessageDialog(parentComponent, "Item Code and Quantity cannot be empty.", "Update Item", JOptionPane.WARNING_MESSAGE);
                return;
            }

            model.setValueAt(newItemCode, selectedRow, 0);
            model.setValueAt(newQuantity, selectedRow, 1);

            JOptionPane.showMessageDialog(parentComponent, "Row updated successfully.", "Update Item", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Error updating row: " + e.getMessage());
        }
    }

    public boolean removeRecord(String prIDToDelete) {
        String filePath = "src/PurchaseRequisition/PR.txt";
        String tempFile = "src/PurchaseRequisition/PR_temp.txt";
        File oldFile = new File(filePath);
        File newFile = new File(tempFile);

        boolean recordFound = false;

        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr); FileWriter fw = new FileWriter(newFile); BufferedWriter bw = new BufferedWriter(fw); PrintWriter pw = new PrintWriter(bw)) {

            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                // Split the line to get the PR ID
                String[] parts = currentLine.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (parts.length > 0 && parts[0].trim().equals(prIDToDelete)) {
                    recordFound = true;
                } else {
                    pw.println(currentLine);
                }
            }

            pw.flush();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in removeRecord: " + e.getMessage());
            return false; // Indicate failure
        }

        if (oldFile.delete()) {
            if (!newFile.renameTo(oldFile)) {
                JOptionPane.showMessageDialog(null, "Could not rename temporary file to original file.");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Could not delete original file.");
            return false;
        }

        return recordFound;
    }

    // Add this method to allow saving a PROperation to PR.txt
    public void add(PROperation newPr) {
        if (newPr == null) {
            JOptionPane.showMessageDialog(null, "PR data is null!");
            return;
        }
        // Ensure the date is always saved in the correct format
        String formattedDate = newPr.getDate();
        if (formattedDate == null || formattedDate.isEmpty()) {
            formattedDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
        }
        String formattedItemCodes = "{" + newPr.getItemCode().replace("\n", ", ") + "}";
        String formattedQuantities = "{" + newPr.getQuantity().replace("\n", ", ") + "}";
        String record = newPr.getPRID() + ", " + formattedDate + ", " + newPr.getPrCreatedByName() + ", "
                + newPr.getPrCreatedByID() + ", " + formattedItemCodes + ", " + formattedQuantities + ", "
                + newPr.getExDate() + ", " + newPr.getStatus();
        try {
            TextFile.appendTo("src/PurchaseRequisition/PR.txt", record);
            prlist.add(newPr);
            JOptionPane.showMessageDialog(null, "PR added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving PR to file: " + e.getMessage());
        }
    }

    public void savePRFromTable(
        javax.swing.JTable table,
        javax.swing.JTextField txtPRID,
        javax.swing.JTextField txtDate,
        javax.swing.JTextField txtSMName,
        javax.swing.JTextField txtSMID,
        javax.swing.JComboBox<String> cbStatus,
        com.toedter.calendar.JDateChooser txtExDate,
        java.awt.Component parentComponent
    ) {
        try {
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
            int rowCount = model.getRowCount();
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(parentComponent, "Please add at least one item to the table.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String prID = txtPRID.getText().trim();
            String date = txtDate.getText().trim();
            String smName = txtSMName.getText().trim();
            String smID = txtSMID.getText().trim();
            String status = cbStatus.getSelectedItem() != null ? cbStatus.getSelectedItem().toString() : "PENDING";
            String exDate = "";
            if (txtExDate.getDate() != null) {
                exDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(txtExDate.getDate());
            }
            java.util.List<String> itemCodes = new java.util.ArrayList<>();
            java.util.List<String> quantities = new java.util.ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                String code = model.getValueAt(i, 0).toString().trim();
                String qty = model.getValueAt(i, 1).toString().trim();
                itemCodes.add(code);
                quantities.add(qty);
            }
            PROperation pr = new PROperation(
                prID,
                date,
                smName,
                smID,
                String.join("\n", itemCodes),
                String.join("\n", quantities),
                exDate,
                status
            );
            add(pr);
            JOptionPane.showMessageDialog(parentComponent, "Purchase Requisition generated and saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Error generating PR: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
