package com.example.ambulans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PesanActivity extends AppCompatActivity {
    private Button mSaveButton;
    private EditText mPemesan, mRumahSakit, mNomor, mEmail, mLatitude, mLongitude;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        mSaveButton = (Button)findViewById(R.id.btn_pesan);
        mPemesan = (EditText)findViewById(R.id.nama_pemesan);
        mRumahSakit = (EditText)findViewById(R.id.nama_rs);
        mNomor = (EditText)findViewById(R.id.nomor_telepon);
        mEmail = (EditText)findViewById(R.id.email);
        mLatitude = (EditText)findViewById(R.id.latitude);
        mLongitude = (EditText)findViewById(R.id.longitude);

        mFirestore = FirebaseFirestore.getInstance();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pemesan = mPemesan.getText().toString();
                String rs = mRumahSakit.getText().toString();
                String telepon = mNomor.getText().toString();
                String mail = mEmail.getText().toString();
                String lati = mLatitude.getText().toString();
                String longi = mLongitude.getText().toString();

                Map<String, Object> pesanMap = new HashMap<>();
                pesanMap.put("nama_pemesan", pemesan);
                pesanMap.put("nama_rs", rs);
                pesanMap.put("nomor_telephone", telepon);
                pesanMap.put("email", mail);
                pesanMap.put("latitude", lati);
                pesanMap.put("longitude", longi);

// Add a new document with a generated ID
                mFirestore.collection("pesan")
                        .add(pesanMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(PesanActivity.this, "Pesanan telah masuk", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String error = e.getMessage();
                                Toast.makeText(PesanActivity.this, "Error", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

    }
}