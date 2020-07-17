package note.wic.FinalProject.events;


import note.wic.FinalProject.database.NotesDAO;
import note.wic.FinalProject.model.Note;

public class NoteFoldersUpdatedEvent{

	int noteId;

	public NoteFoldersUpdatedEvent(int noteId){
		this.noteId = noteId;
	}

	public int getNoteId(){
		return noteId;
	}

	public Note getNote(){
		return NotesDAO.getNote(noteId);
	}
}
