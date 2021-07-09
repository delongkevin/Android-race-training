package com.technerds.racelogger.dataModels;

public class UploadFileModel {

    private int status;
    private String message;
    private UploadFileDataModel data;
    private String error;

    public UploadFileModel() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UploadFileDataModel getData() {
        return data;
    }

    public void setData(UploadFileDataModel data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
