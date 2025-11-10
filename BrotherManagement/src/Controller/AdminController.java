/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import brothermanagement.Admistration;
import Model.Product;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

/**
 *
 * @author Dell Precision T5810
 */
public class AdminController {
    private JComboBox<String> productTypeList;
    private JComboBox<String> statusTypeList;
    private JTable table;
    
    
    public AdminController(){};
    
    public AdminController(Admistration adminView) {
        this.productTypeList = adminView.getProductTypeList();
        this.statusTypeList = adminView.getStatusTypeList();
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
    
    public Product addProduct(Product pro){
        if (pro.getId() <= 0 ||
        pro.getName() == null || pro.getName().trim().isEmpty() ||
        pro.getType() == null || pro.getType().trim().isEmpty() ||
        pro.getStatus() == null || pro.getStatus().trim().isEmpty()) {

        JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
        return null;
    }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{
        pro.getId(),             
        pro.getName(),
        pro.getType(),
        String.format("%,.0f VND", pro.getPrice()),
        pro.getStock(),
        pro.getStatus()
    });

    return pro;
    }
}
