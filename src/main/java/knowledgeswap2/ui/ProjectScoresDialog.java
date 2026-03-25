package knowledgeswap2.ui;

import knowledgeswap2.model.Project;
import knowledgeswap2.model.Review;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProjectScoresDialog extends JDialog {
    public ProjectScoresDialog(Window owner, Project project) {
        super(owner, "My assignment reviews", ModalityType.APPLICATION_MODAL);

        setSize(540, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout(8, 4));
        header.setBorder(new EmptyBorder(12, 12, 0, 12));
        header.setBackground(UiTheme.windowBg());

        JLabel title = new JLabel(project.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setForeground(UiTheme.textColor());

        String major = project.getOwner() == null ? "" : project.getOwner().getMajorCode().trim();
        JLabel ownerLabel = new JLabel("Student: " + project.getOwner().getName() +
                " (ID: " + project.getOwner().getId() + ")" +
                (major.isEmpty() ? "" : "  •  " + major));
        ownerLabel.setForeground(UiTheme.mutedText());

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(title);
        left.add(ownerLabel);

        JLabel avgLabel = new JLabel(ScoreStyle.averageText(project));
        avgLabel.setFont(avgLabel.getFont().deriveFont(Font.BOLD, 13f));
        ScoreStyle.applyAverageColors(avgLabel, project);

        header.add(left, BorderLayout.WEST);
        header.add(avgLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        DefaultListModel<Review> model = new DefaultListModel<>();
        for (Review r : project.getReviews()) model.addElement(r);

        JList<Review> list = new JList<>(model);
        list.setCellRenderer(new ReviewCardRenderer());
        list.setFixedCellHeight(-1);
        list.setVisibleRowCount(10);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() < 2 || !SwingUtilities.isLeftMouseButton(e)) return;
                Review r = list.getSelectedValue();
                if (r == null) return;
                new ReviewDetailsDialog(owner, r).setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(new EmptyBorder(8, 12, 12, 12));
        scroll.getViewport().setBackground(UiTheme.cardBg());

        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(UiTheme.windowBg());
        bottom.setBorder(new EmptyBorder(0, 12, 12, 12));

        ModernButton close = ModernButton.primary("Close");
        close.addActionListener(e -> dispose());
        bottom.add(close);

        add(bottom, BorderLayout.SOUTH);
    }
}

