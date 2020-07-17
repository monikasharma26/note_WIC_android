package note.wic.FinalProject.activites.folders.addFolder;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import note.wic.FinalProject.R;
import note.wic.FinalProject.database.FolderNoteDAO;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;


class SelectFolderViewHolder extends RecyclerView.ViewHolder{
	private static final String TAG = "SelectFolderViewHolder";
	private final Adapter adapter;
	@BindView(R.id.checkbox) CheckBox checkBox;
	@BindView(R.id.folder_name_text) TextView folderName;
	private Folder folder;
	private Note note;

	public SelectFolderViewHolder(final View itemView, final Adapter adapter){
		super(itemView);
		ButterKnife.bind(this, itemView);
		this.adapter = adapter;
		itemView.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				setChecked(!checkBox.isChecked());
			}
		});
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if (isChecked){
					adapter.getCheckedFolders().add(folder);
					FolderNoteDAO.createFolderNoteRelation(folder, note);
				}else{
					adapter.getCheckedFolders().remove(folder);
					FolderNoteDAO.removeFolderNoteRelation(folder, note);
				}
				Log.e(TAG, "onClick: checkedFolders:" + adapter.getCheckedFolders());
			}
		});
	}

	public void setData(Folder folder, Note note){
		this.folder = folder;
		this.note = note;
		folderName.setText(folder.getName());
	}

	public void setChecked(boolean checked){
		checkBox.setChecked(checked);
	}
}
