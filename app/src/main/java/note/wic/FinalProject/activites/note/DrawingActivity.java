package note.wic.FinalProject.activites.note;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.gcacace.signaturepad.views.SignaturePad;

import butterknife.BindView;
import butterknife.ButterKnife;

import note.wic.FinalProject.App;
import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.jobs.SaveDrawingJob;
import note.wic.FinalProject.database.NotesDAO;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.utils.Utils;
import note.wic.FinalProject.utils.ViewUtils;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class DrawingActivity extends AppCompatActivity {
	private static final String TAG = "DrawingActivity";
	@Extra
	Integer noteId;
	Note note;

	private boolean hasDrawnSomething = false;

	@BindView(R.id.drawing_pad) SignaturePad drawingPad;
	@BindView(R.id.toolbar)
    Toolbar mToolbar;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawing);
		DrawingActivityIntentBuilder.inject(getIntent(), this);
		ButterKnife.bind(this);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationIcon(ViewUtils.tintDrawable(R.drawable.ic_arrow_back_white_24dp, R.color.md_blue_grey_400));
		mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				onBackPressed();
			}
		});
		note = NotesDAO.getNote(noteId);
		Log.e(TAG, "onCreate: noteId= " + noteId + ", note= " + note);
		drawingPad.setOnSignedListener(new SignaturePad.OnSignedListener(){
			@Override public void onStartSigning(){
			}

			@Override public void onSigned(){
				hasDrawnSomething = true;
			}

			@Override public void onClear(){
			}
		});
	}

	@Override protected void onStop(){
		super.onStop();
		if (hasDrawnSomething)
			App.JOB_MANAGER.addJobInBackground(new SaveDrawingJob(drawingPad, note.getId()));
	}

	@Override protected void onStart(){
		super.onStart();
		try{
			drawingPad.setSignatureBitmap(Utils.getImage(note.getDrawing().getBlob()));
		}catch (NullPointerException e){
			Log.i(TAG, "Empty Drawing onStart: ", e);
		}
	}
}