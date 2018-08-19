package com.ticktalk.translateto.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rafael S. Martin
 */

@Entity
public class FromResult {

    @Id(autoincrement = true)
    private Long id;

    @Property()
    private String languageCode;

    @Property()
    private String text;

    @Property()
    private Long listPosition;

    @Property()
    private Integer backgroundColor;

    @Property()
    private Integer fontSize;

    @Property()
    private String synonyms;

    @Property()
    private Boolean favoriteAnimation;

    @Transient
    private boolean isAds;

    @Transient
    private boolean isExpanded;

    @Transient
    List<ToResult> toResultList = new ArrayList<>();

    @Transient
    private boolean isPlayingSound = false;


    @Generated(hash = 1713946238)
    public FromResult(Long id, String languageCode, String text, Long listPosition,
            Integer backgroundColor, Integer fontSize, String synonyms, Boolean favoriteAnimation) {
        this.id = id;
        this.languageCode = languageCode;
        this.text = text;
        this.listPosition = listPosition;
        this.backgroundColor = backgroundColor;
        this.fontSize = fontSize;
        this.synonyms = synonyms;
        this.favoriteAnimation = favoriteAnimation;
    }

    @Generated(hash = 608412254)
    public FromResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getListPosition() {
        return listPosition;
    }

    public void setListPosition(Long listPosition) {
        this.listPosition = listPosition;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public boolean isAds() {
        return isAds;
    }

    public void setAds(boolean ads) {
        isAds = ads;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isPlayingSound() {
        return isPlayingSound;
    }

    public void setPlayingSound(boolean playingSound) {
        isPlayingSound = playingSound;
    }

    public List<ToResult> getToResultList()
    {
        return toResultList;
    }


    public void addToResult(ToResult toResult)
    {
        toResultList.add(toResult);
    }

    public Boolean getFavoriteAnimation() {
        return favoriteAnimation;
    }

    public void setFavoriteAnimation(Boolean favoriteAnimation) {
        this.favoriteAnimation = favoriteAnimation;
    }
}
