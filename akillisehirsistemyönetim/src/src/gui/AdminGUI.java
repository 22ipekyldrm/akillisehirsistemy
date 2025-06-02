//AdminGUI Sınıfı
package src.gui; // GUI sınıfının ait olduğu paket
import javax.swing.*; // Swing bileşenlerini içe aktarır
import src.Model.*; // Model sınıflarını içe aktarır
import java.awt.*; // AWT bileşenlerini içe aktarır
import java.awt.event.ActionEvent; // ActionEvent sınıfını içe aktarır
import java.awt.event.ActionListener; // ActionListener sınıfını içe aktarır

public class AdminGUI extends JFrame { // Admin kullanıcıları için GUI sınıfı
    private User adminUser; // Yönetici kullanıcıyı temsil eden değişken

    public AdminGUI(User user) { // Yapıcı metot, admin kullanıcı bilgilerini alır
        super("Yönetici Paneli - " + user.getUsername()); // Pencere başlığını ayarlar
        this.adminUser = user; // Admin kullanıcı bilgisini saklar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pencere kapandığında programı sonlandırır
        setSize(900, 600); // Pencere boyutlarını ayarlar
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştirir

        // Menü Paneli oluşturur
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Menü için panel oluşturur
        JButton manageVehiclesButton = new JButton("Araç Yönetimi"); // Araç yönetimi butonu
        JButton manageBuildingsButton = new JButton("Bina Yönetimi"); // Bina yönetimi butonu
        JButton manageEnergyButton = new JButton("Enerji Kaynakları Yönetimi"); // Enerji yönetimi butonu
        JButton manageReportsButton = new JButton("Raporlar"); // Raporlar butonu
        JButton manageTrafficButton = new JButton("Trafik Yönetimi"); // Trafik yönetimi butonu
        JButton manageWaterButton = new JButton("Su Yönetimi"); // Su yönetimi butonu
        JButton logoutButton = new JButton("Çıkış Yap"); // Çıkış yap butonu

        // Menü paneline butonları ekler
        menuPanel.add(manageVehiclesButton);
        menuPanel.add(manageBuildingsButton);
        menuPanel.add(manageEnergyButton);
        menuPanel.add(manageReportsButton);
        menuPanel.add(manageTrafficButton);
        menuPanel.add(manageWaterButton);
        menuPanel.add(logoutButton);

        // İçerik Paneli oluşturur
        JPanel contentPanel = new JPanel(); // İçerik paneli
        contentPanel.setLayout(new CardLayout()); // CardLayout kullanır

        // Örnek içerik panelleri oluşturur
        JPanel homePanel = new JPanel(); // Ana sayfa paneli
        homePanel.add(new JLabel("Yönetici Paneline Hoş Geldiniz!")); // Mesaj etiketi ekler

        JPanel vehicleManagementPanel = new JPanel(); // Araç yönetimi paneli
        vehicleManagementPanel.add(new JLabel("Araç Yönetimi Sayfası")); // Mesaj etiketi ekler

        JPanel buildingManagementPanel = new JPanel(); // Bina yönetimi paneli
        buildingManagementPanel.add(new JLabel("Bina Yönetimi Sayfası")); // Mesaj etiketi ekler

        JPanel energyManagementPanel = new JPanel(); // Enerji yönetimi paneli
        energyManagementPanel.add(new JLabel("Enerji Kaynakları Yönetimi Sayfası")); // Mesaj etiketi ekler

        JPanel reportsPanel = new JPanel(); // Raporlar paneli
        reportsPanel.add(new JLabel("Raporlar Sayfası")); // Mesaj etiketi ekler

        JPanel trafficManagementPanel = new JPanel(); // Trafik yönetimi paneli
        trafficManagementPanel.add(new JLabel("Trafik Yönetimi Sayfası")); // Mesaj etiketi ekler

        JPanel waterManagementPanel = new JPanel(); // Su yönetimi paneli
        waterManagementPanel.add(new JLabel("Su Yönetimi Sayfası")); // Mesaj etiketi ekler

        // İçerik paneline alt panelleri ekler
        contentPanel.add(homePanel, "Home");
        contentPanel.add(vehicleManagementPanel, "Vehicles");
        contentPanel.add(buildingManagementPanel, "Buildings");
        contentPanel.add(energyManagementPanel, "Energy");
        contentPanel.add(reportsPanel, "Reports");
        contentPanel.add(trafficManagementPanel, "Traffic");
        contentPanel.add(waterManagementPanel, "Water");

        // Menü butonlarına ActionListener ekler
        manageVehiclesButton.addActionListener(e -> { // Araç yönetimi butonuna tıklandığında
            switchPanel(contentPanel, "Vehicles"); // Paneli değiştirir
            new VehicleManagementGUI(); // Araç yönetim GUI'sini açar
        });

        manageBuildingsButton.addActionListener(e -> { // Bina yönetimi butonuna tıklandığında
            switchPanel(contentPanel, "Buildings"); // Paneli değiştirir
            new BuildingManagementGUI(); // Bina yönetim GUI'sini açar
        });

        manageEnergyButton.addActionListener(e -> { // Enerji yönetimi butonuna tıklandığında
            switchPanel(contentPanel, "Energy"); // Paneli değiştirir
            new EnergyManagementGUI(); // Enerji yönetim GUI'sini açar
        });

        manageReportsButton.addActionListener(e -> { // Raporlar butonuna tıklandığında
            switchPanel(contentPanel, "Reports"); // Paneli değiştirir
            new ReportsGUI(); // Raporlar GUI'sini açar
        });

        manageTrafficButton.addActionListener(e -> { // Trafik yönetimi butonuna tıklandığında
            switchPanel(contentPanel, "Traffic"); // Paneli değiştirir
            new TrafficManagementGUI(); // Trafik yönetim GUI'sini açar
        });

        manageWaterButton.addActionListener(e -> { // Su yönetimi butonuna tıklandığında
            switchPanel(contentPanel, "Water"); // Paneli değiştirir
            new WaterManagementGUI(); // Su yönetim GUI'sini açar
        });

        logoutButton.addActionListener(e -> { // Çıkış yap butonuna tıklandığında
            dispose(); // Mevcut pencereyi kapatır
            new LoginGUI(); // Login GUI'sini açar
        });

        add(menuPanel, BorderLayout.NORTH); // Menü panelini pencerenin üst kısmına ekler
        add(contentPanel, BorderLayout.CENTER); // İçerik panelini pencerenin merkezine ekler

        setVisible(true); // Pencereyi görünür hale getirir
    }

    private void switchPanel(JPanel contentPanel, String panelName) { // Paneller arası geçiş yapan metot
        CardLayout cl = (CardLayout) (contentPanel.getLayout()); // CardLayout'u alır
        cl.show(contentPanel, panelName); // Verilen panel adını gösterir
    }
}
