/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author ThinkBook
 */
public class Bill {
    private int billId;
    private Timestamp billDate;
    private double totalAmount;
    
    public Bill(int billId, Timestamp billDate, double totalAmount){
        this.billId = billId;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
    }
    
    public int getId(){return billId;}
    public Timestamp getDate(){return billDate;}   
    public double getTotal(){return totalAmount;}
}
