package note.wic.FinalProject.events;

import note.wic.FinalProject.model.Folder;

public class FolderDeletedEvent {
	Folder folder;

	public FolderDeletedEvent(Folder folder){
		this.folder = folder;
	}

	public Folder getFolder(){
		return folder;
	}
}
