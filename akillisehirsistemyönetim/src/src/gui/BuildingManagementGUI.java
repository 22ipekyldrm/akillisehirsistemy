//BuildingManagementGUI Sınıfı
package src.gui; // GUI sınıfının bulunduğu paket
import src.dao.*; // DAO sınıflarını içe aktarır
import javax.swing.*; // Swing bileşenlerini içe aktarır
import javax.swing.table.DefaultTableModel; // Tablo modeli için sınıfı içe aktarır
import src.Model.*; // Model sınıflarını içe aktarır
import java.awt.*; // AWT bileşenlerini içe aktarır
import java.awt.event.*; // AWT olaylarını içe aktarır
import java.util.List; // List sınıfını içe aktarır
import java.io.BufferedWriter; // Dosya yazımı için BufferedWriter sınıfını içe aktarır
import java.io.FileWriter; // Dosya yazımı için FileWriter sınıfını içe aktarır
import java.io.IOException; // IO hatalarını işlemek için sınıfı içe aktarır

public class BuildingManagementGUI extends JFrame { // Bina yönetimi için GUI sınıfı
    private JTable buildingTable; // Bina verilerini göstermek için tablo
    private DefaultTableModel tableModel; // Tablo modeli
    private JButton addButton, editButton, deleteButton; // İşlem butonları
    private BuildingDAO buildingDAO; // Bina verilerine erişim sağlayan DAO

