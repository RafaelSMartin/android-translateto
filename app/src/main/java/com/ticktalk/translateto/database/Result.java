package com.ticktalk.translateto.database;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Indogroup02 on 17/01/2018.
 */

public class Result {

    @Id(autoincrement = true)
    private long id;

    @Property()
    private String languageCodeFrom;

    @Property()
    private String lagnguageFrom;

    @Property()
    private String languageCodeTo;

    @Property()
    private String lagnguageTo;

    @Property()
    private Long listPosition;

    @Property()
    private Integer fontSize;

    @Property()
    private String textFrom;

    @Property()
    private String textTo;

    @Property()
    private String synonyms;

    @Transient
    List<Result> resultList = new ArrayList<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLanguageCodeFrom() {
        return languageCodeFrom;
    }

    public void setLanguageCodeFrom(String languageCodeFrom) {
        this.languageCodeFrom = languageCodeFrom;
    }

    public String getLagnguageFrom() {
        return lagnguageFrom;
    }

    public void setLagnguageFrom(String lagnguageFrom) {
        this.lagnguageFrom = lagnguageFrom;
    }

    public String getLanguageCodeTo() {
        return languageCodeTo;
    }

    public void setLanguageCodeTo(String languageCodeTo) {
        this.languageCodeTo = languageCodeTo;
    }

    public String getLagnguageTo() {
        return lagnguageTo;
    }

    public void setLagnguageTo(String lagnguageTo) {
        this.lagnguageTo = lagnguageTo;
    }

    public Long getListPosition() {
        return listPosition;
    }

    public void setListPosition(Long listPosition) {
        this.listPosition = listPosition;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getTextFrom() {
        return textFrom;
    }

    public void setTextFrom(String textFrom) {
        this.textFrom = textFrom;
    }

    public String getTextTo() {
        return textTo;
    }

    public void setTextTo(String textTo) {
        this.textTo = textTo;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public List<Result> getResultList()
    {
        return resultList;
    }


    public void addResult(Result result)
    {
        resultList.add(result);
    }
}
