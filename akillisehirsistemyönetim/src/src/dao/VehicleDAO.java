//VehicleDAO Sınıfı
package src.dao; // DAO (Veri Erişim Nesnesi) sınıflarını içeren paket
import src.Model.*; // Model sınıflarını içe aktarır

import java.io.*; // Dosya işlemleri için gerekli sınıflar
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // Liste veri yapısını içe aktarır
import javax.swing.JOptionPane; // Kullanıcıya mesaj göstermek için gerekli sınıf

public class VehicleDAO { // Araç bilgileri için veri erişim sınıfı
    public final String VEHICLE_FILE = "data/vehicles.txt"; // Araç bilgilerini tutan dosyanın yolu

    public VehicleDAO() { // Yapıcı metot
        File file = new File(VEHICLE_FILE); // Araç dosyasını temsil eden bir dosya nesnesi oluşturur
        if (!file.exists()) { // Dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Klasör yoksa oluşturur
                file.createNewFile(); // Yeni bir dosya oluşturur
                // Dosyaya başlık satırını ekler
                BufferedWriter bw = new BufferedWriter(new FileWriter(VEHICLE_FILE));
                bw.write("id,type,licensePlate,status"); // Başlık bilgilerini yazar
                bw.newLine(); // Yeni satır ekler
                bw.close(); // Yazma işlemini kapatır
            } catch(IOException e) { // Dosya oluşturulamazsa istisna yakalar
                e.printStackTrace(); // Hata detaylarını konsola yazdırır
                JOptionPane.showMessageDialog(null, "Araç dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public List<Vehicle> getAllVehicles() { // Tüm araçları döndüren metot
        List<Vehicle> vehicles = new ArrayList<>(); // Araç bilgilerini tutacak bir liste oluşturur
        try(BufferedReader br = new BufferedReader(new FileReader(VEHICLE_FILE))) { // Dosyayı okumak için BufferedReader kullanır
            String line; // Satır bilgisi için değişken tanımlanır
            br.readLine(); // İlk satırdaki başlığı atlar
            while((line = br.readLine()) != null) { // Dosyadaki her bir satırı okur
                String[] parts = line.split(",", 4); // Satırı virgüllere göre ayırır
                if(parts.length == 4) { // Doğru formatta bir satırsa
                    int id = Integer.parseInt(parts[0]); // ID'yi alır ve tam sayıya dönüştürür
                    String type = parts[1]; // Araç tipini alır
                    String licensePlate = parts[2]; // Plaka bilgisini alır
                    String status = parts[3]; // Durum bilgisini alır
                    vehicles.add(new Vehicle(id, type, licensePlate, status)); // Yeni bir araç nesnesi oluşturur ve listeye ekler
                }
            }
        } catch(IOException e) { // Dosya okunamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Araç dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return vehicles; // Okunan araç bilgilerini döner
    }

    public void addVehicle(Vehicle vehicle) { // Yeni araç ekleyen metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(VEHICLE_FILE, true))) { // Dosyayı yazma modunda açar
            bw.write(vehicle.getId() + "," + vehicle.getType() + "," + vehicle.getLicensePlate() + "," + vehicle.getStatus()); // Araç bilgilerini yazar
            bw.newLine(); // Yeni satır ekler
        } catch(IOException e) { // Dosyaya yazılamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Araç eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    public int getNextId() { // Bir sonraki kullanılabilir ID'yi döndüren metot
        List<Vehicle> vehicles = getAllVehicles(); // Tüm araçları alır
        int maxId = 0; // En büyük ID'yi tutmak için bir değişken oluşturur
        for(Vehicle vehicle : vehicles) { // Her bir araç için döngü
            if(vehicle.getId() > maxId) { // Eğer aracın ID'si mevcut maxId'den büyükse
                maxId = vehicle.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // En büyük ID'nin bir fazlasını döner
    }

    public String getVehicleFile() { // Araç dosyasını dönen metot (şu anda tamamlanmamış)
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVehicleFile'"); // Henüz uygulanmamış bir hata döner
    }

    // Diğer CRUD işlemleri (Güncelleme, Silme) eklenebilir
}
