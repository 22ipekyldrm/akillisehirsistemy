// src/Main.java
package src; // Kaynak kodun bulunduğu paketi tanımlar

import src.gui.*; // GUI (grafiksel kullanıcı arayüzü) ile ilgili sınıfları içe aktarır
import src.dao.*; // DAO (veri erişim nesnesi) ile ilgili sınıfları içe aktarır

import javax.swing.SwingUtilities; // Swing bileşenlerini iş parçacığı güvenli bir şekilde çalıştırmak için gerekli sınıfı içe aktarır

public class Main { // Ana sınıf tanımı
    public static void main(String[] args) { // Ana metot tanımı
        // UserDAO'yı başlatarak varsayılan kullanıcıların oluşturulmasını sağlar
        new UserDAO();

        // Giriş ekranını başlatır
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}
