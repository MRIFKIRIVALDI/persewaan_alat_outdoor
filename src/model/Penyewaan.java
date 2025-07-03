package model;

import java.util.Date;

public class Penyewaan {
    private int id;
    private String namaPenyewa;
    private Date tanggalSewa;
    private String statusPengembalian;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNamaPenyewa() { return namaPenyewa; }
    public void setNamaPenyewa(String namaPenyewa) { this.namaPenyewa = namaPenyewa; }

    public Date getTanggalSewa() { return tanggalSewa; }
    public void setTanggalSewa(Date tanggalSewa) { this.tanggalSewa = tanggalSewa; }

    public String getStatusPengembalian() { return statusPengembalian; }
    public void setStatusPengembalian(String statusPengembalian) { this.statusPengembalian = statusPengembalian; }
}
