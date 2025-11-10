/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import database.brotherconnection;
/**
 *
 * @author Dell Precision T5810
 */
public class ProductController {
    public List<Product> getProductsByCategory(String category) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category = ?";

        try (Connection con = brotherconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("image"),
                    rs.getString("status"),
                    rs.getInt("Stock")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
