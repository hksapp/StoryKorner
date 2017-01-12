package com.hkapps.storykorner;

/**
 * Created by kamal on 25-12-2016.
 */

public class FollowObject {


    private String follower_id, follower_name, following_id, following_name, photolink, uname, user_email, userid, liked_id;


    public FollowObject() {


    }


    public FollowObject(String follower_id, String follower_name, String following_id, String following_name, String photolink, String uname, String user_email, String userid, String liked_id) {


        this.follower_id = follower_id;
        this.follower_name = follower_name;
        this.following_id = following_id;
        this.following_name = following_name;
        this.photolink = photolink;
        this.uname = uname;
        this.user_email = user_email;
        this.userid = userid;
        this.liked_id = liked_id;



    }


    public String getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(String follower_id) {
        this.follower_id = follower_id;
    }


    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }


    public String getFollowing_id() {
        return following_id;
    }

    public void setFollowing_id(String following_id) {
        this.following_id = following_id;
    }


    public String getFollowing_name() {
        return following_name;
    }

    public void setFollowing_name(String following_name) {
        this.following_name = following_name;
    }


    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


    public String getPhotolink() {
        return photolink;
    }

    public void setPhotolink(String photolink) {
        this.photolink = photolink;
    }


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getLiked_id() {
        return liked_id;
    }

    public void setLiked_id(String liked_id) {
        this.liked_id = liked_id;
    }





}


