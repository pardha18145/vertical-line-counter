import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class VerticalLineCounterGUI extends JFrame {
    private DefaultListModel<ImageResult> listModel;
    private JList<ImageResult> imageList;
    private JButton browseButton;

    public VerticalLineCounterGUI() {
        setTitle("Vertical Line Counter with Image Preview");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Colorful button with gradient effect
        browseButton = new JButton("Browse Images") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 140, 0),
                        0, h, new Color(255, 215, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        browseButton.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        browseButton.setForeground(Color.WHITE);
        browseButton.setFocusPainted(false);
        browseButton.setBorder(new EmptyBorder(12, 25, 12, 25));
        browseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        browseButton.setContentAreaFilled(false);

        listModel = new DefaultListModel<>();
        imageList = new JList<>(listModel);
        imageList.setCellRenderer(new ColorfulImageResultRenderer());
        imageList.setBackground(new Color(255, 250, 240));
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(imageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        browseButton.addActionListener(e -> openFileChooser());

        getContentPane().setLayout(new BorderLayout(12, 12));
        getContentPane().add(browseButton, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Background gradient for frame
        ((JComponent) getContentPane()).setOpaque(false);
        setContentPane(new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(65, 105, 225),
                        w, h, new Color(255, 182, 193));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
                super.paintComponent(g);
            }
        });
        getContentPane().setLayout(new BorderLayout(12, 12));
        getContentPane().add(browseButton, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void openFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            listModel.clear();
            File[] files = chooser.getSelectedFiles();
            for (File file : files) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    int count = countVerticalBlackLines(img);
                    ImageIcon thumbnail = new ImageIcon(img.getScaledInstance(120, -1, Image.SCALE_SMOOTH));
                    listModel.addElement(new ImageResult(file.getName(), count, thumbnail));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error loading " + file.getName() + ": " + ex.getMessage(),
                            "Loading Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private int countVerticalBlackLines(BufferedImage image) {
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

    static class ImageResult {
        String filename;
        int lineCount;
        ImageIcon thumbnail;

        ImageResult(String filename, int lineCount, ImageIcon thumbnail) {
            this.filename = filename;
            this.lineCount = lineCount;
            this.thumbnail = thumbnail;
        }

        @Override
        public String toString() {
            return filename + ": " + lineCount + " vertical lines";
        }
    }

    static class ColorfulImageResultRenderer extends JPanel implements ListCellRenderer<ImageResult> {
        private JLabel imageLabel = new JLabel();
        private JLabel textLabel = new JLabel();

        public ColorfulImageResultRenderer() {
            setLayout(new BorderLayout(15, 15));
            add(imageLabel, BorderLayout.WEST);
            add(textLabel, BorderLayout.CENTER);
            setBorder(new EmptyBorder(12, 12, 12, 12));
            textLabel.setFont(new Font("Papyrus", Font.BOLD, 16));
            textLabel.setForeground(new Color(25, 25, 112));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ImageResult> list, ImageResult value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            imageLabel.setIcon(value.thumbnail);
            textLabel.setText(value.toString());

            if (isSelected) {
                setBackground(new Color(255, 99, 71));
                textLabel.setForeground(Color.WHITE);
            } else {
                setBackground(new Color(255, 228, 225));
                textLabel.setForeground(new Color(25, 25, 112));
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerticalLineCounterGUI app = new VerticalLineCounterGUI();
            app.setVisible(true);
        });
    }
}