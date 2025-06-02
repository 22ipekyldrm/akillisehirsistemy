// VehicleManagementGUI Sınıfı
package src.gui; // GUI (Arayüz) ile ilgili paket

import src.dao.*; // DAO sınıfı: Veri tabanı veya dosya işlemleri için gerekli sınıflar
import src.Model.*; // Model sınıfı: Araç verilerini temsil eder

import javax.swing.*; // Swing bileşenleri: GUI elemanları için
import javax.swing.table.DefaultTableModel; // Tablo yapısı ve modeli
import java.awt.*; // Layout ve GUI yapılandırma için
import java.awt.event.*; // Olay dinleyicileri
import java.io.BufferedWriter; // Dosya yazma işlemleri için
import java.io.FileWriter; // Dosya yazma işlemleri için
import java.io.IOException; // Hata yakalamaları için
import java.util.List; // Liste yönetimi için

// Ana sınıf: Araç yönetimi ekranını temsil eder
public class VehicleManagementGUI extends JFrame {
    private JTable vehicleTable; // Araç bilgilerini gösteren tablo
    private DefaultTableModel tableModel; // Tablonun veri modeli
    private JButton addButton, editButton, deleteButton; // Ekleme, düzenleme ve silme butonları
    private VehicleDAO vehicleDAO; // Araç veri erişim nesnesi

