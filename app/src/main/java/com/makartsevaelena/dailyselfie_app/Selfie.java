package com.makartsevaelena.dailyselfie_app;

import java.io.Serializable;

public class Selfie implements Serializable {

    private transient String pathSelfieFile;

    public Selfie(String pathSelfieFile) {
        this.pathSelfieFile = pathSelfieFile;
    }

    public String getPathSelfieFile() {
        return pathSelfieFile;
    }

    @Override
    public String toString() {
        return "Selfie{" +
                "pathSelfieFile='" + pathSelfieFile + '\'' +
                '}';
    }
}
