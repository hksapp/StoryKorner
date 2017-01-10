package com.hkapps.storykorner;

/**
 * Created by kamal on 10-01-2017.
 */
public class NotifyObject {

    private String liker_id, liker_name, post_id;
    private long timestamp;


    public NotifyObject() {

    }

    public NotifyObject(String liker_id, String liker_name, String post_id, long timestamp) {
        this.liker_id = liker_id;
        this.liker_name = liker_name;
        this.post_id = post_id;
        this.timestamp = timestamp;
    }


    public String getLiker_id() {
        return liker_id;
    }

    public void setLiker_id(String liker_id) {
        this.liker_id = liker_id;
    }

    public String getLiker_name() {
        return liker_name;
    }

    public void setLiker_name(String liker_name) {
        this.liker_name = liker_name;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
