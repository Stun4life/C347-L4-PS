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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = SecondActivity.class.getSimpleName();

    // Views
    private Button btnShow;

    // List View Components
    private ArrayAdapter<Song> songArrayAdapter;
    private ArrayList<Song> songs = new ArrayList<>();
    private ListView listView;

    // Spinner Components
    private Spinner yearsSpinner;
    private HashMap<Integer, ArrayList<Song>> songsByYear;
    private ArrayList<String> years = new ArrayList<>();
    private ArrayAdapter<String> yearArrayAdapter;

    private int REQUEST_CODE_MODIFY = 5;

    private boolean displayAll = true;

    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialize Views
        listView = findViewById(R.id.lv);
        Button btnShow = findViewById(R.id.btnShow);
        yearsSpinner = findViewById(R.id.years_spinner);
        yearsSpinner.setOnItemSelectedListener(this);

        // Populate Spinner with Years
        songsByYear = dbHelper.getSongsByYear();
        for (Map.Entry<Integer, ArrayList<Song>> entry: songsByYear.entrySet()) {
            years.add(entry.getKey() + "");
            Log.d(TAG, entry.getKey() + "");
        }

        // Initialize Spinner Components
        yearArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, years);
        yearsSpinner.setAdapter(yearArrayAdapter);

        songs.clear();
        songs.addAll(dbHelper.getAllSongs());
        songArrayAdapter = new SongArrayAdapter(this, R.layout.song_list_item, songs);

        for (Song song: songs) {
            Log.d("SecondActivity", song.toString() + "\n");
        }

        listView.setAdapter(songArrayAdapter);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songs.clear();


                if(!displayAll){
                    songs.addAll(dbHelper.getAllSongs());
                    displayAll = true;

                    btnShow.setText("Show All Songs with 5 Stars");

                }else{
                    ArrayList<Song> allSong = dbHelper.getAllSongs();

                    for(Song song : allSong){
                        if(song.getStars() == 5){
                            songs.add(song);
                        }
                    }

                    btnShow.setText("Show All Songs");

                    displayAll = false;
                }

                songArrayAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song target = songs.get(position);
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
        songs.clear();
        songs.addAll(dbHelper.getAllSongs());

        songArrayAdapter.notifyDataSetChanged();
    }


    // Item in Spinner is Selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "in onItemSelected");
        int year = Integer.parseInt(years.get(position));
        songs.clear();
        songs.addAll(songsByYear.get(year));
        songArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do Nothing
    }
}