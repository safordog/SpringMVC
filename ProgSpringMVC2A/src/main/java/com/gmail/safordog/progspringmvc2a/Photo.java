package com.gmail.safordog.progspringmvc2a;

import com.sun.javafx.beans.IDProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Photo {

    @Id
    private long id;
    private byte[] image;

    public Photo() {
    }

    public Photo(long id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
