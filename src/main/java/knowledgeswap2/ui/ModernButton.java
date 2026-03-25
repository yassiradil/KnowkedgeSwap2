package knowledgeswap2.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private final Color normalBg;
    private final Color hoverBg;

    private ModernButton(String text, Color normalBg, Color hoverBg, Color fg) {
        super(text);
        this.normalBg = normalBg;
        this.hoverBg = hoverBg;

        setForeground(fg);
        setBackground(normalBg);
        setFocusPainted(false);
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(false);
        setBorder(new EmptyBorder(8, 14, 8, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { setBackground(ModernButton.this.hoverBg); }
            @Override public void mouseExited(MouseEvent e) { setBackground(ModernButton.this.normalBg); }
        });
    }

    public static ModernButton primary(String text) {
        return new ModernButton(text, Color.BLACK, new Color(30, 30, 30), Color.WHITE);
    }

    public static ModernButton secondary(String text) {
        ModernButton b = new ModernButton(text, new Color(242, 242, 242), new Color(232, 232, 232), UiTheme.textColor());
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        return b;
    }

    public static ModernButton danger(String text) {
        ModernButton b = new ModernButton(text, new Color(176, 20, 20), new Color(140, 15, 15), Color.WHITE);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 10, 10), 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        return b;
    }
}

