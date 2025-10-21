package com.example.verticalcounter.model;

import javax.swing.ImageIcon;

public class ImageResult {
    private String filename;
    private int lineCount;
    private ImageIcon thumbnail;

    public ImageResult(String filename, int lineCount, ImageIcon thumbnail) {
        this.filename = filename;
        this.lineCount = lineCount;
        this.thumbnail = thumbnail;
    }

    public String getFilename() {
        return filename;
    }

    public int getLineCount() {
        return lineCount;
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return filename + ": " + lineCount + " vertical lines";
    }
}
