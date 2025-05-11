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
    private String no, prid, date, smname, smid, itemcode, quantity, status;

    public PROperation() {

    }

    public void setDataID(int did) {
        this.did = did;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getSMName() {
        return smname;
    }

    public void setSMName(String smname) {
        this.smname = smname;
    }

    public String getSMID() {
        return smid;
    }

    public void setSMID(String smid) {
        this.smid = smid;
    }
    

    public String getItemCode() {
        return itemcode;
    }

    public void setItemCode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getQuntity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String generateNextNo() {
        int nextNo = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseRequisition/PR.txt"))) {
            String lastLine = null;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                lastLine = currentLine;
            }
            if (lastLine != null) {
                String[] parts = lastLine.split(",");
                if (parts.length > 0 && parts[0].matches("\\d+")) {
                    nextNo = Integer.parseInt(parts[0]) + 1;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file for generating No.: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error parsing No. from file: " + e.getMessage());
        }
        return String.format("%04d", nextNo); // Format to 4 digits with leading zeros
    }
    
    public void add() {
        this.no = generateNextNo(); // Generate the "No." automatically

        if (this.prid == null || this.date == null || this.smname == null || this.smid == null
                || this.itemcode == null  || this.quantity == null || this.status == null) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return;
        }

        try {
            FileWriter writer = new FileWriter("src/PurchaseRequisition/PR.txt", true);
            writer.write(this.no + "," + this.prid + "," + this.date + "," + this.smname + "," + this.smid + ","
                    + this.itemcode  + "," + this.quantity + "," + this.status);
            writer.write(System.getProperty("line.separator"));
            writer.close();
            JOptionPane.showMessageDialog(null, "Data Added with No.: " + this.no);
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
        if (this.no == null || this.prid == null || this.date == null || this.smname == null || this.smid == null 
                || this.itemcode == null  || this.quantity == null || this.status == null) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return;
        }

        try {
            // Create temporary file
            File inputFile = new File("src/PurchaseRequisition/PR.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            boolean found = false;

            // Read through the file line by line
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts.length >= 5 && parts[0].equals(this.no)) {
                    // Found the record to update - write the new data
                    writer.write(this.no + "," + this.prid + "," + this.date + "," + this.smname + "," + this.smid + "," 
                    + this.itemcode  + "," + this.quantity + "," + this.status);
                    found = true;
                } else {
                    // Write the existing record as is
                    writer.write(currentLine);
                }
                writer.newLine();
            }

            writer.close();
            reader.close();

            if (!found) {
                JOptionPane.showMessageDialog(null, "Record with No. " + this.no + " not found!");
                tempFile.delete();
                return;
            }

            // Replace the original file with the updated one
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
