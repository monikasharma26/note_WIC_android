package note.wic.FinalProject.activites.note;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.commonsware.cwac.richedit.RichEditText;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.greenfrvr.hashtagview.HashtagView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import note.wic.FinalProject.MapsActivity;
import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.audio.AudioActivity;
import note.wic.FinalProject.activites.folders.addFolder.NoteFoldersActivityIntentBuilder;
import note.wic.FinalProject.database.FolderNoteDAO;
import note.wic.FinalProject.database.NotesDAO;
import note.wic.FinalProject.events.NoteDeletedEvent;
import note.wic.FinalProject.events.NoteEditedEvent;
import note.wic.FinalProject.events.NoteFoldersUpdatedEvent;
import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;
import note.wic.FinalProject.model.Note_Table;
import note.wic.FinalProject.utils.CompressImage;
import note.wic.FinalProject.utils.ImagePicker;
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

    @BindView(R.id.img_card)
    CardView imgCard;

    @BindView(R.id.txt_upload_audio_ha)
    TextView txtUploadAudio;
    @BindView(R.id.music_card)
    CardView musicCard;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.save_sl)
    ShadowLayout saveSl;
    @BindView(R.id.saveNote)
    Button saveNote;
    ArrayList<String> myImagesUrl = new ArrayList<String>();
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

    private int GALLERY = 1, CAMERA = 2;
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
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    RichEditWidgetView richEditWidgetView;
    @BindView(R.id.rich_edit_widget)
    RichEditWidgetView richEditWidget;
    private boolean shouldFireDeleteEvent = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    LatLng recentLatLng = null;
    private static final String IMAGE_DIRECTORY = "/note";
    double latitude;
    double longitude;
    Uri mediaUri;
    File photoFile;
    boolean audio = false;
    boolean image = false;
    boolean update = false;
    String realPth = "";
    Uri imageUri = null;
    String compressed_real_path = "";
    String audioUrl;
    ArrayList<Bitmap> mImgIds = new ArrayList<Bitmap>();
    CompressImage compressImage;
    String AudioSavePathInDevice = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        ButterKnife.bind(this);
        NoteActivityIntentBuilder.inject(getIntent(), this);
        getLocationPermission();
        getDeviceLocation();

        requestMultiplePermissions();
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
            recentLatLng = new LatLng(note.getLatitude(), note.getLongitude());
        } else {
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

        compressImage = new CompressImage(NoteActivity.this);
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
        recentLatLng = new LatLng(note.getLatitude(), note.getLongitude());
        if (note.getImage1() != null ) {

            File imgFile = new File("" + note.getImage1());

            if (imgFile.exists()) {
                imgCard.setVisibility(View.VISIBLE);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgNote.setImageBitmap(myBitmap);
            }
        }
        if (note.getDrawingTrimmed() == null)
            drawingImage.setVisibility(View.GONE);
        else {
            drawingImage.setVisibility(View.VISIBLE);
           // drawingImage.setImageBitmap(note.getImage1());
             drawingImage.setImageBitmap(Utils.getImage(note.getDrawingTrimmed().getBlob()));
        }
        createTimeText.setText("Created " + TimeUtils.getHumanReadableTimeDiff(note.getCreatedAt()));
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                // check if all permissions are granted
                if (report.areAllPermissionsGranted()) {
                }
                if (report.isAnyPermissionPermanentlyDenied()) {
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
            }
        }).onSameThread()
                .check();
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;


            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NoteActivity.this);
        try {
            if (mLocationPermissionsGranted) {
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                        } else {
                            Toast.makeText(NoteActivity.this, "Error in Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        recentLatLng = latLng;
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
        Log.d("ss", "idis" + noteId);
        if (mToolbar.getTitle() == "Edit Note") {
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
        Intent intent = new NoteFoldersActivityIntentBuilder(note.getId()).build(this);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoteEditedEvent(NoteEditedEvent noteEditedEvent) {
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

    public void mapButtonClick(View view) {

        Intent intent = new Intent(NoteActivity.this, MapsActivity.class);
        intent.putExtra("Latlng", recentLatLng);
        startActivity(intent);
    }

    public void audioButtonClick(View view) {
        Intent intent = new Intent(NoteActivity.this, AudioActivity.class);
//        intent.putExtra("Latlng", recentLatLng);
        startActivity(intent);
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {

            if (data != null) {
                imageUri = data.getData();
                realPth = getPathFromURI(imageUri);
                imgNote.setImageURI(imageUri);
                compressed_real_path = compressImage.compressImage(realPth);
                image = true;
                imgNote.setVisibility(View.VISIBLE);
                imgCard.setVisibility(View.VISIBLE);

            } else if (requestCode == CAMERA) {
                realPth = photoFile.getPath();
                imageUri = Uri.parse(realPth);
                compressed_real_path = compressImage.compressImage(realPth);
                imgNote.setImageURI(imageUri);
                image = true;
                imgNote.setVisibility(View.VISIBLE);
                imgCard.setVisibility(View.VISIBLE);

            }
        }
    }


    @OnClick(R.id.save_tv)
    public void onViewClicked() {
        Log.d("aa", "ssd");
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
            note.setLatitude(latitude);
            note.setLongitude(longitude);
            note.setImage1(compressed_real_path);
            note.save();
            EventBus.getDefault().postSticky(new NoteEditedEvent(note.getId()));

        }
        finish();

    }


    public Bitmap returnImageBitmap(String imgURL) {
        File imgFile = new File(imgURL);
        Bitmap myBitmap = null;

        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }


    private void appendImgData(Bitmap img) {
        mImgIds.add(img);
    }

    public void cameraButtonClick(View view) {
        try {
            takePhotoFromCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void galleryButtonClick(View view) {
        choosePhotoFromGallary();
    }

    void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    public void takePhotoFromCamera() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public String getPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = NoteActivity.this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


}
