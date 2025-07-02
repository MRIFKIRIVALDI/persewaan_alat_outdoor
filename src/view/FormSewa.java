package view;

import dao.AlatDAO;
import database.Koneksi;
import model.Alat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormSewa extends JFrame {
    private JTextField txtNama, txtNoHp, txtTglPinjam, txtTglKembali;
    private JComboBox<Alat> comboAlat;
    private JTextField txtJumlah;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Alat> daftarAlat;

    public FormSewa() {
        setTitle("Form Sewa Alat");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Input
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        txtNama = new JTextField();
        txtNoHp = new JTextField();
        txtTglPinjam = new JTextField(LocalDate.now().toString());
        txtTglKembali = new JTextField(LocalDate.now().plusDays(1).toString());
        comboAlat = new JComboBox<>();
        txtJumlah = new JTextField("1");

        for (Alat a : AlatDAO.getAllAlat()) {
            comboAlat.addItem(a);
        }

        inputPanel.add(new JLabel("Nama")); inputPanel.add(txtNama);
        inputPanel.add(new JLabel("No HP")); inputPanel.add(txtNoHp);
        inputPanel.add(new JLabel("Tanggal Pinjam")); inputPanel.add(txtTglPinjam);
        inputPanel.add(new JLabel("Tanggal Kembali")); inputPanel.add(txtTglKembali);
        inputPanel.add(new JLabel("Pilih Alat")); inputPanel.add(comboAlat);
        inputPanel.add(new JLabel("Jumlah")); inputPanel.add(txtJumlah);

        JButton btnTambah = new JButton("Tambah ke Daftar");
        btnTambah.addActionListener(e -> tambahKeDaftar());

        JButton btnSimpan = new JButton("Simpan Sewa");
        btnSimpan.addActionListener(e -> simpanSewa());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Alat", "Jumlah", "Harga"}, 0);
        table = new JTable(tableModel);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnTambah);
        btnPanel.add(btnSimpan);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        daftarAlat = new ArrayList<>();
    }

    private void tambahKeDaftar() {
        Alat selected = (Alat) comboAlat.getSelectedItem();
        int jumlah = Integer.parseInt(txtJumlah.getText());
        int total = selected.getHarga() * jumlah;
        tableModel.addRow(new Object[]{selected, jumlah, "Rp " + total});
        for (int i = 0; i < jumlah; i++) {
            daftarAlat.add(selected);
        }
    }

    private void simpanSewa() {
        String nama = txtNama.getText();
        String noHp = txtNoHp.getText();
        LocalDate tglPinjam = LocalDate.parse(txtTglPinjam.getText());
        LocalDate tglKembali = LocalDate.parse(txtTglKembali.getText());

        try (Connection conn = Koneksi.getConnection()) {
            conn.setAutoCommit(false); // transaksi

            // 1. Simpan ke tabel penyewaan
            String sqlPenyewaan = "INSERT INTO penyewaan(nama_penyewa, no_hp, tanggal_pinjam, tanggal_kembali) VALUES (?, ?, ?, ?)";
            PreparedStatement psPenyewaan = conn.prepareStatement(sqlPenyewaan, Statement.RETURN_GENERATED_KEYS);
            psPenyewaan.setString(1, nama);
            psPenyewaan.setString(2, noHp);
            psPenyewaan.setDate(3, Date.valueOf(tglPinjam));
            psPenyewaan.setDate(4, Date.valueOf(tglKembali));
            psPenyewaan.executeUpdate();

            ResultSet rs = psPenyewaan.getGeneratedKeys();
            int idPenyewaan = 0;
            if (rs.next()) {
                idPenyewaan = rs.getInt(1);
            }

            // 2. Simpan ke tabel detail_penyewaan
            String sqlDetail = "INSERT INTO detail_penyewaan(id_penyewaan, id_alat, jumlah) VALUES (?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Alat alat = (Alat) tableModel.getValueAt(i, 0);
                int jumlah = Integer.parseInt(tableModel.getValueAt(i, 1).toString());

                psDetail.setInt(1, idPenyewaan);
                psDetail.setInt(2, alat.getId());
                psDetail.setInt(3, jumlah);
                psDetail.addBatch();
            }

            psDetail.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Data penyewaan berhasil disimpan!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
        }
    }
}
