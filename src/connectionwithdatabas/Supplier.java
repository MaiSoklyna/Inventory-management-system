package connectionwithdatabas;

public class Supplier {
    private int supplier_id;
    private String name;
    private String phone;
    private String email;

    // Getters and Setters
    public int getId() { return supplier_id; }
    public void setId(int id) { this.supplier_id = supplier_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

