package note.wic.FinalProject.database;


import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
import java.util.Random;

import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Note;

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
    }
