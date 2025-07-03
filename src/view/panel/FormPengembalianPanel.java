package view.panel;

import dao.PenyewaanDAO;
import model.DetailPenyewaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormPengembalianPanel extends JPanel {

    private JTextField txtIdPenyewaan;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnCari, btnKembalikan, btnCetak, btnKembali;
    private PenyewaanDAO penyewaanDAO = new PenyewaanDAO();

    public FormPengembalianPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TOP PANEL
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtIdPenyewaan = new JTextField(10);
        btnCari = new JButton("Cari Penyewaan");
        topPanel.add(new JLabel("ID Penyewaan: "));
        topPanel.add(txtIdPenyewaan);
        topPanel.add(btnCari);
        add(topPanel, BorderLayout.NORTH);

        // TABLE
        tableModel = new DefaultTableModel(new String[]{"ID Alat", "Nama Alat", "Jumlah Disewa", "Jumlah Dikembalikan"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 3; // hanya kolom "Jumlah Dikembalikan" yang bisa diedit
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnKembali = new JButton("⬅ Kembali");
        btnCetak = new JButton("Cetak Struk 🖨️");
        btnKembalikan = new JButton("Kembalikan Sekarang ✅");

        bottomPanel.add(btnKembali);
        bottomPanel.add(btnCetak);
        bottomPanel.add(btnKembalikan);
        add(bottomPanel, BorderLayout.SOUTH);

        // EVENT HANDLERS
        btnCari.addActionListener(e -> loadDetailPenyewaan());
        btnKembalikan.addActionListener(e -> prosesPengembalian());
        btnCetak.addActionListener(e -> JOptionPane.showMessageDialog(this, "(Placeholder) Struk dicetak."));
        btnKembali.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new MenuUtama());
            frame.revalidate();
        });
    }

    private void loadDetailPenyewaan() {
        try {
            int id = Integer.parseInt(txtIdPenyewaan.getText());
            List<DetailPenyewaan> list = penyewaanDAO.getDetailByPenyewaan(id);

            tableModel.setRowCount(0);
            for (DetailPenyewaan dp : list) {
                tableModel.addRow(new Object[]{
                        dp.getIdAlat(),
                        dp.getNamaAlat(),
                        dp.getJumlah(),
                        dp.getJumlah() // default: jumlah dikembalikan = jumlah disewa
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + e.getMessage());
        }
    }

    private void prosesPengembalian() {
        try {
            int id = Integer.parseInt(txtIdPenyewaan.getText());

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int idAlat = (int) tableModel.getValueAt(i, 0);
                int jumlahDisewa = (int) tableModel.getValueAt(i, 2);
                int jumlahKembali = Integer.parseInt(tableModel.getValueAt(i, 3).toString());

                if (jumlahKembali < 0 || jumlahKembali > jumlahDisewa) {
                    throw new Exception("Jumlah dikembalikan tidak valid untuk alat ID: " + idAlat);
                }

                penyewaanDAO.kembalikanAlatPerItem(id, idAlat, jumlahKembali);
            }

            penyewaanDAO.finalizePengembalian(id);
            JOptionPane.showMessageDialog(this, "Pengembalian berhasil.");
            tableModel.setRowCount(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal proses pengembalian: " + e.getMessage());
        }
    }
}
