//EnergySourceDAO Sınıfı
package src.dao; // DAO (Veri Erişim Nesnesi) sınıflarının bulunduğu paketi tanımlar
import java.io.*; // Giriş/çıkış işlemleri için gerekli sınıfları içe aktarır
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // Liste veri yapısını içe aktarır
import javax.swing.JOptionPane; // Kullanıcıya hata mesajları göstermek için gerekli sınıfı içe aktarır
import src.Model.*; // EnergySource gibi model sınıflarını içe aktarır

public class EnergySourceDAO { // Enerji kaynakları için veri erişim sınıfı
    public final String ENERGY_SOURCE_FILE = "data/energy_sources.txt"; // Enerji kaynaklarının tutulduğu dosya yolu

    public EnergySourceDAO() { // Yapıcı metot
        File file = new File(ENERGY_SOURCE_FILE); // Enerji kaynağı dosyasını temsil eden bir dosya nesnesi oluşturur
        if (!file.exists()) { // Dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Klasör yoksa oluşturur
                file.createNewFile(); // Yeni bir dosya oluşturur
                // Dosyaya başlık satırını ekler
                BufferedWriter bw = new BufferedWriter(new FileWriter(ENERGY_SOURCE_FILE));
                bw.write("id,type,capacity,currentOutput"); // Başlık bilgilerini yazar
                bw.newLine(); // Yeni satır ekler
                bw.close(); // Dosya yazmayı kapatır
            } catch(IOException e) { // Dosya oluşturulamazsa istisna yakalar
                e.printStackTrace(); // Hata detaylarını konsola yazdırır
                JOptionPane.showMessageDialog(null, "Enerji kaynağı dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public List<EnergySource> getAllEnergySources() { // Tüm enerji kaynaklarını döndüren metot
        List<EnergySource> energySources = new ArrayList<>(); // Enerji kaynaklarını tutacak bir liste oluşturur
        try(BufferedReader br = new BufferedReader(new FileReader(ENERGY_SOURCE_FILE))) { // Dosyayı okumak için BufferedReader kullanır
            String line;
            br.readLine(); // İlk satırdaki başlığı atlar
            while((line = br.readLine()) != null) { // Dosyadaki her bir satırı okur
                String[] parts = line.split(",", 4); // Satırı virgüllere göre ayırır
                if(parts.length == 4) { // Doğru formatta bir satırsa
                    int id = Integer.parseInt(parts[0]); // ID'yi ayıklar ve tam sayıya dönüştürür
                    String type = parts[1]; // Tip bilgisini alır
                    double capacity = Double.parseDouble(parts[2]); // Kapasiteyi alır ve ondalık sayıya dönüştürür
                    double currentOutput = Double.parseDouble(parts[3]); // Mevcut çıkışı alır ve ondalık sayıya dönüştürür
                    energySources.add(new EnergySource(id, type, capacity, currentOutput)); // Yeni bir enerji kaynağı nesnesi oluşturur ve listeye ekler
                }
            }
        } catch(IOException e) { // Dosya okunamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Enerji kaynağı dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return energySources; // Okunan enerji kaynaklarını döner
    }

    public void addEnergySource(EnergySource energySource) { // Yeni bir enerji kaynağı ekleyen metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ENERGY_SOURCE_FILE, true))) { // Dosyayı yazma modunda açar
            bw.write(energySource.getId() + "," + energySource.getType() + "," + energySource.getCapacity() + "," + energySource.getCurrentOutput()); // Enerji kaynağı bilgilerini yazdırır
            bw.newLine(); // Yeni satır ekler
        } catch(IOException e) { // Dosyaya yazılamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Enerji kaynağı eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    public int getNextId() { // Bir sonraki kullanılabilir ID'yi döndüren metot
        List<EnergySource> energySources = getAllEnergySources(); // Tüm enerji kaynaklarını alır
        int maxId = 0; // En büyük ID'yi tutmak için bir değişken oluşturur
        for(EnergySource es : energySources) { // Her bir enerji kaynağı için döngü
            if(es.getId() > maxId) { // Eğer enerji kaynağı ID'si mevcut maxId'den büyükse
                maxId = es.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // En büyük ID'nin bir fazlasını döner
    }

    public String getEnergySourceFile() { // Henüz uygulanmamış bir metot
        // TODO Otomatik oluşturulan metot iskeleti
        throw new UnsupportedOperationException("Unimplemented method 'getEnergySourceFile'"); // Uygulanmadığını belirten bir istisna atar
    }

    // Diğer CRUD işlemleri (Güncelleme, Silme) eklenebilir
}
