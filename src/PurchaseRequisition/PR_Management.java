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

                    // Assuming PRIDs are like "PR001", "PR002", etc.
                    if (currentPRID.startsWith("PR")) {
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

        // Increment the max ID and return new PRID in format PR001, PR002, etc.
        return String.format("PR%03d", maxID + 1);
    }

    public void add(PROperation newPr) {
        // No need to read the file again here, just add the new PR object
        // and then write it to the file.

        if (newPr == null || newPr.getPRID() == null || newPr.getDate() == null || newPr.getPrCreatedByName() == null
                || newPr.getPrCreatedByID() == null || newPr.getItemCode() == null || newPr.getQuantity() == null
                || newPr.getExDate() == null || newPr.getStatus() == null) {
            JOptionPane.showMessageDialog(null, "All fields of the PR must be filled!");
            return;
        }

        // Format the item codes and quantities back into {a,b} format for writing to file
        String formattedItemCodes = "{" + newPr.getItemCode().replace("\n", ", ") + "}";
        String formattedQuantities = "{" + newPr.getQuantity().replace("\n", ", ") + "}";

        String record = newPr.getPRID() + ", " + newPr.getDate() + ", " + newPr.getPrCreatedByName() + ", "
                + newPr.getPrCreatedByID() + ", " + formattedItemCodes + ", " + formattedQuantities + ", "
                + newPr.getExDate() + ", " + newPr.getStatus();

        try {
            TextFile.appendTo("src/PurchaseRequisition/PR.txt", record);
            this.prlist.add(newPr); // Add to the in-memory list as well
            JOptionPane.showMessageDialog(null, "Data Added Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void delete(String prIDToDelete) {
        if (prIDToDelete == null || prIDToDelete.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a PR ID to delete!");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete PR ID: " + prIDToDelete + "?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                if (removeRecord(prIDToDelete)) { // Call the updated removeRecord
                    // Remove from in-memory list
                    Iterator<PROperation> iterator = prlist.iterator();
                    while (iterator.hasNext()) {
                        PROperation pr = iterator.next();
                        if (pr.getPRID().equals(prIDToDelete)) {
                            iterator.remove();
                            break; // Assuming PR IDs are unique, can stop after finding
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Successfully Deleted PR ID: " + prIDToDelete);
                } else {
                    JOptionPane.showMessageDialog(null, "PR ID: " + prIDToDelete + " not found or an error occurred.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error during delete operation: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Delete operation cancelled");
        }
    }

    public void update(PROperation updatedPr) {
        // 1. Validate the input PROperation object
        if (updatedPr == null || updatedPr.getPRID() == null || updatedPr.getPRID().trim().isEmpty()
                || updatedPr.getDate() == null || updatedPr.getPrCreatedByName() == null
                || updatedPr.getPrCreatedByID() == null || updatedPr.getItemCode() == null
                || updatedPr.getQuantity() == null || updatedPr.getExDate() == null
                || updatedPr.getStatus() == null) {
            JOptionPane.showMessageDialog(null, "All fields of the updated PR must be filled!");
            return;
        }

        String filePath = "src/PurchaseRequisition/PR.txt";
        String tempFile = "src/PurchaseRequisition/PR_temp.txt"; // Use a distinct temp file name for updates
        File inputFile = new File(filePath);
        File outputFile = new File(tempFile);

        boolean recordFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)); PrintWriter pw = new PrintWriter(writer)) {

            String currentLine;
            // Read and process lines
            while ((currentLine = reader.readLine()) != null) {
                // Use regex to properly split by commas outside of curly braces
                String[] parts = currentLine.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (parts.length > 0 && parts[0].trim().equals(updatedPr.getPRID())) {
                    // This is the record to update
                    // Format the item codes and quantities back into {a,b} format for writing to file
                    String formattedItemCodes = "{" + updatedPr.getItemCode().replace("\n", ", ") + "}";
                    String formattedQuantities = "{" + updatedPr.getQuantity().replace("\n", ", ") + "}";

                    String updatedRecord = updatedPr.getPRID() + ", " + updatedPr.getDate() + ", "
                            + updatedPr.getPrCreatedByName() + ", " + updatedPr.getPrCreatedByID() + ", "
                            + formattedItemCodes + ", " + formattedQuantities + ", "
                            + updatedPr.getExDate() + ", " + updatedPr.getStatus();
                    pw.println(updatedRecord);
                    recordFound = true;
                } else {
                    // Write all other lines as they are
                    pw.println(currentLine);
                }
            }
            pw.flush();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error during update operation: " + e.getMessage());
            // If an error occurs, try to delete the temporary file to clean up
            outputFile.delete();
            return;
        }

        if (!recordFound) {
            JOptionPane.showMessageDialog(null, "Record with PR ID " + updatedPr.getPRID() + " not found for update!");
            outputFile.delete(); // Delete temp file as no update happened
            return;
        }

        // Replace the original file with the updated temporary file
        if (inputFile.delete()) {
            if (!outputFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(null, "Error renaming temporary file to original file after update.");
            } else {
                // Update the in-memory list as well
                for (int i = 0; i < prlist.size(); i++) {
                    if (prlist.get(i).getPRID().equals(updatedPr.getPRID())) {
                        prlist.set(i, updatedPr); // Replace the old object with the updated one
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, "Data Updated Successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error deleting original file during update. Check file permissions.");
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
}
