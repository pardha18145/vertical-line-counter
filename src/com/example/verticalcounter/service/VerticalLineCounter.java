package com.example.verticalcounter.service;

import java.awt.image.BufferedImage;

public class VerticalLineCounter {
    public static int countVerticalBlackLines(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        final int BLACK_THRESHOLD = 50;
        final int MIN_BLACK_PIXELS = height / 2;

        int lines = 0;
        boolean inLine = false;

        for (int x = 0; x < width; x++) {
            int blackPixels = 0;
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int gray = (int)(0.299*((rgb>>16)&0xFF) + 0.587*((rgb>>8)&0xFF) + 0.114*(rgb&0xFF));
                if (gray < BLACK_THRESHOLD) blackPixels++;
            }
            if (blackPixels >= MIN_BLACK_PIXELS) {
                if (!inLine) { lines++; inLine = true; }
            } else inLine = false;
        }
        return lines;
    }
}
