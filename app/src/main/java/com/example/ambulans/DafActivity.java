package com.example.ambulans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.ambulans.R;
import com.example.ambulans.model.Requests;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DafActivity extends AppCompatActivity {
    private DatabaseReference database;

    private ArrayList<Requests> daftarReq;
    private RequestAdapterRecyclerView requestAdapterRecyclerView;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daf);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = findViewById(R.id.rc_list_request);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(DafActivity.this,
                null, "Please wait", false);
        database.child("list_rs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : datasnapshot.getChildren()){

                Requests requests = noteDataSnapshot.getValue(Requests.class);
                requests.setKey(noteDataSnapshot.getKey());
                daftarReq.add(requests);
            }
            requestAdapterRecyclerView = new RequestAdapterRecyclerView(daftarReq);
            rc_list_request.setAdapter(requestAdapterRecyclerView);
            loading.dismiss();
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            loading.dismiss();
            }
        });

    }
}