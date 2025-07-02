package dao;

import database.Koneksi;
import model.Alat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlatDAO {

    public static List<Alat> getAllAlat() {
        List<Alat> daftarAlat = new ArrayList<>();

        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT id_alat AS id, nama_alat AS nama, harga_sewa AS harga FROM alat";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alat a = new Alat();
                a.setId(rs.getInt("id"));           // alias 'id' dari query
                a.setNama(rs.getString("nama"));    // alias 'nama'
                a.setHarga(rs.getInt("harga"));     // alias 'harga'
                daftarAlat.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarAlat;
    }
}
