package note.wic.FinalProject.activites.home;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.dashboard.DashBoardActivity;


public class HomeFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.textdateandtime)
    TextView textdateandtime;
    @BindView(R.id.txt_noe_ed)
    TextView txtNoeEd;
    @BindView(R.id.alert_img)
    ImageView alertImg;
    @BindView(R.id.txt_nov_ed)
    TextView txtNovEd;
    @BindView(R.id.ll_dashtotal)
    LinearLayout llDashtotal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View layout = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,layout);
        return layout;

    }
    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                ((DashBoardActivity) getActivity()).mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }

    private void init(View view) {
        txtNovEd  = view.findViewById(R.id.txt_nov_ed);
        txtNoeEd  =view.findViewById( R.id.txt_noe_ed);
        textdateandtime =view.findViewById(R.id.textdateandtime);
        img1=view.findViewById(R.id.img1);
        Calendar c = Calendar.getInstance();
        AnimationDrawable animation = new AnimationDrawable();
        //animation.addFrame(getResources().getDrawable(R.drawable.img3), 1000);
        //animation.addFrame(getResources().getDrawable(R.drawable.img4), 2000);
        //animation.addFrame(getResources().getDrawable(R.drawable.mainimg), 3000);
        animation.setOneShot(false);

        img1.setBackground(animation);

        // start the animation!
        animation.start();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd \n hh:mm:ss aa");
        String formattedDate = df.format(c.getTime());

        // Now we display formattedDate value in TextView
        textdateandtime.setText(formattedDate);


    }

    @Override public void onStart(){
        super.onStart();

    }

    @Override public void onStop(){
        super.onStop();

    }


}
