package connectionwithdatabas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Add new supplier
    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, phone, email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getEmail());
            stmt.executeUpdate();

            System.out.println("✅ Supplier added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all suppliers
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("supplier_id"));
                s.setName(rs.getString("name"));
                s.setPhone(rs.getString("phone"));
                s.setEmail(rs.getString("email"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update supplier
    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, phone = ?, email = ? WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getEmail());
            stmt.setInt(4, supplier.getId());
            stmt.executeUpdate();

            System.out.println("✅ Supplier updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete supplier
    public void deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplierId);
            stmt.executeUpdate();

            System.out.println("🗑️ Supplier deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
