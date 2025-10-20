import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VerticalLineCounter {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java VerticalLineCounter <image-path>");
            System.exit(1);
        }

        String imagePath = args[0];

        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image == null) {
                System.err.println("Invalid image or unsupported format: " + imagePath);
                System.exit(1);
            }

            int lineCount = countVerticalBlackLines(image);
            System.out.println("Number of vertical black lines: " + lineCount);
        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Counts the number of vertical black lines in the given black & white image.
     * The image is assumed to have black lines on a white background.
     */
    private static int countVerticalBlackLines(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Threshold definition for black pixel detection (based on grayscale intensity)
        final int BLACK_THRESHOLD = 50; // adjust if needed based on images

        // Since lines are continuous from top to bottom, define minimal black pixels needed per column.
        final int MIN_BLACK_PIXELS_PER_COLUMN = height / 2;

        int lineCount = 0;
        boolean inLine = false; // track if currently scanning a line column-wise

        for (int x = 0; x < width; x++) {
            int blackPixelsInColumn = 0;

            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int gray = getGrayFromRGB(rgb);

                if (gray < BLACK_THRESHOLD) {
                    blackPixelsInColumn++;
                }
            }

            // If column has enough black pixels, treat it as part of a vertical line
            if (blackPixelsInColumn >= MIN_BLACK_PIXELS_PER_COLUMN) {
                if (!inLine) {
                    lineCount++;
                    inLine = true;
                }
            } else {
                inLine = false;
            }
        }

        return lineCount;
    }

    /**
     * Convert the RGB to grayscale using standard luminance formula.
     */
    private static int getGrayFromRGB(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        // Standard luminance calculation weighted average
        return (int)(0.299 * r + 0.587 * g + 0.114 * b);
    }
}