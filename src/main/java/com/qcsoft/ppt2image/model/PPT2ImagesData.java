package com.qcsoft.ppt2image.model;

import java.io.Serializable;

public class PPT2ImagesData<T> implements Serializable {

    private String imagePath;

    private T object;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
