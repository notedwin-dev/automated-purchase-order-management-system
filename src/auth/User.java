/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import roles.Role;

/**
 *
 * @author USER
 * @author notedwin-dev
 */
public class User {

    private int did;
    private String id, username, password, department, role;
    private Role userRole; // Role object from the roles package

    public User(String id, String username, String password, String department, String role) {
    }

    public void setDataID(int did) {
        this.did = did;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.userRole = roles.RoleFactory.createRole(role);
    }

    /**
     * Gets the Role object for this user
     *
     * @return The Role object
     */
    public Role getUserRole() {
        if (userRole == null && role != null) {
            userRole = roles.RoleFactory.createRole(role);
        }
        return userRole;
    }

    /**
     * Checks if the user has access to a specific feature
     *
     * @param featureName The name of the feature
     * @return True if the user has access, false otherwise
     */
    public boolean hasAccess(String featureName) {
        return getUserRole() != null && getUserRole().hasAccess(featureName);
    }

    /**
     * Checks if the user can modify a specific feature
     *
     * @param featureName The name of the feature
     * @return True if the user can modify, false otherwise
     */
    public boolean canModify(String featureName) {
        return getUserRole() != null && getUserRole().canModify(featureName);
    }

    /**
     * Get the proper path to the user login file
     *
     * @return The path to the login file
     */
    private String getLoginFilePath() {
        return "src/auth/txtlogin.txt";
    }

    /**
     * Get the proper path to the temporary file used during updates
     *
     * @return The path to the temporary file
     */
    private String getTempFilePath() {
        return "src/auth/temp.txt";
    }

    Object getPRID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
