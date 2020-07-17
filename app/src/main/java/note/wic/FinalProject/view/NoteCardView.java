package note.wic.FinalProject.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.greenfrvr.hashtagview.HashtagView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import note.wic.FinalProject.App;
import note.wic.FinalProject.R;
import note.wic.FinalProject.database.FolderNoteDAO;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.utils.Utils;


public class NoteCardView extends CardView{

	private static final String TAG = "NoteCardView";
	@BindView(R.id.title) TextView title;
	@BindView(R.id.body) TextView body;
	@BindView(R.id.drawing_image) ImageView drawingImage;
	@BindView(R.id.folders_tag_view)
    HashtagView foldersTagView;
	private Note note;

	public NoteCardView(Context context){
		this(context, null);
	}

	public NoteCardView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}

	public NoteCardView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
		View view = LayoutInflater.from(context).inflate(R.layout.view_note_card, this, true);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		int noteGap = (int) Utils.dp2px(App.CONTEXT.getResources().getDimensionPixelSize(R.dimen.notes_gap)) / 2;
		lp.setMargins(noteGap, noteGap, 0, 0);
		view.setLayoutParams(lp);
		ButterKnife.bind(this, view);
	}

	public Note getNote(){
		return note;
	}

	public void bindModel(Note note){
		boolean isTitleEmpty = TextUtils.isEmpty(note.getTitle());
		boolean isBodyEmpty = TextUtils.isEmpty(note.getSpannedBody());
		title.setText(note.getTitle());
		body.setText(note.getSpannedBody());
		title.setVisibility(isTitleEmpty ? GONE : VISIBLE);
		body.setVisibility(isBodyEmpty ? GONE : VISIBLE);

		List<Folder> folders = FolderNoteDAO.getFolders(note.getId());
		HashtagView.DataTransform<Folder> dt = new HashtagView.DataTransform<Folder>(){
			@Override public CharSequence prepare(Folder item){
				return item.getName();
			}
		};
		foldersTagView.setData(folders, dt);
		if (note.getDrawingTrimmed() == null)
			drawingImage.setVisibility(View.GONE);
		else{
			drawingImage.setVisibility(View.VISIBLE);
			Bitmap imageBitMap = Utils.getImage(note.getDrawingTrimmed().getBlob());
			drawingImage.setImageBitmap(imageBitMap);
		}
		this.note = note;
	}
}
