package com.example.ambulans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogDriverActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_driver);
        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        mAuth = FirebaseAuth.getInstance();

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRegister = new Intent(LogDriverActivity.this, RegisActivityDriver.class);
                startActivity(goRegister);
                finish();
            }
        });

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.equals("")){
                    Toast.makeText(LogDriverActivity.this, "Silahkan input username", Toast.LENGTH_SHORT).show();

                } else if (password.equals("")){
                    Toast.makeText(LogDriverActivity.this, "Silahkan input password", Toast.LENGTH_SHORT).show();

                } else {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(LogDriverActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent accountIntent= new Intent(LogDriverActivity.this, GridDriverActivity.class);
                                        startActivity(accountIntent);
                                        finish();
                                        /*Toast.makeText(LogActivity.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();*/
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LogDriverActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });

                }
            }
        });
    }

    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}