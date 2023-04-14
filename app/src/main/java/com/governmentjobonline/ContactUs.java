package com.governmentjobonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.governmentjobonline.R;

public class ContactUs extends AppCompatActivity {
    private CardView Mailus,facebook,youtube,telegram,whatsapp,instagram,twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Mailus = findViewById(R.id.Mail_us);
        facebook = findViewById(R.id.FacebookPage);
        youtube = findViewById(R.id.YoutubeChannel);
        telegram = findViewById(R.id.TelegramChannel);
        whatsapp = findViewById(R.id.whatsappGroup);
        instagram = findViewById(R.id.Instagram);
        twitter = findViewById(R.id.twitter);

        Mailus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + "contact@governmentjobonline.in"));
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("https://www.facebook.com/Government-job-online-113002520332718/");
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("https://www.youtube.com/channel/UCN9vWPXNKXN_SOB0C6LKs4A");
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("https://t.me/joinchat/AAAAAEfRsCeM7CJghQsGPg");
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("https://wa.me/message/MSIHUF6TIOJMM1");
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("https://instagram.com/governmentjobonline?igshid=1c6fbtepiolcg");
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("https://twitter.com/GovernmentJob74?s=09");
            }
        });

    }

    private void getUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}