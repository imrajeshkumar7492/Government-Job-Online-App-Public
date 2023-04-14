package com.governmentjobonline.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.governmentjobonline.RecyclerView_for_All.DataModel;
import com.governmentjobonline.RecyclerView_for_All.RecyclerAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.governmentjobonline.R;

import java.util.ArrayList;
import java.util.List;


public class ExamUpdates extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private List<DataModel> list;
    private RecyclerAdapter adapter;
    private SpinKitView spinKitView;
    EditText search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_exam_updates, container, false);
        recyclerView = view.findViewById(R.id.Exam_updateRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("AllData").child("Exam Updates");
        spinKitView = view.findViewById(R.id.spin_kit1);
        search = view.findViewById(R.id.searchText);

        getData();
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

       return view;
    }

    private void filter(String text) {
        try {
            ArrayList<DataModel> filterlist = new ArrayList<>();
            for (DataModel item : list){
                if (item.getTitle().toLowerCase().contains(text.toLowerCase())){
                    filterlist.add(item);

                }
            }
            adapter.Filteredlist(filterlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
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
                spinKitView.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                spinKitView.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}