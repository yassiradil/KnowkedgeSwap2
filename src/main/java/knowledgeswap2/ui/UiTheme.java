package knowledgeswap2.ui;

import javax.swing.*;
import java.awt.*;

public class UiTheme {
    public static void apply() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Color bg = windowBg();
        Color text = textColor();
        Color border = border();
        Color selectionBg = new Color(235, 235, 235);

        UIManager.put("control", bg);
        UIManager.put("info", bg);
        UIManager.put("text", text);
        UIManager.put("Panel.background", bg);
        UIManager.put("Viewport.background", bg);
        UIManager.put("List.background", bg);
        UIManager.put("List.foreground", text);
        UIManager.put("List.selectionBackground", selectionBg);
        UIManager.put("List.selectionForeground", text);
        UIManager.put("Label.foreground", text);
        UIManager.put("TextField.background", bg);
        UIManager.put("TextField.foreground", text);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        UIManager.put("TextArea.background", bg);
        UIManager.put("TextArea.foreground", text);
        UIManager.put("ScrollPane.background", bg);
        UIManager.put("TabbedPane.background", bg);
        UIManager.put("TabbedPane.foreground", text);
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.contentAreaColor", bg);
        UIManager.put("TabbedPane.focus", bg);

        Font font = new Font("Segoe UI", Font.PLAIN, 13);
        setDefaultFont(font);
    }

    public static Color windowBg() { return new Color(246, 246, 246); }
    public static Color cardBg() { return Color.WHITE; }
    public static Color textColor() { return new Color(18, 18, 18); }
    public static Color mutedText() { return new Color(90, 90, 90); }
    public static Color border() { return new Color(220, 220, 220); }

    private static void setDefaultFont(Font font) {
        for (Object key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof Font) UIManager.put(key, font);
        }
    }
}

