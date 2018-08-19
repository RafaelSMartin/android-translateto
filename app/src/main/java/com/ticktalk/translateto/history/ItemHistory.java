package com.ticktalk.translateto.history;

/**
 * Created by Rafael S. Martin
 */

public class ItemHistory {

    private String fromLanguageTitle;
    private String fromText;
    private String toLanguageTitle;
    private String toText;
    private int fromLanguageFlag;
    private int toLanguageFlag;

    private int favourite;
    private Boolean favoriteAnimation;

    public ItemHistory(String fromLanguageTitle, String fromText,
                       String toLanguageTitle, String toText,
                       int favourite, int fromLanguageFlag, int toLanguageFlag, Boolean favoriteAnimation){

        this.fromLanguageTitle = fromLanguageTitle;
        this.fromText = fromText;
        this.toLanguageTitle = toLanguageTitle;
        this.toText = toText;
        this.favourite = favourite;
        this.fromLanguageFlag = fromLanguageFlag;
        this.toLanguageFlag = toLanguageFlag;
        this.favoriteAnimation = favoriteAnimation;

    }


    public String getFromLanguageTitle() {
        return fromLanguageTitle;
    }

    public void setFromLanguageTitle(String fromLanguageTitle) {
        this.fromLanguageTitle = fromLanguageTitle;
    }

    public String getFromText() {
        return fromText;
    }

    public void setFromText(String fromText) {
        this.fromText = fromText;
    }

    public String getToLanguageTitle() {
        return toLanguageTitle;
    }

    public void setToLanguageTitle(String toLanguageTitle) {
        this.toLanguageTitle = toLanguageTitle;
    }

    public String getToText() {
        return toText;
    }

    public void setToText(String toText) {
        this.toText = toText;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getFromLanguageFlag() {
        return fromLanguageFlag;
    }

    public void setFromLanguageFlag(int fromLanguageFlag) {
        this.fromLanguageFlag = fromLanguageFlag;
    }

    public int getToLanguageFlag() {
        return toLanguageFlag;
    }

    public void setToLanguageFlag(int toLanguageFlag) {
        this.toLanguageFlag = toLanguageFlag;
    }

    public Boolean getFavoriteAnimation() {
        return favoriteAnimation;
    }

    public void setFavoriteAnimation(Boolean favoriteAnimation) {
        this.favoriteAnimation = favoriteAnimation;
    }
}
