//Report Sınıfı
package src.Model;

public class Report {
    private int id;
    private String type; // Örneğin: Trafik, Enerji, Su
    private String description;
    private String date; // YYYY-MM-DD formatında tarih

    public Report(int id, String type, String description, String date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
}
