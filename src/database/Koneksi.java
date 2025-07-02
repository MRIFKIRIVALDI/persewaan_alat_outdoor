package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/persewaan_alat_outdoor"; // Ubah ini
            String user = "root"; // sesuaikan jika kamu pakai password
            String pass = "";     // jika ada password, masukkan di sini
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✔ Koneksi ke database berhasil");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Koneksi database gagal: " + e.getMessage());
            throw e;
        }
    }
}
