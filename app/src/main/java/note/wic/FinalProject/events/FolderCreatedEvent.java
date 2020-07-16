package note.wic.FinalProject.events;


import note.wic.FinalProject.model.Folder;

public class FolderCreatedEvent {
	private Folder folder;

	public FolderCreatedEvent(Folder folder){
		this.folder = folder;
	}

	public Folder getFolder(){
		return folder;
	}
}
