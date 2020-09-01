package com.example.ambulans.model;

import java.io.Serializable;

public class Requests implements Serializable {
    private String nama;
    private String alamat;


    private String key;

    public Requests(){}
    public Requests(String nama, String alamat){
        this.nama = nama;
        this.alamat = alamat;

    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getAlamat() {
        return alamat;
    }
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return " "+nama+"\n" +
                " "+alamat;
    }

}
