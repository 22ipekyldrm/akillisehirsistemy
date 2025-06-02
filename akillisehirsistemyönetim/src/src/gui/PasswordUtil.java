// PassWordUtil Sınıfı
package src.gui;

import java.security.MessageDigest; // Şifreleme için gerekli sınıf
import java.security.NoSuchAlgorithmException; // Algoritma hatası için gerekli sınıf

// Şifreleme işlemleri için yardımcı sınıf
public class PasswordUtil {

    // Şifreyi SHA-256 algoritmasıyla hashlemek için
    public static String hashPassword(String password) {
        try {
            // SHA-256 algoritması için MessageDigest nesnesi oluştur
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Şifreyi byte dizisine çevirip hash işlemini gerçekleştir
            byte[] hashedBytes = md.digest(password.getBytes());

            // Hash sonucu byte dizisini hex (16'lık) formata çevir
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                // Her byte'ı iki haneli hexadecimal stringe çevir
                sb.append(String.format("%02x", b));
            }

            // Hashlenmiş şifreyi string olarak döndür
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Eğer algoritma bulunamazsa hata mesajını yazdır
            e.printStackTrace();

            // Hata durumunda bir RuntimeException fırlat
            throw new RuntimeException("Hashleme işlemi başarısız oldu!");
        }
    }
}
