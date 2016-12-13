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

    //
    private String comment_text;
    private String commented_user;
    //

    public StoryObject(){


    }

    public StoryObject(String title, String story, String username, String userid, long timestamp, String comment_text, String commented_user) {
        this.title = title;
        this.story = story;
        this.username = username;
        this.userid = userid;
        this.timestamp = timestamp;
        //
        this.comment_text = comment_text;
        this.commented_user = commented_user;
        //
    }

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title; }

    public String getStory() { return  story;}

    public void setStory(String story) { this.story = story; }


    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username; }


    public String getUserid() { return userid;}

    public void setUserid(String userid) { this.userid = userid; }

    //
    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String userid) {
        this.comment_text = comment_text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