    // Yapıcı metot: GUI'yi başlatır
    public VehicleManagementGUI() {
        super("Araç Yönetimi"); // Pencere başlığı
        setSize(700, 500); // Pencere boyutları
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştir
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında sadece bu ekranı kapat

        vehicleDAO = new VehicleDAO(); // DAO nesnesi oluşturulur

        // Tablo modeli oluşturuluyor
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tür", "Plaka", "Durum"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücrelerin düzenlenmesini engeller
            }
        };
        vehicleTable = new JTable(tableModel); // Tablo oluşturulur
        JScrollPane scrollPane = new JScrollPane(vehicleTable); // Tablonun kaydırılabilir olması sağlanır

        loadVehicles(); // Veritabanından araç bilgileri yüklenir

        // Butonlar oluşturuluyor
        addButton = new JButton("Araç Ekle"); // Araç ekleme butonu
        editButton = new JButton("Araç Düzenle"); // Araç düzenleme butonu
        deleteButton = new JButton("Araç Sil"); // Araç silme butonu

        JPanel buttonPanel = new JPanel(); // Butonların ekleneceği panel
        buttonPanel.add(addButton); // Ekle butonu panele eklenir
        buttonPanel.add(editButton); // Düzenle butonu panele eklenir
        buttonPanel.add(deleteButton); // Sil butonu panele eklenir

        add(scrollPane, BorderLayout.CENTER); // Tablo merkezi konuma eklenir
        add(buttonPanel, BorderLayout.SOUTH); // Butonlar alt bölüme eklenir

        // Ekleme butonunun işlem dinleyicisi
        addButton.addActionListener(e -> {
            VehicleDialog dialog = new VehicleDialog(VehicleManagementGUI.this, "Araç Ekle", null); // Yeni araç ekleme dialog'u açılır
            dialog.setVisible(true); // Dialog görünür yapılır
            Vehicle newVehicle = dialog.getVehicle(); // Yeni araç bilgisi alınır
            if (newVehicle != null) { // Eğer bilgi mevcutsa
                vehicleDAO.addVehicle(newVehicle); // Veritabanına eklenir
                tableModel.addRow(new Object[]{newVehicle.getId(), newVehicle.getType(), newVehicle.getLicensePlate(), newVehicle.getStatus()}); // Tabloya eklenir
            }
        });

        // Düzenleme butonunun işlem dinleyicisi
        editButton.addActionListener(e -> {
            int selectedRow = vehicleTable.getSelectedRow(); // Seçilen satır
            if (selectedRow >= 0) { // Eğer bir satır seçilmişse
                int id = (int) tableModel.getValueAt(selectedRow, 0); // ID alınır
                String type = (String) tableModel.getValueAt(selectedRow, 1); // Tür bilgisi alınır
                String licensePlate = (String) tableModel.getValueAt(selectedRow, 2); // Plaka bilgisi alınır
                String status = (String) tableModel.getValueAt(selectedRow, 3); // Durum bilgisi alınır

                Vehicle existingVehicle = new Vehicle(id, type, licensePlate, status); // Mevcut araç bilgisi oluşturulur
                VehicleDialog dialog = new VehicleDialog(VehicleManagementGUI.this, "Araç Düzenle", existingVehicle); // Düzenleme dialog'u açılır
                dialog.setVisible(true); // Dialog görünür yapılır
                Vehicle updatedVehicle = dialog.getVehicle(); // Güncellenmiş bilgi alınır
                if (updatedVehicle != null) { // Eğer bilgi mevcutsa
                    tableModel.setValueAt(updatedVehicle.getType(), selectedRow, 1); // Tablo güncellenir
                    tableModel.setValueAt(updatedVehicle.getLicensePlate(), selectedRow, 2);
                    tableModel.setValueAt(updatedVehicle.getStatus(), selectedRow, 3);
                    saveAllVehicles(); // Tüm araçlar dosyaya kaydedilir
                }
            } else {
                JOptionPane.showMessageDialog(VehicleManagementGUI.this, "Düzenlemek için bir araç seçin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı gösterilir
            }
        });

        // Silme butonunun işlem dinleyicisi
        deleteButton.addActionListener(e -> {
            int selectedRow = vehicleTable.getSelectedRow(); // Seçilen satır
            if (selectedRow >= 0) { // Eğer bir satır seçilmişse
                int confirm = JOptionPane.showConfirmDialog(VehicleManagementGUI.this, "Seçili aracı silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION); // Silme işlemi onaylatılır
                if (confirm == JOptionPane.YES_OPTION) { // Eğer onaylanmışsa
                    int id = (int) tableModel.getValueAt(selectedRow, 0); // ID alınır
                    deleteVehicle(id); // Araç silinir
                    tableModel.removeRow(selectedRow); // Tablo güncellenir
                }
            } else {
                JOptionPane.showMessageDialog(VehicleManagementGUI.this, "Silmek için bir araç seçin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı gösterilir
            }
        });

        setVisible(true); // Pencere görünür yapılır
    }

    // Araç bilgilerini yükleyen metot
    private void loadVehicles() {
        for (Vehicle vehicle : vehicleDAO.getAllVehicles()) { // Tüm araçlar alınır
            tableModel.addRow(new Object[]{vehicle.getId(), vehicle.getType(), vehicle.getLicensePlate(), vehicle.getStatus()}); // Tabloya eklenir
        }
    }

    // Tüm araçları dosyaya kaydeden metot
    private void saveAllVehicles() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(vehicleDAO.getVehicleFile()))) { // Dosya yazma işlemi
            bw.write("id,type,licensePlate,status"); // Başlık yazılır
            bw.newLine();
            for (int i = 0; i < tableModel.getRowCount(); i++) { // Tablodaki tüm veriler döngüyle alınır
                int id = (int) tableModel.getValueAt(i, 0);
                String type = (String) tableModel.getValueAt(i, 1);
                String licensePlate = (String) tableModel.getValueAt(i, 2);
                String status = (String) tableModel.getValueAt(i, 3);
                bw.write(id + "," + type + "," + licensePlate + "," + status); // Veriler yazılır
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Hata ayıklama çıktısı
            JOptionPane.showMessageDialog(VehicleManagementGUI.this, "Araç dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
        }
    }

    // Araç silme metodu
  
        private void deleteVehicle(int id) {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles(); // Mevcut araçlar alınır
            vehicles.removeIf(v -> v.getId() == id); // Silinecek aracın ID'sine göre listeden çıkarılır
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(vehicleDAO.VEHICLE_FILE))) { // Dosya yazma işlemi
                bw.write("id,type,licensePlate,status"); // Başlık satırı
                bw.newLine();
                for (Vehicle v : vehicles) { // Kalan araçlar dosyaya tekrar yazılır
                    bw.write(v.getId() + "," + v.getType() + "," + v.getLicensePlate() + "," + v.getStatus()); // Veriler yazılır
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Hata çıktısı
                JOptionPane.showMessageDialog(VehicleManagementGUI.this, "Araç dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
            }
        }

        // Araç Ekleme/Düzenleme Dialog'u
        class VehicleDialog extends JDialog {
            private JTextField typeField, licensePlateField; // Tür ve plaka giriş alanları
            private JComboBox<String> statusComboBox; // Durum seçimi için combo box
            private JButton saveButton, cancelButton; // Kaydet ve iptal butonları
            private Vehicle vehicle; // Araç bilgisi

            // Dialog oluşturucu
            public VehicleDialog(JFrame parent, String title, Vehicle vehicle) {
                super(parent, title, true); // Dialog üst sınıf yapıcı çağrısı
                setSize(400, 300); // Dialog boyutu
                setLocationRelativeTo(parent); // Dialog, parent pencerenin ortasında açılır
                this.vehicle = vehicle; // Mevcut araç bilgisi atanır

                JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)); // Giriş alanları için panel oluşturulur
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Panel kenarlık boşlukları

                panel.add(new JLabel("Tür:")); // Tür etiketi
                typeField = new JTextField(); // Tür giriş alanı
                panel.add(typeField); // Panele eklenir

                panel.add(new JLabel("Plaka:")); // Plaka etiketi
                licensePlateField = new JTextField(); // Plaka giriş alanı
                panel.add(licensePlateField); // Panele eklenir

                panel.add(new JLabel("Durum:")); // Durum etiketi
                statusComboBox = new JComboBox<>(new String[]{"Aktif", "Bakımda"}); // Durum seçenekleri
                panel.add(statusComboBox); // Panele eklenir

                saveButton = new JButton("Kaydet"); // Kaydet butonu
                cancelButton = new JButton("İptal"); // İptal butonu
                panel.add(saveButton); // Panele eklenir
                panel.add(cancelButton); // Panele eklenir

                add(panel); // Panel dialog'a eklenir

                // Eğer araç düzenleniyorsa, mevcut değerler alanlara doldurulur
                if (vehicle != null) {
                    typeField.setText(vehicle.getType()); // Tür alanı doldurulur
                    licensePlateField.setText(vehicle.getLicensePlate()); // Plaka alanı doldurulur
                    statusComboBox.setSelectedItem(vehicle.getStatus()); // Durum alanı seçilir
                }

                // Kaydet butonu işlem dinleyicisi
                saveButton.addActionListener(e -> {
                    String type = typeField.getText().trim(); // Tür alanı değeri
                    String licensePlate = licensePlateField.getText().trim(); // Plaka alanı değeri
                    String status = (String) statusComboBox.getSelectedItem(); // Durum seçimi

                    if (type.isEmpty() || licensePlate.isEmpty()) { // Alanlar boşsa hata mesajı
                        JOptionPane.showMessageDialog(VehicleDialog.this, "Tür ve Plaka alanları boş olamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return; // İşlem sonlandırılır
                    }

                    if (VehicleDialog.this.vehicle == null) { // Yeni araç ekleniyorsa
                        int newId = vehicleDAO.getNextId(); // Yeni bir ID oluşturulur
                        VehicleDialog.this.vehicle = new Vehicle(newId, type, licensePlate, status); // Yeni araç nesnesi oluşturulur
                    } else { // Mevcut araç düzenleniyorsa
                        VehicleDialog.this.vehicle.setType(type); // Tür güncellenir
                        VehicleDialog.this.vehicle.setLicensePlate(licensePlate); // Plaka güncellenir
                        VehicleDialog.this.vehicle.setStatus(status); // Durum güncellenir
                    }
                    dispose(); // Dialog kapatılır
                });

                // İptal butonu işlem dinleyicisi
                cancelButton.addActionListener(e -> {
                    VehicleDialog.this.vehicle = null; // Araç bilgisi temizlenir
                    dispose(); // Dialog kapatılır
                });
            }

            // Kullanıcı tarafından girilen aracı döner
            public Vehicle getVehicle() {
                return vehicle;
            }
        }
    }
