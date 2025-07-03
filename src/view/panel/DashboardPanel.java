package view.panel;

import dao.AlatDAO;
import dao.KategoriDAO;
import dao.PenyewaanDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.Alat;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel lblTotalAlat       = new JLabel();
        JLabel lblStokTersedia    = new JLabel();
        JLabel lblTotalKategori   = new JLabel();
        JLabel lblTotalPotensi    = new JLabel();
        JLabel lblBelumKembali    = new JLabel();
        JLabel lblSudahKembaliBln = new JLabel();

        try {
            List<Alat> list = AlatDAO.getAll();

            int totalAlat = list.size();
            int stokTersedia = list.stream()
                                   .mapToInt(Alat::getStokTersedia)
                                   .sum();

            int totalPotensi = list.stream()
                                   .mapToInt(a -> a.getHargaSewaPerHari() * a.getStokTersedia())
                                   .sum();

            int totalKategori = KategoriDAO.getAll().size();

            int belumKembali = new PenyewaanDAO().getJumlahBelumDikembalikan();
            int sudahKembaliBlnIni = new PenyewaanDAO().getJumlahPengembalianBulanIni();

            lblTotalAlat.setText("🔢 Total Alat: " + totalAlat);
            lblStokTersedia.setText("📦 Alat Tersedia: " + stokTersedia);
            lblTotalKategori.setText("⛺ Kategori Tersedia: " + totalKategori);
            lblTotalPotensi.setText("💰 Potensi Pendapatan (1 hari): Rp " + totalPotensi);
            lblBelumKembali.setText("⏳ Belum Dikembalikan: " + belumKembali + " penyewaan");
            lblSudahKembaliBln.setText("✅ Sudah Dikembalikan (bulan ini): " + sudahKembaliBlnIni + " penyewaan");

        } catch (Exception ex) {
            ex.printStackTrace();
            add(new JLabel("❌ Gagal memuat data dashboard: " + ex.getMessage()));
            return;
        }

        statsPanel.add(lblTotalAlat);
        statsPanel.add(lblStokTersedia);
        statsPanel.add(lblTotalKategori);
        statsPanel.add(lblTotalPotensi);
        statsPanel.add(lblBelumKembali);
        statsPanel.add(lblSudahKembaliBln);

        add(statsPanel, BorderLayout.CENTER);

        // Tombol menuju Form Pengembalian
        JButton btnPengembalian = new JButton("➡️ Form Pengembalian");
        btnPengembalian.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.setContentPane(new FormPengembalianPanel());
            topFrame.revalidate();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnPengembalian);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
