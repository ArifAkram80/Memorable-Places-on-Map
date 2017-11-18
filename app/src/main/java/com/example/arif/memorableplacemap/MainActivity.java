package com.example.arif.memorableplacemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static  ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView List_Of_Places = (ListView) findViewById(R.id.List_Of_Places);


        places.add("Add a new Place");
        locations.add(new LatLng(0,0));

         arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);

        List_Of_Places.setAdapter(arrayAdapter);
        List_Of_Places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("PlaceNumber", i);
                startActivity(intent);
            }
        });

    }
}
