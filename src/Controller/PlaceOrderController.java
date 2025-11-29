/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;


import java.sql.ResultSet;
import Model.Product;
import UI.BillFrame;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import UI.PlaceOrderPanel;
import brothermanagement.ProductItemPanel;
import database.brotherconnection;
import java.awt.Color;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ThinkBook
 */
public class PlaceOrderController {
    private PlaceOrderPanel take_btn_panel;
    private JPanel productContainer;
    private JScrollPane scrollPane;
    private JPanel placeorder_pane;
    private JButton all_btn;
    private AdminController adminController;
    
    private int batchSize = 12;
    private int loadedCount = 0;
    private ArrayList<Product> list = new ArrayList<>();

    private JTable place_table; 
    private JTextField Total; 
    private PlaceOrderPanel orderPanel; 
    
    public PlaceOrderController(PlaceOrderPanel panel) {
        this.take_btn_panel = panel;
        this.productContainer = panel.getProductContainer();
        this.productContainer = panel.getProductContainer();
        this.scrollPane = panel.getScrollPane();
        this.placeorder_pane = panel.getPlaceorderpanel();
        this.all_btn = panel.getAllbutton();
        
        this.orderPanel = panel;
        this.place_table = orderPanel.getPlaceTable();
        this.Total = orderPanel.getTotalTextField();
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Number) {
                    value = currencyFormat.format(value);
                }
                super.setValue(value);
            }
        };
    
   
        currencyRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

    
        try {
            place_table.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer);
            place_table.getColumnModel().getColumn(5).setCellRenderer(currencyRenderer);
        } catch (Exception e) {
            System.out.println("Error");
        }

        setupDeleteButton();
        setupResetButton();
    }
    
    public void setAdminController(AdminController adminCtrl) {
        this.adminController = adminCtrl;
    }
    
    public void click_default(){
        List<Product> prods = getProductsByCategory("All");
        highlightButtonDefault();
        showProducts(prods);
    }
    
    public List<JButton> getButtonsInPanel(JPanel panel) {
        List<JButton> list = new ArrayList<>();
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                list.add((JButton) comp);
            }
        }
        return list;
    }
    
    
    
    public void registerButtonEvents() {
        List<JButton> btns = getButtonsInPanel(take_btn_panel.getButtonPanel());

        for (JButton b : btns) {
            b.addActionListener(e -> {
                highlightSelectedButton(b, btns);
                List<Product> prods = getProductsByCategory(b.getText());
                showProducts(prods);
            });
        }
    }

    
    private void highlightButtonDefault(){
        List<JButton> btns = getButtonsInPanel(take_btn_panel.getButtonPanel());
        for (JButton b : btns) {
            if (b == all_btn) {
                b.setBackground(new Color(204, 0, 0));
                b.setForeground(new Color(204,204,204));
            } else {
                b.setBackground(new Color(255, 51, 51));
                b.setForeground(Color.WHITE);
            }
        }
    }
    
    private void highlightSelectedButton(JButton selected, List<JButton> all) {
        
        for (JButton b : all) {
            if (b == selected) {
                b.setBackground(new Color(204, 0, 0));
                b.setForeground(new Color(204,204,204));
            } else {
                b.setBackground(new Color(255, 51, 51));
                b.setForeground(new Color(255,255,255));
            }
        }
    }
    
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();

        String sql = category.equalsIgnoreCase("All")
                ? "SELECT * FROM product"
                : "SELECT * FROM product WHERE pro_type = ?";

        try (var con = brotherconnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            if (!category.equalsIgnoreCase("All")) {
                ps.setString(1, category);
            }

            var rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("pro_id"),
                        rs.getString("pro_name"),
                        rs.getString("pro_type"),
                        rs.getDouble("pro_price"),
                        rs.getString("pro_status"),
                        rs.getString("pro_image"),
                        rs.getInt("pro_stock")
                );
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

        return products;
    }
        
    public void showProducts(){
        productContainer.removeAll();    
        loadedCount = 0;                
        String sql = "SELECT * FROM product";
        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("pro_id"),
                    rs.getString("pro_name"),
                    rs.getString("pro_type"),
                    rs.getDouble("pro_price"),
                    rs.getString("pro_status"),
                    rs.getString("pro_image"),
                    rs.getInt("pro_stock")
                );

                ProductItemPanel productPanel = new ProductItemPanel(product);

                productPanel.setAdminController(this); 

                productContainer.add(productPanel.getitems_panel());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        productContainer.revalidate();
        productContainer.repaint();
    }
    
    public void showProducts(List<Product> products) {
        productContainer.removeAll();

        for (Product p : products) {
            ProductItemPanel item = new ProductItemPanel(p);
            item.setAdminController(this);
            productContainer.add(item.getitems_panel());
        }

        productContainer.revalidate();
        productContainer.repaint();
    }
    
    public void loadMoreProducts(JPanel productContainer) {
        int end = Math.min(loadedCount + batchSize, list.size());

        for (int i = loadedCount; i < end; i++) {
            ProductItemPanel item = new ProductItemPanel(list.get(i));
            item.setAdminController(this);
            productContainer.add(item);
        }

        loadedCount = end;
        productContainer.revalidate();
        productContainer.repaint();
    }

    
    public void enableAutoLoad() {
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            int extent = bar.getModel().getExtent();
            int value = bar.getValue();
            int max = bar.getMaximum();

            if (value + extent >= max - 50) {
                loadMoreProducts(productContainer);
            }
        });
    }
    
    public void addProductToTable(int productId, int quantity) {
        String sql = "SELECT pro_name, pro_type, pro_price, pro_stock FROM product WHERE pro_id = ?";
        System.out.print("add product");
        try (Connection con = brotherconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("pro_name");
                String type = rs.getString("pro_type");
                double price = rs.getDouble("pro_price");

                int stock = rs.getInt("pro_stock");

                if (quantity > stock ) {
                    JOptionPane.showMessageDialog(null, 
                        "Not enough stock for product: " + name + "\nAvailable: " + stock, 
                        "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                DefaultTableModel model = (DefaultTableModel) place_table.getModel();
                boolean found = false;

                for (int i = 0; i < model.getRowCount(); i++) {
                    int currentId = Integer.parseInt(model.getValueAt(i, 0).toString());
                    
                    if (currentId == productId) {
                        int currentQty = Integer.parseInt(model.getValueAt(i, 3).toString());
                        int newQty = currentQty + quantity;

                        if (newQty > stock) {
                            JOptionPane.showMessageDialog(null, 
                             "Some products is out of stock",
                             "Out of stock (OOS)",
                                 JOptionPane.WARNING_MESSAGE);
                            return; 
                        }
                    }
                };
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    int currentId = Integer.parseInt(model.getValueAt(i, 0).toString());
                    if (currentId == productId) {
                        int currentQty = Integer.parseInt(model.getValueAt(i, 3).toString());
                        int newQty = currentQty + quantity;
                        double newSubtotal = newQty * price;

                        model.setValueAt(newQty, i, 3);       // Update quantity
                        model.setValueAt(newSubtotal, i, 5);  // Update subtotal
                        found = true;
                        break;
                    }
                }

                // ➕ If not found, add as new row
                if (!found) {
                    double subtotal = price * quantity;
                    model.addRow(new Object[]{
                        productId,
                        name,
                        type,
                        quantity,
                        price,
                        subtotal
                    });
                }

                updateTotal(); // Update total after adding/updating row
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding product: " + e.getMessage());
        }
    }
    
    private void updateTotal() {
        DefaultTableModel model = (DefaultTableModel) place_table.getModel();
        double total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object obj = model.getValueAt(i, 5); // subtotal
            double subtotal = obj instanceof Number ? ((Number) obj).doubleValue() : 0;
            total += subtotal;
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        Total.setText(currencyFormat.format(total));
    }

    private void setupDeleteButton() {
        if (orderPanel != null && orderPanel.getDeleteButton() != null) {
            orderPanel.getDeleteButton().addActionListener(e -> deleteSelectedRow());
        }
    }
    
        // changes: Setup delete button listener
        private void setupResetButton() {
        if (orderPanel != null && orderPanel.getResetButton() != null) {
            orderPanel.getResetButton().addActionListener(e -> ResetTable());
        }
    }

    //changes: delete row
    private void deleteSelectedRow() {
        JTable placeTable = orderPanel.getPlaceTable();
        DefaultTableModel model = (DefaultTableModel) placeTable.getModel();
        int selectedRow = placeTable.getSelectedRow();

        if (selectedRow >= 0) {
            model.removeRow(selectedRow);   // Remove row
            updateTotal();                  // Update total after deletion
        }     
    }
    //changes: reset table
    private void ResetTable(){
        JTable placeTable = orderPanel.getPlaceTable();
        DefaultTableModel model = (DefaultTableModel) placeTable.getModel();
        
        model.setRowCount(0); // Clear the table
        updateTotal();  
    }

    //changes: order and print button
    public void handleOrderAndPrint() {
        DefaultTableModel model = (DefaultTableModel) place_table.getModel();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No items in the order!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("************ Brother Chicken ***********\n");
        receipt.append("----------------------------------------\n");
        receipt.append(String.format("%-20s %-5s %s\n", "Item", "Quantity", "Price"));
        receipt.append("----------------------------------------\n");

        double total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String item = model.getValueAt(i, 1).toString();  // Name
            int qty = (int) model.getValueAt(i, 3);           // Quantity
            double subtotal = (double) model.getValueAt(i, 5); // Subtotal as double

            receipt.append(String.format("%-20s %-5d %,.0f\n", item, qty, subtotal));
            total += subtotal;
        }

        receipt.append("----------------------------------------\n");
        receipt.append(String.format("TOTAL: %,.0f VND\n", total));
        receipt.append("----------------------------------------\n");
        receipt.append("Thanks for choosing us!\n");

        new BillFrame(receipt.toString(), orderPanel) {{
            setLocationRelativeTo(null);
            setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        }};
    }

    //changes: update items in bill, bill_items
    public void saveBillToDatabase(DefaultTableModel model, double total) {
        try (Connection conn = brotherconnection.getConnection()) {
            conn.setAutoCommit(false);

            // insert into bill table
            String sqlBill = "INSERT INTO bill (bill_date, total_amount) VALUES (NOW(), ?)";
            PreparedStatement psBill = conn.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS);
            psBill.setDouble(1, total);
            psBill.executeUpdate();

            ResultSet rs = psBill.getGeneratedKeys();
            int billId = -1;
            if (rs.next()) {
                billId = rs.getInt(1);
            }

            // insert into bill_items
            String sqlItem = "INSERT INTO bill_items (bill_id, product_id, product_name, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psItem = conn.prepareStatement(sqlItem);

            // update product stock
            String sqlUpdateStock = "UPDATE product SET pro_stock = pro_stock - ? WHERE pro_id = ?";
            PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);

            String sqlUpdateStatus = "UPDATE product SET pro_status = 'Unavailable' WHERE pro_id = ? AND pro_stock <= 0";
            PreparedStatement psStatus = conn.prepareStatement(sqlUpdateStatus);
            
            for (int i = 0; i < model.getRowCount(); i++) {
                int productId = Integer.parseInt(model.getValueAt(i, 0).toString());
                String productName = model.getValueAt(i, 1).toString();
                int qty = Integer.parseInt(model.getValueAt(i, 3).toString());
                double subtotal = Double.parseDouble(model.getValueAt(i, 5).toString());

                // Insert bill item
                psItem.setInt(1, billId);
                psItem.setInt(2, productId);
                psItem.setString(3, productName);
                psItem.setInt(4, qty);
                psItem.setDouble(5, subtotal);
                psItem.addBatch();

                // Reduce stock
                psStock.setInt(1, qty);
                psStock.setInt(2, productId);
                psStock.addBatch();
                
                psStatus.setInt(1, productId);
                psStatus.addBatch();
            }

            psItem.executeBatch();
            psStock.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving bill: " + e.getMessage());
        }

        model.setRowCount(0); 
        updateTotal();
        
        if (this.adminController != null) {
            this.adminController.loadProductsFromDatabase();
        }
        
        click_default();
    }
}

    

