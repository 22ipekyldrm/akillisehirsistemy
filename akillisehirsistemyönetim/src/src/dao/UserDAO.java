//UserDAO Sınıfı
package src.dao; // DAO (Veri Erişim Nesnesi) sınıflarını içeren paket

import src.Model.*; // Model sınıflarını içe aktarır
import src.gui.*; // GUI (Grafiksel Kullanıcı Arayüzü) sınıflarını içe aktarır
import java.io.*; // Dosya işlemleri için gerekli sınıflar
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // Liste veri yapısını içe aktarır
import javax.swing.JOptionPane; // Kullanıcıya mesaj göstermek için gerekli sınıf

public class UserDAO { // Kullanıcı bilgileri için veri erişim sınıfı
    private final String USER_FILE = "data/users.txt"; // Kullanıcı bilgilerini tutan dosyanın yolu

    public UserDAO() { // Yapıcı metot
        File file = new File(USER_FILE); // Kullanıcı dosyasını temsil eden bir dosya nesnesi oluşturur
        if (!file.exists()) { // Dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Klasör yoksa oluşturur
                file.createNewFile(); // Yeni bir dosya oluşturur
                // Dosyaya başlık satırını ekler
                BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE));
                bw.write("id,username,password,role"); // Başlık bilgilerini yazar
                bw.newLine(); // Yeni satır ekler
                bw.close(); // Yazma işlemini kapatır
                
                // Varsayılan Admin Kullanıcısını Ekle (Şifreleri hashleyerek)
                addUser(new User(1, "admin", PasswordUtil.hashPassword("admin123"), "Admin"));
                // Varsayılan Citizen Kullanıcısını Ekle
                addUser(new User(2, "citizen", PasswordUtil.hashPassword("citizen123"), "Citizen"));
                
                JOptionPane.showMessageDialog(null, "Varsayılan kullanıcılar oluşturuldu.\nAdmin: admin/admin123\nCitizen: citizen/citizen123", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            } catch(IOException e) { // Dosya oluşturulamazsa istisna yakalar
                e.printStackTrace(); // Hata detaylarını konsola yazdırır
                JOptionPane.showMessageDialog(null, "Kullanıcı dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public User authenticateUser(String username, String password) { // Kullanıcı kimlik doğrulama metodu
        String hashedPassword = PasswordUtil.hashPassword(password); // Şifreyi hashleyerek kontrol eder
        List<User> users = getAllUsers(); // Tüm kullanıcıları alır
        for(User user : users) { // Her bir kullanıcı için döngü
            if(user.getUsername().equals(username) && user.getPassword().equals(hashedPassword)) { // Kullanıcı adı ve şifre eşleşiyorsa
                return user; // Kullanıcıyı döner
            }
        }
        return null; // Hiçbir eşleşme yoksa null döner
    }

    public List<User> getAllUsers() { // Tüm kullanıcıları döndüren metot
        List<User> users = new ArrayList<>(); // Kullanıcı bilgilerini tutacak bir liste oluşturur
        try(BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) { // Dosyayı okumak için BufferedReader kullanır
            String line;
            br.readLine(); // İlk satırdaki başlığı atlar
            while((line = br.readLine()) != null) { // Dosyadaki her bir satırı okur
                String[] parts = line.split(","); // Satırı virgüllere göre ayırır
                if(parts.length == 4) { // Doğru formatta bir satırsa
                    int id = Integer.parseInt(parts[0]); // ID'yi alır ve tam sayıya dönüştürür
                    String username = parts[1]; // Kullanıcı adını alır
                    String password = parts[2]; // Şifreyi alır
                    String role = parts[3]; // Kullanıcı rolünü alır
                    users.add(new User(id, username, password, role)); // Yeni bir kullanıcı nesnesi oluşturur ve listeye ekler
                }
            }
        } catch(IOException e) { // Dosya okunamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Kullanıcı dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return users; // Okunan kullanıcı bilgilerini döner
    }

    // Yeni kullanıcı eklemek için
    public void addUser(User user) { // Kullanıcı bilgisi ekleyen metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) { // Dosyayı yazma modunda açar
            bw.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole()); // Kullanıcı bilgilerini yazar
            bw.newLine(); // Yeni satır ekler
        } catch(IOException e) { // Dosyaya yazılamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Kullanıcı eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    // ID'ye göre en yüksek ID'yi bulmak için
    public int getNextId() { // Bir sonraki kullanılabilir ID'yi döndüren metot
        List<User> users = getAllUsers(); // Tüm kullanıcıları alır
        int maxId = 0; // En büyük ID'yi tutmak için bir değişken oluşturur
        for(User user : users) { // Her bir kullanıcı için döngü
            if(user.getId() > maxId) { // Eğer kullanıcının ID'si mevcut maxId'den büyükse
                maxId = user.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // En büyük ID'nin bir fazlasını döner
    }
}
