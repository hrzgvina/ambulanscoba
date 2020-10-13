package com.example.ambulans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisActivityRider extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etUsername, etPassword, etNama;
    Button btnLogin;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_rider);
        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.pw);
        etNama = findViewById(R.id.et_nama);

        //etEmail = findViewById(R.id.email);
        btnLogin = findViewById(R.id.btn_login);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin = new Intent(RegisActivityRider.this, LogActivity.class);
                startActivity(goLogin);
                finish();
            }
        });

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String nama = etNama.getText().toString();


                if (username.equals("")) {
                    Toast.makeText(RegisActivityRider.this, "Silahkan isi username Anda.",
                            Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(RegisActivityRider.this, "Silahkan isi password Anda.",
                            Toast.LENGTH_SHORT).show();
                } else if (nama.equals("")) {
                    Toast.makeText(RegisActivityRider.this, "Silahkan isi nama Anda.",
                            Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(RegisActivityRider.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        //FirebaseUser user_id = mAuth.getCurrentUser();
                                        String user_id = mAuth.getCurrentUser().getUid();
                                        RegisterUser rider = new RegisterUser();
                                        rider.setNama(etNama.getText().toString());
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(user_id);
                                        current_user_db.setValue(rider);

                                        Toast.makeText(RegisActivityRider.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();


                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(RegisActivityRider.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();



                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }
    @Override

    public void onBackPressed(){
        Intent goLogin = new Intent(RegisActivityRider.this, LogActivity.class);
        startActivity(goLogin);
        finish();
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
