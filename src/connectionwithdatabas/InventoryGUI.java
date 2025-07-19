package connectionwithdatabas;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private InventoryDAO inventoryDAO;

    public InventoryGUI() {
        inventoryDAO = new InventoryDAO();
        setTitle("Inventory Management Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Fix: Add 5 columns including Product Name
        model = new DefaultTableModel(new Object[]{"ID", "Product ID", "Product Name", "Quantity", "Last Updated"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> showEditDialog());
        deleteBtn.addActionListener(e -> deleteSelectedInventory());
        refreshBtn.addActionListener(e -> loadTableData());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadTableData();
    }

    private void loadTableData() {
        boolean lowStockFound = false;
        List<InventoryWithProduct> inventoryList = inventoryDAO.getInventoryWithProductNames();

        model.setRowCount(0); // Clear table

        for (InventoryWithProduct inv : inventoryList) {
            model.addRow(new Object[]{
                    inv.getInventoryId(),
                    inv.getProductId(),
                    inv.getProductName(),
                    inv.getQuantity(),
                    inv.getLastUpdated()
            });

            if (inv.getQuantity() < 10) {
                lowStockFound = true;
            }
        }

        // Optional: color rows red for low stock
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int qty = (int) table.getModel().getValueAt(row, 3); // Quantity is now at index 3
                if (qty < 10) {
                    c.setBackground(Color.PINK);
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        if (lowStockFound) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Warning: One or more products are low in stock (<10).",
                    "Low Stock Alert",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddDialog() {
        JTextField productIdField = new JTextField();
        JTextField quantityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Product ID:"));
        panel.add(productIdField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Inventory", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int pid = Integer.parseInt(productIdField.getText());
                int qty = Integer.parseInt(quantityField.getText());

                Inventory inv = new Inventory();
                inv.setProductId(pid);
                inv.setQuantity(qty);

                inventoryDAO.insertInventory(inv);
                JOptionPane.showMessageDialog(this, "✅ Inventory added successfully.");
                loadTableData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input. Please enter numbers only.");
            }
        }
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a row to edit.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int productId = (int) model.getValueAt(row, 1);
        int quantity = (int) model.getValueAt(row, 3); // Quantity now in column index 3

        JTextField productIdField = new JTextField(String.valueOf(productId));
        JTextField quantityField = new JTextField(String.valueOf(quantity));

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Product ID:"));
        panel.add(productIdField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Inventory", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Inventory updated = new Inventory();
                updated.setId(id);
                updated.setProductId(Integer.parseInt(productIdField.getText()));
                updated.setQuantity(Integer.parseInt(quantityField.getText()));

                inventoryDAO.updateInventory(updated);
                JOptionPane.showMessageDialog(this, "✅ Inventory updated successfully.");
                loadTableData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input.");
            }
        }
    }

    private void deleteSelectedInventory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a row to delete.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?");
        if (confirm == JOptionPane.YES_OPTION) {
            inventoryDAO.deleteInventory(id);
            JOptionPane.showMessageDialog(this, "🗑️ Inventory deleted successfully.");
            loadTableData();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryGUI().setVisible(true));
    }
}
