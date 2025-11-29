/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import database.brotherconnection;
import java.sql.*;
import javax.swing.JOptionPane;
import Model.User;


/**
 *
 * @author ThinkBook
 */
public class UserController {
    private boolean validateForm(String email, String username, char[] password, boolean isSignup) {
        if (email.isEmpty() || password == null || (isSignup && username.isEmpty())) {
            JOptionPane.showMessageDialog(null, "Please fill all information!");
            return false;
        }

        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            JOptionPane.showMessageDialog(null, "Invalid email!");
            return false;
        }

        if (isSignup && password.length < 6) {
            JOptionPane.showMessageDialog(null, "Password must be at least 6 characters!");
            return false;
        }

        return true;
    }

    public boolean signup(User user) {
        if (!validateForm(user.getEmail(), user.getUsername(), user.getPassword(), true)) {
            return false;
        }

        String query = "INSERT INTO login (email, username, password) VALUES (?, ?, ?)";

        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, new String(user.getPassword()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Sign up successful!");
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Email already exist!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return false;
    }

    public User login(User user) {
        if (!validateForm(user.getEmail(), "", user.getPassword(), false)) {
        return null;
        }

        String query = "SELECT * FROM login WHERE email = ? AND password = ?";

        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, new String(user.getPassword()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Login successful!");

                User loggedUser = new User();
                loggedUser.setEmail(rs.getString("email"));
                loggedUser.setUsername(rs.getString("username"));

                return loggedUser;
            } else {
                JOptionPane.showMessageDialog(null, "Email or password wrong");
                return null;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return null;
        }
    }
    
    public User checkStaff(User user) {
        if (!validateForm(user.getEmail(), "", user.getPassword(), false)) {
        return null;
        }

        String query = "SELECT * FROM login WHERE email = ? AND password = ?";

        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, new String(user.getPassword()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User loggedUser = new User();
                loggedUser.setEmail(rs.getString("email"));
                loggedUser.setUsername(rs.getString("username"));

                return loggedUser;
            } else {
                return null;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return null;
        }
    }
}