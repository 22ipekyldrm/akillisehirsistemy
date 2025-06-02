//CitizenGUI Sınıfı
package src.gui;

import javax.swing.*; // Swing kütüphanesi
import src.Model.*; // Model sınıfı
import java.awt.*; // Layoutlar için
import java.awt.event.ActionEvent; // ActionEvent için
import java.awt.event.ActionListener; // ActionListener için

class CitizenGUI extends JFrame {
    private User citizenUser; // Vatandaş kullanıcı bilgisi

    public CitizenGUI(User user) {
        super("Vatandaş Paneli - " + user.getUsername()); // Başlık ayarı
        this.citizenUser = user; // Kullanıcı bilgisi ataması
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Kapatma işlemi
        setSize(900, 600); // Pencere boyutu
        setLocationRelativeTo(null); // Ekranın ortasına yerleştirme

        // Menü Paneli
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Menü düzeni
        JButton trafficInfoButton = new JButton("Trafik Bilgisi"); // Trafik bilgisi butonu
        JButton energyReportButton = new JButton("Enerji Raporu"); // Enerji raporu butonu
        JButton waterLevelButton = new JButton("Su Seviyesi"); // Su seviyesi butonu
        JButton logoutButton = new JButton("Çıkış Yap"); // Çıkış butonu

        // Menü butonlarını ekleme
        menuPanel.add(trafficInfoButton);
        menuPanel.add(energyReportButton);
        menuPanel.add(waterLevelButton);
        menuPanel.add(logoutButton);

        // İçerik Paneli
        JPanel contentPanel = new JPanel(); // İçerik paneli
        contentPanel.setLayout(new CardLayout()); // CardLayout kullanımı

        // Örnek İçerik Panelleri
        JPanel homePanel = new JPanel(); // Ana sayfa paneli
        homePanel.add(new JLabel("Vatandaş Paneline Hoş Geldiniz!")); // Hoş geldiniz mesajı

        JPanel trafficInfoPanel = new JPanel(); // Trafik bilgisi paneli
        trafficInfoPanel.add(new JLabel("Trafik Bilgisi Sayfası")); // Trafik bilgisi etiketi

        JPanel energyReportPanel = new JPanel(); // Enerji raporu paneli
        energyReportPanel.add(new JLabel("Enerji Raporu Sayfası")); // Enerji raporu etiketi

        JPanel waterLevelPanel = new JPanel(); // Su seviyesi paneli
        waterLevelPanel.add(new JLabel("Su Seviyesi Sayfası")); // Su seviyesi etiketi

        // İçerik panellerini ekleme
        contentPanel.add(homePanel, "Home");
        contentPanel.add(trafficInfoPanel, "TrafficInfo");
        contentPanel.add(energyReportPanel, "EnergyReport");
        contentPanel.add(waterLevelPanel, "WaterLevel");

        // Menü Butonlarına ActionListener Ekleme
        trafficInfoButton.addActionListener(e -> {
            switchPanel(contentPanel, "TrafficInfo"); // Trafik paneline geçiş
            new TrafficManagementGUI(); // Trafik Bilgisi penceresini aç
        });

        energyReportButton.addActionListener(e -> {
            switchPanel(contentPanel, "EnergyReport"); // Enerji raporu paneline geçiş
            new ReportsGUI(); // Enerji Raporu penceresini aç
        });

        waterLevelButton.addActionListener(e -> {
            switchPanel(contentPanel, "WaterLevel"); // Su seviyesi paneline geçiş
            new WaterManagementGUI(); // Su Seviyesi penceresini aç
        });

        logoutButton.addActionListener(e -> {
            dispose(); // Mevcut pencereyi kapat
            new LoginGUI(); // Giriş ekranını aç
        });

        // Menü ve içerik panellerini pencereye ekleme
        add(menuPanel, BorderLayout.NORTH); // Menü paneli üstte
        add(contentPanel, BorderLayout.CENTER); // İçerik paneli ortada

        setVisible(true); // Pencereyi görünür yap
    }

    private void switchPanel(JPanel contentPanel, String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout()); // CardLayout referansı
        cl.show(contentPanel, panelName); // Belirtilen paneli göster
    }
}
