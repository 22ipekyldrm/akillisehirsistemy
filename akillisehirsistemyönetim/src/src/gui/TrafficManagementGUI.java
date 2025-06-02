package src.gui;
import src.dao.TrafficDAO;
import src.Model.Traffic;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TrafficManagementGUI extends JFrame {
    private JTable trafficTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, viewButton, reportButton;
    private TrafficDAO trafficDAO;

    public TrafficManagementGUI() {
        super("Trafik Yönetimi");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        trafficDAO = new TrafficDAO();

        // Tablo modeli
        tableModel = new DefaultTableModel(new Object[]{"ID", "Araç", "Açıklama", "Tarih"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        trafficTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(trafficTable);

        loadTraffic();

        // Butonlar
        addButton = new JButton("Trafik Ekle");
        editButton = new JButton("Trafik Düzenle");
        deleteButton = new JButton("Trafik Sil");
        viewButton = new JButton("Araç İzle");
        reportButton = new JButton("Trafik Yoğunluğu Raporla");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(reportButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Trafik ekleme işlemi
        addButton.addActionListener(e -> {
            JTextField locationField = new JTextField();
            JTextField descriptionField = new JTextField();
            JTextField dateField = new JTextField();

            Object[] fields = {
                "Araç:", locationField,
                "Açıklama:", descriptionField,
                "Tarih (YYYY-MM-DD):", dateField
            };

            int option = JOptionPane.showConfirmDialog(
                TrafficManagementGUI.this,
                fields,
                "Trafik Ekle",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (option == JOptionPane.OK_OPTION) {
                String location = locationField.getText().trim();
                String description = descriptionField.getText().trim();
                String date = dateField.getText().trim();

                if (location.isEmpty() || description.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Tarih formatı YYYY-MM-DD olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int newId = trafficDAO.getNextId();
                Traffic newTraffic = new Traffic(newId, location, description, date);
                trafficDAO.addTraffic(newTraffic);
                tableModel.addRow(new Object[]{newTraffic.getId(), newTraffic.getLocation(), newTraffic.getDescription(), newTraffic.getDate()});
            }
        });

        // Trafik düzenleme işlemi
        editButton.addActionListener(e -> {
            int selectedRow = trafficTable.getSelectedRow();
            if (selectedRow >= 0) {
                String currentLocation = (String) tableModel.getValueAt(selectedRow, 1);
                String currentDescription = (String) tableModel.getValueAt(selectedRow, 2);
                String currentDate = (String) tableModel.getValueAt(selectedRow, 3);

                JTextField locationField = new JTextField(currentLocation);
                JTextField descriptionField = new JTextField(currentDescription);
                JTextField dateField = new JTextField(currentDate);

                Object[] fields = {
                    "Araç:", locationField,
                    "Açıklama:", descriptionField,
                    "Tarih (YYYY-MM-DD):", dateField
                };

                int option = JOptionPane.showConfirmDialog(
                    TrafficManagementGUI.this,
                    fields,
                    "Trafik Düzenle",
                    JOptionPane.OK_CANCEL_OPTION
                );

                if (option == JOptionPane.OK_OPTION) {
                    String location = locationField.getText().trim();
                    String description = descriptionField.getText().trim();
                    String date = dateField.getText().trim();

                    if (location.isEmpty() || description.isEmpty() || date.isEmpty()) {
                        JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Tarih formatı YYYY-MM-DD olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    tableModel.setValueAt(location, selectedRow, 1);
                    tableModel.setValueAt(description, selectedRow, 2);
                    tableModel.setValueAt(date, selectedRow, 3);
                    saveAllTraffic();
                }
            } else {
                JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Düzenlemek için bir trafik seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Trafik silme işlemi
        deleteButton.addActionListener(e -> {
            int selectedRow = trafficTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(TrafficManagementGUI.this, "Seçili trafiği silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteTraffic(id);
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Silmek için bir trafik seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Araç izle butonu
        viewButton.addActionListener(e -> {
            // Araç izleme işlemi
            AracIzle();
        });

        // Trafik yoğunluğu raporu butonu
        reportButton.addActionListener(e -> {
            // Trafik yoğunluğu raporlama işlemi
            TrafikYogunlugunuRaporla();
        });

        setVisible(true);
    }

    private void loadTraffic() {
        for (Traffic traffic : trafficDAO.getAllTraffic()) {
            tableModel.addRow(new Object[]{traffic.getId(), traffic.getLocation(), traffic.getDescription(), traffic.getDate()});
        }
    }

    private void saveAllTraffic() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(trafficDAO.getTrafficFile()))) {
            bw.write("id,location,description,date");
            bw.newLine();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int id = (int) tableModel.getValueAt(i, 0);
                String location = (String) tableModel.getValueAt(i, 1);
                String description = (String) tableModel.getValueAt(i, 2);
                String date = (String) tableModel.getValueAt(i, 3);
                bw.write(id + "," + location + "," + description + "," + date);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Trafik dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTraffic(int id) {
        List<Traffic> trafficList = trafficDAO.getAllTraffic();
        trafficList.removeIf(t -> t.getId() == id);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(trafficDAO.TRAFFIC_FILE))) {
            bw.write("id,location,description,date");
            bw.newLine();
            for (Traffic t : trafficList) {
                bw.write(t.getId() + "," + t.getLocation() + "," + t.getDescription() + "," + t.getDate());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(TrafficManagementGUI.this, "Trafik dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Araç İzleme fonksiyonu
    private void AracIzle() {
        // Araç izleme işlemlerini burada tanımlayabilirsiniz.
        JOptionPane.showMessageDialog(this, "Araç izleme işlemi gerçekleştiriliyor.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
    }

    // Trafik Yoğunluğu Raporu fonksiyonu
    private void TrafikYogunlugunuRaporla() {
        // Trafik yoğunluğu raporlama işlemleri burada yapılacak.
        JOptionPane.showMessageDialog(this, "Trafik yoğunluğu raporu oluşturuluyor.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
    }
}
