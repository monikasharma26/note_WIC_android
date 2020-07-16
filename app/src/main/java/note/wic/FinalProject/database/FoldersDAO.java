package note.wic.FinalProject.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import note.wic.FinalProject.model.Folder;
import note.wic.FinalProject.model.Folder_Table;

public class FoldersDAO {

    public static List<Folder> getLatestFolders(){
        return SQLite.select().from(Folder.class).orderBy(Folder_Table.createdAt, false).queryList();
    }

    public static Folder getFolder(int id){
        return SQLite.select().from(Folder.class).where(Folder_Table.id.is(id)).querySingle();
    }
}

