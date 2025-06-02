// LoginGUI Sınıfı
package src.gui; // GUI paketinin içine dahil

import javax.swing.*; // Swing kütüphanesi için import
import src.Model.*; // Model sınıfları için import
import src.dao.*; // DAO sınıfları için import

import java.awt.*; // Layout ve GUI bileşenleri için
import java.awt.event.ActionEvent; // ActionEvent sınıfı için
import java.awt.event.ActionListener; // ActionListener arabirimi için

// LoginGUI sınıfı
public class LoginGUI extends JFrame {
    private JTextField usernameField; // Kullanıcı adı girişi için text field
    private JPasswordField passwordField; // Şifre girişi için password field
    private JButton loginButton, registerButton; // Giriş ve kayıt butonları

    private UserDAO userDAO; // Kullanıcı verilerini yönetmek için DAO sınıfı

    // LoginGUI constructor
    public LoginGUI() {
        super("Akıllı Şehir Yönetim Sistemi - Giriş"); // Pencere başlığı
        userDAO = new UserDAO(); // UserDAO nesnesi oluşturma

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Çıkış işlemi
        setSize(400, 250); // Pencere boyutu
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştirme

        // Panel ve düzen ayarları
        JPanel panel = new JPanel(new GridBagLayout()); // GridBagLayout kullanımı
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagLayout için ayarlar

        // Kullanıcı adı ve şifre etiketleri
        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        JLabel passLabel = new JLabel("Şifre:");

        // Kullanıcı adı ve şifre alanları
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // Butonlar
        loginButton = new JButton("Giriş Yap");
        registerButton = new JButton("Kayıt Ol");

        // GridBagLayout konumlandırma
        gbc.insets = new Insets(10, 10, 10, 10); // Bileşenler arası boşluklar
        gbc.gridx = 0; // İlk sütun
        gbc.gridy = 0; // İlk satır
        panel.add(userLabel, gbc); // Kullanıcı adı etiketini ekle

        gbc.gridx = 1; // İkinci sütun
        panel.add(usernameField, gbc); // Kullanıcı adı alanını ekle

        gbc.gridx = 0; // İlk sütun
        gbc.gridy = 1; // İkinci satır
        panel.add(passLabel, gbc); // Şifre etiketini ekle

        gbc.gridx = 1; // İkinci sütun
        panel.add(passwordField, gbc); // Şifre alanını ekle

        gbc.gridx = 1; // İkinci sütun
        gbc.gridy = 2; // Üçüncü satır
        gbc.anchor = GridBagConstraints.EAST; // Sağ hizalama
        panel.add(loginButton, gbc); // Giriş butonunu ekle

        gbc.gridx = 1; // İkinci sütun
        gbc.gridy = 3; // Dördüncü satır
        panel.add(registerButton, gbc); // Kayıt butonunu ekle

        add(panel); // Paneli pencereye ekle

        // Giriş butonuna ActionListener ekleme
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin(); // Giriş işlemini çağır
            }
        });

        // Kayıt butonuna ActionListener ekleme
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationGUI(); // Kayıt ekranını aç
            }
        });

        setVisible(true); // Pencereyi görünür yap
    }

    // Giriş işlemi
    private void handleLogin() {
        String username = usernameField.getText().trim(); // Kullanıcı adını al
        String password = new String(passwordField.getPassword()).trim(); // Şifreyi al

        // Kullanıcı adı veya şifre boşsa hata mesajı göster
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kullanıcı adı ve şifre boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kullanıcıyı doğrula
        User user = userDAO.authenticateUser(username, password);

        // Eğer kullanıcı geçerliyse
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Giriş Başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Giriş penceresini kapat
            if (user.getRole().equalsIgnoreCase("Admin")) { // Admin kullanıcıysa
                new AdminGUI(user); // Admin ekranını aç
            } else { // Vatandaş kullanıcıysa
                new CitizenGUI(user); // Vatandaş ekranını aç
            }
        } else {
            // Geçersiz kullanıcı adı veya şifre
            JOptionPane.showMessageDialog(this, "Geçersiz kullanıcı adı veya şifre.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
