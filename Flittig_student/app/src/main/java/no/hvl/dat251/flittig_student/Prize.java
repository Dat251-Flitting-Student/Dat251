package no.hvl.dat251.flittig_student;

import android.media.Image;

public class Prize {


    public String offer;
    public int prize;
    public String providerName;
    public int image;

    public Prize(String offer, int prize, String providerName, int image){
        this.offer = offer;
        this.prize = prize;
        this.providerName = providerName;
        this.image = image;
    }

    public Prize() {

    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }



}
