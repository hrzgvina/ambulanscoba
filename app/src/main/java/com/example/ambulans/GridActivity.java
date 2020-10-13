package com.example.ambulans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GridActivity extends AppCompatActivity {
GridView gridView;
    String[] itemNames = {"List", "Terdekat", "Pesan"};
    int[] itemImages = {R.drawable.ic_launcher_hospital, R.drawable.ic_launcher_places, R.drawable.ic_launcher_order};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridView = findViewById(R.id.gridview);

        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {
                switch (position){
                    case 0:
                        Intent newActivity = new Intent(GridActivity.this, DafActivity.class);
                        startActivity(newActivity);
                        break;
                    case 1:
                        Intent new1Activity = new Intent(GridActivity.this, MapsMarkerActivity.class);
                        startActivity(new1Activity);
                        break;
                    case 2:
                        Intent new2Activity = new Intent(GridActivity.this, MarkerActivity.class);
                        startActivity(new2Activity);
                        break;
                    default:
                        Toast.makeText(GridActivity.this, "Wrong Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return itemImages.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data, null);


            TextView name = view1.findViewById(R.id.item);
            ImageView image = view1.findViewById(R.id.images);

            name.setText(itemNames[i]);
            image.setImageResource(itemImages[i]);
            return view1;
        }
    }
}