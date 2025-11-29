/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Bill;
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
public class BillDAO {
     public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();

        try {
            Connection con = brotherconnection.getConnection();
            String sql = "SELECT * FROM bill";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill(
                    rs.getInt("bill_id"),
                    rs.getTimestamp("bill_date"),
                    rs.getInt("total_amount")
                );
                list.add(bill);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
