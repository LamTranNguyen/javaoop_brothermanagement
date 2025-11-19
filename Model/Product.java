package Model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dell Precision T5810
 */
public class Product {
    private int pro_id;
    private String pro_name;
    private String category;
    private double price;
    private String image;
    private String status;
    private int stock;
    
    public Product(){};
    
    public Product(int id, String name, String type, double price, String status, 
            String image, int stock){
        this.pro_id = id;
        this.pro_name = name;
        this.category = type;
        this.price = price;
        this.image = image;
        this.status = status;
        this.stock = stock;
    }
    
    public void setImage(String img){
        this.image = img;
    }
    
    public int getId() { return pro_id; }
    public String getName() { return pro_name; }
    public String getType() { return category; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public int getStock() { return stock; }
    public String getImage() { return image; }
}
