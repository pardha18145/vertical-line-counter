import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * VerticalLineCounter
 * Counts vertical black lines in a black-and-white image (JPG/PNG) using pixel analysis.
 * Usage: java VerticalLineCounter <path-to-image>
 * Example: java -cp out VerticalLineCounter resources/img_1.jpg
 */
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
                throw new IOException("Unable to read image: " + imagePath);
            }
            int lines = countVerticalBlackLines(image);
            System.out.println("Number of vertical black lines: " + lines);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Counts the number of vertical black lines by analyzing columns for continuous black pixels.
     * Assumes lines are thick enough to have multiple black pixels per column and are continuous from top to bottom.
     */
    private static int countVerticalBlackLines(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        final int BLACK_THRESHOLD = 50; // Tune for your images—typical for MS Paint
        final int MIN_BLACK_PIXELS_PER_COLUMN = height / 2; // Line must span at least half the image vertically

        int lines = 0;
        boolean inLine = false;

        for (int x = 0; x < width; x++) {
            int blackPixels = 0;
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int gray = getGrayIntensity(rgb);
                if (gray < BLACK_THRESHOLD) {
                    blackPixels++;
                }
            }

            if (blackPixels >= MIN_BLACK_PIXELS_PER_COLUMN) {
                if (!inLine) {
                    lines++;
                    inLine = true;
                }
            } else {
                inLine = false;
            }
        }

        return lines;
    }

    /**
     * Converts an RGB triple to grayscale intensity using a weighted average.
     */
    private static int getGrayIntensity(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (int) (0.299 * r + 0.587 * g + 0.114 * b);
    }
}