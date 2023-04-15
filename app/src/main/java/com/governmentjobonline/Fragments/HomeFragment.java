package com.governmentjobonline.Fragments;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.governmentjobonline.BooksAndGK.Books_Study_Materials;
import com.governmentjobonline.BooksAndGK.SubjectActivity;
import com.governmentjobonline.Exploresection.exploreAdapter;
import com.governmentjobonline.QualificationSection.QualificationDetailActivity;
import com.governmentjobonline.R;
import com.governmentjobonline.RecyclerView_for_All.DataModel;
import com.governmentjobonline.RecyclerView_for_All.RecyclerAdapter;
import com.governmentjobonline.statewise.StateWiseAdapter;
import com.governmentjobonline.trending.View_pagerModel;
import com.governmentjobonline.trending.View_pager_Adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {
    private DatabaseReference reference;

    private List<DataModel> list;
    private RecyclerAdapter adapter;
    private SpinKitView spinKitView, progressBar;
    private TextView day, date;
    private ViewPager viewPager;
    private View_pager_Adapter viewPagerAdapter;
    private List<View_pagerModel> viewPagerModels;
    String data;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.upcoming_RV);


        day = view.findViewById(R.id.titleDay);
        date = view.findViewById(R.id.titleDate);
        spinKitView = view.findViewById(R.id.spin_kit);
        progressBar = view.findViewById(R.id.progressBar);
        viewPager = view.findViewById(R.id.viewPager);
        
        getUpcomingData();
        getDate();
        getTrendingData();


        return view;
    }

    private void getTrendingData() {
        reference = FirebaseDatabase.getInstance().getReference().child("AllData").child("Trending Jobs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewPagerModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    viewPagerModels.add(0, snapshot.getValue(View_pagerModel.class));
                }
                viewPagerAdapter = new View_pager_Adapter(getContext(), viewPagerModels);
                spinKitView.setVisibility(View.GONE);
                viewPager.setAdapter(viewPagerAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                spinKitView.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDate() {

        String format = DateFormat.getDateInstance(2, new Locale("En", "US")).format(new Date());
        day.setText(new SimpleDateFormat("EEEE").format(new Date()));
        date.setText(format);
    }

    private void getUpcomingData() {
        reference = FirebaseDatabase.getInstance().getReference().child("AllData").child("Upcoming Jobs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataModel data = snapshot.getValue(DataModel.class);
                    list.add(0, data);
                }
                adapter = new RecyclerAdapter(getContext(), list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
