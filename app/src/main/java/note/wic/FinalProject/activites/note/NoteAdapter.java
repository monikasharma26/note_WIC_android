package note.wic.FinalProject.activites.note;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import note.wic.FinalProject.database.NotesDAO;
import note.wic.FinalProject.events.NoteDeletedEvent;
import note.wic.FinalProject.events.NoteEditedEvent;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.utils.NCViewHolder;
import note.wic.FinalProject.view.NoteCardView;

public class NoteAdapter extends RecyclerView.Adapter{

    private static final String TAG = "Adapter";
    private final Folder folder;
    String data = "";
    List<Note> notesList;
    List<Note> notes;
    View.OnClickListener noteOnClickListener = new View.OnClickListener(){
        @Override public void onClick(View v){
            if (v instanceof NoteCardView){
                NoteCardView noteCardView = (NoteCardView) v;
                Intent intent = new NoteActivityIntentBuilder().noteId(noteCardView.getNote().getId()).build(v.getContext());
                v.getContext().startActivity(intent);
            }
        }
    };


    private View zeroNotesView;
    public NoteAdapter(View zeroNotesView, Folder folder){
        this.zeroNotesView = zeroNotesView;
        this.folder = folder;
    }
    public NoteAdapter(View zeroNotesView, Folder folder,String s){
        this.zeroNotesView = zeroNotesView;
        this.folder = folder;
        this.data = s;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = new NoteCardView(parent.getContext());
        view.setOnClickListener(noteOnClickListener);
        return new NCViewHolder(view);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if (holder.itemView instanceof NoteCardView){
            NoteCardView noteCardView = (NoteCardView) holder.itemView;
            noteCardView.bindModel(notes.get(position));
        }
    }

    @Override public int getItemCount(){
        int size = notes == null ? 0 : notes.size();
        if (size == 0) zeroNotesView.setVisibility(View.VISIBLE);
        else zeroNotesView.setVisibility(View.GONE);
        return size;
    }

    void loadFromDatabase(){
        notes = NotesDAO.getLatestNotes(folder);
        notifyDataSetChanged();
        notesList = new ArrayList<>(notes);
    }

    void registerEventBus(){
        EventBus.getDefault().register(this);
    }

    void unregisterEventBus(){
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true) public void onNoteEditedEvent(NoteEditedEvent noteEditedEvent){
        Note note = noteEditedEvent.getNote();
        if (notes.contains(note)){
            int index = notes.indexOf(note);
            notes.set(index, note);
            notifyItemChanged(index);
        }else{
            notes.add(0, note);
            notifyItemInserted(0);
        }
        EventBus.getDefault().removeStickyEvent(NoteEditedEvent.class);
    }

    @Subscribe(sticky = true) public void onNoteDeletedEvent(NoteDeletedEvent noteDeletedEvent){
        final Note note = noteDeletedEvent.getNote();
        if (notes.contains(note)){
            int index = notes.indexOf(note);
            notes.remove(index);
            notifyItemRemoved(index);
            Snackbar
                    .make(zeroNotesView, "Deleted Note " + note.getTitle(), Snackbar.LENGTH_LONG)
                    .setAction("UNDO",
                            new View.OnClickListener(){
                                @Override public void onClick(View v){
                                    note.save();
                                    EventBus.getDefault().post(new NoteEditedEvent(note.getId()));
                                }
                            })
                    .show();
        }
        EventBus.getDefault().removeStickyEvent(NoteDeletedEvent.class);
    }
    public Filter getFilter() {
        return notesFilter;
    }



    private Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(notesList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Note item : notesList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern) || item.getBody().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public  void getByTitle(){
        if(notesList.size()!=0) {
            Collections.sort(notes, new Comparator<Note>() {
                public int compare(Note o1, Note o2) {
                    return o1.getBody().compareTo(o2.getBody());
                }
            });
            notifyDataSetChanged();
        }
    }

    public  void getByDate(){
        if(notes.size()!=0){
            Collections.sort(notes, new Comparator<Note>() {
                public int compare(Note o1, Note o2) {
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            });
            notifyDataSetChanged();
        }
    }

    public  void getByDesc(){
        if(notes.size()!=0){
            Collections.sort(notes, new Comparator<Note>() {
                public int compare(Note o1, Note o2) {
                    return o1.getBody().compareTo(o2.getBody());
                }
            });
            notifyDataSetChanged();
        }
    }


}

