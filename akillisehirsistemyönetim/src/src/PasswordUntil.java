package src; // Kodun bulunduğu paketi tanımlar

import java.security.MessageDigest; // Şifreleme için gerekli sınıfı içe aktarır
import java.security.NoSuchAlgorithmException; // Algoritma bulunamazsa fırlatılacak istisna için gerekli sınıfı içe aktarır

class PasswordUtil { // Şifreleme işlemleri için yardımcı sınıf tanımı
    public static String hashPassword(String password) { // Şifreyi hashlemek için statik metot tanımı
        try {
            // SHA-256 algoritmasını kullanarak bir MessageDigest nesnesi oluşturur
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Girilen şifreyi bayt dizisi olarak hashler
            byte[] hashed = md.digest(password.getBytes());
            
            // Hash sonucu hexadecimal (16'lık) formatta bir stringe dönüştürmek için StringBuilder kullanır
            StringBuilder sb = new StringBuilder();
            
            // Her bir baytı hexadecimal formatta stringe ekler
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            
            // Hashlenmiş şifreyi string olarak döner
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { // Algoritma bulunamadığında fırlatılacak istisna yakalanır
            e.printStackTrace(); // Hata ayıklama için istisna detaylarını yazdırır
            return null; // Hata durumunda null döner
        }
    }
}