    public BuildingManagementGUI() { // Yapıcı metot
        super("Bina Yönetimi"); // Pencere başlığını ayarlar
        setSize(700, 500); // Pencere boyutlarını ayarlar
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştirir
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında sadece bu pencereyi kapatır

        buildingDAO = new BuildingDAO(); // BuildingDAO nesnesi oluşturur

        // Tablo Modeli oluşturur
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tür", "Kat Sayısı", "Enerji Verimliliği"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücrelerin düzenlenmesini engeller
            }
        };
        buildingTable = new JTable(tableModel); // Tabloyu oluşturur
        JScrollPane scrollPane = new JScrollPane(buildingTable); // Tabloyu kaydırılabilir yapar

        loadBuildings(); // Tabloya bina verilerini yükler

        // İşlem Butonlarını oluşturur
        addButton = new JButton("Bina Ekle"); // Bina ekleme butonu
        editButton = new JButton("Bina Düzenle"); // Bina düzenleme butonu
        deleteButton = new JButton("Bina Sil"); // Bina silme butonu

        JPanel buttonPanel = new JPanel(); // Butonlar için panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER); // Tabloyu pencerenin merkezine ekler
        add(buttonPanel, BorderLayout.SOUTH); // Butonları pencerenin altına ekler

        // Buton İşlemleri
        addButton.addActionListener(e -> { // Bina ekleme butonuna tıklandığında
            BuildingDialog dialog = new BuildingDialog(BuildingManagementGUI.this, "Bina Ekle", null);
            dialog.setVisible(true);
            Building newBuilding = dialog.getBuilding(); // Yeni bina bilgilerini alır
            if (newBuilding != null) {
                buildingDAO.addBuilding(newBuilding); // Bina bilgilerini DAO'ya ekler
                tableModel.addRow(new Object[]{newBuilding.getId(), newBuilding.getType(), newBuilding.getFloors(), newBuilding.getEnergyEfficiency()}); // Tabloya ekler
            }
        });

        editButton.addActionListener(e -> { // Bina düzenleme butonuna tıklandığında
            int selectedRow = buildingTable.getSelectedRow(); // Seçili satırı alır
            if(selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0); // Seçili binanın ID'sini alır
                String type = (String) tableModel.getValueAt(selectedRow, 1); // Seçili binanın türünü alır
                int floors = (int) tableModel.getValueAt(selectedRow, 2); // Seçili binanın kat sayısını alır
                double energyEfficiency = (double) tableModel.getValueAt(selectedRow, 3); // Seçili binanın enerji verimliliğini alır

                Building existingBuilding = new Building(id, type, floors, energyEfficiency); // Mevcut bina nesnesini oluşturur
                BuildingDialog dialog = new BuildingDialog(BuildingManagementGUI.this, "Bina Düzenle", existingBuilding); // Düzenleme dialogunu açar
                dialog.setVisible(true);
                Building updatedBuilding = dialog.getBuilding(); // Güncellenmiş bina bilgilerini alır
                if (updatedBuilding != null) {
                    // Mevcut satırı günceller
                    tableModel.setValueAt(updatedBuilding.getType(), selectedRow, 1);
                    tableModel.setValueAt(updatedBuilding.getFloors(), selectedRow, 2);
                    tableModel.setValueAt(updatedBuilding.getEnergyEfficiency(), selectedRow, 3);
                    saveAllBuildings(); // Dosyayı yeniden yazar
                }
            } else {
                JOptionPane.showMessageDialog(BuildingManagementGUI.this, "Düzenlemek için bir bina seçin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
            }
        });

        deleteButton.addActionListener(e -> { // Bina silme butonuna tıklandığında
            int selectedRow = buildingTable.getSelectedRow(); // Seçili satırı alır
            if(selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(BuildingManagementGUI.this, "Seçili binayı silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION); // Onay kutusu
                if(confirm == JOptionPane.YES_OPTION) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0); // Silinecek binanın ID'sini alır
                    deleteBuilding(id); // Binayı siler
                    tableModel.removeRow(selectedRow); // Satırı tablodan kaldırır
                }
            } else {
                JOptionPane.showMessageDialog(BuildingManagementGUI.this, "Silmek için bir bina seçin.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
            }
        });

        setVisible(true); // Pencereyi görünür hale getirir
    }

    private void loadBuildings() { // Tüm binaları tabloya yükleyen metot
        for(Building building : buildingDAO.getAllBuildings()) {
            tableModel.addRow(new Object[]{building.getId(), building.getType(), building.getFloors(), building.getEnergyEfficiency()});
        }
    }

    private void saveAllBuildings() { // Tüm binaları dosyaya yeniden yazan metot
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(buildingDAO.getBuildingFile()))) {
            bw.write("id,type,floors,energyEfficiency"); // Başlık satırı
            bw.newLine();
            for(int i = 0; i < tableModel.getRowCount(); i++) {
                int id = (int) tableModel.getValueAt(i, 0); // ID sütunu
                String type = (String) tableModel.getValueAt(i, 1); // Tür sütunu
                int floors = (int) tableModel.getValueAt(i, 2); // Kat sayısı sütunu
                double energyEfficiency = (double) tableModel.getValueAt(i, 3); // Enerji verimliliği sütunu
                bw.write(id + "," + type + "," + floors + "," + energyEfficiency); // Satırı dosyaya yazar
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace(); // Hata ayıklama için istisnayı yazdırır
            JOptionPane.showMessageDialog(BuildingManagementGUI.this, "Bina dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
        }
    }

    private void deleteBuilding(int id) { // Binayı silen metot
        List<Building> buildings = buildingDAO.getAllBuildings(); // Tüm bina verilerini alır
        buildings.removeIf(b -> b.getId() == id); // Silinecek binayı listeden kaldırır
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(buildingDAO.BUILDING_FILE))) {
            bw.write("id,type,floors,energyEfficiency"); // Başlık satırı
            bw.newLine();
            for(Building b : buildings) {
                bw.write(b.getId() + "," + b.getType() + "," + b.getFloors() + "," + b.getEnergyEfficiency()); // Kalan binaları dosyaya yazar
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace(); // Hata ayıklama için istisnayı yazdırır
            JOptionPane.showMessageDialog(BuildingManagementGUI.this, "Bina dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
        }
    }

    // Bina Ekleme/Düzenleme Dialog'u
    class BuildingDialog extends JDialog { // Yeni veya düzenlenmiş bina bilgilerini almak için dialog
        private JTextField typeField, floorsField, energyField; // Kullanıcı giriş alanları
        private JButton saveButton, cancelButton; // Kaydet ve iptal butonları
        private Building building; // Dialog'da kullanılan bina nesnesi

        public BuildingDialog(JFrame parent, String title, Building building) { // Yapıcı metot
            super(parent, title, true); // Üst pencereyi ayarlar
            setSize(400, 300); // Dialog boyutlarını ayarlar
            setLocationRelativeTo(parent); // Üst pencerenin ortasına yerleştirir
            this.building = building;

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)); // Giriş alanları için panel
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Kenarlık boşluğu ekler

            panel.add(new JLabel("Tür:")); // Tür etiketi
            typeField = new JTextField(); // Tür girişi
            panel.add(typeField);

            panel.add(new JLabel("Kat Sayısı:")); // Kat sayısı etiketi
            floorsField = new JTextField(); // Kat sayısı girişi
            panel.add(floorsField);

            panel.add(new JLabel("Enerji Verimliliği:")); // Enerji verimliliği etiketi
            energyField = new JTextField(); // Enerji verimliliği girişi
            panel.add(energyField);

            saveButton = new JButton("Kaydet"); // Kaydet butonu
            cancelButton = new JButton("İptal"); // İptal butonu
            panel.add(saveButton);
            panel.add(cancelButton);

            add(panel); // Paneli dialog'a ekler

            if (building != null) { // Eğer bina düzenleniyorsa mevcut bilgileri doldurur
                typeField.setText(building.getType());
                floorsField.setText(String.valueOf(building.getFloors()));
                energyField.setText(String.valueOf(building.getEnergyEfficiency()));
            }

            // Buton İşlemleri
            saveButton.addActionListener(e -> { // Kaydet butonuna tıklandığında
                String type = typeField.getText().trim(); // Tür bilgisini alır
                int floors; // Kat sayısını tutar
                double energyEfficiency; // Enerji verimliliğini tutar
                try {
                    floors = Integer.parseInt(floorsField.getText().trim()); // Kat sayısını sayısal değere dönüştürür
                    energyEfficiency = Double.parseDouble(energyField.getText().trim()); // Enerji verimliliğini sayısal değere dönüştürür
                } catch(NumberFormatException ex) { // Eğer hata olursa
                    JOptionPane.showMessageDialog(BuildingDialog.this, "Kat Sayısı ve Enerji Verimliliği sayısal değerler olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
                    return;
                }

                if (type.isEmpty()) { // Eğer tür boşsa
                    JOptionPane.showMessageDialog(BuildingDialog.this, "Tür alanı boş bırakılamaz.", "Hata", JOptionPane.ERROR_MESSAGE); // Hata mesajı
                    return;
                }

                if (BuildingDialog.this.building == null) { // Eğer bina yeni oluşturuluyorsa
                    int newId = buildingDAO.getNextId(); // Yeni ID oluşturur
                    BuildingDialog.this.building = new Building(newId, type, floors, energyEfficiency); // Yeni bina nesnesi oluşturur
                } else { // Eğer bina düzenleniyorsa
                    BuildingDialog.this.building.setType(type);
                    BuildingDialog.this.building.setFloors(floors);
                    BuildingDialog.this.building.setEnergyEfficiency(energyEfficiency);
                }
                dispose(); // Dialog'u kapatır
            });

            cancelButton.addActionListener(e -> { // İptal butonuna tıklandığında
                BuildingDialog.this.building = null; // Bina bilgilerini temizler
                dispose(); // Dialog'u kapatır
            });
        }

        public Building getBuilding() { // Binayı döndüren metot
            return building;
        }
    }
}
