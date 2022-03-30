package com.example.mediaserver;

import java.util.Date;

public class MediaObject
{
    String name;
    String fileLocation;
    boolean uploaded;

    public MediaObject(String name, String fileLocation)
    {
        this.name = name;
        this.fileLocation = fileLocation;
        this.uploaded = false;

    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public boolean isUploaded() {
        return uploaded;
    }
    public void uploadSuccessful()
    {
        this.uploaded = true;
    }
}
