package src.Model;

public class EnergySource {
    private int id;
    private String type; // Örneğin: Güneş, Rüzgar, Nükleer, Fosil Yakıt
    private double capacity; // Örneğin, 100 kW
    private double currentOutput; // Mevcut üretim miktarı

    public EnergySource(int id, String type, double capacity, double currentOutput) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.currentOutput = currentOutput;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getType() { return type; }
    public double getCapacity() { return capacity; }
    public double getCurrentOutput() { return currentOutput; }

    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setCapacity(double capacity) { this.capacity = capacity; }
    public void setCurrentOutput(double currentOutput) { this.currentOutput = currentOutput; }

    // Tüketim uyarısı metodu
    public void TuketimUyarisi() {
        double threshold = 0.9 * capacity; // Kapasitenin %90'ı sınır olarak belirlendi
        if (currentOutput >= threshold) {
            System.out.println("UYARI: Enerji kaynağı (" + type + ") üretim kapasitesine yaklaşıyor!");
            System.out.println("Mevcut üretim: " + currentOutput + " / Kapasite: " + capacity);
        } else {
            System.out.println("Enerji kaynağı (" + type + ") normal seviyede çalışıyor.");
            System.out.println("Mevcut üretim: " + currentOutput + " / Kapasite: " + capacity);
        }
    }
}
