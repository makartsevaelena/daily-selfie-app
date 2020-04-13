package com.makartsevaelena.dailyselfie_app;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Selfie implements Serializable {

    private transient String pathSelfieFile;
    private transient String name;
    private transient Bitmap selfieBitmap;


    public Selfie(String pathSelfieFile, String name, Bitmap bitmap) {
        this.pathSelfieFile = pathSelfieFile;
        this.selfieBitmap = bitmap;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getPathSelfieFile() {
        return pathSelfieFile;
    }
    public Bitmap getSelfieBitmap() {
        return selfieBitmap;
    }


    @Override
    public String toString() {
        return "Selfie{" +
                "nameSelfieFile='" +
                ", pathSelfieFile='" + pathSelfieFile + '\'' +
                '}';
    }
}
