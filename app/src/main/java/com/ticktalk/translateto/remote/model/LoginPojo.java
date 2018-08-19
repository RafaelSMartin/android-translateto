package com.ticktalk.translateto.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo que tomara los valores de JSON que recibamos
 * Tiene varias etiquetas que implementa el WebService en la web
 *
 * */

public class LoginPojo {

    @SerializedName("code_message_out")
    private String codMessage;

    @SerializedName("access")
    private String access;

    @SerializedName("first_name_out")
    private String firstName;

    @SerializedName("last_name_out")
    private String lastName;

    @SerializedName("email_out")
    private String email;

    @SerializedName("password_out")
    private String pass;

    @SerializedName("IpAddress")
    private String ipAddress;

    @SerializedName("UserAgent")
    private String userAgent;

    @SerializedName("code_lenguage_out")
    private String lenguajeNativo;

    @SerializedName("language_out")
    private String language;

    @SerializedName("Sesion")
    private String sesion;

    @SerializedName("Premium")
    private String premium;

    @SerializedName("find_out")
    private String find;

    @SerializedName("find_login_out")
    private String findLogin;

    @SerializedName("find_pass_out")
    private String findPass;

    @SerializedName("find_email_out")
    private String findEmail;

    @SerializedName("find_end_out")
    private String findEnd;


    public String getCodMessage() {
        return codMessage;
    }

    public void setCodMessage(String codMessage) {
        this.codMessage = codMessage;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLenguajeNativo() {
        return lenguajeNativo;
    }

    public void setLenguajeNativo(String lenguajeNativo) {
        this.lenguajeNativo = lenguajeNativo;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getFind() {
        return find;
    }

    public void setFind(String find) {
        this.find = find;
    }

    public String getFindLogin() {
        return findLogin;
    }

    public void setFindLogin(String findLogin) {
        this.findLogin = findLogin;
    }

    public String getFindPass() {
        return findPass;
    }

    public void setFindPass(String findPass) {
        this.findPass = findPass;
    }

    public String getFindEmail() {
        return findEmail;
    }

    public void setFindEmail(String findEmail) {
        this.findEmail = findEmail;
    }

    public String getFindEnd() {
        return findEnd;
    }

    public void setFindEnd(String findEnd) {
        this.findEnd = findEnd;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}