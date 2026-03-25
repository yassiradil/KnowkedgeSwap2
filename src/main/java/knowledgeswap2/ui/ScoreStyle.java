package knowledgeswap2.ui;

import knowledgeswap2.model.Project;

import javax.swing.*;
import java.awt.*;

public final class ScoreStyle {
    private ScoreStyle() {}

    public static final Color NO_REVIEWS_BG = new Color(244, 236, 224);
    public static final Color NO_REVIEWS_FG = new Color(111, 92, 72);

    public static boolean hasReviews(Project p) {
        return p != null && p.getReviews() != null && !p.getReviews().isEmpty();
    }

    public static String badgeText(Project p) {
        if (!hasReviews(p)) return "-/100";
        return String.format("%.0f/100", p.calculateAverageScore());
    }

    public static String averageText(Project p) {
        if (!hasReviews(p)) return "Average: -/100";
        return String.format("Average: %.2f/100", p.calculateAverageScore());
    }

    public static void applyBadgeColors(JLabel badge, Project p) {
        if (badge == null) return;
        if (!hasReviews(p)) {
            badge.setBackground(NO_REVIEWS_BG);
            badge.setForeground(NO_REVIEWS_FG);
            return;
        }

        double avg = p.calculateAverageScore();
        if (avg >= 85) {
            badge.setBackground(new Color(220, 252, 231));
            badge.setForeground(new Color(22, 101, 52));
        } else if (avg >= 60) {
            badge.setBackground(new Color(254, 249, 195));
            badge.setForeground(new Color(133, 77, 14));
        } else {
            badge.setBackground(new Color(254, 226, 226));
            badge.setForeground(new Color(153, 27, 27));
        }
    }

    public static void applyAverageColors(JLabel label, Project p) {
        if (label == null) return;
        label.setForeground(hasReviews(p) ? UiTheme.textColor() : NO_REVIEWS_FG);
    }
}

