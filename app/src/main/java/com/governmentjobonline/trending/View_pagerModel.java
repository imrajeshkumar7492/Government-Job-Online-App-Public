package com.governmentjobonline.trending;

public class View_pagerModel {
    String title,url;

    public View_pagerModel() {

    }

    public View_pagerModel(View_pagerModel data) {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public View_pagerModel(String title, String url) {
        this.title = title;
        this.url = url;
    }
}
