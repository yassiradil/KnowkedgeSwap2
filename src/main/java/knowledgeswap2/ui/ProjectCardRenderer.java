package knowledgeswap2.ui;

import knowledgeswap2.model.Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProjectCardRenderer extends JPanel implements ListCellRenderer<Project> {
    private final EllipsisLabel titleLabel = new EllipsisLabel();
    private final JLabel subLabel = new JLabel();
    private final JLabel badgeLabel = new JLabel();

    public ProjectCardRenderer() {
        setLayout(new BorderLayout(10, 8));
        setOpaque(true);
        setBorder(new EmptyBorder(10, 12, 10, 12));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        subLabel.setForeground(UiTheme.mutedText());

        badgeLabel.setOpaque(true);
        badgeLabel.setBorder(new EmptyBorder(4, 8, 4, 8));
        badgeLabel.setFont(badgeLabel.getFont().deriveFont(Font.BOLD, 12f));

        JPanel center = new JPanel(new GridLayout(2, 1, 0, 2));
        center.setOpaque(false);
        center.add(titleLabel);
        center.add(subLabel);

        add(center, BorderLayout.CENTER);
        add(badgeLabel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Project> list, Project value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            titleLabel.setFullText("");
            subLabel.setText("");
            badgeLabel.setText("");
            return this;
        }

        titleLabel.setFullText(value.getTitle());
        String major = value.getOwner() == null ? "" : value.getOwner().getMajorCode().trim();
        if (!major.isEmpty()) {
            subLabel.setText(value.getOwner().getName() + "  •  ID: " + value.getOwner().getId() + "  •  " + major);
        } else {
            subLabel.setText(value.getOwner().getName() + "  •  ID: " + value.getOwner().getId());
        }

        badgeLabel.setText(ScoreStyle.badgeText(value));
        ScoreStyle.applyBadgeColors(badgeLabel, value);

        Color bg = isSelected ? new Color(235, 235, 235) : UiTheme.cardBg();
        Color border = isSelected ? new Color(170, 170, 170) : UiTheme.border();
        setBackground(bg);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        titleLabel.setForeground(UiTheme.textColor());
        subLabel.setForeground(UiTheme.mutedText());

        return this;
    }
}

