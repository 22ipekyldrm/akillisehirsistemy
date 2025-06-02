//RegistrationGUI Sınıfı
package src.gui;

import src.Model.*; // Model sınıfı
import src.dao.*; // Veri erişim katmanı

import javax.swing.*; // Swing bileşenleri
import java.awt.*; // Layoutlar ve bileşenlerin boyutlandırılması için
import java.awt.event.ActionEvent; // ActionEvent için
import java.awt.event.ActionListener; // ActionListener için
import java.util.List; // Kullanıcı listesi yönetimi için

// Kullanıcı kayıt ekranını temsil eden sınıf
public class RegistrationGUI extends JFrame {
    // GUI bileşenleri
    private JTextField usernameField; // Kullanıcı adı girişi
    private JPasswordField passwordField; // Şifre girişi
    private JComboBox<String> roleComboBox; // Kullanıcı rolü seçimi (Admin veya Citizen)
    private JButton registerButton, cancelButton; // Kayıt ve İptal butonları

    private UserDAO userDAO; // Kullanıcı veritabanı erişim nesnesi

    // Yapıcı metot
    public RegistrationGUI() {
        super("Kayıt Ol"); // Pencere başlığı
        userDAO = new UserDAO(); // UserDAO nesnesi oluştur

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında yalnızca bu ekranı kapat
        setSize(400, 300); // Pencere boyutunu belirle
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştir

        // Ana panel ve layout ayarları
        JPanel panel = new JPanel(new GridBagLayout()); // GridBagLayout ile düzen
        GridBagConstraints gbc = new GridBagConstraints(); // Layout ayarları için yardımcı sınıf

        // Bileşenlerin tanımlanması
        JLabel userLabel = new JLabel("Kullanıcı Adı:"); // Kullanıcı adı etiketi
        JLabel passLabel = new JLabel("Şifre:"); // Şifre etiketi
        JLabel roleLabel = new JLabel("Rol:"); // Rol etiketi

        usernameField = new JTextField(15); // Kullanıcı adı için metin alanı
        passwordField = new JPasswordField(15); // Şifre için gizli metin alanı
        roleComboBox = new JComboBox<>(new String[]{"Citizen", "Admin"}); // Rol seçimi için açılır kutu

        registerButton = new JButton("Kayıt Ol"); // Kayıt ol butonu
        cancelButton = new JButton("İptal"); // İptal butonu

        // Bileşenlerin panele eklenmesi
        gbc.insets = new Insets(10, 10, 10, 10); // Bileşenler arası boşluk
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc); // Kullanıcı adı etiketi ekleniyor

        gbc.gridx = 1;
        panel.add(usernameField, gbc); // Kullanıcı adı girişi ekleniyor

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc); // Şifre etiketi ekleniyor

        gbc.gridx = 1;
        panel.add(passwordField, gbc); // Şifre girişi ekleniyor

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(roleLabel, gbc); // Rol etiketi ekleniyor

        gbc.gridx = 1;
        panel.add(roleComboBox, gbc); // Rol seçimi ekleniyor

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(registerButton, gbc); // Kayıt ol butonu ekleniyor

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(cancelButton, gbc); // İptal butonu ekleniyor

        // Ana panel pencereye ekleniyor
        add(panel);

        // Kayıt butonuna tıklama işlemi
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration(); // Kayıt işlemini başlat
            }
        });

        // İptal butonuna tıklama işlemi
        cancelButton.addActionListener(e -> dispose()); // Pencereyi kapat

        setVisible(true); // Pencereyi görünür yap
    }

    // Kayıt işlemi metodu
    private void handleRegistration() {
        String username = usernameField.getText().trim(); // Kullanıcı adı al ve boşlukları kaldır
        String password = new String(passwordField.getPassword()).trim(); // Şifre al ve boşlukları kaldır
        String role = (String) roleComboBox.getSelectedItem(); // Seçilen rolü al

        // Kullanıcı adı ve şifre boş mu kontrol et
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kullanıcı adı ve şifre boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return; // İşlemi sonlandır
        }

        // Kullanıcı adının benzersiz olup olmadığını kontrol et
        List<User> users = userDAO.getAllUsers(); // Mevcut kullanıcıları getir
        for (User user : users) {
            if (user.getUsername().equals(username)) { // Kullanıcı adı çakışması kontrolü
                JOptionPane.showMessageDialog(this, "Bu kullanıcı adı zaten kullanılıyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                return; // İşlemi sonlandır
            }
        }

        // Yeni kullanıcı ID'si ve hashlenmiş şifre oluştur
        int newId = userDAO.getNextId(); // Yeni ID al
        String hashedPassword = PasswordUtil.hashPassword(password); // Şifreyi hashle
        User newUser = new User(newId, username, hashedPassword, role); // Yeni kullanıcı oluştur
        userDAO.addUser(newUser); // Yeni kullanıcıyı veritabanına ekle

        // Başarılı kayıt mesajı göster ve pencereyi kapat
        JOptionPane.showMessageDialog(this, "Kayıt başarılı! Giriş yapabilirsiniz.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
