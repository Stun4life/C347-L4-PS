package com.example.c347l4ps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ThirdActivity.class.getSimpleName();

    // Database Helper
    DBHelper dbHelper = new DBHelper(this);

    // Data
    private Song selectedSong;

    // Views
    private TextView songIDTextView;
    private EditText songTitleEditText, singersEditText, yearEditText;
    private Button updateButton, deleteButton, cancelButton;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_song);

        // Initialize Views
        initViews();

        // Change Title of Top Bar
        getActionBar().setTitle("P05-NDPSongs ~ Modify Song");

        // Retrieved and set selected song
        Intent intent = getIntent();
        selectedSong = (Song) intent.getSerializableExtra("song");
    }

    private void initViews() {
        songIDTextView = findViewById(R.id.song_id_text_view);
        songTitleEditText = findViewById(R.id.song_title_edit_text);
        singersEditText = findViewById(R.id.singers_edit_text);
        yearEditText = findViewById(R.id.year_edit_text);

        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        cancelButton = findViewById(R.id.my_cancel_button);
        updateButton.setOnClickListener(this::onClick);
        deleteButton.setOnClickListener(this::onClick);
        cancelButton.setOnClickListener(this::onClick);

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setStepSize(1);
        ratingBar.setRating(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.update_button:
                doUpdateSong();
                break;
            case R.id.delete_button:
                doDeleteSong();
                break;
            case R.id.my_cancel_button:
                setResult(RESULT_CANCELED);
                finish();
        }

        if (id == updateButton.getId() || id == deleteButton.getId()) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private boolean validateFields(String title, String singers, String year, int numOfStarts) {
        return !title.isEmpty() && !singers.isEmpty() && !year.isEmpty() && numOfStarts != 0;
    }

    private void doUpdateSong() {
        String songTitle = songTitleEditText.getText() + "".trim();
        String singers = singersEditText.getText() + "".trim();
        String year = yearEditText.getText() + "".trim();
        int numOfStars = (int) ratingBar.getRating();

        boolean isValid = validateFields(songTitle, singers, year, numOfStars);

        if (isValid) {
            // Update Fields of Song
            selectedSong.setTitle(songTitle);
            selectedSong.setSingers(singers);
            selectedSong.setYear(Integer.parseInt(year));
            selectedSong.setStars(numOfStars);

            // Do Update in Database
            int result = dbHelper.updateSong(selectedSong);
            Log.d(TAG + "in doUpdateSong",  "Result > " + result);

            // Check Status
            if (result == 1) {
                Toast.makeText(this, "Successfully updated song", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update song", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Fill in all fields", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();
    }

    private void doDeleteSong() {

        // Delete Song with that ID
        int result = dbHelper.deleteSong(selectedSong.getId());
        Log.d(TAG + "in doDeleteSong", "Result > " + result);

        // Check Status
        if (result == 1) {
            Toast.makeText(this, "Successfully deleted the song", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete the song", Toast.LENGTH_SHORT).show();
        }
    }
}