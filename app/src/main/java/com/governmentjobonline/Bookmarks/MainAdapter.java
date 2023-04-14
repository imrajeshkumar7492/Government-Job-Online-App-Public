package com.governmentjobonline.Bookmarks;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.governmentjobonline.R;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.viewHolder> {
    private Activity context;
    private List<MainData> dataList;
    private RoomDB database;

    public MainAdapter(Activity context, List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull viewHolder holder, int position) {
        MainData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.textView.setText(data.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder.setExitAnimations(context, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                customTabsIntent.launchUrl(context, Uri.parse(data.getUrl()));


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainData d = dataList.get(holder.getAdapterPosition());
                database.mainDao().delete(d);
                int removedPosition = holder.getAdapterPosition();
                dataList.remove(removedPosition);
                notifyItemRemoved(removedPosition);
                notifyItemRangeChanged(position, dataList.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void Filteredlist(ArrayList<MainData> filterlist) {
        dataList = filterlist;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton delete;

        public viewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Data_title);
            delete = itemView.findViewById(R.id.delete_btn);
        }
    }
}
