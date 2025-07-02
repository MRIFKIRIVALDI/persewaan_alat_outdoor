package view.panel;

import javax.swing.*;

import view.FormSewa;

import java.awt.*;

public class MenuUtama extends JFrame {
    public MenuUtama() {
        setTitle("Sewa Alat Outdoor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JButton btnSewa = new JButton("Sewa Alat");

        // GANTI baris ini:
        // btnSewa.addActionListener(e -> new FormSewa().setVisible(true));

        // DENGAN kode ini:
        btnSewa.addActionListener(e -> {
        FormSewa form = new FormSewa();
        form.setVisible(true); // ✅ tampilkan form
    });

        JPanel panel = new JPanel();
        panel.add(btnSewa);

        setLayout(new BorderLayout());
        add(new JLabel("Selamat Datang di Sewa Alat Outdoor", JLabel.CENTER), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
    }
}
