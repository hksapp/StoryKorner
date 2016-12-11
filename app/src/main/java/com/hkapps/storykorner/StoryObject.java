package com.hkapps.storykorner;

/**
 * Created by kamal on 11-12-2016.
 */

public class StoryObject {

    private String Title;
    private String Story;

    public StoryObject(){

    }

    public StoryObject(String Title , String Story){
        this.Title = Title;
        this.Story = Story;
    }

    public String getTitle() { return  Title;}

    public void setTitle(String Title) { this.Title = Title; }

    public String getStory() { return  Story;}

    public void setStory(String Story) { this.Story = Story; }


}
