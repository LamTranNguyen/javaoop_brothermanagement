/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.BillItem;
import database.brotherconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ThinkBook
 */
public class BillItemDAO {
    public List<BillItem> getBillItemsByBillId(int billId) {
        List<BillItem> list = new ArrayList<>();

        try {
            Connection con = brotherconnection.getConnection();
            String sql = "SELECT * FROM bill_items WHERE bill_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BillItem item = new BillItem(
                    rs.getInt("bill_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getInt("subtotal")
                );
                list.add(item);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
