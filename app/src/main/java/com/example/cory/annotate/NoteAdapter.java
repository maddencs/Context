package com.example.cory.annotate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/*
 * Created by cory on 9/21/15.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private final Note[] notes;

    public NoteAdapter(Context context, Note[] notes) {
        super(context, -1, notes);
        this.context = context;
        this.notes = notes;
    }

    static class ViewHolder{
        TextView title;
        TextView bodyPreview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();
        final Note note = notes[position];
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_note, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.noteListItemTitle);
            viewHolder.bodyPreview = (TextView) convertView.findViewById(R.id.noteListItemPreview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(notes[position].getNoteTitle());
        try {
            viewHolder.bodyPreview.setText(notes[position].getBody().substring(0, 20) + "...");
        } catch (StringIndexOutOfBoundsException e){
        viewHolder.bodyPreview.setText(notes[position].getBody());
    }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
               Dialog dialog = new Dialog(context);
               dialog.setContentView(R.layout.long_click_note);
               TextView deleteNote = (TextView) dialog.findViewById(R.id.dialogDeleteNote);
               TextView editNote = (TextView) dialog.findViewById(R.id.dialogEditeNote);
               deleteNote.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       DBManager manager = new DBManager(context);
                       manager.deleteNote(note.getId());
                       Intent intent = new Intent(context, AvailableNetworks.class);
                       context.startActivity(intent);
                   }
               });
               dialog.show();
               return false;
           }
       });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Note.class);
                intent.putExtra("title", note.getNoteTitle());
                intent.putExtra("body", note.getBody());
                context.startActivity(intent);
            }
        });
        return convertView;

    }
}
