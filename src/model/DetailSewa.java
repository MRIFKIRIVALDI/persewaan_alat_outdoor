package model;

public class DetailSewa {
    private int idSewa;
    private int idAlat;
    private int harga;

    public DetailSewa(int idSewa, int idAlat, int harga) {
        this.idSewa = idSewa;
        this.idAlat = idAlat;
        this.harga = harga;
    }

    public int getIdSewa() { return idSewa; }
    public int getIdAlat() { return idAlat; }
    public int getHarga() { return harga; }
}
