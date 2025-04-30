/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

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
public class User {
    private int did;
    private String id, username, password, department;
    private String role;
    
    public User(){
    }
    
    public void setDataID(int did) {
        this.did = did;
    }
    
    public String getID(){
        return id;
    }
    
    public void setID(String id){
        this.id = id;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    
    public String getDepartment(){
        return department;
    }
    
    public void setDepartment(String department){
        this.department = department;
    }
    
    public String getRole(){
        return role;
    }
    
    public void setRole(String role){
        this.role = role;
    }
    
    
    
    public void add(){       
        if (this.id == null || this.username == null || this.password == null || this.department == null || this.role == null) {
    JOptionPane.showMessageDialog(null, "All fields must be filled!");
    return;
}

// ID must be numeric
if (!this.id.matches("\\d+")) {
    JOptionPane.showMessageDialog(null, "ID must be numeric!");
    return;
}

// Check if ID is already used
try (BufferedReader reader = new BufferedReader(new FileReader("txtlogin.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length > 0 && parts[0].equals(this.id)) {
            JOptionPane.showMessageDialog(null, "This ID already exists!");
            throw new Error();
        }
    }
} catch (IOException e) {
    JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
    return;
}

try {
    FileWriter writer = new FileWriter("txtlogin.txt", true);
    writer.write(this.id + "," + this.username + "," + this.password + "," + this.department + "," + this.role);
    writer.write(System.getProperty("line.separator"));
    writer.close();
    JOptionPane.showMessageDialog(null, "Data Added");
} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
}
 
    }
    
    public void delete(){
        if (did == 0) {
        JOptionPane.showMessageDialog(null, "Please select a row to delete!");
        return;
    }
    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
        try{
            removeRecord("txtlogin.txt",did);
            JOptionPane.showMessageDialog(null,"Successfully Deleted");
            did = 0; // Reset did after successful deletion
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error during delete operation: " + e.getMessage());
            did = 0; // Reset did even if there's an error
        }
    } else {
        JOptionPane.showMessageDialog(null, "Delete operation cancelled");
        did = 0; // Reset did after cancellation
    }
        
    }
    
    public void update() {
    if (this.id == null || this.username == null || this.password == null || this.department == null || this.role == null) {
        JOptionPane.showMessageDialog(null, "All fields must be filled!");
        return;
    }

    try {
        // Create temporary file
        File inputFile = new File("txtlogin.txt");
        File tempFile = new File("temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;
        boolean found = false;

        // Read through the file line by line
        while ((currentLine = reader.readLine()) != null) {
            String[] parts = currentLine.split(",");
            if (parts.length >= 5 && parts[0].equals(this.id)) {
                // Found the record to update - write the new data
                writer.write(this.id + "," + this.username + "," + this.password + "," + 
                            this.department + "," + this.role);
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
            JOptionPane.showMessageDialog(null, "Record with ID " + this.id + " not found!");
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
        
        int line=0;
        String currentLine;
        
        try {
            FileWriter fw=new FileWriter(tempFile,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            
            FileReader fr = new FileReader(fileFath);
            BufferedReader br = new BufferedReader(fr);
            
            while((currentLine = br.readLine()) !=null)
            {
            
                line++;
                if(deleteLine !=line)
                {
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
            File dump= new File(fileFath);
            newFile.renameTo(dump);
            
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
}

