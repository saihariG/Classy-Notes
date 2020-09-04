package com.classyinc.noteit.adapters;


import android.annotation.SuppressLint;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.classyinc.noteit.R;



import com.classyinc.noteit.entities.Note;
import com.classyinc.noteit.listeners.NotesListener;


import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> notes;
    private NotesListener notesListener;
    private Timer timer;
    private List<Note> notesSource;




    public NotesAdapter(List<Note> notes, NotesListener notesListener) {
        this.notes = notes;
        this.notesListener = notesListener;

        notesSource = notes;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           return new NoteViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_note,
                            parent,
                            false
                    )
            );


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

           NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
           noteViewHolder.setNote(notes.get(position));
           noteViewHolder.layoutNote.setOnClickListener(new View.OnClickListener() {
               @RequiresApi(api = Build.VERSION_CODES.KITKAT)
               @Override
               public void onClick(View v) {

                   notesListener.onNoteClicked(notes.get(position), position);

               }
           });

    }

    @Override
    public int getItemCount() {
        return notes.size() ;
    }

    @Override
    public int getItemViewType(int position) {



       return position ;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle , textSubtitle , textDateTime,textWeb;
        LinearLayout layoutNote;
        RoundedImageView RoundedimageNote;


        NoteViewHolder(@NonNull final View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.txtTitle_id);
            textSubtitle = itemView.findViewById(R.id.txtSubTitle_id);
            textDateTime = itemView.findViewById(R.id.txtDatetime_id);
            layoutNote = itemView.findViewById(R.id.layoutNote_id);
            RoundedimageNote = itemView.findViewById(R.id.RoundedimageNote_id);
            textWeb = itemView.findViewById(R.id.txtWEB_id);

        }

         void setNote(Note note) {
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            }else {
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDate_time());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();

            if(note.getColor().equals("#FFFFFF")) {

                textTitle.setTextColor(Color.parseColor("#000000"));
                textSubtitle.setTextColor(Color.parseColor("#000000"));
                textDateTime.setTextColor(Color.parseColor("#000000"));
            }


             if(note.getColor().equals("#333333")) {

                 textTitle.setTextColor(Color.parseColor("#FFFFFF"));
                 textSubtitle.setTextColor(Color.parseColor("#FFFFFF"));
                 textDateTime.setTextColor(Color.parseColor("#FFFFFF"));
             }

            if(note.getColor().equals("#000000")) {

                textTitle.setTextColor(Color.parseColor("#FFFFFF"));
                textSubtitle.setTextColor(Color.parseColor("#FFFFFF"));
                textDateTime.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if(note.getColor() != null ) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else {

                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getImagepath() != null ) {
                RoundedimageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagepath()));
                RoundedimageNote.setVisibility(View.VISIBLE);
            }else {
                RoundedimageNote.setVisibility(View.GONE);
            }

            if(note.getWeblink() != null) {
                textWeb.setText(note.getWeblink());
                textWeb.setVisibility(View.VISIBLE);
            }else {
                textWeb.setVisibility(View.GONE);
            }

        }
    }

    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()) {
                    notes = notesSource;
                }else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for(Note note : notesSource) {
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                     note.getNotetxt().toLowerCase().contains(searchKeyword.toLowerCase())  ) {

                                temp.add(note);

                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }

    public void cancelTimer() {

        if(timer != null) {
            timer.cancel();
        }

    }


}
