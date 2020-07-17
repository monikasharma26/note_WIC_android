package note.wic.FinalProject.activites.folders;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import note.wic.FinalProject.R;
import note.wic.FinalProject.events.FolderDeletedEvent;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Folder_Table;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.model.Note_Table;
import note.wic.FinalProject.utils.Utils;


class EditFolderViewHolder extends RecyclerView.ViewHolder implements OpenCloseable{
	private final Adapter adapter;
	Folder folder;
	@BindView(R.id.left_button) AppCompatImageButton leftButton;
	@BindView(R.id.folder_name_text) TextView folderName;
	@BindView(R.id.right_button) AppCompatImageButton rightButton;
	@BindView(R.id.folder_name_til) TextInputLayout folderNameTIL;

	public EditFolderViewHolder(final View itemView, Adapter adapter){
		super(itemView);
		ButterKnife.bind(this, itemView);
		this.adapter = adapter;
		folderName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			@Override
            public void onFocusChange(View v, boolean hasFocus){
				if (hasFocus){
					open();
				}
			}
		});
		folderName.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			@Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if (actionId == EditorInfo.IME_ACTION_DONE){
					apply();
					close();
					return true;
				}
				return false;
			}
		});

		leftButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				delete(id);



			}
		});
	}

	@OnClick(R.id.left_button) void clickLeftButton(View view){
		if (isOpen()){
			close();
			delete(view.getId());
		}
	}



	@OnClick(R.id.right_button) void clickRightButton(View view){
		if (isOpen()){
			apply();
			close();
		}else{
			open();
		}
	}

	@Override
    public void open(){
		//leftButton.setImageResource(R.drawable.ic_delete_white_24dp);
		rightButton.setImageResource(R.drawable.ic_done_white_24dp);
		itemView.setBackgroundResource(R.color.md_white_1000);
		if (adapter.getLastOpened() != null)
			adapter.getLastOpened().close();
		adapter.setLastOpened(this);
	}

	@Override
    public boolean isOpen(){
		return folderName.hasFocus();
	}

	@Override
    public void close(){
		Utils.hideSoftKeyboard(itemView);
		leftButton.setImageResource(R.drawable.finfo);
		folderName.setText(folder.getName());
		folderName.clearFocus();
		rightButton.setImageResource(R.drawable.newedd);
		itemView.setBackgroundResource(0);
		if (adapter.getLastOpened() == this) adapter.setLastOpened(null);
	}

	private void apply(){
		if (TextUtils.isEmpty(folderName.getText())){
			folderNameTIL.setError("Enter a folder name");
			return;
		}
		folder.setName(folderName.getText().toString());
		folder.save();
	}

	private void delete(int id){
	new AlertDialog.Builder(itemView.getContext(),R.style.DialogTheme)
				.setCancelable(true)
				.setTitle("Delete folder?")
				.setMessage("Folder '" + folder.getName() + "' will be deleted however notes in this folder will remain safe")
				.setPositiveButton("Delete Folder", new DialogInterface.OnClickListener(){
					@Override
                    public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
						folder.delete();
						EventBus.getDefault().post(new FolderDeletedEvent(folder));
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
					@Override
                    public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				})
				.show();


		/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
		alertDialogBuilder.setTitle("Are you Sure to Delete");
		alertDialogBuilder.setCancelable(true);

		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});

		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				dialogInterface.dismiss();
			//	SQLite.delete().from(Folder.class).where(Folder_Table.id.is(id).execute());

				EventBus.getDefault().post(new FolderDeletedEvent(folder));

			}
		});


		AlertDialog mAlertDialog = alertDialogBuilder.create();
		mAlertDialog.show();*/
	}



	public void setFolder(Folder folder){
		this.folder = folder;
		folderName.setText(folder.getName());
		close();
	}
}
