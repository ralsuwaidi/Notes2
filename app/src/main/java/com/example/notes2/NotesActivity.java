package com.example.notes2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.notes2.Notes.ItemClickSupport;
import com.example.notes2.Notes.Note;
import com.example.notes2.Notes.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.notes2.R.id.title_write;

public class NotesActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();
    public RecyclerView recyclerView;
    private NoteAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_notes);

        mAdapter = new NoteAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Snackbar.make(recyclerView, "this is text number "+position, BaseTransientBottomBar.LENGTH_LONG)
                 //       .setAction("Action", null).show();


                Intent writeIntent= new Intent(NotesActivity.this, WriteNote.class);
                writeIntent.putExtra("EXTRA_POSITION", position);
                startActivity(writeIntent);


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                int noteSize=noteList.size();
                Intent writeIntent= new Intent(NotesActivity.this, WriteNote.class);
                writeIntent.putExtra("EXTRA_POSITION", noteSize);
               startActivity(writeIntent);
            }
        });

        addNote("start", "date");




    }

    public void addNote(String title, String date) {
        Note note = new Note(title, date);
        noteList.add(note);

        mAdapter.notifyDataSetChanged();
    }



}
