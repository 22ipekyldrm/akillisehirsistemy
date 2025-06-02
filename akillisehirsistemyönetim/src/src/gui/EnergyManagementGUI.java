// EnergyManagementGUI Sınıfı
package src.gui;

import src.dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.Model.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// Ana GUI sınıfı
public class EnergyManagementGUI extends JFrame {
    // GUI bileşenleri
     JTable energyTable; // Enerji kaynaklarını göstermek için tablo
    private DefaultTableModel tableModel; // Tablo modeli
    private JButton addButton, editButton, deleteButton; // Ekle, düzenle ve sil butonları
    private EnergySourceDAO energySourceDAO; // Veri erişim nesnesi

    public EnergyManagementGUI() {
        super("Enerji Kaynakları Yönetimi"); // Pencere başlığı
        setSize(700, 500); // Pencere boyutu
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştir
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında yalnızca bu pencereyi kapat

        energySourceDAO = new EnergySourceDAO(); // Veri erişim nesnesini oluştur

        // Tablo Modeli
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Tür", "Kapasite (kW)", "Mevcut Üretim (kW)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücrelerin düzenlenmesini engelle
            }
        };
        energyTable = new JTable(tableModel); // Tabloyu modelle ilişkilendir
        JScrollPane scrollPane = new JScrollPane(energyTable); // Kaydırılabilir alan ekle

        loadEnergySources(); // Enerji kaynaklarını yükle

        // Butonlar oluşturuluyor
        addButton = new JButton("Enerji Kaynağı Ekle");
        editButton = new JButton("Enerji Kaynağı Düzenle");
        deleteButton = new JButton("Enerji Kaynağı Sil");

        // Buton paneli oluşturuluyor
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // GUI bileşenlerini yerleştir
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Buton İşlemleri
        addButton.addActionListener(e -> {
            // Yeni bir enerji kaynağı eklemek için diyalog aç
            EnergySourceDialog dialog = new EnergySourceDialog(
                EnergyManagementGUI.this, "Enerji Kaynağı Ekle", null
            );
            dialog.setVisible(true);
            EnergySource newEnergySource = dialog.getEnergySource();
            if (newEnergySource != null) {
                energySourceDAO.addEnergySource(newEnergySource); // Yeni enerji kaynağını veritabanına ekle
                tableModel.addRow(
                    new Object[]{newEnergySource.getId(), newEnergySource.getType(), newEnergySource.getCapacity(), newEnergySource.getCurrentOutput()}
                );
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = energyTable.getSelectedRow(); // Seçili satırı al
            if(selectedRow >= 0) {
                // Seçili satırdan veri al
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String type = (String) tableModel.getValueAt(selectedRow, 1);
                double capacity = (double) tableModel.getValueAt(selectedRow, 2);
                double currentOutput = (double) tableModel.getValueAt(selectedRow, 3);

                // Mevcut enerji kaynağını düzenlemek için diyalog aç
                EnergySource existingEnergySource = new EnergySource(id, type, capacity, currentOutput);
                EnergySourceDialog dialog = new EnergySourceDialog(
                    EnergyManagementGUI.this, "Enerji Kaynağı Düzenle", existingEnergySource
                );
                dialog.setVisible(true);
                EnergySource updatedEnergySource = dialog.getEnergySource();
                if (updatedEnergySource != null) {
                    // Tabloyu güncelle
                    tableModel.setValueAt(updatedEnergySource.getType(), selectedRow, 1);
                    tableModel.setValueAt(updatedEnergySource.getCapacity(), selectedRow, 2);
                    tableModel.setValueAt(updatedEnergySource.getCurrentOutput(), selectedRow, 3);
                    saveAllEnergySources(); // Değişiklikleri dosyaya kaydet
                }
            } else {
                JOptionPane.showMessageDialog(EnergyManagementGUI.this, "Düzenlemek için bir enerji kaynağı seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = energyTable.getSelectedRow(); // Seçili satırı al
            if(selectedRow >= 0) {
                // Silme işlemini onaylat
                int confirm = JOptionPane.showConfirmDialog(EnergyManagementGUI.this, "Seçili enerji kaynağını silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteEnergySource(id); // Enerji kaynağını sil
                    tableModel.removeRow(selectedRow); // Satırı tablodan kaldır
                }
            } else {
                JOptionPane.showMessageDialog(EnergyManagementGUI.this, "Silmek için bir enerji kaynağı seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true); // GUI'yi görünür yap
    }

    // Enerji kaynaklarını yükler
    private void loadEnergySources() {
        for(EnergySource es : energySourceDAO.getAllEnergySources()) {
            tableModel.addRow(new Object[]{es.getId(), es.getType(), es.getCapacity(), es.getCurrentOutput()});
        }
    }

    // Tüm enerji kaynaklarını dosyaya kaydeder
    private void saveAllEnergySources() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(energySourceDAO.getEnergySourceFile()))) {
            bw.write("id,type,capacity,currentOutput");
            bw.newLine();
            for(int i = 0; i < tableModel.getRowCount(); i++) {
                int id = (int) tableModel.getValueAt(i, 0);
                String type = (String) tableModel.getValueAt(i, 1);
                double capacity = (double) tableModel.getValueAt(i, 2);
                double currentOutput = (double) tableModel.getValueAt(i, 3);
                bw.write(id + "," + type + "," + capacity + "," + currentOutput);
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(EnergyManagementGUI.this, "Enerji kaynağı dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Belirtilen ID'ye sahip enerji kaynağını siler
    private void deleteEnergySource(int id) {
        List<EnergySource> energySources = energySourceDAO.getAllEnergySources();
        energySources.removeIf(es -> es.getId() == id);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(energySourceDAO.ENERGY_SOURCE_FILE))) {
            bw.write("id,type,capacity,currentOutput");
            bw.newLine();
            for(EnergySource es : energySources) {
                bw.write(es.getId() + "," + es.getType() + "," + es.getCapacity() + "," + es.getCurrentOutput());
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(EnergyManagementGUI.this, "Enerji kaynağı dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Enerji kaynağı ekleme ve düzenleme için kullanılan diyalog
    class EnergySourceDialog extends JDialog {
        private JTextField typeField, capacityField, currentOutputField;
        private JButton saveButton, cancelButton;
        private EnergySource energySource;

        public EnergySourceDialog(JFrame parent, String title, EnergySource energySource) {
            super(parent, title, true); // Modal dialog
            setSize(400, 300);
            setLocationRelativeTo(parent);
            this.energySource = energySource;

            // Panel düzenlemesi
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("Tür:"));
            typeField = new JTextField();
            panel.add(typeField);

            panel.add(new JLabel("Kapasite (kW):"));
            capacityField = new JTextField();
            panel.add(capacityField);

            panel.add(new JLabel("Mevcut Üretim (kW):"));
            currentOutputField = new JTextField();
            panel.add(currentOutputField);

            saveButton = new JButton("Kaydet");
            cancelButton = new JButton("İptal");
            panel.add(saveButton);
            panel.add(cancelButton);

            add(panel);

            if (energySource != null) {
                typeField.setText(energySource.getType());
                capacityField.setText(String.valueOf(energySource.getCapacity()));
                currentOutputField.setText(String.valueOf(energySource.getCurrentOutput()));
            }

            // Buton İşlemleri
            saveButton.addActionListener(e -> {
                String type = typeField.getText().trim();
                double capacity;
                double currentOutput;
                try {
                    capacity = Double.parseDouble(capacityField.getText().trim());
                    currentOutput = Double.parseDouble(currentOutputField.getText().trim());
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EnergySourceDialog.this, "Kapasite ve Mevcut Üretim sayısal değerler olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (type.isEmpty()) {
                    JOptionPane.showMessageDialog(EnergySourceDialog.this, "Tür alanı boş bırakılamaz.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (EnergySourceDialog.this.energySource == null) {
                    int newId = energySourceDAO.getNextId(); // Yeni ID oluştur
                    EnergySourceDialog.this.energySource = new EnergySource(newId, type, capacity, currentOutput);
                } else {
                    EnergySourceDialog.this.energySource.setType(type);
                    EnergySourceDialog.this.energySource.setCapacity(capacity);
                    EnergySourceDialog.this.energySource.setCurrentOutput(currentOutput);
                }
                dispose();
            });

            cancelButton.addActionListener(e -> {
                EnergySourceDialog.this.energySource = null; // Kullanıcı işlemi iptal etti
                dispose();
            });
        }

        // Kullanıcının girdisini döndür
        public EnergySource getEnergySource() {
            return energySource;
        }
    }
}
