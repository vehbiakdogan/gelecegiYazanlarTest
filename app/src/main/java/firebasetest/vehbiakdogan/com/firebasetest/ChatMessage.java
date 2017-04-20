package firebasetest.vehbiakdogan.com.firebasetest;

import android.net.Uri;

/**
 * Created by vehbiakdogan on 19.04.2017.
 */

public class ChatMessage {
    private String mesaj, tokenID,getMessageImageUrl ;
    int kategori;

    public ChatMessage() {
    }


    public ChatMessage(String message, String UID, int mesajKategori, String getMessageImageUrl) {
        mesaj = message;
        tokenID = UID;
        kategori = mesajKategori;
        this.getMessageImageUrl = getMessageImageUrl;


    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public int getKategori() {
        return kategori;
    }

    public void setKategori(int kategori) {
        this.kategori = kategori;
    }

    public String getGetMessageImageUrl() {
        return getMessageImageUrl;
    }

    public void setGetMessageImageUrl(String getMessageImageUrl) {
        this.getMessageImageUrl = getMessageImageUrl;
    }
}
