package com.hkapps.storykorner;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryObject {

    private String title, saved_post_id;
    private String story;
    private String username;
    private String userid;
    private String category;
    private long timestamp;


    public StoryObject(){


    }

    public StoryObject(String title, String story, String username, String userid, String category, long timestamp, String saved_post_id) {
        this.title = title;
        this.story = story;
        this.username = username;
        this.userid = userid;
        this.category = category;
        this.timestamp = timestamp;
        this.saved_post_id = saved_post_id;
    }

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title; }

    public String getStory() { return  story;}

    public void setStory(String story) { this.story = story; }


    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username; }


    public String getUserid() { return userid;}

    public void setUserid(String userid) { this.userid = userid; }

    public String getCategory() { return category;}

    public void setCategory(String category) { this.category = category; }

    public String getSaved_post_id() {
        return saved_post_id;
    }

    public void setSaved_post_id(String saved_post_id) {
        this.saved_post_id = saved_post_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
