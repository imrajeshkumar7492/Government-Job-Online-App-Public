package com.governmentjobonline.RecyclerView_for_All;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.governmentjobonline.Bookmarks.MainData;
import com.governmentjobonline.Bookmarks.RoomDB;
import com.governmentjobonline.R;
import com.governmentjobonline.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private Context context;
    private List<DataModel> list;
    List<MainData> dataList = new ArrayList<>();
    RoomDB database;


    public RecyclerAdapter(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent, false);


        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        database = RoomDB.getInstance(context);
        dataList = database.mainDao().getAll();
        String currentData = list.get(position).getTitle();
        MainData mainData1 = database.mainDao().isExist(currentData);
        if (mainData1 == null) {
            holder.bookmarks.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.ic_fav_border));

        } else {
            holder.bookmarks.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.ic_fav_filled));
        }

        holder.title.setText(list.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                CustomTabsIntent customTabsIntent = builder.build();
//                builder.setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                builder.setExitAnimations(context, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
//                customTabsIntent.launchUrl(context, Uri.parse(list.get(position).getUrl()));

                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", list.get(position).getUrl());
                context.startActivity(intent);


            }

        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playStoreUrl = "\nhttps://play.google.com/store/apps/details?id="+context.getApplicationContext().getPackageName();
                String body = "Post name:" + list.get(position).getTitle()  + playStoreUrl;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Goverment Job Online");
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                context.startActivity(Intent.createChooser(shareIntent, "Share Via"));
            }
        });
        holder.bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentData = list.get(position).getTitle();
                MainData mainData1 = database.mainDao().isExist(currentData);
                MainData data = new MainData();
                if (mainData1 == null) {
                    Toast.makeText(context, "Added to Favourite", Toast.LENGTH_SHORT).show();
                    holder.bookmarks.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.ic_fav_filled));
                    String title = list.get(position).getTitle();
                    String url = list.get(position).getUrl();

                    data.setText(title);
                    data.setUrl(url);
                    database.mainDao().insert(data);
                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                    holder.bookmarks.setImageDrawable(context.getApplicationContext().getDrawable(R.drawable.ic_fav_border));
                    database.mainDao().delete(mainData1);
                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    notifyDataSetChanged();
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void Filteredlist(ArrayList<DataModel> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView share, bookmarks;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Data_title);
            share = itemView.findViewById(R.id.share);
            bookmarks = itemView.findViewById(R.id.fav);
        }
    }

}
