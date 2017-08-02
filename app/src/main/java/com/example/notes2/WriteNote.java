package com.example.notes2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.notes2.NotesActivity.PREFS_NAME;
import static com.example.notes2.R.id.content_write;
import static com.example.notes2.R.id.home;
import static com.example.notes2.R.id.title_write;

public class WriteNote extends AppCompatActivity {

    public static String TITLES_SET = "titlesSet";
    public static String DATE_SET = "datesSet";
    String currentDateString = DateFormat.getDateInstance().format(new Date());

    String titleText;
    String contentText;
    String EXTRA_POS = "recyclerViewPositionClicked";

    ArrayList<String> titlesList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    Gson gson = new Gson();

    //DELETE BUTTON is clicked
    public void delete_button() {

        //delete title, content from file
        //get title text
        File dir = getFilesDir();
        EditText titleEditText = (EditText) findViewById(title_write);
        titleText = titleEditText.getText().toString();
        if (!titleText.isEmpty()) {

            //get title array from shared pref
            SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String json = sharedPref.getString(TITLES_SET, null);
            String json2 = sharedPref.getString(DATE_SET, null);
            ArrayList titlesSet = gson.fromJson(json, ArrayList.class);
            ArrayList dateSet = gson.fromJson(json2, ArrayList.class);

            //get position clicked
            Bundle extras = getIntent().getExtras();
            int position = extras.getInt(EXTRA_POS);

            //get file and delete it
            File fileTitle = new File(dir, fileNumber(position));
            fileTitle.delete();

            //remove title from list of titles
            titlesSet.remove(position);
            dateSet.remove(position);
            titlesList.clear();
            titlesList.addAll(titlesSet);
            dateList.clear();
            dateList.addAll(dateSet);

            //save new list of titles
            SharedPreferences.Editor editor = sharedPref.edit();
            json = gson.toJson(titlesList);
            editor.putString(TITLES_SET, json);
            editor.apply();
        }

        //start new intent
        Intent toNoteList = new Intent(WriteNote.this, NotesActivity.class);
        startActivity(toNoteList);
    }


    //SAVE BUTTON is clicked
    public void save_content(View view) {

        // TODO: 01/08/2017 dont save file to new file if they have the same title

        // 30/07/2017  save title text to string and add to string array
        EditText titleEditText = (EditText) findViewById(title_write);
        EditText contentEditText = (EditText) findViewById(content_write);
        titleText = titleEditText.getText().toString();
        contentText = contentEditText.getText().toString();


        // 31/07/2017 dont save if the title is empty
        if (!titleText.isEmpty()) {



            // 31/07/2017 show saved file if recycler view is clicked
            Bundle extras = getIntent().getExtras();
            int position = extras.getInt(EXTRA_POS);
            
            
                
            
            if(!titlesList.get(titlesList.size()-1).equals(titleText)){
                titlesList.add(titleText);
                dateList.add(currentDateString);
            }
           

            // 30/07/2017 save to a shared pref set
            SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();


            String json = gson.toJson(titlesList);
            String json2 = gson.toJson(dateList);
            editor.putString(DATE_SET, json2);
            editor.putString(TITLES_SET, json);
            editor.apply();

            // 30/07/2017 make a new save file with title as the name of the file, put content inside it
            String titleFileName = removeSpaces(titleText);
            writeToFile(contentText, getApplicationContext(), titleFileName);
        }


        // 30/07/2017 start the new activity to return to the main list menu
        Intent toNoteList = new Intent(WriteNote.this, NotesActivity.class);
        startActivity(toNoteList);
    }


    //when the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: 01/08/2017 change editview to textview on click 


        // 31/07/2017 show saved file if recycler view is clicked
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt(EXTRA_POS);

        //shared pref
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = sharedPref.getString(TITLES_SET, null);
        String json2 = sharedPref.getString(DATE_SET, null);
        ArrayList titlesSet = gson.fromJson(json, ArrayList.class);
        ArrayList dateSet = gson.fromJson(json2, ArrayList.class);


        if (titlesSet != null) {

            titlesList.clear();
            titlesList.addAll(titlesSet);
            dateList.clear();
            dateList.addAll(dateSet);
            if (position < titlesList.size()) {


                EditText titleEditText = (EditText) findViewById(title_write);

                EditText contentEditText = (EditText) findViewById(content_write);

                titleEditText.setText(titlesList.get(position));

                contentEditText.setText(checkString(position));
            }

        }


    }


    //create action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.write_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {

            delete_button();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        save_content(findViewById(home));
        return true;
    }


    //save note content to a file
    private void writeToFile(String data, Context context, String fileName) {


        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    //read content from the file
    private String readFromFile(Context context, Integer position) {

        String ret = "";


        try {
            InputStream inputStream = context.openFileInput(fileNumber(position));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString).append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    //access the file content depending on the position
    public String fileNumber(int position) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = sharedPref.getString(TITLES_SET, null);
        ArrayList titlesSet = gson.fromJson(json, ArrayList.class);
        List<String> titleStringSet = new ArrayList<>();
        titleStringSet.addAll(titlesSet);

        String file = titleStringSet.get(position);

        file = removeSpaces(file);
        return file;
    }

    private String checkString(int position) {


        return readFromFile(this, position);


    }

    private String removeSpaces(String string) {
        string = string.replace(" ", "");
        string = string.trim();
        string = string + ".txt";
        return string;
    }


}
