package com.example.c347l4ps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etSongTitle, etSingers, etYear;
    private RadioGroup ratingRadioGroup;
    private RadioButton selectedRatingRadio, r1Default;

    private Button btnInsert, btnShowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSongTitle = findViewById(R.id.etSongTitle);
        etSingers = findViewById(R.id.etSongSinger);
        etYear = findViewById(R.id.etSongYear);

        ratingRadioGroup = findViewById(R.id.ratingRadioGroup);
        r1Default = findViewById(R.id.r1);
        r1Default.setChecked(true);

        btnInsert = findViewById(R.id.btnInsert);
        btnShowList = findViewById(R.id.btnShowList);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSongTitle.getText().toString().equals("") && !etSingers.getText().toString().equals("") && !etYear.getText().toString().equals("")){
                    String title = etSongTitle.getText().toString();
                    String singer = etSingers.getText().toString();

                    String stringYear = etYear.getText().toString();

                    // get selected radio button from radioGroup
                    int selectedId = ratingRadioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    selectedRatingRadio = (RadioButton) findViewById(selectedId);
                    int selectedRating = Integer.parseInt((String) selectedRatingRadio.getText());

                    try {
                        int intYear = Integer.parseInt(stringYear);
                        //Log.i("",num+" is a number");
                        DBHelper dbh = new DBHelper(MainActivity.this);

                        Song song = new Song(title, singer, intYear, selectedRating);

                        long inserted_id = dbh.insertSong(song);
                        dbh.close();

                        if(inserted_id != -1){
                            Toast.makeText(MainActivity.this, "Song has been successfully added", Toast.LENGTH_SHORT).show();
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid year", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this,
                            "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showActivity = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(showActivity);
            }
        });

    }
}