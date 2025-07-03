package view.panel;

import java.awt.*;
import javax.swing.*;
import view.FormSewa;

public class MenuUtama extends JFrame {
    private JPanel contentPanel;

    public MenuUtama() {
        setTitle("Sewa Alat Outdoor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());

        // === Panel Navigasi Atas ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(new Color(230, 240, 255));

        JButton btnDashboard = new JButton("📊 Dashboard");
        JButton btnSewa = new JButton("📝 Form Sewa");
        JButton btnRiwayat = new JButton("📜 Riwayat Penyewaan");

        btnPanel.add(btnDashboard);
        btnPanel.add(btnSewa);
        btnPanel.add(btnRiwayat);

        // === Panel Konten Tengah ===
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Default tampilan awal
        try {
            contentPanel.add(new DashboardPanel(), BorderLayout.CENTER);
        } catch (Exception e) {
            contentPanel.add(new JLabel("Gagal memuat Dashboard."), BorderLayout.CENTER);
        }

        // Gabungkan semua panel
        mainPanel.add(btnPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Aksi tombol
        btnDashboard.addActionListener(e -> gantiPanel(new DashboardPanel()));
        btnSewa.addActionListener(e -> gantiPanel(new FormSewa()));
        btnRiwayat.addActionListener(e -> gantiPanel(new RiwayatPenyewaanPanel()));

        setContentPane(mainPanel);
    }

    private void gantiPanel(JPanel panelBaru) {
        contentPanel.removeAll();
        contentPanel.add(panelBaru, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
