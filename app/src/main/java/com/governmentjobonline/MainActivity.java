package com.governmentjobonline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.governmentjobonline.Bookmarks.SavedJobs;
import com.governmentjobonline.BooksAndGK.Books_Study_Materials;
import com.governmentjobonline.BooksAndGK.SubjectActivity;
import com.governmentjobonline.Fragments.AdmissionFragment;
import com.governmentjobonline.Fragments.AdmitCardFragment;
import com.governmentjobonline.Fragments.AnswerKeyFragment;
import com.governmentjobonline.Fragments.Certificate_veri;
import com.governmentjobonline.Fragments.ExamUpdates;
import com.governmentjobonline.Fragments.FragmentAdapter;
import com.governmentjobonline.Fragments.HomeFragment;
import com.governmentjobonline.Fragments.Important;
import com.governmentjobonline.Fragments.LatestJobsFragment;
import com.governmentjobonline.Fragments.LatestResultFragment;
import com.governmentjobonline.Fragments.SyllabusFragment;
import com.governmentjobonline.Fragments.scholorship;

import java.util.ArrayList;
import java.util.Objects;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TabLayout tabLayout;
    ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int checkeditem;
    private String selected;
    private final String CHECKEDITEM = "checked_item";
    private static final int backPressedTime = 2000;
    private long backpressed;
    private Toast backtoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        sharedPreferences = this.getSharedPreferences("themes", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        switch (getCheckeditem()) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        ArrayList <Fragment> fragmentArrayList = new ArrayList< >();

        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new LatestJobsFragment());
        fragmentArrayList.add(new LatestResultFragment());
        fragmentArrayList.add(new AdmitCardFragment());
        fragmentArrayList.add(new AnswerKeyFragment());
        fragmentArrayList.add(new SyllabusFragment());
        fragmentArrayList.add(new AdmissionFragment());
        fragmentArrayList.add(new ExamUpdates());
        fragmentArrayList.add(new Certificate_veri());
        fragmentArrayList.add(new Important());
        fragmentArrayList.add(new scholorship());

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragmentArrayList));
        viewPager.setOffscreenPageLimit(4);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
        if (!isConnected(MainActivity.this)) {
            buildDialog(MainActivity.this).show();
        }


    }

    public boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else return false;
    }

    public AlertDialog.Builder buildDialog(Context context) {

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.no_internet_dialog, null);
        Button close = view.findViewById(R.id.Closebtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view).create();


        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;


        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.navigation_Home:
                Intent intent = getIntent();

                finish();
                startActivity(intent);
                break;

            case R.id.navigation_Savedjobs:
                Intent bookmarksIntent = new Intent(getApplicationContext(), SavedJobs.class);
                startActivity(bookmarksIntent);
                break;

            case R.id.navigation_gk:
                Intent gkIntent = new Intent(getApplicationContext(), Books_Study_Materials.class);
                gkIntent.putExtra("title", "Gk & Current aff");
                startActivity(gkIntent);
                break;


            case R.id.navigation_website:
//                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
//                CustomTabsIntent customTabsIntent4 = builder4.build();
//                builder4.setStartAnimations(MainActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                builder4.setExitAnimations(MainActivity.this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
//                customTabsIntent4.launchUrl(MainActivity.this, Uri.parse("https://www.governmentjobonline.in/"));
                Intent in = new Intent(this, WebViewActivity.class);
                in.putExtra("url", "\"https://www.governmentjobonline.in/");
                startActivity(in);

                break;

            case R.id.navigation_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Government Job Online");
                    i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    startActivity(Intent.createChooser(i, "Share With"));
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to share", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.navigation_Rating:
                getUrl("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                break;

            case R.id.navigation_moreApps:
                getUrl("https://play.google.com/store/apps/developer?id=Government+Job+Online");
                break;

            case R.id.navigation_About:

                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent3 = builder3.build();
                builder3.setStartAnimations(MainActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder3.setExitAnimations(MainActivity.this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                customTabsIntent3.launchUrl(MainActivity.this, Uri.parse("https://www.governmentjobonline.in/about-us/"));

                break;

            case R.id.navigation_theme:
                showdialog();
                break;


            case R.id.navigation_privacy:

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.setStartAnimations(MainActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder.setExitAnimations(MainActivity.this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse("https://www.governmentjobonline.in/privacy-policy/"));
                break;

            case R.id.navigation_conditions:
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent1 = builder1.build();
                builder1.setStartAnimations(MainActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder1.setExitAnimations(MainActivity.this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                customTabsIntent1.launchUrl(MainActivity.this, Uri.parse("https://www.governmentjobonline.in/terms-and-conditions"));

                break;

            case R.id.navigation_Contact:
                startActivity(new Intent(this, ContactUs.class));
                break;

            case R.id.navigation_disclaimer:
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent2 = builder2.build();
                builder2.setStartAnimations(MainActivity.this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder2.setExitAnimations(MainActivity.this, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                customTabsIntent2.launchUrl(MainActivity.this, Uri.parse("https://www.governmentjobonline.in/disclaimer/"));

                break;
        }
        return true;
    }


    private void getUrl(String s) {
        Uri uri = Uri.parse(s);

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showdialog() {

        String[] themes = this.getResources().getStringArray(R.array.theme);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Select Theme");
        builder.setSingleChoiceItems(R.array.theme, getCheckeditem(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected = themes[i];
                checkeditem = i;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if (selected == null) {
                        selected = themes[i];
                        checkeditem = i;
                    }
                    switch (selected) {
                        case "System Default":
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                            break;
                        case "Dark":
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                            break;
                        case "Light":
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            Intent intent2 = getIntent();
                            finish();
                            startActivity(intent2);
                            break;
                    }
                    setcheckedItem(checkeditem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button b1 = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (b != null) {
            b.setTextColor(getResources().getColor(R.color.purple_500));
        }
        if (b1 != null) {
            b1.setTextColor(getResources().getColor(R.color.purple_500));
        }

    }


    private int getCheckeditem() {
        return sharedPreferences.getInt(CHECKEDITEM, 0);
    }

    private void setcheckedItem(int i) {
        editor.putInt(CHECKEDITEM, i);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        backtoast = Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (backpressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            backtoast.cancel();
            return;
        } else {
            backtoast.show();
        }
        backpressed = System.currentTimeMillis();
    }
}
