package com.example.verticalcounter.service;

import java.awt.image.BufferedImage;

public class VerticalLineCounter {

    // takes a BufferedImage as input and returns the count of vertical black lines detected in that image
    public static int countVerticalBlackLines(BufferedImage image) {

        // Gets the width and height (in pixels) of the image for use in later calculations
        int width = image.getWidth();
        int height = image.getHeight();

        // BLACK_THRESHOLD is the grayscale value below which a pixel is considered "black" (0 = black, 255 = white).
        // MIN_BLACK_PIXELS sets the minimum number of black pixels needed in a column to consider that column as part of a vertical black line. Here, at least half the image height must be black to count
        final int BLACK_THRESHOLD = 50;
        final int MIN_BLACK_PIXELS = height / 2;

        // lines will store the total number of vertical lines found
        // inLine is a flag indicating whether we're currently traversing a black line column
        int lines = 0;
        boolean inLine = false;

        // Reads all pixels from the entire image
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        // Starts a loop over each column. For column x, initializes blackPixels to count how many pixels in this column are black
        for (int x = 0; x < width; x++) {
            int blackPixels = 0;

            // Loops over each row in the current column. rgb value is extracted from the pixel
            for (int y = 0; y < height; y++) {
                int rgb = pixels[x + y * width];

                // Extracts the red, green, and blue components from the RGB value
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Calculates the grayscale value of the pixel using the common weighted formula
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);

                // If the grayscale brightness is below the threshold, it's considered a black pixel.
                if (gray < BLACK_THRESHOLD) blackPixels++;
            }

            // If enough black pixels were found in this column
            if (blackPixels >= MIN_BLACK_PIXELS) {

                // If not already "in" a line, increment the line count and set the flag to true.
                if (!inLine) {
                    lines++;
                    inLine = true;
                }

                // If not enough black pixels in this column
            } else {

                // Signal that we're no longer in a black line
                inLine = false;
            }
        }

        // After checking all columns, return the total number of vertical black lines detected.
        return lines;
    }
}
