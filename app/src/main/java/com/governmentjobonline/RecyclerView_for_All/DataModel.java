package com.governmentjobonline.RecyclerView_for_All;

public class DataModel {
    String title,url;

    public DataModel() {
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

    public DataModel(String title, String url) {
        this.title = title;
        this.url = url;
    }
}
