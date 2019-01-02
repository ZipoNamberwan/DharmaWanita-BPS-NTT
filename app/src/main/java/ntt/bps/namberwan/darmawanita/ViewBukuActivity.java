package ntt.bps.namberwan.darmawanita;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

public class ViewBukuActivity extends AppCompatActivity {

    private String id;
    private String judul;
    private String deskripsi;
    private String tanggalMasuk;
    private boolean isDipinjam;
    private String peminjam;
    private String tanggalPinjam;
    private String urlGambar;
    private String sumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buku);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = getIntent().getStringExtra(BukuAdapter.ID_BUKU);
        judul = getIntent().getStringExtra(BukuAdapter.JUDUL_BUKU);
        deskripsi = getIntent().getStringExtra(BukuAdapter.DESKRIPSI_BUKU);
        tanggalMasuk = getIntent().getStringExtra(BukuAdapter.TANGGAL_MASUK);
        isDipinjam = getIntent().getBooleanExtra(BukuAdapter.IS_DIPINJAM, false);
        peminjam = getIntent().getStringExtra(BukuAdapter.PEMINJAM);
        tanggalPinjam = getIntent().getStringExtra(BukuAdapter.TANGGAL_PINJAM);
        urlGambar = getIntent().getStringExtra(BukuAdapter.URL_GAMBAR);
        sumber = getIntent().getStringExtra(BukuAdapter.SUMBER_BUKU);

        TextView judulTextView = findViewById(R.id.judul);
        TextView deskripsiTextView = findViewById(R.id.desksripsi);
        TextView sumberTextView = findViewById(R.id.sumber);
        TextView tanggalMasukTextView = findViewById(R.id.tanggalmasuk);
        TextView statusTextView = findViewById(R.id.status);
        ImageView gambarImageView = findViewById(R.id.gambar);

        judulTextView.setText(judul);
        deskripsiTextView.setText(deskripsi);
        tanggalMasukTextView.setText(tanggalMasuk);
        sumberTextView.setText(sumber);
        if (!isDipinjam){
            statusTextView.setText("Available");
            statusTextView.setTextColor(getResources().getColor(R.color.md_green_600));
        }else {
            statusTextView.setText("Dipinjam Oleh: " + peminjam + " pada " + tanggalPinjam);
            statusTextView.setTextColor(getResources().getColor(R.color.md_red_600));
        }

        Picasso.get()
                .load(urlGambar)
                .error(new IconicsDrawable(this).color(ContextCompat.getColor(this, R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(this).color(ContextCompat.getColor(this, R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .fit()
                .into(gambarImageView);

    }

}
