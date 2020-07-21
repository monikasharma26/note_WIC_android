package note.wic.FinalProject.model;


import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import com.commonsware.cwac.richtextutils.SpannableStringGenerator;
import com.commonsware.cwac.richtextutils.SpannedXhtmlGenerator;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import note.wic.FinalProject.database.AppDatabase;

@Table(database = AppDatabase.class, allFields = true)
public class Note extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private int id;
    private String title;
    private String body;
    private String audio;
    private Blob drawing;
    private Blob drawingTrimmed;
    private Date createdAt;
    private double latitude;
    private double longitude;
    private int imageId;
    private String image1;
    private String image2;
    private String image3;

    public Note(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Blob getDrawing() {
        return drawing;
    }

    public void setDrawing(Blob drawing) {
        this.drawing = drawing;
    }

    public Blob getDrawingTrimmed() {
        return drawingTrimmed;
    }

    public void setDrawingTrimmed(Blob drawingTrimmed) {
        this.drawingTrimmed = drawingTrimmed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
    public Spannable getSpannedBody(){
        SpannableStringGenerator spannableStringGenerator = new SpannableStringGenerator();
        try{
            return spannableStringGenerator.fromXhtml(body);
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new SpannableString("!ERROR!");
    }

    public void setSpannedBody(Spannable spannedBody){
        SpannedXhtmlGenerator spannedXhtmlGenerator = new SpannedXhtmlGenerator();
        body = spannedXhtmlGenerator.toXhtml(spannedBody);
        body = body.replaceAll("(?m)(^ *| +(?= |$))", "")
                .replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1")
                .replace("<br/>", "\n");
    }

    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        return id == note.id;
    }

    @Override public int hashCode(){
        return id;
    }

    @Override public String toString(){
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                "} " + super.toString();
    }

    public static Comparator<Note> StuTitleComparator = new Comparator<Note>() {
        public int compare(Note s1, Note s2) {
            String noteTitle1 = s1.getTitle().toUpperCase();
            String noteTitle2 = s2.getTitle().toUpperCase();
            Log.d("sad","adad"+noteTitle1);
            return noteTitle1.compareTo(noteTitle2);


        }};
    public static Comparator<Note> descCo = new Comparator<Note>() {
        public int compare(Note s1, Note s2) {
            String noteTitle1 = s1.getBody().toUpperCase();
            String noteTitle2 = s2.getBody().toUpperCase();
            return noteTitle1.compareTo(noteTitle2);
        }};
    public static Comparator<Note> dateComparator = new Comparator<Note>() {
            public int compare(Note o1, Note o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        };
}
