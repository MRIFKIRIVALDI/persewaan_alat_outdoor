package view.panel;

import dao.AlatDAO;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import model.Alat;

public class SewaDialog extends JDialog {

    private JTextField tfNama, tfNoHP, tfTanggalPinjam, tfTanggalKembali;
    private JComboBox<String> cbAlat;
    private JLabel lblTotal;
    private List<Alat> daftarAlat;

    public SewaDialog(Frame parent) {
        super(parent, "Form Sewa Alat", true);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(8, 2, 5, 5));

        tfNama = new JTextField();
        tfNoHP = new JTextField();
        tfTanggalPinjam = new JTextField(LocalDate.now().toString());
        tfTanggalKembali = new JTextField(LocalDate.now().plusDays(1).toString());

        cbAlat = new JComboBox<>();
        daftarAlat = AlatDAO.getAllAlat();
        for (Alat alat : daftarAlat) {
            cbAlat.addItem(alat.getNama());
        }

        lblTotal = new JLabel("Total: Rp 0");

        JButton btnHitung = new JButton("Hitung Total");
        JButton btnSimpan = new JButton("Simpan");
        JButton btnKembali = new JButton("⬅ Kembali");

        // Tambahkan komponen ke form
        add(new JLabel("Nama"));
        add(tfNama);

        add(new JLabel("No HP"));
        add(tfNoHP);

        add(new JLabel("Pilih Alat"));
        add(cbAlat);

        add(new JLabel("Tanggal Pinjam (YYYY-MM-DD)"));
        add(tfTanggalPinjam);

        add(new JLabel("Tanggal Kembali"));
        add(tfTanggalKembali);

        add(btnHitung);
        add(lblTotal);

        add(btnKembali);  // tombol kembali
        add(btnSimpan);   // tombol simpan

        // Action listeners
        btnHitung.addActionListener(e -> hitungTotal());
        btnSimpan.addActionListener(e -> simpanData());
        btnKembali.addActionListener(e -> dispose()); // Menutup dialog
    }

    private void hitungTotal() {
        int selectedIndex = cbAlat.getSelectedIndex();
        if (selectedIndex >= 0) {
            Alat alat = daftarAlat.get(selectedIndex);
            try {
                LocalDate pinjam = LocalDate.parse(tfTanggalPinjam.getText());
                LocalDate kembali = LocalDate.parse(tfTanggalKembali.getText());
                long hari = java.time.temporal.ChronoUnit.DAYS.between(pinjam, kembali);
                if (hari < 1) hari = 1;

                int total = (int) (alat.getHarga() * hari);
                lblTotal.setText("Total: Rp " + total);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Tanggal tidak valid");
            }
        }
    }

    private void simpanData() {
        String nama = tfNama.getText();
        String nohp = tfNoHP.getText();

        if (nama.isEmpty() || nohp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan No HP wajib diisi!");
            return;
        }

        // Simpan ke database bisa ditambahkan di sini
        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        dispose(); // Tutup dialog setelah simpan
    }
}
