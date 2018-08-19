package com.ticktalk.translateto.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo que tomara los valores de JSON que recibamos
 * Tiene etiquetas que implementa el WebService en la web
 *
 * */

public class MessagePojo {

    //Para los headers
    @SerializedName("Content-Type")
    private String contentType;

    @SerializedName("keyToken")
    private String keyToken;

    @SerializedName("CodMensaje")
    private String codMessage;

    @SerializedName("Acceso")
    private String access;

    @SerializedName("TextoTraducir")
    private String text;

    @SerializedName("TextoTraducido")
    private String traducedText;

    @SerializedName("IdiomaOrigen")
    private String languageOrigin;

    @SerializedName("IdiomaFinal")
    private String languageFinal;

    @SerializedName("Sinonimos")
    private String synonyms;

    @SerializedName("FindEnd")
    private String findEnd;


    public void setCodMessage(String codMensaje){
        this.codMessage = codMensaje;
    }

    public void setlanguageOrigin(String idiomaOrigen){
        this.languageOrigin = idiomaOrigen;
    }

    public void setlanguageFinal(String languageFinal){
        this.languageFinal = languageFinal;
    }

    public void setSynonyms(String sinonimos){
        this.synonyms = sinonimos;
    }

    public String getCodMessage(){
        return codMessage;
    }

    public String getLanguageOrigin(){ return languageOrigin; }

    public String getLanguageFinal(){ return languageFinal; }

    public String getSynonyms(){
        return synonyms;
    }

    public String getFindEnd() {
        return findEnd;
    }

    public void setFindEnd(String findEnd) {
        this.findEnd = findEnd;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTraducedText() {
        return traducedText;
    }

    public void setTraducedText(String traducedText) {
        this.traducedText = traducedText;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

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
}
