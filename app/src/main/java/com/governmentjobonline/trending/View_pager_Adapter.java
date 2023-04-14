package com.governmentjobonline.trending;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.viewpager.widget.PagerAdapter;

import com.governmentjobonline.R;




public class View_pager_Adapter extends PagerAdapter {
    private Context context;
    private List<View_pagerModel> list;

    public View_pager_Adapter(Context context, List<View_pagerModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_item_layout, container, false);


        TextView brTitle;


        brTitle = view.findViewById(R.id.br_title);

        brTitle.setText(list.get(position).getTitle());
        brTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                builder.setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                builder.setExitAnimations(context,android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                customTabsIntent.launchUrl(context, Uri.parse(list.get(position).getUrl()));
            }
        });


        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
