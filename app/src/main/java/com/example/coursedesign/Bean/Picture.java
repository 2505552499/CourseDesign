package com.example.coursedesign.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Picture implements Serializable {
    int id;
    String name;
    String path;
    String audio_cn;
    String audio_en;
    String text_cn;
    String text_en;
    String type;

    public Picture(){}
    public Picture(int id, String name, String path, String audio_cn, String audio_en, String text_cn, String text_en, String type) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.audio_cn = audio_cn;
        this.audio_en = audio_en;
        this.text_cn = text_cn;
        this.text_en = text_en;
        this.type = type;
    }
    public static Picture getInstance(int id, String name, String path, String audio_cn, String audio_en, String text_cn, String text_en, String type){
        return new Picture(id, name, path, audio_cn, text_en, text_cn, text_en, type);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAudio_cn() {
        return audio_cn;
    }

    public void setAudio_cn(String audio_cn) {
        this.audio_cn = audio_cn;
    }

    public String getAudio_en() {
        return audio_en;
    }

    public void setAudio_en(String audio_en) {
        this.audio_en = audio_en;
    }

    public String getText_cn() {
        return text_cn;
    }

    public void setText_cn(String text_cn) {
        this.text_cn = text_cn;
    }

    public String getText_en() {
        return text_en;
    }

    public void setText_en(String text_en) {
        this.text_en = text_en;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
