package connectionwithdatabas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // ✅ Fetch all inventory records
    public List<Inventory> getAllStock() {
        List<Inventory> stockList = new ArrayList<>();
        String sql = "SELECT * FROM inventory";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setId(rs.getInt("inventory_id"));
                inv.setProductId(rs.getInt("product_id"));
                inv.setQuantity(rs.getInt("quantity"));
                inv.setLastUpdated(rs.getTimestamp("last_updated"));
                stockList.add(inv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockList;
    }

    // ✅ Insert new inventory record
    public void insertInventory(Inventory inv) {
        String sql = "INSERT INTO inventory (product_id, quantity, last_updated) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inv.getProductId());
            stmt.setInt(2, inv.getQuantity());

            stmt.executeUpdate();
            System.out.println("✅ Inventory added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Update existing inventory quantity
    public void updateInventory(Inventory inv) {
        String sql = "UPDATE inventory SET quantity = ?, last_updated = CURRENT_TIMESTAMP WHERE inventory_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inv.getQuantity());
            stmt.setInt(2, inv.getId());

            stmt.executeUpdate();
            System.out.println("✅ Inventory updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Delete inventory record by ID
    public void deleteInventory(int inventoryId) {
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventoryId);
            stmt.executeUpdate();
            System.out.println("✅ Inventory deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // ✅ New method to get inventory with product names
    public List<InventoryWithProduct> getInventoryWithProductNames() {
        List<InventoryWithProduct> stockList = new ArrayList<>();

        String sql = """
        SELECT i.inventory_id, i.product_id, p.name AS product_name, 
               i.quantity, i.last_updated
        FROM inventory i
        JOIN products p ON i.product_id = p.product_id
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                InventoryWithProduct inv = new InventoryWithProduct();
                inv.setInventoryId(rs.getInt("inventory_id"));
                inv.setProductId(rs.getInt("product_id"));
                inv.setProductName(rs.getString("product_name"));
                inv.setQuantity(rs.getInt("quantity"));
                inv.setLastUpdated(rs.getTimestamp("last_updated"));
                stockList.add(inv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockList;
    }


    // ✅ Fetch inventory by ID (optional helper)
    public Inventory getInventoryById(int inventoryId) {
        Inventory inv = null;
        String sql = "SELECT * FROM inventory WHERE inventory_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventoryId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                inv = new Inventory();
                inv.setId(rs.getInt("inventory_id"));
                inv.setProductId(rs.getInt("product_id"));
                inv.setQuantity(rs.getInt("quantity"));
                inv.setLastUpdated(rs.getTimestamp("last_updated"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inv;
    }
}
