package ntt.bps.namberwan.darmawanita;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.Holder>{

    public static final String ID_BUKU = "id buku";
    public static final String JUDUL_BUKU = "judul buku";
    public static final String DESKRIPSI_BUKU = "deskripsi buku";
    public static final String TANGGAL_MASUK = "tanggal masuk buku";
    public static final String IS_DIPINJAM = "is dipinjam";
    public static final String PEMINJAM = "peminjam";
    public static final String TANGGAL_PINJAM = "tanggal pinjam";
    public static final String URL_GAMBAR = "url gambar";
    public static final String SUMBER_BUKU = "sumber buku";
    public static final String PDF_BUKU = "pdf buku";

    private List<BukuItem> list;
    private Context context;
    private RecyclerViewClickListener listener;

    public BukuAdapter(List<BukuItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private TextView judul;
        private TextView tgl;
        private TextView status;
        private ImageView gambar;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            tgl = itemView.findViewById(R.id.tanggal);
            status = itemView.findViewById(R.id.status);
            gambar = itemView.findViewById(R.id.gambar);
        }

        public void bind(final BukuItem bukuItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(bukuItem);
                }
            });
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_buku_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final BukuItem bukuItem = list.get(position);
        holder.bind(bukuItem, listener);
        holder.judul.setText(bukuItem.getJudul());
        //holder.tgl.setText(bukuItem.getTanggalMasuk());
        holder.tgl.setVisibility(View.GONE);
        Picasso.get()
                .load(bukuItem.getUrlGambar())
                .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .fit()
                .into(holder.gambar);
        if (!bukuItem.isDipinjam()){
            holder.status.setText("Available" );
            holder.status.setTextColor(context.getResources().getColor(R.color.md_green_600));
        }else {
            holder.status.setText("Dipinjam");
            holder.status.setTextColor(context.getResources().getColor(R.color.md_red_600));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

