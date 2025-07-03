package model;

public class Alat {
    private int id;
    private String nama;
    private int harga;
    private int stokTersedia;

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }

    public int getStokTersedia() { return stokTersedia; }
    public void setStokTersedia(int stokTersedia) { this.stokTersedia = stokTersedia; }

    // Supaya comboBox & tabel menampilkan nama alat
    @Override
    public String toString() {
        return nama;
    }

    // Alias untuk nama method lain yang mungkin digunakan di bagian dashboard
    public int getHargaSewaPerHari() {
        return harga;
    }
}
