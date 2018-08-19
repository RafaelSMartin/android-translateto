package com.ticktalk.translateto.webservicesvolley.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Indogroup02 on 18/12/2017.
 */

public class SynonymsPojo {

    @SerializedName("word")
    @Expose
    private String word;

    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms = null;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }


}
