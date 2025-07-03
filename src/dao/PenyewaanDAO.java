package dao;

import database.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Penyewaan;
import model.DetailPenyewaan;

public class PenyewaanDAO {

    public List<Penyewaan> getBelumDikembalikan() throws SQLException {
        List<Penyewaan> list = new ArrayList<>();
        String sql = "SELECT * FROM penyewaan WHERE status_pengembalian = 'Belum'";

        try (Connection con = Koneksi.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Penyewaan p = new Penyewaan();
                p.setId(rs.getInt("id"));
                p.setNamaPenyewa(rs.getString("nama_penyewa"));
                p.setTanggalSewa(rs.getDate("tanggal_sewa"));
                p.setStatusPengembalian(rs.getString("status_pengembalian"));
                list.add(p);
            }
        }
        return list;
    }

    public void kembalikanBarang(int idPenyewaan) throws SQLException {
        try (Connection con = Koneksi.getConnection()) {
            // Ambil detail alat yang disewa
            String queryDetail = "SELECT id_alat, jumlah FROM detail_penyewaan WHERE id_penyewaan = ?";
            PreparedStatement psDetail = con.prepareStatement(queryDetail);
            psDetail.setInt(1, idPenyewaan);
            ResultSet rs = psDetail.executeQuery();

            while (rs.next()) {
                int idAlat = rs.getInt("id_alat");
                int jumlah = rs.getInt("jumlah");

                // Update stok_tersedia alat
                String updateStok = "UPDATE alat SET stok_tersedia = stok_tersedia + ? WHERE alat_id = ?";
                PreparedStatement psUpdate = con.prepareStatement(updateStok);
                psUpdate.setInt(1, jumlah);
                psUpdate.setInt(2, idAlat);
                psUpdate.executeUpdate();
            }

            // Update status pengembalian
            String updateStatus = "UPDATE penyewaan SET status_pengembalian = 'Sudah', tanggal_pengembalian = CURDATE() WHERE id = ?";
            PreparedStatement psStatus = con.prepareStatement(updateStatus);
            psStatus.setInt(1, idPenyewaan);
            psStatus.executeUpdate();
        }
    }

    public void kembalikanAlatPerItem(int idPenyewaan, int idAlat, int jumlah) throws SQLException {
        String updateStok = "UPDATE alat SET stok_tersedia = stok_tersedia + ? WHERE alat_id = ?";
        try (Connection con = Koneksi.getConnection();
             PreparedStatement ps = con.prepareStatement(updateStok)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, idAlat);
            ps.executeUpdate();
        }
    }

    public void finalizePengembalian(int idPenyewaan) throws SQLException {
        String sql = "UPDATE penyewaan SET status_pengembalian = 'Sudah', tanggal_pengembalian = CURDATE() WHERE id = ?";
        try (Connection con = Koneksi.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPenyewaan);
            ps.executeUpdate();
        }
    }

    public int getJumlahBelumDikembalikan() throws SQLException {
        String sql = "SELECT COUNT(*) FROM penyewaan WHERE status_pengembalian = 'Belum'";
        try (Connection con = Koneksi.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getJumlahPengembalianBulanIni() throws SQLException {
        String sql = "SELECT COUNT(*) FROM penyewaan WHERE status_pengembalian = 'Sudah' " +
                     "AND MONTH(tanggal_pengembalian) = MONTH(CURDATE()) " +
                     "AND YEAR(tanggal_pengembalian) = YEAR(CURDATE())";
        try (Connection con = Koneksi.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<DetailPenyewaan> getDetailByPenyewaan(int idPenyewaan) throws SQLException {
        List<DetailPenyewaan> list = new ArrayList<>();
        String sql = "SELECT dp.id_alat, a.nama_alat, dp.jumlah " +
                     "FROM detail_penyewaan dp " +
                     "JOIN alat a ON dp.id_alat = a.alat_id " +
                     "WHERE dp.id_penyewaan = ?";

        try (Connection con = Koneksi.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPenyewaan);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetailPenyewaan dp = new DetailPenyewaan();
                dp.setIdAlat(rs.getInt("id_alat"));
                dp.setNamaAlat(rs.getString("nama_alat"));
                dp.setJumlah(rs.getInt("jumlah"));
                list.add(dp);
            }
        }
        return list;
    }
}
