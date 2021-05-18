package com.example.c347l4ps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "songList.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SONG = "song";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SINGERS = "singers";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_STARS = "stars";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSongTableSql = "CREATE TABLE " + TABLE_SONG + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"+ COLUMN_SINGERS + " TEXT," +
                " "+ COLUMN_YEAR + " INTEGER, "+ COLUMN_STARS + " INTEGER) ";
        db.execSQL(createSongTableSql);
        Log.i("info", "created tables");
        Log.d(TAG, createSongTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }

    public long insertSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, song.getTitle());
        values.put(COLUMN_SINGERS, song.getSingers());
        values.put(COLUMN_YEAR, song.getYear());
        values.put(COLUMN_STARS, song.getStars());
        long result = db.insert(TABLE_SONG, null, values);
        db.close();
        Log.d("SQL Insert","ID:"+ result); //id returned, shouldn’t be -1
        return result;
    }
    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<Song>();

        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_TITLE + "," + COLUMN_SINGERS + "," + COLUMN_YEAR + ","
                + COLUMN_STARS + " FROM " + TABLE_SONG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String songTitle = cursor.getString(1);
                String songSingers = cursor.getString(2);
                int songYear = cursor.getInt(3);
                int songStars = cursor.getInt(4);
                Song song = new Song(songTitle, songSingers, songYear, songStars);
                song.setId(id);
                songs.add(song);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }

    public int updateSong(Song data){

        Log.d(TAG, data.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, data.getTitle());
        values.put(COLUMN_SINGERS, data.getSingers());
        values.put(COLUMN_YEAR, data.getYear());
        values.put(COLUMN_STARS, data.getStars());
        String condition = COLUMN_ID + " = ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_SONG, values, condition, args);
        db.close();

        Log.d(TAG, "result: " + result);
        return result;
    }


    public int deleteSong(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + " = ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_SONG, condition, args);
        db.close();

        return result;
    }

    // Sort By Year
    public HashMap<Integer, ArrayList<Song>> getSongsByYear() {

        // Map Data Structure - a year can have many songs
        // key -> Year, value -> Songs
        HashMap<Integer, ArrayList<Song>> hashMap = new HashMap<>();

        // Get all songs
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                TABLE_SONG,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String singers = cursor.getString(2);
                int year = cursor.getInt(3);
                int stars = cursor.getInt(4);

                Song song = new Song(title, singers, year, stars);
                song.setId(id);

                if (!hashMap.containsKey(year)) {
                    // create an array if the year of the song is not in the map
                    // add song to songs array
                    // new key value pair where key is the year
                    ArrayList<Song> songs = new ArrayList<>();
                    songs.add(song);
                    hashMap.put(year, songs);
                } else {
                    // add to an existing songs array
                    // update the value of key (year) to the update songs array
                    ArrayList<Song> songs = hashMap.get(year);
                    songs.add(song);
                    hashMap.put(year, songs);
                }

            } while (cursor.moveToNext());
        }

        // check if all keys (years) are distinct
        for (Map.Entry<Integer, ArrayList<Song>> entry: hashMap.entrySet()) {
            Log.d(TAG + "in getSongsByYear", entry.getKey() + "");
        }

        return hashMap;
    }

}
