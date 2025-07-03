package view.panel;

import database.Koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RiwayatPenyewaanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnKembali;

    public RiwayatPenyewaanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Judul
        JLabel label = new JLabel("📜 Riwayat Penyewaan", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        add(label, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(new String[]{
                "ID", "Nama", "Tanggal Pinjam", "Tanggal Kembali", "Alat", "Jumlah"
        }, 0);
        table = new JTable(tableModel);
        loadData();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol kembali
        btnKembali = new JButton("⬅ Kembali");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnKembali);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action tombol kembali
        btnKembali.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new MenuUtama());
            frame.revalidate();
        });
    }

    private void loadData() {
        try (Connection conn = Koneksi.getConnection()) {
            String sql = """
                SELECT p.id, p.nama_penyewa, p.tanggal_pinjam, p.tanggal_kembali,
                       a.nama_alat, d.jumlah
                FROM penyewaan p
                JOIN detail_penyewaan d ON p.id = d.id_penyewaan
                JOIN alat a ON d.id_alat = a.alat_id
                ORDER BY p.tanggal_pinjam DESC
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("nama_penyewa"),
                        rs.getDate("tanggal_pinjam"),
                        rs.getDate("tanggal_kembali"),
                        rs.getString("nama_alat"),
                        rs.getInt("jumlah")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat: " + e.getMessage());
        }
    }
}
