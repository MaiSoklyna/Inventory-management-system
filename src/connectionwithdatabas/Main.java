package connectionwithdatabas;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> inventoryList = inventoryDAO.getAllStock();

        for (Inventory inv : inventoryList) {
            System.out.println("Inventory ID: " + inv.getId());
            System.out.println("Product ID: " + inv.getProductId());
            System.out.println("Quantity: " + inv.getQuantity());
            System.out.println("Last Updated: " + inv.getLastUpdated());
            System.out.println("------------------------------");
        }
    }
}
