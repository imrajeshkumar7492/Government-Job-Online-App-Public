package com.governmentjobonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.governmentjobonline.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 4000;

    Animation topanim,botomanim;
    private ImageView logo;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        botomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        logo = findViewById(R.id.splashlogo);
        text = findViewById(R.id.splashtext);
        logo.setAnimation(topanim);
        text.setAnimation(botomanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);


    }
}