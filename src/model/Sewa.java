package model;

public class Sewa {
    private int id;
    private String nama;
    private String noHp;
    private String tanggalPinjam;
    private String tanggalKembali;

    public Sewa(int id, String nama, String noHp, String tanggalPinjam, String tanggalKembali) {
        this.id = id;
        this.nama = nama;
        this.noHp = noHp;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getNoHp() { return noHp; }
    public String getTanggalPinjam() { return tanggalPinjam; }
    public String getTanggalKembali() { return tanggalKembali; }
}
