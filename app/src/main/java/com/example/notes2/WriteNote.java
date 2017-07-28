package com.example.notes2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

import static android.R.id.button1;
import static com.example.notes2.R.id.btn_save;
import static com.example.notes2.R.id.content_write;
import static com.example.notes2.R.id.title_write;

public class WriteNote extends AppCompatActivity {

    String fileName;

    //ToDo: make list send number and the number can be used to call the list. make fab call the list size plus one and use empty variable to make a new note

    public void save_content(View view) {

        EditText title= (EditText) findViewById(title_write);
        String titleText = title.getText().toString();

        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("EXTRA_POSITION");

        Intent toNoteList= new Intent(WriteNote.this, NotesActivity.class);


        toNoteList.putExtra("EXTRA_TITLE_TEXT", titleText);
        //toNoteList.putExtra("EXTRA_LIST", );
        startActivity(toNoteList);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText journal = (EditText) findViewById(content_write);
        journal.append(readFromFile(this));


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int position = extras.getInt("EXTRA_POSITION");
            //The key argument here must match that used in the other activity


            EditText title= (EditText) findViewById(title_write);
            title.append("This is the title"+position);
        }


    }

    private void writeToFile(String data,Context context) {
        Bundle extras = getIntent().getExtras();

            int position = extras.getInt("EXTRA_POSITION");
            //The key argument here must match that used in the other activity

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileNumber(position), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile(Context context) {

        String ret = "";
        Bundle extras = getIntent().getExtras();

        int position = extras.getInt("EXTRA_POSITION");


        try {
            InputStream inputStream = context.openFileInput(fileNumber(position));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }



    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Thanks for using application!!", Toast.LENGTH_LONG).show();
        finish();

        EditText journal = (EditText) findViewById(content_write);
        String journalText = journal.getText().toString();

        EditText title= (EditText) findViewById(title_write);
        String titleText = title.getText().toString();

        //fact.addNote("ss", "ww");
        writeToFile(journalText, this);




        return;
    }


    public String fileNumber(int position){
        String file = "note"+position+".txt";
        return file;
    }




}
