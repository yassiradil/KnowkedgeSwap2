package knowledgeswap2.ui;

import javax.swing.*;
import java.awt.*;

public class EllipsisLabel extends JLabel {
    private String fullText = "";

    public EllipsisLabel() {
        super("");
    }

    public EllipsisLabel(String text) {
        setFullText(text);
    }

    public void setFullText(String text) {
        fullText = text == null ? "" : text;
        setToolTipText(fullText);
        updateDisplayedText();
    }

    @Override
    public void setText(String text) {
        setFullText(text);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        updateDisplayedText();
    }

    private void updateDisplayedText() {
        int w = getWidth();
        if (w <= 0) {
            super.setText(fullText);
            return;
        }

        FontMetrics fm = getFontMetrics(getFont());
        if (fm.stringWidth(fullText) <= w) {
            super.setText(fullText);
            return;
        }

        String dots = "...";
        int max = w - fm.stringWidth(dots);
        if (max <= 0) {
            super.setText(dots);
            return;
        }

        String cut = fullText;
        while (!cut.isEmpty() && fm.stringWidth(cut) > max) {
            cut = cut.substring(0, cut.length() - 1);
        }
        super.setText(cut + dots);
    }
}

