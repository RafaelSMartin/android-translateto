package com.ticktalk.translateto.remote.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Rafael S. Martin
 */

public class HumanPojo {

    //Para los headers
    @SerializedName("Content-Type")
    private String contentType;

    @SerializedName("keyToken")
    private String keyToken;

    @SerializedName("access")
    private String access;

    @SerializedName("email_in")
    private String email_in;

    @SerializedName("password_in")
    private String password_in;

    @SerializedName("language_source")
    private String language_source;

    @SerializedName("language_target")
    private String language_target;

    @SerializedName("text_translation")
    private String text_translation;

    @SerializedName("numero_language")
    private String numero_language;

    @SerializedName("instructions_language")
    private String instructions_language;

    @SerializedName("topic_language")
    private String topic_language;

    @SerializedName("tone_language")
    private String tone_language;

    @SerializedName("author_gender")
    private String author_gender;

    @SerializedName("audience_gender")
    private String audience_gender;

    @SerializedName("total_precio")
    private String total_precio;

    @SerializedName("total_words")
    private String total_words;

    @SerializedName("find_end_out")
    private String findEnd;

    @SerializedName("payer_email")
    private String payer_email;

    @SerializedName("payer_fname")
    private String payer_fname;

    @SerializedName("payer_lname")
    private String payer_lname;

    @SerializedName("payer_id")
    private String payer_id;

    @SerializedName("payer_address")
    private String payer_address;

    @SerializedName("payer_street")
    private String payer_street;

    @SerializedName("payer_city")
    private String payer_city;

    @SerializedName("payer_state")
    private String payer_state;

    @SerializedName("payer_country_code")
    private String payer_country_code;

    @SerializedName("payer_zip")
    private String payer_zip;

    @SerializedName("currency")
    private String currency;

    @SerializedName("payment_fee")
    private String payment_fee;

    @SerializedName("payment_gross")
    private String payment_gross;

    @SerializedName("payment_status")
    private String payment_status;

    @SerializedName("item")
    private String payment_item;

    @SerializedName("qty")
    private String qty;

    @SerializedName("date")
    private String date;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getKeyToken() {
        return keyToken;
    }

    public void setKeyToken(String keyToken) {
        this.keyToken = keyToken;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getEmail_in() {
        return email_in;
    }

    public void setEmail_in(String email_in) {
        this.email_in = email_in;
    }

    public String getPassword_in() {
        return password_in;
    }

    public void setPassword_in(String password_in) {
        this.password_in = password_in;
    }

    public String getLanguage_source() {
        return language_source;
    }

    public void setLanguage_source(String language_source) {
        this.language_source = language_source;
    }

    public String getLanguage_target() {
        return language_target;
    }

    public void setLanguage_target(String language_target) {
        this.language_target = language_target;
    }

    public String getText_translation() {
        return text_translation;
    }

    public void setText_translation(String text_translation) {
        this.text_translation = text_translation;
    }

    public String getNumero_language() {
        return numero_language;
    }

    public void setNumero_language(String numero_language) {
        this.numero_language = numero_language;
    }

    public String getInstructions_language() {
        return instructions_language;
    }

    public void setInstructions_language(String instructions_language) {
        this.instructions_language = instructions_language;
    }

    public String getTopic_language() {
        return topic_language;
    }

    public void setTopic_language(String topic_language) {
        this.topic_language = topic_language;
    }

    public String getTone_language() {
        return tone_language;
    }

    public void setTone_language(String tone_language) {
        this.tone_language = tone_language;
    }

    public String getAuthor_gender() {
        return author_gender;
    }

    public void setAuthor_gender(String author_gender) {
        this.author_gender = author_gender;
    }

    public String getAudience_gender() {
        return audience_gender;
    }

    public void setAudience_gender(String audience_gender) {
        this.audience_gender = audience_gender;
    }

    public String getTotal_precio() {
        return total_precio;
    }

    public void setTotal_precio(String total_precio) {
        this.total_precio = total_precio;
    }

    public String getTotal_words() {
        return total_words;
    }

    public void setTotal_words(String total_words) {
        this.total_words = total_words;
    }

    public String getFindEnd() {
        return findEnd;
    }

    public void setFindEnd(String findEnd) {
        this.findEnd = findEnd;
    }

    public String getPayer_email() {
        return payer_email;
    }

    public void setPayer_email(String payer_email) {
        this.payer_email = payer_email;
    }

    public String getPayer_fname() {
        return payer_fname;
    }

    public void setPayer_fname(String payer_fname) {
        this.payer_fname = payer_fname;
    }

    public String getPayer_lname() {
        return payer_lname;
    }

    public void setPayer_lname(String payer_lname) {
        this.payer_lname = payer_lname;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getPayer_address() {
        return payer_address;
    }

    public void setPayer_address(String payer_address) {
        this.payer_address = payer_address;
    }

    public String getPayer_city() {
        return payer_city;
    }

    public void setPayer_city(String payer_city) {
        this.payer_city = payer_city;
    }

    public String getPayer_state() {
        return payer_state;
    }

    public void setPayer_state(String payer_state) {
        this.payer_state = payer_state;
    }

    public String getPayer_country_code() {
        return payer_country_code;
    }

    public void setPayer_country_code(String payer_country_code) {
        this.payer_country_code = payer_country_code;
    }

    public String getPayer_zip() {
        return payer_zip;
    }

    public void setPayer_zip(String payer_zip) {
        this.payer_zip = payer_zip;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayment_fee() {
        return payment_fee;
    }

    public void setPayment_fee(String payment_fee) {
        this.payment_fee = payment_fee;
    }

    public String getPayment_gross() {
        return payment_gross;
    }

    public void setPayment_gross(String payment_gross) {
        this.payment_gross = payment_gross;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayment_item() {
        return payment_item;
    }

    public void setPayment_item(String payment_item) {
        this.payment_item = payment_item;
    }

    public String getPayer_street() {
        return payer_street;
    }

    public void setPayer_street(String payer_street) {
        this.payer_street = payer_street;
    }
}
