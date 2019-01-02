package ntt.bps.namberwan.darmawanita;

public class BukuItem {

    private String id;
    private String judul;
    private String deskripsi;
    private boolean isDipinjam;
    private String peminjam;
    private String tanggalMasuk;
    private String tanggalPinjam;
    private String urlGambar;
    private String sumber;

    public BukuItem(String id, String judul, String deskripsi, String tanggalMasuk, boolean isDipinjam,
                    String peminjam, String tanggalPinjam, String urlGambar, String sumber ){
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.isDipinjam = isDipinjam;
        this.peminjam = peminjam;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalPinjam = tanggalPinjam;
        this.urlGambar = urlGambar;
        this.sumber = sumber;
    }


    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public boolean isDipinjam() {
        return isDipinjam;
    }

    public String getPeminjam() {
        return peminjam;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public String getTanggalPinjam() {
        return tanggalPinjam;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public String getSumber() {
        return sumber;
    }
}
