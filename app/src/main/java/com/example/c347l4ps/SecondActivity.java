package com.example.c347l4ps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private int REQUEST_CODE_MODIFY = 5;

    private boolean displayAll = true;

    DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        lv = findViewById(R.id.lv);
        Button btnShow = findViewById(R.id.btnShow);


        al = db.getAllSongs();
        aa = new SongArrayAdapter(this, R.layout.row, al);

        for (Song song: al) {
            Log.d("SecondActivity", song.toString() + "\n");
        }

        lv.setAdapter(aa);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al.clear();


                if(!displayAll){
                    al.addAll(db.getAllSongs());
                    displayAll = true;

                    btnShow.setText("Show All Songs with 5 Stars");

                }else{
                    ArrayList<Song> allSong = db.getAllSongs();

                    for(Song song : allSong){
                        if(song.getStars() == 5){
                            al.add(song);
                        }
                    }

                    btnShow.setText("Show All Songs");

                    displayAll = false;
                }

                aa.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song target = al.get(position);
                Intent thirdActivity = new Intent(SecondActivity.this, ThirdActivity.class);
                thirdActivity.putExtra("data", target);

                startActivityForResult(thirdActivity, REQUEST_CODE_MODIFY);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_MODIFY){
            displayAllData();
        }

    }

    public void displayAllData(){
        al.clear();
        al.addAll(db.getAllSongs());

        aa.notifyDataSetChanged();
    }
}