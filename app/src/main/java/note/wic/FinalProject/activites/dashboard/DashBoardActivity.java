package note.wic.FinalProject.activites.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.home.HomeFragment;

public class DashBoardActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int DATABASE_MENU_ID = -1;
    private static final int ALL_NOTES_MENU_ID = -2;
    private static final int EDIT_FOLDERS_MENU_ID = -3;
    private static final int SAVE_DATABASE_MENU_ID = -4;
    private static final int IMPORT_DATABASE_MENU_ID = -5;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        mDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override public void onGlobalLayout(){
                mDrawerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                setFragment();
            }
        });

          mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override public boolean onNavigationItemSelected(MenuItem item){
                Log.e(TAG, "onNavigationItemSelected() called with: " + "item id = [" + item.getItemId() + "]");
                int menuId = item.getItemId();
                if (menuId == ALL_NOTES_MENU_ID){

                }else if (menuId == EDIT_FOLDERS_MENU_ID){

                }else if (menuId == SAVE_DATABASE_MENU_ID){

                }else if (menuId == IMPORT_DATABASE_MENU_ID){

                }else{

                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                inflateNavigationMenus(menuId);
                return true;
            }
        });
    }

    @Override protected void onStart(){
        super.onStart();
        //inflateNavigationMenus(ALL_NOTES_MENU_ID);
        inflateNavigationMenus(DATABASE_MENU_ID);
    }

    @Override public void onBackPressed(){
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            super.onBackPressed();
        }
    }
    //Inflate Menu
    public void inflateNavigationMenus(int checkedItemId){
        Menu menu = mNavigationView.getMenu();
        menu.clear();


        menu
                .add(Menu.NONE, DATABASE_MENU_ID, Menu.NONE, "DashBoard")
                .setIcon(R.drawable.ic_note_white_24dp)
                .setChecked(checkedItemId == DATABASE_MENU_ID);


        final SubMenu subMenu = menu.addSubMenu("Folders");
        subMenu
                .add(Menu.NONE, ALL_NOTES_MENU_ID, Menu.NONE, "Notes")
                .setIcon(R.drawable.ic_note_white_24dp);


        menu
                .add(Menu.NONE, EDIT_FOLDERS_MENU_ID, Menu.NONE, "Create or edit folders")
                .setIcon(R.drawable.ic_add_white_24dp);
        SubMenu backupSubMenu = menu.addSubMenu("Backup and restore");
        backupSubMenu
                .add(Menu.NONE, SAVE_DATABASE_MENU_ID, Menu.NONE, "Backup data")
                .setIcon(R.drawable.ic_save_white_24dp);
        backupSubMenu
                .add(Menu.NONE, IMPORT_DATABASE_MENU_ID, Menu.NONE, "Restore data")
                .setIcon(R.drawable.ic_restore_white_24dp);
    }

    public void setFragment(){
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = new HomeFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}