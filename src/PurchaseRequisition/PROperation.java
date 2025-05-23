/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseRequisition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class PROperation {

    private int did;
    private String prid, date, prCreatedByName, prCreatedByID, itemcode, quantity, exdate, status;

    public PROperation(String prid, String date, String prCreatedByName, String prCreatedByID, String itemcode, String quantity, String exdate, String status) {
        this.prid = prid;
        this.prCreatedByName = prCreatedByName;
        this.prCreatedByID = prCreatedByID;
        this.itemcode = itemcode;
        this.quantity = quantity;
        this.exdate = exdate;
        this.status = status;
    }

    public void setDataID(int did) {
        this.did = did;
    }

    public String getPRID() {
        return prid;
    }

    public void setPRID(String prid) {
        this.prid = prid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the prCreatedByName
     */
    public String getPrCreatedByName() {
        return prCreatedByName;
    }

    /**
     * @param prCreatedByName the prCreatedByName to set
     */
    public void setPrCreatedByName(String prCreatedByName) {
        this.prCreatedByName = prCreatedByName;
    }

    /**
     * @return the prCreatedByID
     */
    public String getPrCreatedByID() {
        return prCreatedByID;
    }

    /**
     * @param prCreatedByID the prCreatedByID to set
     */
    public void setPrCreatedByID(String prCreatedByID) {
        this.prCreatedByID = prCreatedByID;
    }
    

    public String getItemCode() {
        return itemcode;
    }

    public void setItemCode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExDate() {
        return exdate;
    }

    public void setExDate(String exdate) {
        this.exdate = exdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    prCreatedByName, prCreatedByID,
    
    public void add() {
        if (this.prid == null || this.date == null || this.prCreatedByName == null || this.prCreatedByID == null
                || this.itemcode == null || this.quantity == null || this.exdate == null || this.status == null) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return;
        }

        try {
            FileWriter writer = new FileWriter("src/PurchaseRequisition/PR.txt", true);
            writer.write(this.prid + ", " + this.date + ", " + this.prCreatedByName + ", " + this.prCreatedByID + ", "
                    + this.itemcode + ", " + this.quantity + ", " + this.exdate + ", " + this.status);
            writer.write(System.getProperty("line.separator"));
            writer.close();
            JOptionPane.showMessageDialog(null, "Data Added Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }

    public void delete() {
        if (did == 0) {
            JOptionPane.showMessageDialog(null, "Please select a row to delete!");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                removeRecord("src/PurchaseRequisition/PR.txt", did);
                JOptionPane.showMessageDialog(null, "Successfully Deleted");
                did = 0; // Reset did after successful deletion
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error during delete operation: " + e.getMessage());
                did = 0; // Reset did even if there's an error
            }
        } else {
            JOptionPane.showMessageDialog(null, "Delete operation cancelled");
            did = 0; // Reset did after cancellation
        }
    }

    public void update() {
        if (this.prid == null || this.date == null || this.prCreatedByName == null || this.prCreatedByID == null
                || this.itemcode == null || this.quantity == null || this.exdate == null || this.status == null) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return;
        }

        try {
            File inputFile = new File("src/PurchaseRequisition/PR.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            String headerLine = reader.readLine(); // Read the header line
            writer.write(headerLine); // Write the header line to the temp file
            writer.newLine();
            boolean found = false;

            while ((currentLine = reader.readLine()) != null) {
                // Use regex to properly split by commas outside of curly braces
                String[] parts = currentLine.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (parts.length >= 1 && parts[0].trim().equals(this.prid)) {
                    // Write updated data
                    writer.write(this.prid + ", " + this.date + ", " + this.prCreatedByName + ", " + this.prCreatedByID + ", "
                            + this.itemcode + ", " + this.quantity + ", " + this.exdate + ", " + this.status);
                    found = true;
                } else {
                    writer.write(currentLine);
                }
                writer.newLine();
            }

            writer.close();
            reader.close();

            if (!found) {
                JOptionPane.showMessageDialog(null, "Record with PRID " + this.prid + " not found!");
                tempFile.delete();
                return;
            }

            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    JOptionPane.showMessageDialog(null, "Error renaming file");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error deleting original file");
            }

            JOptionPane.showMessageDialog(null, "Data Updated");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }

    }

    public static void removeRecord(String fileFath, int deleteLine) {

        String tempFile = "temp.txt";
        File oldFile = new File(fileFath);
        File newFile = new File(tempFile);

        int line = 0;
        String currentLine;

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr = new FileReader(fileFath);
            BufferedReader br = new BufferedReader(fr);

            while ((currentLine = br.readLine()) != null) {

                line++;
                if (deleteLine != line) {
                    pw.println(currentLine);
                }

            }

            pw.flush();
            pw.close();
            fr.close();
            br.close();
            bw.close();
            fw.close();

            oldFile.delete();
            File dump = new File(fileFath);
            newFile.renameTo(dump);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

  
    
}
