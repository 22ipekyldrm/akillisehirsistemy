// BuildingDAO
package src.dao; // DAO (Veri Erişim Nesnesi) sınıflarının bulunduğu paketi tanımlar
import java.io.*; // Giriş/çıkış işlemleri için gerekli sınıfları içe aktarır
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // Liste veri yapısını içe aktarır
import javax.swing.JOptionPane; // Kullanıcıya hata mesajları göstermek için gerekli sınıfı içe aktarır
import src.Model.*; // Building sınıfı gibi model sınıflarını içe aktarır

public class BuildingDAO { // Binalar için veri erişim sınıfı
    public final String BUILDING_FILE = "data/buildings.txt"; // Bina verilerinin tutulduğu dosya yolu

    public BuildingDAO() { // Yapıcı metot
        File file = new File(BUILDING_FILE); // Bina dosyasını temsil eden bir dosya nesnesi oluşturur
        if (!file.exists()) { // Dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Klasör yoksa oluşturur
                file.createNewFile(); // Yeni bir dosya oluşturur
                // Dosyaya başlık satırını ekler
                BufferedWriter bw = new BufferedWriter(new FileWriter(BUILDING_FILE));
                bw.write("id,type,floors,energyEfficiency"); // Başlık bilgilerini yazar
                bw.newLine(); 
                bw.close(); 
            } catch(IOException e) { 
                e.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "Bina dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public List<Building> getAllBuildings() { // Tüm binaları döndüren metot
        List<Building> buildings = new ArrayList<>(); 
        try(BufferedReader br = new BufferedReader(new FileReader(BUILDING_FILE))) { 
            String line;
            br.readLine(); // İlk satırdaki başlığı atlar
            while((line = br.readLine()) != null) { // Dosyadaki her bir satırı okur
                String[] parts = line.split(",", 4); // Satırı virgüllere göre ayırır
                if(parts.length == 4) { // Doğru formatta bir satırsa
                    int id = Integer.parseInt(parts[0]); // ID'yi ayıklar ve tam sayıya dönüştürür
                    String type = parts[1]; // Tip bilgisini alır
                    int floors = Integer.parseInt(parts[2]); // Kat sayısını alır ve tam sayıya dönüştürür
                    double energyEfficiency = Double.parseDouble(parts[3]); // Enerji verimliliğini alır ve ondalık sayıya dönüştürür
                    buildings.add(new Building(id, type, floors, energyEfficiency)); // Yeni bir bina nesnesi oluşturur ve listeye ekler
                }
            }
        } catch(IOException e) { // Dosya okunamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Bina dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return buildings; // Okunan binaları döner
    }

    public void addBuilding(Building building) { // Yeni bir bina ekleyen metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(BUILDING_FILE, true))) { // Dosyayı yazma modunda açar
            bw.write(building.getId() + "," + building.getType() + "," + building.getFloors() + "," + building.getEnergyEfficiency()); // Bina bilgilerini yazdırır
            bw.newLine(); // Yeni satır ekler
        } catch(IOException e) { // Dosyaya yazılamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Bina eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    public int getNextId() { // Bir sonraki kullanılabilir ID'yi döndüren metot
        List<Building> buildings = getAllBuildings(); // Tüm binaları alır
        int maxId = 0; // En büyük ID'yi tutmak için bir değişken oluşturur
        for(Building building : buildings) { // Her bir bina için döngü
            if(building.getId() > maxId) { // Eğer bina ID'si mevcut maxId'den büyükse
                maxId = building.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // En büyük ID'nin bir fazlasını döner
    }

    public String getBuildingFile() { // Henüz uygulanmamış bir metot
        // TODO Otomatik oluşturulan metot iskeleti
        throw new UnsupportedOperationException("Unimplemented method 'getBuildingFile'"); // Uygulanmadığını belirten bir istisna atar
    }

   
}
