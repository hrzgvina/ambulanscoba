package com.example.ambulans;

import android.app.VoiceInteractor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ambulans.model.Requests;

import java.util.List;

public class RequestAdapterRecyclerView extends RecyclerView.Adapter<RequestAdapterRecyclerView.MyViewHolder> {
    private List<Requests> rslist;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout rl_layout;
        public TextView nama_rumah_sakit, alamat_rumah_sakit;

        public MyViewHolder(View view) {
            super(view);
                rl_layout = view.findViewById(R.id.rl_layout);
                nama_rumah_sakit = view.findViewById(R.id.nama_rumah_sakit);
                alamat_rumah_sakit = view.findViewById(R.id.alamat_rumah_sakit);
        }
    }
    public RequestAdapterRecyclerView(List<Requests> rslist) {this.rslist = rslist;}
        @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
                View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_requests, parent, false);
                return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Requests rs = rslist.get(position);
        holder.nama_rumah_sakit.setText(rs.getNama());
        holder.alamat_rumah_sakit.setText(rs.getAlamat());

    }
    @Override
    public int getItemCount(){
        return rslist.size();
    }
}
