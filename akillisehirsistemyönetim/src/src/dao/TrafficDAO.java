//TrafficDAO Sınıfı
package src.dao; // DAO (Veri Erişim Nesnesi) sınıflarını içeren paket

import src.Model.*; // Model sınıflarını içe aktarır
import java.io.*; // Dosya işlemleri için gerekli sınıflar
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // Liste veri yapısını içe aktarır
import javax.swing.JOptionPane; // Kullanıcıya hata mesajları göstermek için gerekli sınıf

public class TrafficDAO { // Trafik bilgileri için veri erişim sınıfı
    public final String TRAFFIC_FILE = "data/traffic.txt"; // Trafik bilgilerini tutan dosyanın yolu

    public TrafficDAO() { // Yapıcı metot
        File file = new File(TRAFFIC_FILE); // Trafik dosyasını temsil eden bir dosya nesnesi oluşturur
        if (!file.exists()) { // Dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Klasör yoksa oluşturur
                file.createNewFile(); // Yeni bir dosya oluşturur
                // Dosyaya başlık satırını ekler
                BufferedWriter bw = new BufferedWriter(new FileWriter(TRAFFIC_FILE));
                bw.write("id,location,description,date"); // Başlık bilgilerini yazar
                bw.newLine(); // Yeni satır ekler
                bw.close(); // Yazma işlemini kapatır
            } catch(IOException e) { // Dosya oluşturulamazsa istisna yakalar
                e.printStackTrace(); // Hata detaylarını konsola yazdırır
                JOptionPane.showMessageDialog(null, "Trafik dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public List<Traffic> getAllTraffic() { // Tüm trafik bilgilerini döndüren metot
        List<Traffic> trafficList = new ArrayList<>(); // Trafik bilgilerini tutacak bir liste oluşturur
        try(BufferedReader br = new BufferedReader(new FileReader(TRAFFIC_FILE))) { // Dosyayı okumak için BufferedReader kullanır
            String line;
            br.readLine(); // İlk satırdaki başlığı atlar
            while((line = br.readLine()) != null) { // Dosyadaki her bir satırı okur
                String[] parts = line.split(",", 4); // Satırı virgüllere göre ayırır
                if(parts.length == 4) { // Doğru formatta bir satırsa
                    int id = Integer.parseInt(parts[0]); // ID'yi alır ve tam sayıya dönüştürür
                    String location = parts[1]; // Konum bilgisini alır
                    String description = parts[2]; // Açıklama bilgisini alır
                    String date = parts[3]; // Tarih bilgisini alır
                    trafficList.add(new Traffic(id, location, description, date)); // Yeni bir trafik nesnesi oluşturur ve listeye ekler
                }
            }
        } catch(IOException e) { // Dosya okunamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Trafik dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return trafficList; // Okunan trafik bilgilerini döner
    }

    public void addTraffic(Traffic traffic) { // Yeni bir trafik bilgisi ekleyen metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(TRAFFIC_FILE, true))) { // Dosyayı yazma modunda açar
            bw.write(traffic.getId() + "," + traffic.getLocation() + "," + traffic.getDescription() + "," + traffic.getDate()); // Trafik bilgilerini yazar
            bw.newLine(); // Yeni satır ekler
        } catch(IOException e) { // Dosyaya yazılamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Trafik eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    public int getNextId() { // Bir sonraki kullanılabilir ID'yi döndüren metot
        List<Traffic> trafficList = getAllTraffic(); // Tüm trafik bilgilerini alır
        int maxId = 0; // En büyük ID'yi tutmak için bir değişken oluşturur
        for(Traffic traffic : trafficList) { // Her bir trafik bilgisi için döngü
            if(traffic.getId() > maxId) { // Eğer trafik bilgisinin ID'si mevcut maxId'den büyükse
                maxId = traffic.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // En büyük ID'nin bir fazlasını döner
    }

    public String getTrafficFile() { // Henüz uygulanmamış bir metot
        // TODO Otomatik oluşturulan metot iskeleti
        throw new UnsupportedOperationException("Unimplemented method 'getTrafficFile'"); // Uygulanmadığını belirten bir istisna atar
    }

    // Diğer CRUD işlemleri (Güncelleme, Silme) eklenebilir
}
