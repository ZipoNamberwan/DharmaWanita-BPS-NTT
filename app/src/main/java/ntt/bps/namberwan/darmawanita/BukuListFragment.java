package ntt.bps.namberwan.darmawanita;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BukuListFragment extends Fragment {

    public static final String SEARCH_KEYWORD = "KEYWORD";

    private ArrayList<BukuItem> list;
    private BukuAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private boolean isLoading;
    private RequestQueue queue;
    private String search;

    public BukuListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buku_list, container, false);

        assert getArguments() != null;
        search = getArguments().getString(SEARCH_KEYWORD);
        if (!"".equals(search)){
            search = "&keyword="+search;
        }

        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        recyclerView = view.findViewById(R.id.listview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        isLoading = false;
        progressBar = view.findViewById(R.id.loading);
        recyclerView.setVisibility(View.GONE);

        list = new ArrayList<>();
        /*for (int i = 0; i < 10; i++){
            list.add(new BukuItem(i+"", "Buku "+ i, "Deskripsi "+i, "2018-02-02",false,"Peminjam "+i,"2018-02-02","https://images-na.ssl-images-amazon.com/images/I/51EstVXM1UL._SX331_BO1,204,203,200_.jpg"));
        }*/
        adapter = new BukuAdapter(list, getActivity(), new RecyclerViewClickListener() {
            @Override
            public void onItemClick(BukuItem item) {
                Intent i = new Intent(getActivity(), ViewBukuActivity.class);
                i.putExtra(BukuAdapter.ID_BUKU, item.getId());
                i.putExtra(BukuAdapter.JUDUL_BUKU,item.getJudul());
                i.putExtra(BukuAdapter.DESKRIPSI_BUKU,item.getDeskripsi());
                i.putExtra(BukuAdapter.TANGGAL_MASUK,item.getTanggalMasuk());
                i.putExtra(BukuAdapter.IS_DIPINJAM,item.isDipinjam());
                i.putExtra(BukuAdapter.PEMINJAM,item.getPeminjam());
                i.putExtra(BukuAdapter.TANGGAL_PINJAM,item.getTanggalPinjam());
                i.putExtra(BukuAdapter.URL_GAMBAR,item.getUrlGambar());
                i.putExtra(BukuAdapter.SUMBER_BUKU, item.getSumber());
                getActivity().startActivity(i);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                addDataToArray(page + 1);
            }
        });

        addDataToArray(0);
        return view;
    }
    private void addDataToArray(final int page) {
        isLoading = true;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getString(R.string.web_service)+"dwapi?page="+page+search, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        addJSONToAdapter(response, page);
                        isLoading = false;
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
            }
        });

        queue.add(request);
    }

    private void addJSONToAdapter(JSONArray response, int page) {
        try {
            for (int i = 0; i < response.length(); i++){
                list.add(new BukuItem(response.getJSONObject(i).getString("id"),
                        response.getJSONObject(i).getString("judul"),response.getJSONObject(i).getString("deskripsi"),
                        AppUtil.getDate(response.getJSONObject(i).getString("tanggalmasuk"),false),
                        !response.getJSONObject(i).getString("is_dipinjam").equals("0"),response.getJSONObject(i).getString("peminjam"),
                        AppUtil.getDate(response.getJSONObject(i).getString("tanggalpinjam"), false),
                        getString(R.string.web_service)+response.getJSONObject(i).getString("gambar"),
                        response.getJSONObject(i).getString("sumber")));
            }
            adapter.notifyItemRangeInserted(page * 10 + 1,response.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
