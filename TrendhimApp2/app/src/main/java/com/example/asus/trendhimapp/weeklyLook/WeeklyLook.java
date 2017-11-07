package com.example.asus.trendhimapp.weeklyLook;

import java.util.ArrayList;

public class WeeklyLook {

    private String key, phrase, mainPictureUrl, secondPictureUrl, thirdPictureUrl, fourthPictureUrl, fifthPictureUrl;
    private ArrayList<String> products;

    WeeklyLook(String key, String phrase, String mainPictureUrl, String secondPictureUrl,
               String thirdPictureURl, String fourthPictureUrl, String fifthPictureUrl) {
        this.key = key;
        this.mainPictureUrl = mainPictureUrl;
        this.secondPictureUrl = secondPictureUrl;
        this.thirdPictureUrl = thirdPictureURl;
        this.fourthPictureUrl = fourthPictureUrl;
        this.fifthPictureUrl = fifthPictureUrl;
        this.phrase = phrase;
        products = new ArrayList<>();
    }

    WeeklyLook(){}; // Empty constructor for the database

    public String getKey() {
        return key;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

     public String getSecondPictureUrl() {
        return secondPictureUrl;
    }

    public String getThirdPictureUrl() {
        return thirdPictureUrl;
    }

    public String getFourthPictureUrl() {
        return fourthPictureUrl;
    }

    public String getFifthPictureUrl() {
        return fifthPictureUrl;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public String getPhrase(){
        return  phrase;
    }

}
