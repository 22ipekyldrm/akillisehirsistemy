//Traffic Sınıfı
package src.Model;

public class Traffic {
    private int id;
    private String location;
    private String description;
    private String date; // YYYY-MM-DD formatında tarih

    public Traffic(int id, String location, String description, String date) {
        this.id = id;
        this.location = location;
        this.description = description;
        this.date = date;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    public void setId(int id) { this.id = id; }
    public void setLocation(String location) { this.location = location; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
}
