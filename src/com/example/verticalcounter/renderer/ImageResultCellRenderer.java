package com.example.verticalcounter.renderer;

import com.example.verticalcounter.model.ImageResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ImageResultCellRenderer extends JPanel implements ListCellRenderer<ImageResult> {
    private JLabel imageLabel = new JLabel();
    private JLabel textLabel = new JLabel();

    public ImageResultCellRenderer() {
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
        imageLabel.setIcon(value.getThumbnail());
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
