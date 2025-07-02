package model;

public class Alat {
    private int id;
    private String nama;
    private int harga;

    // Constructor kosong diperlukan oleh framework atau ORM
    public Alat() {}

    // Constructor lengkap
    public Alat(int id, String nama, int harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    // toString() digunakan oleh JComboBox agar menampilkan nama + harga
    @Override
    public String toString() {
        return nama + " (Rp" + harga + ")";
    }
}
