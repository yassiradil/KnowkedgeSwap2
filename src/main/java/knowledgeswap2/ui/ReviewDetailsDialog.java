package knowledgeswap2.ui;

import knowledgeswap2.model.Review;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReviewDetailsDialog extends JDialog {
    public ReviewDetailsDialog(Window owner, Review review) {
        super(owner, "Review details", ModalityType.APPLICATION_MODAL);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBorder(new EmptyBorder(12, 12, 0, 12));
        header.setBackground(UiTheme.windowBg());

        JLabel name = new JLabel(review.getReviewerName());
        name.setFont(name.getFont().deriveFont(Font.BOLD, 18f));
        name.setForeground(UiTheme.textColor());

        JLabel score = new JLabel(review.getScoreOutOf100() + "/100");
        score.setFont(score.getFont().deriveFont(Font.BOLD, 16f));
        score.setOpaque(true);
        score.setBorder(new EmptyBorder(6, 10, 6, 10));

        int s = review.getScoreOutOf100();
        if (s >= 85) {
            score.setBackground(new Color(220, 252, 231));
            score.setForeground(new Color(22, 101, 52));
        } else if (s >= 60) {
            score.setBackground(new Color(254, 249, 195));
            score.setForeground(new Color(133, 77, 14));
        } else {
            score.setBackground(new Color(254, 226, 226));
            score.setForeground(new Color(153, 27, 27));
        }

        header.add(name, BorderLayout.WEST);
        header.add(score, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTextArea text = new JTextArea(review.getComment());
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setFont(text.getFont().deriveFont(14f));
        text.setBackground(UiTheme.cardBg());
        text.setForeground(UiTheme.textColor());
        text.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(text);
        scroll.setBorder(new EmptyBorder(0, 12, 12, 12));
        scroll.getViewport().setBackground(UiTheme.cardBg());

        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBorder(new EmptyBorder(0, 12, 12, 12));
        bottom.setBackground(UiTheme.windowBg());

        ModernButton close = ModernButton.primary("Close");
        close.addActionListener(e -> dispose());
        bottom.add(close);

        add(bottom, BorderLayout.SOUTH);
    }
}

