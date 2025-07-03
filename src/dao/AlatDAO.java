package dao;

import database.Koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Alat;

public class AlatDAO {

    // Untuk FormSewa (versi ringkas + stok tersedia)
    public static List<Alat> getAllAlat() {
        List<Alat> daftarAlat = new ArrayList<>();

        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT alat_id AS id, nama_alat AS nama, harga_sewa_per_hari AS harga, stok_tersedia FROM alat";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alat a = new Alat();
                a.setId(rs.getInt("id"));
                a.setNama(rs.getString("nama"));
                a.setHarga(rs.getInt("harga"));
                a.setStokTersedia(rs.getInt("stok_tersedia")); // ✅ tambahkan ini
                daftarAlat.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarAlat;
    }

    // Untuk Dashboard (versi lengkap dengan stok)
    public static List<Alat> getAll() {
        List<Alat> list = new ArrayList<>();
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM alat";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alat a = new Alat();
                a.setId(rs.getInt("alat_id"));
                a.setNama(rs.getString("nama_alat"));
                a.setHarga(rs.getInt("harga_sewa_per_hari"));
                a.setStokTersedia(rs.getInt("stok_tersedia"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
