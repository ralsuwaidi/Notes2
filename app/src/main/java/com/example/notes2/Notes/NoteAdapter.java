package com.example.notes2.Notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notes2.R;

import java.util.List;




public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private List<Note> noteList;


    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);


        return new NoteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.MyViewHolder holder, int position) {

        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Note item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
        }


    }
}
