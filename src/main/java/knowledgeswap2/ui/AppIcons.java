package knowledgeswap2.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public final class AppIcons {
    private AppIcons() {}

    public static Image loadAppIcon(int size) {
        BufferedImage img = readPng("/logo.png");
        if (img == null) return null;
        return img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }

    public static Icon loadAppIconIcon(int size) {
        Image img = loadAppIcon(size);
        return img == null ? null : new ImageIcon(img);
    }

    private static BufferedImage readPng(String resourcePath) {
        try (InputStream in = AppIcons.class.getResourceAsStream(resourcePath)) {
            if (in == null) return null;
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }
}

