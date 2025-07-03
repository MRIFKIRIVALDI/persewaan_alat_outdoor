package view;

import dao.AlatDAO;
import database.Koneksi;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Alat;

public class FormSewa extends JPanel {
    private JTextField txtNama, txtNoHp, txtTglPinjam, txtTglKembali;
    private JComboBox<Alat> comboAlat;
    private JTextField txtJumlah;
    private JTable table;
    private DefaultTableModel tableModel;

    public FormSewa() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        txtNama = new JTextField();
        txtNoHp = new JTextField();
        txtTglPinjam = new JTextField(LocalDate.now().toString());
        txtTglKembali = new JTextField(LocalDate.now().plusDays(1).toString());
        comboAlat = new JComboBox<>();
        txtJumlah = new JTextField("1");

        for (Alat a : AlatDAO.getAll()) {
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

        tableModel = new DefaultTableModel(new String[]{"Alat", "Jumlah", "Harga"}, 0);
        table = new JTable(tableModel);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnTambah);
        btnPanel.add(btnSimpan);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void tambahKeDaftar() {
        Alat selected = (Alat) comboAlat.getSelectedItem();
        int jumlah = Integer.parseInt(txtJumlah.getText());

        // Hitung total jumlah alat yang sudah ditambahkan sebelumnya
        int jumlahDiTabel = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Alat alatInTable = (Alat) tableModel.getValueAt(i, 0);
            if (alatInTable.getId() == selected.getId()) {
                jumlahDiTabel += Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            }
        }

        if (selected.getStokTersedia() < jumlah + jumlahDiTabel) {
            JOptionPane.showMessageDialog(this, "Stok tidak mencukupi untuk alat: " + selected.getNama());
            return;
        }

        int total = selected.getHarga() * jumlah;
        tableModel.addRow(new Object[]{selected, jumlah, "Rp " + total});
    }

    private void simpanSewa() {
        String nama = txtNama.getText();
        String noHp = txtNoHp.getText();
        LocalDate tglPinjam = LocalDate.parse(txtTglPinjam.getText());
        LocalDate tglKembali = LocalDate.parse(txtTglKembali.getText());

        try (Connection conn = Koneksi.getConnection()) {
            conn.setAutoCommit(false);

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

            String sqlDetail = "INSERT INTO detail_penyewaan(id_penyewaan, id_alat, jumlah) VALUES (?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            String sqlUpdateStok = "UPDATE alat SET stok_tersedia = stok_tersedia - ? WHERE alat_id = ?";
            PreparedStatement psUpdateStok = conn.prepareStatement(sqlUpdateStok);

            StringBuilder struk = new StringBuilder();
            int totalHarga = 0;

            struk.append("=== Struk Penyewaan ===\n");
            struk.append("Nama: ").append(nama).append("\n");
            struk.append("No HP: ").append(noHp).append("\n");
            struk.append("Tanggal Pinjam: ").append(tglPinjam).append("\n");
            struk.append("Tanggal Kembali: ").append(tglKembali).append("\n\n");
            struk.append("Daftar Alat:\n");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Alat alat = (Alat) tableModel.getValueAt(i, 0);
                int jumlah = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int subtotal = alat.getHarga() * jumlah;
                totalHarga += subtotal;

                psDetail.setInt(1, idPenyewaan);
                psDetail.setInt(2, alat.getId());
                psDetail.setInt(3, jumlah);
                psDetail.addBatch();

                psUpdateStok.setInt(1, jumlah);
                psUpdateStok.setInt(2, alat.getId());
                psUpdateStok.addBatch();

                struk.append("- ").append(alat.getNama()).append(" x").append(jumlah)
                        .append(" = Rp ").append(subtotal).append("\n");
            }

            struk.append("\nTotal Harga: Rp ").append(totalHarga);

            psDetail.executeBatch();
            psUpdateStok.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Data penyewaan berhasil disimpan!\n\n" + struk.toString());
            tableModel.setRowCount(0);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + ex.getMessage());
        }
    }

}
