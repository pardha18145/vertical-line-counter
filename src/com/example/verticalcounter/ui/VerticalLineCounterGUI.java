package com.example.verticalcounter.ui;

import com.example.verticalcounter.service.VerticalLineCounter;
import com.example.verticalcounter.model.ImageResult;
import com.example.verticalcounter.renderer.ImageResultCellRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VerticalLineCounterGUI extends JFrame {
    private DefaultListModel<ImageResult> listModel;
    private JList<ImageResult> imageList;
    private JButton browseButton;

    public VerticalLineCounterGUI() {
        // Set Nimbus Look and Feel for classy UI
        setLookAndFeel();

        setTitle("Vertical Line Counter");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        browseButton = new JButton("Browse Images");
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        browseButton.setForeground(new Color(255, 255, 255));
        browseButton.setBackground(new Color(45, 118, 255));
        browseButton.setFocusPainted(false);
        browseButton.setBorder(new EmptyBorder(15, 30, 15, 30));
        browseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        browseButton.setOpaque(true);
        // Hover effect for button using mouse listeners
        browseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                browseButton.setBackground(new Color(30, 90, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                browseButton.setBackground(new Color(45, 118, 255));
            }
        });
        browseButton.addActionListener(e -> openFileChooser());

        listModel = new DefaultListModel<>();
        imageList = new JList<>(listModel);
        imageList.setCellRenderer(new ImageResultCellRenderer());
        imageList.setBackground(new Color(250, 250, 255));
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(imageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        getContentPane().setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(240, 242, 245)); // very light grey-blue background

        // Wrap button in a panel with padding and add some top margin
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(getContentPane().getBackground());
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        buttonPanel.add(browseButton);

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    // Override some Nimbus defaults for more elegance
                    UIManager.put("control", new Color(240, 242, 245));
                    UIManager.put("nimbusBlueGrey", new Color(200, 200, 220));
                    UIManager.put("nimbusFocus", new Color(100, 120, 230));
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus L&F not available, using default.");
        }
    }

    private void openFileChooser() {
    JFileChooser chooser = new JFileChooser();
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp"));
    int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            listModel.clear();
            File[] files = chooser.getSelectedFiles();
            StringBuilder invalidFiles = new StringBuilder();

            for (File file : files) {
                String name = file.getName().toLowerCase();
                if (!(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".bmp"))) {
                    invalidFiles.append("\u2022 ").append(file.getName()).append("\n");
                    continue;
                }
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img == null) throw new IOException("Not a valid image file");
                    int count = VerticalLineCounter.countVerticalBlackLines(img);
                    ImageIcon thumbnail = new ImageIcon(img.getScaledInstance(120, -1, Image.SCALE_SMOOTH));
                    listModel.addElement(new ImageResult(file.getName(), count, thumbnail));
                } catch (IOException ex) {
                    invalidFiles.append("\u2022 ").append(file.getName()).append(" (error: ").append(ex.getMessage()).append(")\n");
                }
            }

            if (invalidFiles.length() > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "These files could not be processed:\n\n" + invalidFiles,
                    "Unsupported File(s) or Errors",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerticalLineCounterGUI app = new VerticalLineCounterGUI();
            app.setVisible(true);
        });
    }
}