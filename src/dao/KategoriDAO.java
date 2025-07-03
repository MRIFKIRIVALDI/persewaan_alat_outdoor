package dao;

import database.Koneksi;
import model.Kategori;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {

    public static List<Kategori> getAll() {
        List<Kategori> list = new ArrayList<>();

        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM kategori";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Kategori k = new Kategori();
                k.setId(rs.getInt("id_kategori"));
                k.setNama(rs.getString("nama_kategori"));
                list.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
