/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import javax.swing.JTable;
import DAO.BillDAO;
import DAO.BillItemDAO;
import Model.Bill;
import Model.BillItem;
import UI.ViewBillPanel;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ThinkBook
 */
public class ViewBillController {
    private ViewBillPanel view;
    
    public ViewBillController(ViewBillPanel view){
        this.view = view;
        
        JTable table = view.getBillTable();
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
        // Căn lề phải cho đẹp
        currencyRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

        // Áp dụng cho cột Total (Cột thứ 3: ID=0, Date=1, Time=2, Total=3)
        // Bạn cần đảm bảo bảng trong Design đã có đủ 4 cột
        try {
            table.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer);
        } catch (Exception e) {
            System.out.println("Lỗi: Bảng Bill chưa đủ cột để set Renderer");
        }
        // ---------------------------------------------

        
        loadBillTable();
        addRowClickListener();
    }
    
    
    private void loadBillTable(){
        JTable table = view.getBillTable();
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        BillDAO dao = new BillDAO();
        List<Bill> bills = dao.getAllBills();
        
        for (Bill b : bills) {
            java.sql.Timestamp ts = b.getDate();
            String date = "";
            String time = "";

            if (ts != null) {
                date = ts.toLocalDateTime().toLocalDate().toString();
                time = ts.toLocalDateTime().toLocalTime().toString();
            }

           
            model.addRow(new Object[]{
                b.getId(),      
                date,         
                time,           
                b.getTotal()    
            });
        }
        
    }
    
    private void addRowClickListener() {
    view.getBillTable().addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            showBillItems();  // call your function
        }
    });
}
    
    private void showBillItems() {
        JTable table = view.getBillTable();
        JTextArea detail = view.getDetailTextArea();

        int row = table.getSelectedRow();
        if (row < 0) return;

        int billId = (int) table.getValueAt(row, 0);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        BillItemDAO dao = new BillItemDAO();
        List<BillItem> items = dao.getBillItemsByBillId(billId);

        StringBuilder sb = new StringBuilder();
        sb.append("*********** Brother Chicken **********\n");
        sb.append("------------- BILL DETAILS -----------\n\n");
        sb.append(String.format("%-20s %-5s %s\n", "Item", "Qty", "Subtotal"));
        sb.append("--------------------------------------\n");

        double total = 0;

        for (BillItem it : items) {
            sb.append(String.format(
                "%-20s %-5d %s\n",
                it.getProductName(),
                it.getQuantity(),
                currencyFormat.format(it.getSubTotal())
            ));
            total += it.getSubTotal();
        }

        sb.append("--------------------------------------\n");
        sb.append("TOTAL: " + currencyFormat.format(total) + "\n");
        sb.append("--------------------------------------\n");
        detail.setText(sb.toString());
    }
}
