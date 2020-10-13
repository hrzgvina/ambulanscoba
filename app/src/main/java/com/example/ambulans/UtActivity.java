package com.example.ambulans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UtActivity extends AppCompatActivity {
    private Button mDriver, mRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ut);
        mDriver = findViewById(R.id.regis_driver);
        mRider = findViewById(R.id.regis_rider);

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRegis = new Intent(UtActivity.this, RegisActivityDriver.class);
                startActivity(goRegis);
                finish();
            }
        });
        mRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRegis = new Intent(UtActivity.this, RegisActivityRider.class);
                startActivity(goRegis);
                finish();
            }
        });
    }
}