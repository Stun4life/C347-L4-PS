package com.example.c347l4ps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SongArrayAdapter extends ArrayAdapter<Song> {

    private Context context;
    private ArrayList<Song> songs;
    private int resource;

    public SongArrayAdapter(Context context, int resource, ArrayList<Song> songs){
        super(context, resource, songs);
        this.context = context;
        this.songs = songs;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        // Inflate View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);

        // Views
        TextView tvYear = rowView.findViewById(R.id.tvYear);
        TextView tvSong = rowView.findViewById(R.id.tvSong);
        TextView tvName = rowView.findViewById(R.id.tvName);
        RatingBar ratingBar = rowView.findViewById(R.id.rating_bar);

        // Get Data
        Song song = songs.get(position);

        // Set Data of Views
        tvYear.setText(song.getYear());
        tvSong.setText(song.getTitle());
        tvName.setText(song.getSingers());
        ratingBar.setRating((float) song.getStars());

        return rowView;
    }

}
