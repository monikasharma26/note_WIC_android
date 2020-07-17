package note.wic.FinalProject.activites.note;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.commonsware.cwac.richedit.RichEditText;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.appbar.AppBarLayout;
import com.greenfrvr.hashtagview.HashtagView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.folders.addFolder.AddToFoldersActivityIntentBuilder;
import note.wic.FinalProject.database.FolderNoteDAO;
import note.wic.FinalProject.database.NotesDAO;
import note.wic.FinalProject.events.NoteDeletedEvent;
import note.wic.FinalProject.events.NoteEditedEvent;
import note.wic.FinalProject.events.NoteFoldersUpdatedEvent;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.model.Note_Table;
import note.wic.FinalProject.utils.ShadowLayout;
import note.wic.FinalProject.utils.TimeUtils;
import note.wic.FinalProject.utils.Utils;
import note.wic.FinalProject.utils.ViewUtils;
import note.wic.FinalProject.view.RichEditWidgetView;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class NoteActivity extends AppCompatActivity {
    private static final String TAG = "NoteActivity";

    @Extra
    @Nullable
    Integer noteId;
    Note note;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;

    @BindView(R.id.img_note)
    ImageView imgNote;
    @BindView(R.id.cross_image)
    ImageView crossImage;
    @BindView(R.id.img_card)
    CardView imgCard;
    @BindView(R.id.cross_music)
    ImageView crossMusic;
    @BindView(R.id.txt_upload_audio_ha)
    TextView txtUploadAudioHa;
    @BindView(R.id.music_card)
    CardView musicCard;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.save_sl)
    ShadowLayout saveSl;
    @BindView(R.id.saveNote)
    Button saveNote;

    @BindView(R.id.btnMap)
    FloatingActionButton btnMap;
    @BindView(R.id.btn)
    FloatingActionMenu btn;
    @BindView(R.id.edit_drawing_button)
    ImageButton editDrawingButton;
    @BindView(R.id.create_time_text)
    TextView createTimeText;
    @BindView(R.id.edit_folders_button)
    ImageButton editFoldersButton;

    @BindView(R.id.folders_tag_view)
    HashtagView foldersTagView;
    @BindView(R.id.drawing_image)
    ImageView drawingImage;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.body)
    RichEditText body;
    @BindView(R.id.ss)
    FrameLayout ss;
    RichEditWidgetView richEditWidgetView;
    @BindView(R.id.rich_edit_widget)
    RichEditWidgetView richEditWidget;
    private boolean shouldFireDeleteEvent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        ButterKnife.bind(this);
        NoteActivityIntentBuilder.inject(getIntent(), this);
        setSupportActionBar(mToolbar);
        imgCard.setVisibility(View.INVISIBLE);
        musicCard.setVisibility(View.INVISIBLE);
        mToolbar.setNavigationIcon(ViewUtils.tintDrawable(R.drawable.ic_arrow_back_white_24dp, R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (noteId == null) {
            mToolbar.setTitle("Add Note");
            note = new Note();
            Date now = new Date();
            note.setCreatedAt(now);
            note.save();
            noteId = note.getId();
        }
        else
        {
            mToolbar.setTitle("Edit Note");
        }
        //  richEditWidgetView.setRichEditView(body);

        bind();

        foldersTagView.addOnTagClickListener(new HashtagView.TagsClickListener() {
            @Override
            public void onItemClicked(Object item) {
                Toast.makeText(NoteActivity.this, "Folder Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bind() {
        note = NotesDAO.getNote(noteId);
        if (note.getTitle() != null) {
            title.setText(note.getTitle());
        }
        if (note.getBody() != null) {
            body.setText(note.getSpannedBody());
        }
        foldersTagView.setData(FolderNoteDAO.getFolders(note.getId()), new HashtagView.DataTransform<Folder>() {
            @Override
            public CharSequence prepare(Folder item) {
                return item.getName();
            }
        });
        if (note.getDrawingTrimmed() == null)
            drawingImage.setVisibility(View.GONE);
        else {
            drawingImage.setVisibility(View.VISIBLE);
            drawingImage.setImageBitmap(Utils.getImage(note.getDrawingTrimmed().getBlob()));
        }
        createTimeText.setText("Created " + TimeUtils.getHumanReadableTimeDiff(note.getCreatedAt()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.delete_note);
        Log.d("ss","idis"+noteId);
        if(mToolbar.getTitle() == "Edit Note"){
                shareItem.setVisible(true);
                ViewUtils.tintMenu(menu, R.id.delete_note, R.color.white);

            }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_note) {
            SQLite.delete().from(Note.class).where(Note_Table.id.is(note.getId())).execute();
            shouldFireDeleteEvent = true;
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.edit_drawing_button, R.id.drawing_image})
    void clickEditDrawingButton() {
        Intent intent = new DrawingActivityIntentBuilder(note.getId()).build(this);
        startActivity(intent);
    }

    @OnClick(R.id.edit_folders_button)
    void clickEditFoldersButton() {
        Intent intent = new AddToFoldersActivityIntentBuilder(note.getId()).build(this);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoteEditedEvent(NoteEditedEvent noteEditedEvent) {
        Log.e(TAG, "onNoteEditedEvent() called with: " + "noteEditedEvent = [" + noteEditedEvent + "]");
        if (note.getId() == noteEditedEvent.getNote().getId()) {
            bind();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoteFoldersUpdatedEvent(NoteFoldersUpdatedEvent noteFoldersUpdatedEvent) {
        if (note.getId() == noteFoldersUpdatedEvent.getNoteId()) {
            bind();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        assert note != null;
        if (shouldFireDeleteEvent) {
            EventBus.getDefault().postSticky(new NoteDeletedEvent(note));
        } else {
            String processedTitle = title.getText().toString().trim();
            String processedBody = body.getText().toString().trim();
            if (TextUtils.isEmpty(processedTitle) && TextUtils.isEmpty(processedBody) && note.getDrawingTrimmed() == null) {
                SQLite.delete().from(Note.class).where(Note_Table.id.is(note.getId())).execute();
                return;
            }

        }
    }

    @OnClick(R.id.save_tv)
    public void onViewClicked() {
        Log.d("aa","ssd");
        assert note != null;
        if (shouldFireDeleteEvent) {
            EventBus.getDefault().postSticky(new NoteDeletedEvent(note));
        } else {
            String processedTitle = title.getText().toString().trim();
            String processedBody = body.getText().toString().trim();
            if (TextUtils.isEmpty(processedTitle) && TextUtils.isEmpty(processedBody) && note.getDrawingTrimmed() == null) {
                SQLite.delete().from(Note.class).where(Note_Table.id.is(note.getId())).execute();
                return;
            }
            note.setSpannedBody(body.getText());
            note.setTitle(processedTitle);
            note.save();
            EventBus.getDefault().postSticky(new NoteEditedEvent(note.getId()));

        }
        finish();

    }
}