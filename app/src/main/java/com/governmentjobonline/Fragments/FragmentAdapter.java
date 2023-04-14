package com.governmentjobonline.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    public FragmentAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return list.get(position);

//        switch (position) {
//            case 1:
//                return new LatestJobsFragment();
//            case 2:
//                return new LatestResultFragment();
//            case 3:
//                return new AdmitCardFragment();
//            case 4:
//                return new AnswerKeyFragment();
//            case 5:
//                return new SyllabusFragment();
//            case 6:
//                return new AdmissionFragment();
//            case 7:
//                return new ExamUpdates();
//            case 8:
//                return new Certificate_veri();
//            case 9:
//                return new Important();
//            case 10:
//                return new scholorship();
//
//            default:
//                return new HomeFragment();
//
//        }

    }

    @Override
    public int getCount() {
        return 11;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = "Home";
                break;
            case 1:
                title = "LATEST JOBS";
                break;
            case 2:
                title = "LATEST RESULTS";
                break;
            case 3:
                title = "ADMIT CARD";
                break;
            case 4:
                title = "ANSWER KEY";
                break;
            case 5:
                title = "SYLLABUS";
                break;
            case 6:
                title = "ADMISSION";
                break;
            case 7:
                title = "Exam Updates";
                break;
            case 8:
                title = "Certificate Verification";
                break;
            case 9:
                title = "Important";
                break;
            case 10:
                title = "Scholarship";
                break;
        }


        return title;
    }
}
