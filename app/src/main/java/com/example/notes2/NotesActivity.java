package com.example.notes2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.notes2.Notes.ItemClickSupport;
import com.example.notes2.Notes.Note;
import com.example.notes2.Notes.NoteAdapter;

import java.util.ArrayList;
import java.util.List;


public class NotesActivity extends AppCompatActivity {

    public List<Note> noteList = new ArrayList<>();
    public RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    public static final String PREFS_NAME = "MyPrefsFile";
    private int listSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_notes);
        mAdapter = new NoteAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //get shared pref
        SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);
        listSize = sharedPref.getInt("listSize", 0);



        //get intent from the newNote activity
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            boolean isNew = extras.getBoolean("IS_NEW");
            if (isNew) {

                extras = getIntent().getExtras();
                String titleText = extras.getString("EXTRA_TITLE_TEXT");
                addNote(titleText, "date");


            }
        }


        //when a recycler view item is clicked
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Snackbar.make(recyclerView, "this is text number "+position, BaseTransientBottomBar.LENGTH_LONG)
                 //       .setAction("Action", null).show();


                Bundle extras = getIntent().getExtras();
                if(extras!=null){
                    boolean isNew = extras.getBoolean("IS_NEW");

                }
                Intent writeIntent= new Intent(NotesActivity.this, WriteNote.class);
                writeIntent.putExtra("EXTRA_POSITION", position);
                startActivity(writeIntent);


            }
        });

        //float action button setting in the note list
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save shared pref
                SharedPreferences sharedPref= getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor= sharedPref.edit();
                editor.putInt("listSize", listSize+1);
                editor.commit();

                int noteSize=noteList.size();

                boolean isNew = true;
                Intent writeIntent= new Intent(NotesActivity.this, WriteNote.class);
                writeIntent.putExtra("EXTRA_POSITION", noteSize);
                writeIntent.putExtra("IS_NEW", isNew);

                startActivity(writeIntent);




            }
        });

        //can use this space to populate note list

        for(int i=0; i<listSize;i++){
            addNote("Title "+i, "Date"+i);
        }



    }


    public void addNote(String title, String date) {
        Note note = new Note(title, date);
        noteList.add(note);

        mAdapter.notifyDataSetChanged();
    }


}
