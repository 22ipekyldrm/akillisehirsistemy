package src.Model;

public class Vehicle {
    private int id;
    private String type; // Örneğin: Otobüs, Otomobil, Kamyon
    private String licensePlate;
    private String status; // Örneğin: Aktif, Bakımda
    private int batteryLevel; // Şarj seviyesini temsil eden yeni bir özellik ekledim

    public Vehicle(int id, String type, String licensePlate, String status) {
        this.id = id;
        this.type = type;
        this.licensePlate = licensePlate;
        this.status = status;
        this.batteryLevel = 100; // Varsayılan şarj seviyesi (örneğin tam dolu olarak ayarlandı)
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getType() { return type; }
    public String getLicensePlate() { return licensePlate; }
    public String getStatus() { return status; }
    public int getBatteryLevel() { return batteryLevel; }

    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setStatus(String status) { this.status = status; }
    public void setBatteryLevel(int batteryLevel) { this.batteryLevel = batteryLevel; }

    // Şarj durumunu gösteren metot
    public void SarjiGoster() {
        System.out.println("Araç ID: " + id + " | Şarj Seviyesi: " + batteryLevel + "%");
    }

    // İstasyona dönme simülasyonu
    public void IstasyonaDon() {
        if (!status.equals("Aktif")) {
            System.out.println("Araç şu anda istasyona dönemiyor çünkü durumu: " + status);
        } else {
            System.out.println("Araç istasyona dönüyor...");
            this.status = "Bakımda"; // İstasyona dönünce bakım moduna geçtiği varsayılmıştır
            this.batteryLevel = 100; // Şarj tam dolu olarak ayarlandı
        }
    }
}
