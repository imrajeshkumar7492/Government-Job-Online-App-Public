package com.governmentjobonline.Bookmarks;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.governmentjobonline.Exploresection.ExploreActivity;
import com.governmentjobonline.MainActivity;
import com.governmentjobonline.R;
import com.governmentjobonline.RecyclerView_for_All.DataModel;

import java.util.ArrayList;
import java.util.List;

public class SavedJobs extends AppCompatActivity {
    private RecyclerView recyclerView;
    List<MainData> dataList = new ArrayList<>();

    LinearLayoutManager layoutManager;
    RoomDB database;
    MainAdapter adapter;
    private TextView toolbar_text;
    private ImageView backbtn;
    EditText search;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_jobs);
        recyclerView = findViewById(R.id.rv_saved_jobs);
        search = findViewById(R.id.searchText);
        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();
        linearLayout = findViewById(R.id.linearlayout2);

        if (dataList.isEmpty()){
            search.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        toolbar_text = findViewById(R.id.tv_tool);
        backbtn = findViewById(R.id.backbtnexplore);
        Toolbar toolbar = findViewById(R.id.toolbar3);

        setSupportActionBar(toolbar);

        toolbar_text.setText("Saved Jobs");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedJobs.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MainAdapter(SavedJobs.this, dataList);
        recyclerView.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());


            }
        });

    }

    private void checkEmpty() {

    }

    private void filter(String text) {
        ArrayList<MainData> filterlist = new ArrayList<>();
        for (MainData item : dataList){
            if (item.getText().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);

            }
        }
        adapter.Filteredlist(filterlist);
    }
}