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

import com.example.notes2.Notes.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.List;

import static android.R.id.button1;
import static com.example.notes2.R.id.btn_save;
import static com.example.notes2.R.id.content_write;
import static com.example.notes2.R.id.title_write;

public class WriteNote extends AppCompatActivity {




    //when the save button is clicked
    public void save_content(View view) {

        EditText journal = (EditText) findViewById(content_write);
        String journalText = journal.getText().toString();

        EditText title= (EditText) findViewById(title_write);
        String titleText = title.getText().toString();

        writeToFile(journalText, this);
        writeToFile2(titleText, this);

        Intent toNoteList= new Intent(WriteNote.this, NotesActivity.class);

        Bundle extras = getIntent().getExtras();
        boolean isNew = extras.getBoolean("IS_NEW");
        if (isNew){
            toNoteList.putExtra("IS_NEW", isNew);
        }

        toNoteList.putExtra("EXTRA_TITLE_TEXT", titleText);
        //toNoteList.putExtra("EXTRA_LIST", );
        startActivity(toNoteList);


    }

    //when the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //extras
        Bundle extras = getIntent().getExtras();
        boolean isNew = extras.getBoolean("IS_NEW");

        if(isNew==false){
            EditText journal = (EditText) findViewById(content_write);
            journal.append(readFromFile(this));

            EditText titleText = (EditText) findViewById(title_write);
            titleText.append(readFromTitle(this));
        }

    }

    //save note content to a file
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

    //save title to a file
    private void writeToFile2(String data,Context context) {
        Bundle extras = getIntent().getExtras();

        int position = extras.getInt("EXTRA_POSITION");
        //The key argument here must match that used in the other activity

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(titleNumber(position), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    //read content from the file
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

    //read title from the file
    private String readFromTitle(Context context) {

        String ret = "";
        Bundle extras = getIntent().getExtras();

        int position = extras.getInt("EXTRA_POSITION");


        try {
            InputStream inputStream = context.openFileInput(titleNumber(position));

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



    //when back button is pressed
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Thanks for using application!!", Toast.LENGTH_LONG).show();
        finish();

        EditText journal = (EditText) findViewById(content_write);
        String journalText = journal.getText().toString();

        EditText title= (EditText) findViewById(title_write);
        String titleText = title.getText().toString();

        writeToFile(journalText, this);
        writeToFile2(titleText, this);

        return;
    }


    //access the file content depending on the position
    public String fileNumber(int position){
        String file = "note"+position+".txt";
        return file;
    }

    //acess file title depending on the position
    public String titleNumber(int position){
        String file = "title"+position+".txt";
        return file;
    }




}
