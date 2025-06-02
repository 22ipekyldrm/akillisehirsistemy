//ReportDAO Sınıfı
package src.dao; // DAO (Veri Erişim Nesnesi) sınıflarının bulunduğu paket

import src.Model.*; // Report model sınıfını içe aktarır
import java.io.*; // Giriş/çıkış işlemleri için gerekli sınıflar
import java.util.ArrayList; // ArrayList veri yapısını içe aktarır
import java.util.List; // Liste veri yapısını içe aktarır
import javax.swing.JOptionPane; // Kullanıcıya hata mesajları göstermek için gerekli sınıf

public class ReportDAO { // Raporlar için veri erişim sınıfı
    private final String REPORT_FILE = "data/reports.txt"; // Raporların tutulduğu dosya yolu

    public ReportDAO() { // Yapıcı metot
        File file = new File(REPORT_FILE); // Rapor dosyasını temsil eden bir dosya nesnesi oluşturur
        if (!file.exists()) { // Dosya yoksa
            try {
                file.getParentFile().mkdirs(); // Klasör yoksa oluşturur
                file.createNewFile(); // Yeni bir dosya oluşturur
                // Dosyaya başlık satırını ekler
                BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_FILE));
                bw.write("id,type,description,date"); // Başlık bilgilerini yazar
                bw.newLine(); // Yeni satır ekler
                bw.close(); // Yazma işlemini kapatır
            } catch(IOException e) { // Dosya oluşturulamazsa istisna yakalar
                e.printStackTrace(); // Hata detaylarını konsola yazdırır
                JOptionPane.showMessageDialog(null, "Rapor dosyası oluşturulamadı.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
            }
        }
    }

    public List<Report> getAllReports() { // Tüm raporları döndüren metot
        List<Report> reports = new ArrayList<>(); // Raporları tutacak bir liste oluşturur
        try(BufferedReader br = new BufferedReader(new FileReader(REPORT_FILE))) { // Dosyayı okumak için BufferedReader kullanır
            String line;
            br.readLine(); // İlk satırdaki başlığı atlar
            while((line = br.readLine()) != null) { // Dosyadaki her bir satırı okur
                String[] parts = line.split(",", 4); // Satırı virgüllere göre ayırır
                if(parts.length == 4) { // Doğru formatta bir satırsa
                    int id = Integer.parseInt(parts[0]); // ID'yi alır ve tam sayıya dönüştürür
                    String type = parts[1]; // Tip bilgisini alır
                    String description = parts[2]; // Açıklama bilgisini alır
                    String date = parts[3]; // Tarih bilgisini alır
                    reports.add(new Report(id, type, description, date)); // Yeni bir rapor nesnesi oluşturur ve listeye ekler
                }
            }
        } catch(IOException e) { // Dosya okunamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Rapor dosyasına erişilemiyor.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
        return reports; // Okunan raporları döner
    }

    public void addReport(Report report) { // Yeni bir rapor ekleyen metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_FILE, true))) { // Dosyayı yazma modunda açar
            bw.write(report.getId() + "," + report.getType() + "," + report.getDescription() + "," + report.getDate()); // Rapor bilgilerini yazar
            bw.newLine(); // Yeni satır ekler
        } catch(IOException e) { // Dosyaya yazılamazsa istisna yakalar
            e.printStackTrace(); // Hata detaylarını konsola yazdırır
            JOptionPane.showMessageDialog(null, "Rapor eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Kullanıcıya hata mesajı gösterir
        }
    }

    public int getNextId() { // Bir sonraki kullanılabilir ID'yi döndüren metot
        List<Report> reports = getAllReports(); // Tüm raporları alır
        int maxId = 0; // En büyük ID'yi tutmak için bir değişken oluşturur
        for(Report report : reports) { // Her bir rapor için döngü
            if(report.getId() > maxId) { // Eğer raporun ID'si mevcut maxId'den büyükse
                maxId = report.getId(); // maxId'yi günceller
            }
        }
        return maxId + 1; // En büyük ID'nin bir fazlasını döner
    }

    public String getReportFile() { // Henüz uygulanmamış bir metot
        // TODO Otomatik oluşturulan metot iskeleti
        throw new UnsupportedOperationException("Unimplemented method 'getReportFile'"); // Uygulanmadığını belirten bir istisna atar
    }

    // Diğer CRUD işlemleri (Güncelleme, Silme) eklenebilir
}
