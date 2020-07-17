package note.wic.FinalProject.activites.jobs;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.raizlabs.android.dbflow.data.Blob;

import org.greenrobot.eventbus.EventBus;

import note.wic.FinalProject.database.NotesDAO;
import note.wic.FinalProject.events.NoteEditedEvent;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.utils.Utils;

public class SaveDrawingJob extends Job{
	private static final String TAG = "SaveDrawingJob";
	private final SignaturePad signaturePad;
	private final int noteId;

	public SaveDrawingJob(SignaturePad signaturePad, int noteId){
		super(new Params(1));
		this.signaturePad = signaturePad;
		this.noteId = noteId;
	}

	@Override public void onAdded(){
		Log.e(TAG, "onAdded() called with: " + "");
	}

	@Override public void onRun() throws Throwable{
		Log.e(TAG, "onRun() called with: " + "");

		Bitmap bitmapTrimmed = signaturePad.getTransparentSignatureBitmap(true);
		byte[] byteBlobTrimmed = Utils.getBytes(bitmapTrimmed);
		Blob blobTrimmed = new Blob(byteBlobTrimmed);
		Note note = NotesDAO.getNote(noteId);
		note.setDrawingTrimmed(blobTrimmed);
		note.save();

		Bitmap bitmap = signaturePad.getTransparentSignatureBitmap(false);
		byte[] byteBlob = Utils.getBytes(bitmap);
		Blob blob = new Blob(byteBlob);
		note.setDrawing(blob);
		note.save();

		EventBus.getDefault().post(new NoteEditedEvent(note.getId()));
	}

	@Override protected void onCancel(int cancelReason, @Nullable Throwable throwable){
		Log.e(TAG, "onCancel() called with: " + "cancelReason = [" + cancelReason + "], throwable = [" + throwable + "]");
	}

	@Override protected RetryConstraint shouldReRunOnThrowable(
			@NonNull Throwable throwable, int runCount, int maxRunCount
	){
		Log.e(TAG, "shouldReRunOnThrowable() called with: "
				+ "throwable = ["
				+ throwable
				+ "], runCount = ["
				+ runCount
				+ "], maxRunCount = ["
				+ maxRunCount
				+ "]");
		return RetryConstraint.CANCEL;
	}
}
