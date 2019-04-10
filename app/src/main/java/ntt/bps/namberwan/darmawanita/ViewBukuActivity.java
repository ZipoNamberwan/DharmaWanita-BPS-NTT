package ntt.bps.namberwan.darmawanita;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class ViewBukuActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "id notifikasi channel darma wanita NTT";

    private static final int NOTIFICATION_DOWNLOAD_ID = 147852369;

    private String id;
    private String judul;
    private String deskripsi;
    private String tanggalMasuk;
    private boolean isDipinjam;
    private String peminjam;
    private String tanggalPinjam;
    private String urlGambar;
    private String sumber;
    private String pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_buku);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        id = getIntent().getStringExtra(BukuAdapter.ID_BUKU);
        judul = getIntent().getStringExtra(BukuAdapter.JUDUL_BUKU);
        deskripsi = getIntent().getStringExtra(BukuAdapter.DESKRIPSI_BUKU);
        tanggalMasuk = getIntent().getStringExtra(BukuAdapter.TANGGAL_MASUK);
        isDipinjam = getIntent().getBooleanExtra(BukuAdapter.IS_DIPINJAM, false);
        peminjam = getIntent().getStringExtra(BukuAdapter.PEMINJAM);
        tanggalPinjam = getIntent().getStringExtra(BukuAdapter.TANGGAL_PINJAM);
        urlGambar = getIntent().getStringExtra(BukuAdapter.URL_GAMBAR);
        sumber = getIntent().getStringExtra(BukuAdapter.SUMBER_BUKU);
        pdf  = getIntent().getStringExtra(BukuAdapter.PDF_BUKU);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_download) {
            String namaFile = judul.replaceAll("\\W+", "");
            download(this, pdf, "Download: " + judul, namaFile + ".pdf");

        }
        return super.onOptionsItemSelected(item);
    }

    private void download(final Activity activity, String url, final String title, String file) {

        //Buat format notifikasi
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.build();

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

        //Konfigurasi download
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(activity)
                .setDownloadConcurrentLimit(3)
                .enableFileExistChecks(false)
                .build();
        final Fetch fetch = Fetch.Impl.getInstance(fetchConfiguration);

        //Listener download
        FetchListener listener = new FetchListener() {
            @Override
            public void onAdded(@NotNull Download download) {
                mBuilder.setSmallIcon(R.drawable.ic_file_download_black_24dp)
                        .setProgress(100, 0, true)
                        .setContentTitle(title)
                        .setOnlyAlertOnce(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "download buku channel", importance);
                    channel.setDescription("download buku channel");
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                    manager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                } else {
                    notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }
            }

            @Override
            public void onQueued(@NotNull Download download, boolean b) {

            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {

            }

            @Override
            public void onCompleted(@NotNull Download download) {

                String filePath = download.getFile();
                File file = new File(filePath);
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext=file.getName().substring(file.getName().indexOf(".")+1);
                String type = mime.getMimeTypeFromExtension(ext);

                Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".ntt.bps.namberwan.darmawanita.provider", file);

                Intent openFile = new Intent(Intent.ACTION_VIEW, uri);
                openFile.setDataAndType(uri, type);
                openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, openFile, 0);

                mBuilder.setSmallIcon(R.drawable.ic_file_download_black_24dp)
                        .setOngoing(false)
                        .setContentText("Download Selesai")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "download buku channel", importance);
                    channel.setDescription("download buku channel");
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                    manager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }else {
                    notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }

                fetch.remove(download.getId());

            }

            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {
                mBuilder.setSmallIcon(R.drawable.ic_file_download_black_24dp)
                        .setOngoing(false)
                        .setContentText("Download Gagal :(");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "download buku channel", importance);
                    channel.setDescription("download buku channel");
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                    manager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }else {
                    notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }

                fetch.remove(download.getId());
            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

            }

            @Override
            public void onProgress(@NotNull Download download, long l, long l1) {
                mBuilder.setProgress(100, download.getProgress(), false)
                        .setOngoing(true)
                        .setContentText(getETAString(activity, l));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "download buku channel", importance);
                    channel.setDescription("download buku channel");
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                    manager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }else {
                    notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, mBuilder.build());
                }
            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        };

        //Download File
        String destinationFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + file;

        final Request request = new Request(url, destinationFile);
        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);

        fetch.addListener(listener);
        fetch.enqueue(request, new Func<Request>() {
            @Override
            public void call(@NotNull Request result) {
                int i = 0;
            }
        }, new Func<Error>() {
            @Override
            public void call(@NotNull Error result) {
                int i = 0;
            }
        });
    }

    private static String getETAString(Activity activity, final long etaInMilliSeconds) {
        if (etaInMilliSeconds < 0) {
            return "";
        }
        int seconds = (int) (etaInMilliSeconds / 1000);
        long hours = seconds / 3600;
        seconds -= hours * 3600;
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        if (hours > 0) {
            return activity.getString(R.string.download_eta_hrs, hours, minutes, seconds);
        } else if (minutes > 0) {
            return activity.getString(R.string.download_eta_min, minutes, seconds);
        } else {
            return activity.getString(R.string.download_eta_sec, seconds);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
