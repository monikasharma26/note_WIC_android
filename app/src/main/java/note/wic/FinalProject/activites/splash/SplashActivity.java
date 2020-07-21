package note.wic.FinalProject.activites.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import note.wic.FinalProject.R;
import note.wic.FinalProject.activites.dashboard.DashBoardActivity;

public class SplashActivity extends AppCompatActivity {

    private static int timeout = 3000;
    private ImageView imageView;
    private TextView splashtxtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imgLogo);
        splashtxtName = findViewById(R.id.splashtxtName);
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.splash_animation);
        imageView.startAnimation(animation);
        splashtxtName.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        },timeout);
    }
}