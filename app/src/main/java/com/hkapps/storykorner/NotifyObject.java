package com.hkapps.storykorner;

/**
 * Created by kamal on 10-01-2017.
 */
public class NotifyObject {

    private String liker_id, liker_name, post_id, cmt, cmt_key_post_id, cmt_name, cmt_user_id;
    private long timestamp;


    public NotifyObject() {

    }

    public NotifyObject(String liker_id, String liker_name, String post_id, long timestamp, String cmt, String cmt_key_post_id, String cmt_name, String cmt_user_id) {
        this.liker_id = liker_id;
        this.liker_name = liker_name;
        this.post_id = post_id;
        this.timestamp = timestamp;
        this.cmt = cmt;
        this.cmt_key_post_id = cmt_key_post_id;
        this.cmt_name = cmt_name;
        this.cmt_user_id = cmt_user_id;


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


    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }


    public String getCmt_key_post_id() {
        return cmt_key_post_id;
    }

    public void setCmt_key_post_id(String cmt_key_post_id) {
        this.cmt_key_post_id = cmt_key_post_id;
    }


    public String getCmt_name() {
        return cmt_name;
    }

    public void setCmt_name(String cmt_name) {
        this.cmt_name = cmt_name;
    }

    public String getCmt_user_id() {
        return cmt_user_id;
    }

    public void setCmt_user_id(String cmt_user_id) {
        this.cmt_user_id = cmt_user_id;
    }

}
