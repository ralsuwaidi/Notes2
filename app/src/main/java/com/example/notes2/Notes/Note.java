package com.example.notes2.Notes;

/**
 * Created by Rashid on 26/07/2017.
 */

public class Note {
    private String title, date;

    public Note(String title, String date){
        this.title=title;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
