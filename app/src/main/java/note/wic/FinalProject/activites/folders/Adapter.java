package note.wic.FinalProject.activites.folders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import note.wic.FinalProject.R;
import note.wic.FinalProject.database.FoldersDAO;
import note.wic.FinalProject.events.FolderCreatedEvent;
import note.wic.FinalProject.events.FolderDeletedEvent;
import note.wic.FinalProject.model.Folder;

class Adapter extends RecyclerView.Adapter{
	private static final int VIEW_TYPE_NEW_FOLDER = 0;
	private static final int VIEW_TYPE_EDIT_A_FOLDER = 1;

	List<Folder> folders;
	OpenCloseable lastOpenedItem;

	@Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		if (viewType == VIEW_TYPE_NEW_FOLDER){
			return new NewFolderViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_folder, parent, false));
		}else if (viewType == VIEW_TYPE_EDIT_A_FOLDER){
			return new EditFolderViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.view_edit_folder, parent, false), this);
		}
		return null;
	}

	@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
		if (holder instanceof EditFolderViewHolder){
			position--;
			EditFolderViewHolder editFolderViewHolder = (EditFolderViewHolder) holder;
			editFolderViewHolder.setFolder(folders.get(position));
		}
	}

	@Override
    public int getItemViewType(int position){
		if (position == 0) return VIEW_TYPE_NEW_FOLDER;
		else return VIEW_TYPE_EDIT_A_FOLDER;
	}

	@Override
    public int getItemCount(){
		return 1 + (folders == null ? 0 : folders.size());
	}

	public void loadFromDatabase(){
		folders = FoldersDAO.getLatestFolders();
		notifyDataSetChanged();
	}

	void registerEventBus(){
		EventBus.getDefault().register(this);
	}

	void unregisterEventBus(){
		EventBus.getDefault().unregister(this);
	}

	@Subscribe public void onFolderDeletedEvent(FolderDeletedEvent folderDeletedEvent){
		int index = folders.indexOf(folderDeletedEvent.getFolder());
		if (index == -1) return;
		folders.remove(index);
		notifyItemRemoved(index + 1);
	}

	@Subscribe public void onFolderCreatedEvent(FolderCreatedEvent folderCreatedEvent){
		if (folders == null) folders = new ArrayList<>();
		folders.add(0, folderCreatedEvent.getFolder());
		notifyItemInserted(1);
	}

	OpenCloseable getLastOpened(){
		return lastOpenedItem;
	}

	void setLastOpened(OpenCloseable lastOpenedItem){
		this.lastOpenedItem = lastOpenedItem;
	}
}
