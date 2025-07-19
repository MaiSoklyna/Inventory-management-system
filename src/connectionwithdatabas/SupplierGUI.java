package connectionwithdatabas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierGUI extends JFrame {

    private JTextField nameField, phoneField, emailField;
    private JButton addBtn, updateBtn, deleteBtn;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private SupplierDAO supplierDAO = new SupplierDAO();

    public SupplierGUI() {
        setTitle("Supplier Management");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Info"));

        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        formPanel.add(buttonPanel);

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Email"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(supplierTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("All Suppliers"));

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);

        // Load data into table
        loadSuppliers();

        // Action listeners
        addBtn.addActionListener(e -> addSupplier());
        updateBtn.addActionListener(e -> updateSupplier());
        deleteBtn.addActionListener(e -> deleteSupplier());

        // Table row click
        supplierTable.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());

        setVisible(true);
    }

    private void loadSuppliers() {
        tableModel.setRowCount(0); // Clear table
        List<Supplier> list = supplierDAO.getAllSuppliers();
        for (Supplier s : list) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getPhone(), s.getEmail()});
        }
    }

    private void addSupplier() {
        if (validateForm()) {
            Supplier supplier = new Supplier();
            supplier.setName(nameField.getText());
            supplier.setPhone(phoneField.getText());
            supplier.setEmail(emailField.getText());
            supplierDAO.addSupplier(supplier);
            clearForm();
            loadSuppliers();
            JOptionPane.showMessageDialog(this, "Supplier added!");
        }
    }

    private void updateSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a supplier to update.");
            return;
        }

        if (validateForm()) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Supplier supplier = new Supplier();
            supplier.setId(id);
            supplier.setName(nameField.getText());
            supplier.setPhone(phoneField.getText());
            supplier.setEmail(emailField.getText());
            supplierDAO.updateSupplier(supplier);
            clearForm();
            loadSuppliers();
            JOptionPane.showMessageDialog(this, "Supplier updated!");
        }
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a supplier to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            supplierDAO.deleteSupplier(id);
            clearForm();
            loadSuppliers();
            JOptionPane.showMessageDialog(this, "Supplier deleted!");
        }
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            phoneField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        supplierTable.clearSelection();
    }

    private boolean validateForm() {
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }
        return true;
    }
}
