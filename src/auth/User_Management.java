/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import TextFile_Handler.TextFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class User_Management {

    private final List<User> userlist;

    public User_Management() {
        this.userlist = new ArrayList<>();
    }

    public void getUserfromtxtfile() {
        List<User> userList = new ArrayList<>();
        List<String> userLines = TextFile.readFile("src/auth/txtlogin.txt");

        for (String line : userLines) {
            String[] parts = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)"); // handle {a,b} fields correctly

            if (parts.length == 5) {
                String id = parts[0].trim();
                String username = parts[1].trim();
                String password = parts[2].trim();
                String department = parts[3].trim();
                String role = parts[4].trim();

                User user = new User(id, username, password, department, role);
                userlist.add(user);
            }
        }
    }

    public User findid(String id) {
        for (User user : userlist) {
            if (user.getID().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUserlist() {
        return userlist;
    }

    public void add(User newUser) {
        if (newUser == null
                || newUser.getID() == null || newUser.getID().trim().isEmpty()
                || newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()
                || newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()
                || newUser.getDepartment() == null || newUser.getDepartment().trim().isEmpty()
                || newUser.getRole() == null || newUser.getRole().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return;
        }

        // Validate ID format: U followed by 4 digits
        if (!newUser.getID().matches("U\\d{4}")) {
            JOptionPane.showMessageDialog(null, "ID must be in the format U0001!");
            return;
        }

        // Check for duplicate ID
        for (User existingUser : userlist) {
            if (existingUser.getID().equals(newUser.getID())) {
                JOptionPane.showMessageDialog(null, "This ID already exists!");
                return;
            }
        }

        // Write to file
        String record = newUser.getID() + "," + newUser.getUsername() + "," + newUser.getPassword() + ","
                + newUser.getDepartment() + "," + newUser.getRole();

        try {
            TextFile.appendTo("src/auth/txtlogin.txt", record);
            userlist.add(newUser); // Update in-memory list
            JOptionPane.showMessageDialog(null, "User added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing to file: " + e.getMessage());
        }
    }

    public void delete(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please provide a valid user ID to delete!");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Delete operation cancelled");
            return;
        }

        File inputFile = new File("src/auth/txtlogin.txt");
        File tempFile = new File("src/auth/txtlogin_temp.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts.length == 5 && parts[0].equals(userId)) {
                    found = true;
                    continue; // Skip writing this line to delete it
                }
                writer.write(currentLine);
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage());
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "User ID " + userId + " not found!");
            tempFile.delete();
            return;
        }

        // Replace original file with updated one
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(null, "Error replacing original file");
            return;
        }

        // Remove from in-memory list
        userlist.removeIf(user -> user.getID().equals(userId));

        JOptionPane.showMessageDialog(null, "User deleted successfully!");
    }

    public void update(User updatedUser) {
        if (updatedUser == null
                || updatedUser.getID() == null || updatedUser.getID().trim().isEmpty()
                || updatedUser.getUsername() == null || updatedUser.getUsername().trim().isEmpty()
                || updatedUser.getPassword() == null || updatedUser.getPassword().trim().isEmpty()
                || updatedUser.getDepartment() == null || updatedUser.getDepartment().trim().isEmpty()
                || updatedUser.getRole() == null || updatedUser.getRole().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled!");
            return;
        }

        File inputFile = new File("src/auth/txtlogin.txt");
        File tempFile = new File("src/auth/txtlogin_temp.txt");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts.length == 5 && parts[0].equals(updatedUser.getID())) {
                    // Replace the matched line with the updated user's details
                    String updatedLine = updatedUser.getID() + "," + updatedUser.getUsername() + ","
                            + updatedUser.getPassword() + "," + updatedUser.getDepartment() + "," + updatedUser.getRole();
                    writer.write(updatedLine);
                    found = true;
                } else {
                    // Write unchanged line
                    writer.write(currentLine);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error during update: " + e.getMessage());
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "User ID " + updatedUser.getID() + " not found!");
            tempFile.delete();
            return;
        }

        // Replace original file with updated one
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(null, "Error replacing original file");
            return;
        }

        // Update in-memory list
        for (int i = 0; i < userlist.size(); i++) {
            if (userlist.get(i).getID().equals(updatedUser.getID())) {
                userlist.set(i, updatedUser);
                break;
            }
        }

        JOptionPane.showMessageDialog(null, "User updated successfully!");
    }

    public String generateUserID() {
        List<String> userLines = TextFile.readFile("src/auth/txtlogin.txt");
        int maxId = 0;

        for (String line : userLines) {
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].startsWith("U")) {
                try {
                    int idNumber = Integer.parseInt(parts[0].substring(1));
                    maxId = Math.max(maxId, idNumber);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }

        return String.format("U%04d", maxId + 1);
    }
}
