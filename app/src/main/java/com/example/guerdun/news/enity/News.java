package com.example.guerdun.news.enity;

import java.io.Serializable;
import java.util.ArrayList;

public class News implements Serializable {

    private String date;
    private ArrayList<question> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<question> getStories() {
        return stories;
    }

    public void setStories(ArrayList<question> stories) {
        this.stories = stories;
    }

    public class question implements Serializable{

        private ArrayList<String> images;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
