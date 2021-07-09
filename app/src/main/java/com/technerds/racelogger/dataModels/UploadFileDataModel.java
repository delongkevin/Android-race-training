package com.technerds.racelogger.dataModels;

public class UploadFileDataModel {

    private String path;
    private String filename;

    public UploadFileDataModel() {
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile_name() {
        return filename;
    }

    public void setFile_name(String file_name) {
        this.filename = file_name;
    }
}
