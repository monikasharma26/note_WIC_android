package note.wic.FinalProject.events;


import note.wic.FinalProject.model.Note;

public class NoteDeletedEvent{
	Note note;

	public NoteDeletedEvent(Note note){
		this.note = note;
	}

	public Note getNote(){
		return note;
	}
}
