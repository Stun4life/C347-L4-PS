package com.example.c347l4ps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    Button btnShow;
    ArrayAdapter<Song> aa;
    ArrayList<Song> al;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        lv = findViewById(R.id.lv);
        Button btnShow = findViewById(R.id.btnShow);
        al = new ArrayList<Song>();
        aa = new ArrayAdapter<Song>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

        DBHelper db = new DBHelper(this);
        ArrayList<Song> songs = db.getAllSongs();
        aa = new SongArrayAdapter(this, R.layout.row, songs);

        lv.setAdapter(aa);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al.clear();
                al.addAll(db.getAllSongs());

                aa.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song target = al.get(position);
                Intent thirdActivity = new Intent(SecondActivity.this, ThirdActivity.class);
                thirdActivity.putExtra("data", target);
                startActivityForResult(thirdActivity, 5);
            }
        });
    }
}