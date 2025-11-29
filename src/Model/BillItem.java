/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ThinkBook
 */
public class BillItem {
    private int billId;
    private int productId;
    private String productName;
    private int qty;
    private double subtotal;
    
    public BillItem(int billId, int productId, String productName, int qty, double subtotal){
        this.billId = billId;
        this.productId = productId;
        this.productName = productName;
        this.qty = qty;
        this.subtotal = subtotal;
    }
    
    public int getBillId(){return billId;}
    public int getProductId(){return productId;}
    public String getProductName(){return productName;}
    public int getQuantity(){return qty;}
    public double getSubTotal(){return subtotal;}
}
