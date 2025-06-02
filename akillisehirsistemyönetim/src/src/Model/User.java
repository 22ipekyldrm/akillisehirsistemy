//User Sınıfı
package src.Model;
public class User {
    private int id;
    private String username;
    private String password; // Şifreler hashlenmiş olarak saklanmalıdır
    private String role; // "Admin" veya "Citizen"

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}
