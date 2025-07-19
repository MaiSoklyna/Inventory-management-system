package connectionwithdatabas;

import javax.swing.*;
import java.awt.*;

public class DashboardGUI extends JFrame {

    public DashboardGUI() {
        setTitle("Inventory Management Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Buttons for modules
        JButton productBtn = new JButton("Manage Products");
        JButton inventoryBtn = new JButton("Manage Inventory");
        JButton supplierBtn = new JButton("Manage Suppliers");

        // Add actions to open respective GUIs
        productBtn.addActionListener(e -> new ProductGUI().setVisible(true));
        inventoryBtn.addActionListener(e -> new InventoryGUI().setVisible(true));
        supplierBtn.addActionListener(e -> new SupplierGUI().setVisible(true));

        // Arrange buttons in layout
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.add(productBtn);
        panel.add(inventoryBtn);
        panel.add(supplierBtn);

        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardGUI().setVisible(true));
    }
}
