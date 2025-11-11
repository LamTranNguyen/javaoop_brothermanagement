/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import brothermanagement.Admistration;
import Model.Product;
import database.brotherconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.*;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Dell Precision T5810
 */
public class AdminController {
    private JComboBox<String> productTypeList;
    private JComboBox<String> statusTypeList;
    private JTable table;
    private JLabel inventoryImageLabel;
    private String imagePath;
    private Admistration view;
    
    public AdminController(){};
    
    public AdminController(Admistration adminView) {
        this.productTypeList = adminView.getProductTypeList();
        this.statusTypeList = adminView.getStatusTypeList();
        this.inventoryImageLabel = adminView.getInventoryImageLabel();
        this.table = adminView.getTable();
    }
    
    public AdminController(JTable table) {
        this.table = table;
    }

    public void loadProductTypes() {
        String[] types = {"Meal", "Drink", "Combo", "Dessert"};
        productTypeList.removeAllItems();
        for (String type : types) {
            productTypeList.addItem(type);
        }
    }
    
    public void loadStatusTypes() {
        String[] types = {"Available", "Unavailable"};
        statusTypeList.removeAllItems();
        for (String type : types) {
            statusTypeList.addItem(type);
        }
    }
    
    public void loadProductsFromDatabase() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String query = "SELECT * FROM product";
        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            var rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("pro_id"),
                    rs.getString("pro_name"),
                    rs.getString("pro_type"),
                    String.format("%,.0f VND", rs.getDouble("pro_price")),
                    rs.getInt("pro_stock"),
                    rs.getString("pro_status"),
                    rs.getString("pro_image")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading products: " + e.getMessage());
        }
        }
    
    public Product addProduct(Product pro){
        if (pro == null) {
        JOptionPane.showMessageDialog(null, "Invalid product data!", "Error", JOptionPane.ERROR_MESSAGE);
        return null;
        }

        if (pro.getName() == null || pro.getName().trim().isEmpty() ||
            pro.getType() == null || pro.getType().trim().isEmpty() ||
            pro.getStatus() == null || pro.getStatus().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }


        String query = "INSERT INTO product (pro_id, pro_name, pro_type, pro_price, pro_image, pro_stock, pro_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, pro.getId());
            ps.setString(2, pro.getName());
            ps.setString(3, pro.getType());
            ps.setDouble(4, pro.getPrice());
            ps.setString(5, pro.getImage());
            ps.setInt(6, pro.getStock());
            ps.setString(7, pro.getStatus());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Product added successfully!");
            loadProductsFromDatabase();
            return pro;

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Product ID already exists!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return null;
    }
    
    public Product updateProduct(Product pro){
        if (pro == null) {
        JOptionPane.showMessageDialog(null, "Invalid product data!", "Error", JOptionPane.ERROR_MESSAGE);
        return null;
        }

        if (pro.getName() == null || pro.getName().trim().isEmpty() ||
            pro.getType() == null || pro.getType().trim().isEmpty() ||
            pro.getStatus() == null || pro.getStatus().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }


        String query = "UPDATE product SET pro_name=?, pro_type=?, pro_price=?, pro_image=?, pro_stock=?, pro_status=? WHERE pro_id=?";
        try (Connection conn = brotherconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, pro.getName());
            ps.setString(2, pro.getType());
            ps.setDouble(3, pro.getPrice());
            ps.setString(4, pro.getImage());
            ps.setInt(5, pro.getStock());
            ps.setString(6, pro.getStatus());
            ps.setInt(7, pro.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Product updated successfully!");
                loadProductsFromDatabase();
            } else {
                JOptionPane.showMessageDialog(null, "No product found with ID " + pro.getId());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return pro;
    } 
    
    public void deleteProduct(JTable table){
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a product to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "DELETE FROM product WHERE pro_id = ?";
        int confirm = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to delete this product?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int productId = (int) model.getValueAt(selectedRow, 0); // Assuming first column is ID

            boolean deleted = deleteProductFromDatabase(productId);
            
            if (deleted) {
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(null, "Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete product from database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean deleteProductFromDatabase(int productId) {
        String query = "DELETE FROM product WHERE pro_id = ?";

        try (java.sql.Connection conn = brotherconnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            int rows = stmt.executeUpdate();
            return rows > 0; // true if delete worked

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }
    
    public void importImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Image select");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "png", "jpg", "jpeg"));

        int width = inventoryImageLabel.getWidth();
        int height = inventoryImageLabel.getHeight();

        if (width == 0 || height == 0) {
            width = 190;
            height = 170;
        }

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();

            ImageIcon icon = new ImageIcon(new ImageIcon(imagePath)
                    .getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));

            if (inventoryImageLabel != null) {
                inventoryImageLabel.setIcon(icon);
                inventoryImageLabel.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Label not found!");
            }
        }
    }

    public String getImagePath() {
        return imagePath;
    }
}
