package com.hkapps.storykorner;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryObject {

    private String title;
    private String story;
    private String username;
    private String userid;
    private long timestamp;

    public StoryObject(){


    }

    public StoryObject(String title, String story, String username, String userid, long timestamp) {
        this.title = title;
        this.story = story;
        this.username = username;
        this.userid = userid;
        this.timestamp = timestamp;
    }

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title; }

    public String getStory() { return  story;}

    public void setStory(String story) { this.story = story; }


    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username; }


    public String getUserid() { return userid;}

    public void setUserid(String userid) { this.userid = userid; }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
