package connectionwithdatabas;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ProductDAO productDAO;

    public ProductGUI() {
        productDAO = new ProductDAO();
        setTitle("Product Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new Object[]{"ID", "Name", "Price"}, 0);
        table = new JTable(model);
        loadTableData();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Product");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> showAddDialog());
        refreshBtn.addActionListener(e -> loadTableData());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(refreshBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadTableData() {
        model.setRowCount(0);
        List<Product> list = productDAO.getAllProducts();
        for (Product p : list) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice()});
        }
    }

    private void showAddDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Product product = new Product();
            product.setName(nameField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            productDAO.insertProduct(product);
            loadTableData();
        }
    }
}

