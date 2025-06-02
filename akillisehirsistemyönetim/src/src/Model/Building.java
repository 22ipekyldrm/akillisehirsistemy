package src.Model;

public class Building {
    private int id;
    private String type; // Örneğin: Ofis, Konut, Mağaza
    private int floors;
    private double energyEfficiency; // Verimlilik değeri (0.0 - 1.0 arasında, 1.0 maksimum verimliliktir)
    private boolean isInSavingsMode; // Tasarruf modunda olup olmadığını belirtmek için yeni bir alan

    public Building(int id, String type, int floors, double energyEfficiency) {
        this.id = id;
        this.type = type;
        this.floors = floors;
        this.energyEfficiency = energyEfficiency;
        this.isInSavingsMode = false; // Varsayılan olarak tasarruf modu kapalı
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getType() { return type; }
    public int getFloors() { return floors; }
    public double getEnergyEfficiency() { return energyEfficiency; }
    public boolean isInSavingsMode() { return isInSavingsMode; }

    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setFloors(int floors) { this.floors = floors; }
    public void setEnergyEfficiency(double energyEfficiency) { this.energyEfficiency = energyEfficiency; }
    public void setInSavingsMode(boolean inSavingsMode) { isInSavingsMode = inSavingsMode; }

    // Tasarruf modunu etkinleştiren metot
    public void TassarufModu() {
        if (!isInSavingsMode) {
            isInSavingsMode = true;
            energyEfficiency += 0.1; // Verimlilik değeri tasarruf modunda artırılır (örnek olarak)
            if (energyEfficiency > 1.0) energyEfficiency = 1.0; // Maksimum verimlilik sınırı
            System.out.println("Bina tasarruf moduna geçti. Güncel enerji verimliliği: " + energyEfficiency);
        } else {
            System.out.println("Bina zaten tasarruf modunda.");
        }
    }

    // Verimlilik raporlayan metot
    public void VerimliligiRaporla() {
        System.out.println("Bina ID: " + id);
        System.out.println("Tür: " + type);
        System.out.println("Kat Sayısı: " + floors);
        System.out.println("Enerji Verimliliği: " + (energyEfficiency * 100) + "%");
        System.out.println("Tasarruf Modunda: " + (isInSavingsMode ? "Evet" : "Hayır"));
    }
}
