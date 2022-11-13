package com.example.demo.model.dto;

public class MediaDTO {
    private String id;
    private String downloadURL;
    private String filename;
    private long fileSize;
    private String fileType;

    public MediaDTO(String downloadURL, String filename, long fileSize, String fileType) {
        this.downloadURL = downloadURL;
        this.filename = filename;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}

