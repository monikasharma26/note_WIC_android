package note.wic.FinalProject.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.model.Note_Table;


public class NotesDAO {

    public static List<Note> getLatestNotes(Folder folder){
        if (folder == null)
            return SQLite.select().from(Note.class).orderBy(Note_Table.createdAt, false).queryList();
        else
            return FolderNoteDAO.getLatestNotes(folder);
    }

    public static Note getNote(int noteId){
        return SQLite.select().from(Note.class).where(Note_Table.id.is(noteId)).querySingle();
    }
}
