package com.ticktalk.translateto.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rafael S. Martin
 */

@Entity
public class ToResult {
    @Id(autoincrement = true)
    private Long id;

    @Property()
    private Long fromId;

    @Property()
    private String languageCode;

    @Property()
    private String text;

    @Property()
    private int translationOrder;

    @Property()
    private String synonyms;

    @Transient
    private boolean isPlayingSound = false;

    @Transient
    private boolean isLoadingSound = false;

    @Generated(hash = 1739036520)
    public ToResult(Long id, Long fromId, String languageCode, String text,
            int translationOrder, String synonyms) {
        this.id = id;
        this.fromId = fromId;
        this.languageCode = languageCode;
        this.text = text;
        this.translationOrder = translationOrder;
        this.synonyms = synonyms;
    }

    @Generated(hash = 754329027)
    public ToResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
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

    public int getTranslationOrder() {
        return translationOrder;
    }

    public void setTranslationOrder(int translationOrder) {
        this.translationOrder = translationOrder;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public boolean isPlayingSound() {
        return isPlayingSound;
    }

    public void setPlayingSound(boolean playingSound) {
        isPlayingSound = playingSound;
    }

    public boolean isLoadingSound() {
        return isLoadingSound;
    }

    public void setLoadingSound(boolean loadingSound) {
        isLoadingSound = loadingSound;
    }


}
