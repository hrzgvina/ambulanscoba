package com.example.ambulans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ambulans.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //GridLayout mainGrid;
    CardView listhospital, nearestplace, orderambulance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listhospital = (CardView) findViewById(R.id.list_hospital);
        nearestplace = (CardView) findViewById(R.id.nearestplace);
        orderambulance = (CardView) findViewById(R.id.order_ambulance);


        listhospital.setOnClickListener(this);
        nearestplace.setOnClickListener(this);
        orderambulance.setOnClickListener(this);
        //mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        //setSingleEvent(mainGrid);

    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()) {
            case R.id.list_hospital :
                i = new Intent(this,DafActivity.class);
                startActivity(i);
                break;
            case R.id.nearestplace :
                i = new Intent(this,MarkerActivity.class);
                startActivity(i);
                break;
            case R.id.order_ambulance :
                i = new Intent(this,PesanActivity.class);
                startActivity(i);
                break;

        }

    }



    /*private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for(int i=0;i<mainGrid.getChildCount();i++)
        {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int finalI=i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0)
                    {
                        Intent intent = new Intent(MainActivity.this, DafActivity.class);
                        startActivity(intent);
                    }
                    else if (finalI == 1)
                    {
                        Intent intent = new Intent(MainActivity.this, MarkerActivity.class);
                        startActivity(intent);
                    }
                    else if (finalI == 2){
                        Intent intent = new Intent(MainActivity.this, PesanActivity.class);
                        startActivity(intent);

                    }
                    else
                        {
                           Toast.makeText(MainActivity.this, "Please set Activity for this card item", Toast.LENGTH_SHORT).show();
                        }

                }
            });
        }
    }*/

}