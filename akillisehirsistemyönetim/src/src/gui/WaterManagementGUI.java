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
public class WaterManagementGUI extends JFrame {
    private JTable waterTable; // Su verilerini göstermek için tablo
    private DefaultTableModel tableModel; // Tabloyu yönetmek için model
    private JButton addButton, editButton, deleteButton, alertButton; // Ekle, düzenle, sil ve uyarı butonları
    private WaterDAO waterDAO; // Su verilerini işlemek için DAO (Data Access Object)

    private final double threshold = 100; // Eşik değeri (örneğin 100 m)

    public WaterManagementGUI() {
        super("Su Yönetimi");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        waterDAO = new WaterDAO(); // DAO nesnesini başlat

        // Tablo Modeli
        tableModel = new DefaultTableModel(new Object[]{"ID", "Konum", "Seviye (m)", "Tarih"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücrelerin düzenlenmesini engelle
            }
        };
        waterTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(waterTable);

        loadWater(); // Mevcut su verilerini yükle

        // Butonlar
        addButton = new JButton("Su Ekle");
        editButton = new JButton("Su Düzenle");
        deleteButton = new JButton("Su Sil");
        alertButton = new JButton("Eşik Altı Uyarısı");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(alertButton); // Uyarı butonunu ekle

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Buton İşlemleri
        addButton.addActionListener(e -> {
            // Kullanıcıya su eklemek için form alanları gösterebiliriz.
            addWaterForm(); 
        });

        editButton.addActionListener(e -> {
            // Kullanıcıya suyu düzenlemek için form alanları gösterebiliriz.
            editWaterForm(); 
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = waterTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(WaterManagementGUI.this, "Seçili suyu silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteWater(id);
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(WaterManagementGUI.this, "Silmek için bir su seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        alertButton.addActionListener(e -> {
            EsikAltiUyarisi(); // Eşik altı uyarısı metodu çağrılır
        });

        setVisible(true);
    }

    private void loadWater() {
        for (Water water : waterDAO.getAllWater()) {
            tableModel.addRow(new Object[]{water.getId(), water.getLocation(), water.getLevel(), water.getDate()});
        }
    }

    private void saveAllWater() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(waterDAO.getWaterFile()))) {
            bw.write("id,location,level,date");
            bw.newLine();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int id = (int) tableModel.getValueAt(i, 0);
                String location = (String) tableModel.getValueAt(i, 1);
                double level = (double) tableModel.getValueAt(i, 2);
                String date = (String) tableModel.getValueAt(i, 3);
                bw.write(id + "," + location + "," + level + "," + date);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WaterManagementGUI.this, "Su dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWater(int id) {
        List<Water> waterList = waterDAO.getAllWater();
        waterList.removeIf(w -> w.getId() == id);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(waterDAO.getWaterFile()))) {
            bw.write("id,location,level,date");
            bw.newLine();
            for (Water w : waterList) {
                bw.write(w.getId() + "," + w.getLocation() + "," + w.getLevel() + "," + w.getDate());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(WaterManagementGUI.this, "Su dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Su Tüketimi ve Rezervi Hesaplama
    public void SuTuketimiveRezervi() {
        double totalLevel = 0;
        int count = tableModel.getRowCount();

        for (int i = 0; i < count; i++) {
            double level = (double) tableModel.getValueAt(i, 2); // Su seviyesini al
            totalLevel += level; // Toplam seviye hesapla
        }

        double averageLevel = totalLevel / count; // Ortalama seviye

        // Rezerv hakkında bilgi verir
        JOptionPane.showMessageDialog(this, "Toplam su seviyesi: " + totalLevel + " m\nOrtalama seviye: " + averageLevel + " m", "Su Tüketimi ve Rezervi", JOptionPane.INFORMATION_MESSAGE);
    }

    // Eşik Altı Uyarısı
    public void EsikAltiUyarisi() {
        boolean thresholdBreached = false;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double level = (double) tableModel.getValueAt(i, 2);
            if (level < threshold) {
                thresholdBreached = true;
                String location = (String) tableModel.getValueAt(i, 1);
                JOptionPane.showMessageDialog(this, location + " konumundaki su seviyesi, eşik değerinin altına düştü! Mevcut Seviye: " + level + " m", "Eşik Altı Uyarısı", JOptionPane.WARNING_MESSAGE);
            }
        }

        if (!thresholdBreached) {
            JOptionPane.showMessageDialog(this, "Su seviyeleri normal seviyelerde.", "Eşik Altı Uyarısı", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Yeni Su Ekleme Formu
    public void addWaterForm() {
        // Yeni su eklemek için gerekli bilgileri almak için text fields oluşturulabilir.
        JTextField locationField = new JTextField();
        JTextField levelField = new JTextField();
        JTextField dateField = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Konum:"));
        panel.add(locationField);
        panel.add(new JLabel("Seviye (m):"));
        panel.add(levelField);
        panel.add(new JLabel("Tarih (YYYY-MM-DD):"));
        panel.add(dateField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Yeni Su Ekle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String location = locationField.getText().trim();
            double level;
            String date = dateField.getText().trim();

            try {
                level = Double.parseDouble(levelField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Seviye sayısal bir değer olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (location.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Tarih formatı YYYY-MM-DD olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int newId = waterDAO.getNextId();
            Water newWater = new Water(newId, location, level, date);
            waterDAO.addWater(newWater);
            tableModel.addRow(new Object[]{newWater.getId(), newWater.getLocation(), newWater.getLevel(), newWater.getDate()});
        }
    }

    // Su Düzenleme Formu
    public void editWaterForm() {
        int selectedRow = waterTable.getSelectedRow();
        if (selectedRow >= 0) {
            String location = (String) tableModel.getValueAt(selectedRow, 1);
            double level = (double) tableModel.getValueAt(selectedRow, 2);
            String date = (String) tableModel.getValueAt(selectedRow, 3);

            JTextField locationField = new JTextField(location);
            JTextField levelField = new JTextField(String.valueOf(level));
            JTextField dateField = new JTextField(date);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2, 10, 10));
            panel.add(new JLabel("Konum:"));
            panel.add(locationField);
            panel.add(new JLabel("Seviye (m):"));
            panel.add(levelField);
            panel.add(new JLabel("Tarih (YYYY-MM-DD):"));
            panel.add(dateField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Su Düzenle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                location = locationField.getText().trim();
                level = Double.parseDouble(levelField.getText().trim());
                date = dateField.getText().trim();

                if (location.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Tarih formatı YYYY-MM-DD olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Water updatedWater = new Water(id, location, level, date);
                tableModel.setValueAt(updatedWater.getLocation(), selectedRow, 1);
                tableModel.setValueAt(updatedWater.getLevel(), selectedRow, 2);
                tableModel.setValueAt(updatedWater.getDate(), selectedRow, 3);
                saveAllWater();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Düzenlemek için bir su seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
