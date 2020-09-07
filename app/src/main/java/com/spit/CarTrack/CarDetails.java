package com.spit.CarTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CarDetails {

    private String Uploader;
    private String Addresss;
    private String accuracy;
    private Long TimeStamp;
    private double Latitude;
    private  boolean proof_status;
   /* TextView TermsAndConditions;*/

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private  String label;
    private double Longitude;
    private String Image_Url;

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }


    public String getUploader() {
        return Uploader;
    }

    public void setUploader(String uploader) {
        Uploader = uploader;
    }

    public String getAddresss() {
        return Addresss;
    }

    public void setAddresss(String addresss) {
        Addresss = addresss;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public CarDetails(String uploader, String address, String accuracy, Long timestamp,String image_path_storage, boolean proof_status) {
        this.Uploader = uploader;
        this.Addresss = address;
        this.accuracy = accuracy;
        this.TimeStamp = timestamp;
        this.Image_Url=image_path_storage;
        this.proof_status= proof_status;

    }

    public CarDetails() {
          }


    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }


    public boolean isProof_status() {
        return proof_status;
    }

    public void setProof_status(boolean proof_status) {
        this.proof_status = proof_status;
    }
}