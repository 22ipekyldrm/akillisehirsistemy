//WaterDAO Sınıfı
package src.dao; // Su verileri için DAO sınıfının ait olduğu paket

import src.Model.*; // Model sınıflarını içe aktarır
import java.io.*; // Dosya işlemleri için gerekli sınıfları içe aktarır
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // List arayüzünü içe aktarır
import javax.swing.JOptionPane; // Mesaj kutuları için Swing bileşenini içe aktarır

public class WaterDAO { // Su DAO sınıfı
    private final String WATER_FILE = "data/water.txt"; // Su dosyasının yolu

    public WaterDAO() { // Yapıcı metot
        File file = new File(WATER_FILE); // Su dosyasını temsil eden bir File nesnesi oluşturur
        if (!file.exists()) { // Eğer dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Gerekli klasörleri oluşturur
                file.createNewFile(); // Dosyayı oluşturur
                // Başlık satırını dosyaya yazar
                BufferedWriter bw = new BufferedWriter(new FileWriter(WATER_FILE)); // BufferedWriter ile dosyayı açar
                bw.write("id,location,level,date"); // Başlık satırını yazar
                bw.newLine(); // Yeni satır oluşturur
                bw.close(); // BufferedWriter'ı kapatır
            } catch(IOException e) { // Dosya oluşturma sırasında bir hata oluşursa
                e.printStackTrace(); // Hata ayıklama çıktısını gösterir
                JOptionPane.showMessageDialog(null, "Su dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public List<Water> getAllWater() { // Tüm su verilerini getirir
        List<Water> waterList = new ArrayList<>(); // Su verilerini tutacak bir liste oluşturur
        try(BufferedReader br = new BufferedReader(new FileReader(WATER_FILE))) { // Dosyayı okumak için BufferedReader kullanır
            String line; // Okunan her satırı tutmak için bir String değişkeni
            br.readLine(); // Başlık satırını atlar
            while((line = br.readLine()) != null) { // Dosyanın sonuna kadar okur
                String[] parts = line.split(",", 4); // Her satırı virgül ile ayırır
                if(parts.length == 4) { // Doğru formatta ise
                    int id = Integer.parseInt(parts[0]); // ID'yi ayıklar
                    String location = parts[1]; // Lokasyonu ayıklar
                    double level = Double.parseDouble(parts[2]); // Su seviyesini ayıklar
                    String date = parts[3]; // Tarihi ayıklar
                    waterList.add(new Water(id, location, level, date)); // Yeni bir Water nesnesi oluşturup listeye ekler
                }
            }
        } catch(IOException e) { // Okuma sırasında bir hata oluşursa
            e.printStackTrace(); // Hata ayıklama çıktısını gösterir
            JOptionPane.showMessageDialog(null, "Su dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return waterList; // Su listesini döndürür
    }

    public void addWater(Water water) { // Yeni bir su kaydı ekler
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(WATER_FILE, true))) { // Dosyaya ekleme modunda BufferedWriter kullanır
            bw.write(water.getId() + "," + water.getLocation() + "," + water.getLevel() + "," + water.getDate()); // Yeni su verisini dosyaya yazar
            bw.newLine(); // Yeni satır oluşturur
        } catch(IOException e) { // Yazma sırasında bir hata oluşursa
            e.printStackTrace(); // Hata ayıklama çıktısını gösterir
            JOptionPane.showMessageDialog(null, "Su eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    public int getNextId() { // Yeni bir su kaydı için kullanılacak ID'yi döndürür
        List<Water> waterList = getAllWater(); // Mevcut su verilerini alır
        int maxId = 0; // Maksimum ID'yi tutmak için bir değişken
        for(Water water : waterList) { // Su listesindeki her su kaydını dolaşır
            if(water.getId() > maxId) { // Eğer mevcut su kaydının ID'si maxId'den büyükse
                maxId = water.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // Yeni ID'yi döndürür
    }

    public String getWaterFile() { // Su dosyasının yolunu döndürmek için bir metot
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWaterFile'"); // Henüz uygulanmamış bir metot
    }

    // Diğer CRUD işlemleri (Güncelleme, Silme) eklenebilir
}
