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
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> stateTitle = new ArrayList<>();
    private ArrayList<String> Statelogo = new ArrayList<>();
    private RecyclerView recyclerView, recyclerExplore, recyclerStateWise;
    private DatabaseReference reference;

    private List<DataModel> list;
    private RecyclerAdapter adapter;
    private com.governmentjobonline.Exploresection.exploreAdapter exploreAdapter;
    private SpinKitView spinKitView, progressBar;
    private TextView day, date;
    private ViewPager viewPager;
    private View_pager_Adapter viewPagerAdapter;
    private List<View_pagerModel> viewPagerModels;
    private CardView cdSearch, cdBokks, card_gk;
    String data;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.upcoming_RV);
        recyclerExplore = view.findViewById(R.id.exploreREcycler);
        recyclerStateWise = view.findViewById(R.id.RecyclerStatewise);
        cdSearch = view.findViewById(R.id.card_search);
        cdBokks = view.findViewById(R.id.card_books_study);
        card_gk = view.findViewById(R.id.card_gk);


        day = view.findViewById(R.id.titleDay);
        date = view.findViewById(R.id.titleDate);
        spinKitView = view.findViewById(R.id.spin_kit);
        progressBar = view.findViewById(R.id.progressBar);
        viewPager = view.findViewById(R.id.viewPager);

        cdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.qualification_dialog, null);

                Button btnCancel = view1.findViewById(R.id.cancel);
                Button btnSubmit = view1.findViewById(R.id.submit);


                ArrayList<String> list1 = new ArrayList();
                list1.add("5,6,7,8th pass");
                list1.add("10th pass");
                list1.add("12th pass");
                list1.add("Graduation Degree");
                list1.add("B.E/B.TECH");
                list1.add("Post Graduate Degree");
                list1.add("M.E/M.TECH");
                list1.add("Diploma");
                list1.add("ITI");
                list1.add("L.L.B/L.L.M");
                list1.add("P.H.D/D.M/PHARMA.D");
                list1.add("B.ED/M.ED");
                list1.add("EX-Servicemen");
                list1.add("Driver");
                list1.add("Others");

                ArrayAdapter<String> adapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, list1);
                AutoCompleteTextView autocompleteView = view1.findViewById(R.id.sr_quali);
                autocompleteView.setAdapter(adapter1);
                autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        data = (String) parent.getItemAtPosition(position);
                    }
                });


                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(view1).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data == null) {
                            Toast.makeText(getContext(), "Please Select Your Qualification", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getContext(), QualificationDetailActivity.class);
                            intent.putExtra("title", data);
                            startActivity(intent);
                        }

                    }
                });
            }
        });

        card_gk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gkIntent = new Intent(getContext(), Books_Study_Materials.class);
                gkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                gkIntent.putExtra("title", "Gk & Current aff");
                startActivity(gkIntent);
            }
        });

        cdBokks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.books_category_dialog, null, false);

                TextView cl1, cl2, cl3, cl4, cl5, cl6, cl7, cl8, cl9, cl10, cl11, cl12, ug, pg, bed, allOther;
                cl1 = view.findViewById(R.id.oneclass);
                cl2 = view.findViewById(R.id.twoclass);
                cl3 = view.findViewById(R.id.thirdclass);
                cl4 = view.findViewById(R.id.fouthclass);
                cl5 = view.findViewById(R.id.fifthclass);
                cl6 = view.findViewById(R.id.sixthclass);
                cl7 = view.findViewById(R.id.seventhclass);
                cl8 = view.findViewById(R.id.eightclass);
                cl9 = view.findViewById(R.id.ninthclass);
                cl10 = view.findViewById(R.id.tenthclass);
                cl11 = view.findViewById(R.id.eleventhclass);
                cl12 = view.findViewById(R.id.twelvethclass);
                ug = view.findViewById(R.id.underGraduate);
                pg = view.findViewById(R.id.postGraduate);
                bed = view.findViewById(R.id.B_ed_med);
                allOther = view.findViewById(R.id.all_comp_exams);

                cl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl1.getText().toString());
                        startActivity(intent);
                    }
                });
                cl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl2.getText().toString());
                        startActivity(intent);
                    }
                });
                cl3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl3.getText().toString());
                        startActivity(intent);
                    }
                });
                cl4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl4.getText().toString());
                        startActivity(intent);
                    }
                });
                cl5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl5.getText().toString());
                        startActivity(intent);
                    }
                });
                cl6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl6.getText().toString());
                        startActivity(intent);
                    }
                });
                cl7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl7.getText().toString());
                        startActivity(intent);
                    }
                });
                cl8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl8.getText().toString());
                        startActivity(intent);
                    }
                });
                cl9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl9.getText().toString());
                        startActivity(intent);
                    }
                });
                cl10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl10.getText().toString());
                        startActivity(intent);
                    }
                });
                cl11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl11.getText().toString());
                        startActivity(intent);
                    }
                });
                cl12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", cl12.getText().toString());
                        startActivity(intent);
                    }
                });
                ug.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", ug.getText().toString());
                        startActivity(intent);
                    }
                });
                pg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", pg.getText().toString());
                        startActivity(intent);
                    }
                });
                bed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", bed.getText().toString());
                        startActivity(intent);
                    }
                });
                allOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), SubjectActivity.class);
                        intent.putExtra("title", allOther.getText().toString());
                        startActivity(intent);
                    }
                });

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(view).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });


        getUpcomingData();
        getDate();
        getExploreImages();
        getStateImages();
        getExploreRecycler();
        getStateWiseRecycler();
        getTrendingData();


        return view;
    }


    private void getStateWiseRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerStateWise.setLayoutManager(gridLayoutManager);
        recyclerStateWise.setHasFixedSize(true);

        StateWiseAdapter adapter = new StateWiseAdapter(stateTitle, Statelogo, getContext());
        recyclerStateWise.setAdapter(adapter);
        recyclerStateWise.removeAllViews();

    }

    private void getStateImages() {

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fandhra%20Pradesh.png?alt=media&token=120c0e35-5e4d-4855-a19e-f2d2ef9a519d");
        stateTitle.add("Andhra Pradesh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FArunachal%20Pradesh.png?alt=media&token=8f9f8df3-93a0-4abc-bafe-cfe71f2c8223");
        stateTitle.add("Arunachal Pradesh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FAssam.jpg?alt=media&token=266824ef-bf10-416b-9aa4-41d12f5588e2");
        stateTitle.add("Assam");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fbihar.png?alt=media&token=0dd67b7f-fdbc-47ed-ba18-8529690c2aa9");
        stateTitle.add("Bihar");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FChhattisgarh.jpg?alt=media&token=a63b9c2f-5a5c-44d2-b011-76234025aeb3");
        stateTitle.add("Chhattisgarh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FGoa.png?alt=media&token=b970d94f-a25a-4d1e-8688-4972ccd4854b");
        stateTitle.add("Goa");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FGujrat.png?alt=media&token=e3d19267-a59e-4e64-9422-d5c39b09cbdc");
        stateTitle.add("Gujrat");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FHaryana.png?alt=media&token=a45388e2-e25a-4827-bb8c-699c4ac96f76");
        stateTitle.add("Haryana");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FHimachal.png?alt=media&token=6b5761d0-e92c-4a14-a44b-dbac32515a1b");
        stateTitle.add("Himanchal Pradesh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FJharkhand.jpg?alt=media&token=6ed0fdf2-a427-4677-9bed-62689d63b4f0");
        stateTitle.add("Jharkhand");


        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fkarnataka.jpg?alt=media&token=53919091-39c0-4658-8594-a2bafbbf9922");
        stateTitle.add("Karnataka");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fkerala.jpg?alt=media&token=4f977b54-b6e6-48c3-a32e-ef3dc82ea5a4");
        stateTitle.add("Kerala");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FMadhya_Pradesh.png?alt=media&token=8e75bd09-ce26-4561-aad9-ef3c0e2baa50");
        stateTitle.add("Madhya Pradesh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fmaharastra.png?alt=media&token=70fc813c-3e79-4eba-a53d-5c87f5a656b7");
        stateTitle.add("Maharastra");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fmanipur.png?alt=media&token=9f90367a-80c1-4567-b5e3-9b3496759ad9");
        stateTitle.add("Manipur");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fmeghalaya.png?alt=media&token=81827ec2-28a0-4575-9542-bc6556c1b1f2");
        stateTitle.add("Meghalaya");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fmizoram.jpg?alt=media&token=bd1037a5-07cb-4d42-9c54-869bd3a6b5b8");
        stateTitle.add("Mizoram");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fnagaland.png?alt=media&token=51b3800e-3678-48ba-b3fa-780ab42d5bc3");
        stateTitle.add("Nagaland");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fodisha.jpg?alt=media&token=37daa698-6411-46ac-9a0b-d16b5779e186");
        stateTitle.add("Odisha");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fpunjab.jpg?alt=media&token=23e144ed-0f50-4e40-9d4b-0158bcd065ab");
        stateTitle.add("Punjab");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FRajasthan.png?alt=media&token=db2489e1-6383-432d-b282-a383b214041d");
        stateTitle.add("Rajasthan");


        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2Fsikkim.jpg?alt=media&token=9efeed87-2931-4e32-8dcb-d287a4768aa6");
        stateTitle.add("Sikkim");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Ftamilnadu.jpg?alt=media&token=ca08f049-04cb-48af-8c9c-d2cce9884c48");
        stateTitle.add("Tamil Nadu");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Ftelangana.png?alt=media&token=9995927b-2111-46e1-ab3d-e87a77a1533b");
        stateTitle.add("Telangana");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Ftripura.png?alt=media&token=d8646516-fd3e-42c4-b4c8-41ac1aa4e544");
        stateTitle.add("Tripura");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Futtrakhand.png?alt=media&token=c66b7704-7b20-41b7-82da-7c1715dd9c16");
        stateTitle.add("Uttrakhand");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FUttarpradesh.png?alt=media&token=92d714d7-76c2-4e59-ab2c-5e851c6d1e06");
        stateTitle.add("Uttar Pradesh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fwest%20bengal.jpg?alt=media&token=5b770ae6-7f77-4d20-a36a-cfdc1b29a4d3");
        stateTitle.add("West Bengal");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FDelhi.png?alt=media&token=ef219f16-14a9-4f81-ab76-bfbb600a8df5");
        stateTitle.add("Delhi");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FDadra%20and%20Nagar%20Haveli%20%26%20Daman%20and%20Diu.png?alt=media&token=5ac07d6f-3d0a-4bfe-a0b3-cbc2b3022357");
        stateTitle.add("Daman and Nagar Hav.");


        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fandaman%20and%20nico..png?alt=media&token=8e47a2b7-22d7-40d8-9c4b-7f997629067c");
        stateTitle.add("Andaman and Nico.");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fjammu%20and%20kash.png?alt=media&token=a34ed4ff-c5be-4733-863b-4a9c90f1a73e");
        stateTitle.add("Jammu and Kash.");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fchandigarh.jpg?alt=media&token=bda9385a-87f2-4441-b5fc-9d4d0d00d311");
        stateTitle.add("Chandigarh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Flashwadeep.png?alt=media&token=7f71b29d-6b02-4f35-abdf-3b0489a38066");
        stateTitle.add("Lakshadweep");


        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2FLadakh.png?alt=media&token=6984aca9-a879-4d4b-80e9-c98a94102bdc");
        stateTitle.add("Ladakh");

        Statelogo.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/States%20%26%20U.Ts%20New%2Fpudducherry.png?alt=media&token=99a168d2-4d8a-4307-883e-e22a08f4cea2");
        stateTitle.add("Puducherry");


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

    private void getExploreRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);

        recyclerExplore.setLayoutManager(gridLayoutManager);
        recyclerExplore.setHasFixedSize(true);
        exploreAdapter adapter = new exploreAdapter(mNames, mImageUrls, getContext());
        recyclerExplore.setAdapter(adapter);

    }

    private void getExploreImages() {
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FSSC.jpg?alt=media&token=89a7241f-914e-46ce-8266-27ca0dc3d5dd");
        mNames.add("SSC");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FPolice.jpeg?alt=media&token=32cf3d9c-2ead-4f4d-97a0-2a7780e40b16");
        mNames.add("Police");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FUPSC.jpg?alt=media&token=833bb2e8-0e25-4375-84d3-fbdb58c35f87");
        mNames.add("UPSC");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FEngineering.jpeg?alt=media&token=19861293-9602-4937-b6bf-47a77d8e7154");
        mNames.add("Enginee.");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FBANK.png?alt=media&token=3b616800-98ec-4da8-998f-2b32fb7c8ace");
        mNames.add("Bank");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FMedical.png?alt=media&token=cd66de96-ffc6-41f2-9c57-3374e71b4349");
        mNames.add("Medical");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FRailway.png?alt=media&token=17ae6088-033c-4c2a-b6e9-36af83abf29a");
        mNames.add("Railway");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FOffline%20Jobs.jpg?alt=media&token=015681ce-96fb-45cc-9476-b1aa27dd1426");
        mNames.add("Offline jobs");


        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2Fupsssc.png?alt=media&token=6ee7791b-e96c-4d05-a5b1-ed2b9b447223");
        mNames.add("UPSSSC");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FOther%20Jobs.jpg?alt=media&token=983ce6b0-4dbb-4383-8bff-7fc56b82e074");
        mNames.add("Other Jobs");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2Fdefence.jpg?alt=media&token=b517829e-f536-4508-bb48-afd627a72ab7");
        mNames.add("Defence");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FJPSC.jpeg?alt=media&token=58517f84-c1a7-4d66-a71b-04aee8b7fd48");
        mNames.add("JPSC");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FBPSC.png?alt=media&token=4537897d-b3b6-470f-8ce5-e2c850b7bd33");
        mNames.add("BPSC");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FRPSC.jpeg?alt=media&token=bdcf7cbe-49f6-4d41-aa8f-d83809ec32d2");
        mNames.add("RPSC");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2Fteaching.jpg?alt=media&token=afe0e053-b204-45fc-8dbc-5b7df39614ea");
        mNames.add("Teaching");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/sarkari-yojana-a7b23.appspot.com/o/Explore%20More%20New%2FHSSC.jpg?alt=media&token=2f0b113c-5e37-4013-9a5a-b2066fa3c66c");
        mNames.add("HSSC");


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