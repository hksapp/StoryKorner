package com.hkapps.storykorner;

/**
 * Created by kamal on 25-12-2016.
 */

public class FollowObject {


    private String follower_id, follower_name, following_id, following_name;


    public FollowObject() {


    }


    public FollowObject(String follower_id, String follower_name, String following_id, String following_name) {


        this.follower_id = follower_id;
        this.follower_name = follower_name;
        this.following_id = following_id;
        this.following_name = following_name;


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

}

