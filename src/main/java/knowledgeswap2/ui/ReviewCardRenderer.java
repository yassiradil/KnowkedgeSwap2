package knowledgeswap2.ui;

import knowledgeswap2.model.Review;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReviewCardRenderer extends JPanel implements ListCellRenderer<Review> {
    private final JLabel nameLabel = new JLabel();
    private final JLabel scoreBadge = new JLabel();

    public ReviewCardRenderer() {
        setLayout(new BorderLayout(10, 0));
        setOpaque(true);

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

        scoreBadge.setOpaque(true);
        scoreBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
        scoreBadge.setFont(scoreBadge.getFont().deriveFont(Font.BOLD, 12f));

        add(nameLabel, BorderLayout.WEST);
        add(scoreBadge, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Review> list, Review value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            nameLabel.setText("");
            scoreBadge.setText("");
            return this;
        }

        nameLabel.setText(value.getReviewerName());
        scoreBadge.setText(value.getScoreOutOf100() + "/100");

        Color bg = isSelected ? new Color(240, 240, 240) : UiTheme.cardBg();
        Color border = isSelected ? new Color(170, 170, 170) : UiTheme.border();

        setBackground(bg);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        nameLabel.setForeground(UiTheme.textColor());

        int score = value.getScoreOutOf100();
        if (score >= 85) {
            scoreBadge.setBackground(new Color(220, 252, 231));
            scoreBadge.setForeground(new Color(22, 101, 52));
        } else if (score >= 60) {
            scoreBadge.setBackground(new Color(254, 249, 195));
            scoreBadge.setForeground(new Color(133, 77, 14));
        } else {
            scoreBadge.setBackground(new Color(254, 226, 226));
            scoreBadge.setForeground(new Color(153, 27, 27));
        }

        return this;
    }
}

