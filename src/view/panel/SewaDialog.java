package view.panel;

import dao.AlatDAO;
import model.Alat;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SewaDialog extends JDialog {
    private JTextField tfNama, tfNoHP, tfTanggalPinjam, tfTanggalKembali;
    private JComboBox<String> cbAlat;
    private JLabel lblTotal;
    private List<Alat> daftarAlat;

    public SewaDialog(Frame parent) {
        super(parent, "Form Sewa Alat", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(7, 2, 5, 5));

        tfNama = new JTextField();
        tfNoHP = new JTextField();
        tfTanggalPinjam = new JTextField(LocalDate.now().toString());
        tfTanggalKembali = new JTextField(LocalDate.now().plusDays(1).toString());

        cbAlat = new JComboBox<>();
        // daftarAlat = AlatDAO.getAllAlat();
        // for (Alat alat : daftarAlat) {
        //     cbAlat.addItem(alat.getNama());
        // }
        daftarAlat = AlatDAO.getAllAlat();
        System.out.println("Jumlah alat dari database: " + daftarAlat.size());
        for (Alat a : daftarAlat) {
            System.out.println("- " + a.getNama() + " Rp" + a.getHarga());
        }

        lblTotal = new JLabel("Total: Rp 0");
        JButton btnHitung = new JButton("Hitung Total");
        JButton btnSimpan = new JButton("Simpan");

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
        add(new JLabel());
        add(btnSimpan);

        btnHitung.addActionListener(e -> hitungTotal());
        btnSimpan.addActionListener(e -> simpanData());
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
        dispose();
    }
}
