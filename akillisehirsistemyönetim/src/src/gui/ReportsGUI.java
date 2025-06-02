//ReportsGUI Sınıfı
package src.gui;
import src.dao.*; // Veri erişim katmanı
import src.Model.*; // Model sınıfı
import javax.swing.*; // Swing bileşenleri
import javax.swing.table.DefaultTableModel; // Tablo modeli için
import java.awt.*; // Layoutlar ve bileşenlerin boyutlandırılması için
import java.awt.event.*; // Event işlemleri için
import java.io.BufferedWriter; // Dosya yazma işlemleri için
import java.io.FileWriter; // Dosya yazma işlemleri için
import java.io.IOException; // IO istisnaları için
import java.util.List; // Rapor listesi yönetimi için
// Rapor yönetimi ekranını temsil eden sınıf
public class ReportsGUI extends JFrame {
    private JTable reportTable; // Raporların gösterileceği tablo
    private DefaultTableModel tableModel; // Tablo modeli
    private JButton addButton, deleteButton; // Rapor ekleme ve silme butonları
    private ReportDAO reportDAO; // Rapor veri erişim nesnesi
    // Yapıcı metot
    public ReportsGUI() {
        super("Raporlar Yönetimi"); // Pencere başlığı
        setSize(700, 500); // Pencere boyutları
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştir
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında sadece bu ekranı kapat
        reportDAO = new ReportDAO(); // ReportDAO nesnesini oluştur
        // Tablo modeli tanımlanıyor
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tür", "Açıklama", "Tarih"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tablo hücrelerinin düzenlenmesini engeller
            }
        };

        reportTable = new JTable(tableModel); // Tablo oluşturuluyor
        JScrollPane scrollPane = new JScrollPane(reportTable); // Tabloya kaydırma desteği ekleniyor

        loadReports(); // Veritabanından raporları yükle

        // Butonlar oluşturuluyor
        addButton = new JButton("Rapor Ekle"); // Rapor ekleme butonu
        deleteButton = new JButton("Rapor Sil"); // Rapor silme butonu

        // Butonların ekleneceği panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton); // Ekle butonu panelde
        buttonPanel.add(deleteButton); // Sil butonu panelde

        add(scrollPane, BorderLayout.CENTER); // Tablo ortada
        add(buttonPanel, BorderLayout.SOUTH); // Butonlar altta

        // Butonların tıklama işlemleri
        addButton.addActionListener(e -> {
            // Yeni rapor ekleme için dialog açılıyor
            ReportDialog dialog = new ReportDialog(ReportsGUI.this, "Rapor Ekle", null);
            dialog.setVisible(true);
            Report newReport = dialog.getReport(); // Kullanıcıdan alınan rapor
            if (newReport != null) {
                reportDAO.addReport(newReport); // Veritabanına raporu ekle
                tableModel.addRow(new Object[]{newReport.getId(), newReport.getType(), newReport.getDescription(), newReport.getDate()}); // Tabloya raporu ekle
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = reportTable.getSelectedRow(); // Seçilen satır
            if (selectedRow >= 0) {
                // Silme işlemini onaylatma
                int confirm = JOptionPane.showConfirmDialog(ReportsGUI.this, "Seçili raporu silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0); // Silinecek raporun ID'si
                    deleteReport(id); // Raporu sil
                    tableModel.removeRow(selectedRow); // Tabloyu güncelle
                }
            } else {
                // Hiçbir satır seçilmediyse hata mesajı göster
                JOptionPane.showMessageDialog(ReportsGUI.this, "Silmek için bir rapor seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true); // Pencereyi görünür yap
    }

    // Raporları yükleyen metot
    private void loadReports() {
        for (Report report : reportDAO.getAllReports()) { // Tüm raporları getir
            tableModel.addRow(new Object[]{report.getId(), report.getType(), report.getDescription(), report.getDate()}); // Tabloya ekle
        }
    }

    // Tüm raporları dosyaya kaydeden metot
    private void saveAllReports() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportDAO.getReportFile()))) {
            bw.write("id,type,description,date"); // Başlık satırı yaz
            bw.newLine();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int id = (int) tableModel.getValueAt(i, 0);
                String type = (String) tableModel.getValueAt(i, 1);
                String description = (String) tableModel.getValueAt(i, 2);
                String date = (String) tableModel.getValueAt(i, 3);
                bw.write(id + "," + type + "," + description + "," + date); // Verileri yaz
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Hata ayıklama çıktısı
            JOptionPane.showMessageDialog(ReportsGUI.this, "Rapor dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Veritabanından rapor silen metot
    private void deleteReport(int id) {
        List<Report> reports = reportDAO.getAllReports(); // Mevcut raporları al
        reports.removeIf(r -> r.getId() == id); // Silinecek raporu listeden çıkar
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportDAO.getReportFile()))) {
            bw.write("id,type,description,date"); // Başlık satırı yaz
            bw.newLine();
            for (Report r : reports) {
                bw.write(r.getId() + "," + r.getType() + "," + r.getDescription() + "," + r.getDate()); // Güncel verileri yaz
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Hata ayıklama çıktısı
            JOptionPane.showMessageDialog(ReportsGUI.this, "Rapor dosyasına yazılırken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Rapor ekleme/düzenleme dialog sınıfı
    class ReportDialog extends JDialog {
        private JTextField typeField, descriptionField, dateField; // Alanlar
        private JButton saveButton, cancelButton; // Kaydet ve İptal butonları
        private Report report; // Rapor nesnesi

        public ReportDialog(JFrame parent, String title, Report report) {
            super(parent, title, true); // Modal dialog
            setSize(400, 300); // Dialog boyutu
            setLocationRelativeTo(parent); // Ana pencerenin ortasına yerleştir
            this.report = report;

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)); // Alanlar için grid düzen
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Kenarlık boşluğu

            panel.add(new JLabel("Tür:")); // Tür etiketi
            typeField = new JTextField(); // Tür girişi
            panel.add(typeField);

            panel.add(new JLabel("Açıklama:")); // Açıklama etiketi
            descriptionField = new JTextField(); // Açıklama girişi
            panel.add(descriptionField);

            panel.add(new JLabel("Tarih (YYYY-MM-DD):")); // Tarih etiketi
            dateField = new JTextField(); // Tarih girişi
            panel.add(dateField);

            saveButton = new JButton("Kaydet"); // Kaydet butonu
            cancelButton = new JButton("İptal"); // İptal butonu
            panel.add(saveButton);
            panel.add(cancelButton);

            add(panel); // Paneli dialoga ekle

            if (report != null) { // Düzenleme durumunda alanları doldur
                typeField.setText(report.getType());
                descriptionField.setText(report.getDescription());
                dateField.setText(report.getDate());
            }

            // Kaydet butonu işlemi
            saveButton.addActionListener(e -> {
                String type = typeField.getText().trim();
                String description = descriptionField.getText().trim();
                String date = dateField.getText().trim();

                if (type.isEmpty() || description.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(ReportDialog.this, "Tüm alanlar doldurulmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tarih formatı kontrolü
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(ReportDialog.this, "Tarih formatı YYYY-MM-DD olmalıdır.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (ReportDialog.this.report == null) {
                    int newId = reportDAO.getNextId(); // Yeni ID al
                    ReportDialog.this.report = new Report(newId, type, description, date); // Yeni rapor oluştur
                } else {
                    // Var olan raporu güncelle
                    ReportDialog.this.report.setType(type);
                    ReportDialog.this.report.setDescription(description);
                    ReportDialog.this.report.setDate(date);
                }
                dispose(); // Dialogu kapat
            });

            // İptal butonu işlemi
            cancelButton.addActionListener(e -> {
                ReportDialog.this.report = null; // Raporu null yap
                dispose(); // Dialogu kapat
            });
        }

        // Raporu döndüren metot
        public Report getReport() {
            return report;
        }
    }
}