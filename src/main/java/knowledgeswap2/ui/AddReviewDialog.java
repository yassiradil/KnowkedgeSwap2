package knowledgeswap2.ui;

import knowledgeswap2.model.Project;
import knowledgeswap2.service.FeedbackSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddReviewDialog extends JDialog {
    private final JTextField reviewerField = new JTextField();
    private final JSpinner scoreSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
    private final JTextArea commentArea = new JTextArea(6, 24);

    public AddReviewDialog(Window owner, FeedbackSystem system, Project project, Runnable onSaved) {
        super(owner, "Add review", ModalityType.APPLICATION_MODAL);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        content.setBackground(UiTheme.windowBg());
        add(content, BorderLayout.CENTER);

        JLabel title = new JLabel("Review for: " + project.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
        title.setForeground(UiTheme.textColor());
        content.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UiTheme.cardBg());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Reviewer name"), c);
        c.gridx = 1; c.gridy = 0;
        form.add(reviewerField, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Score"), c);
        c.gridx = 1; c.gridy = 1;
        form.add(scoreSpinner, c);
        c.gridx = 2; c.gridy = 1;
        form.add(new JLabel("/100"), c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Comment"), c);
        c.gridx = 1; c.gridy = 2;
        c.gridwidth = 2;
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.getViewport().setBackground(UiTheme.cardBg());
        form.add(commentScroll, c);

        content.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(UiTheme.windowBg());

        ModernButton cancel = ModernButton.secondary("Cancel");
        cancel.addActionListener(e -> dispose());

        ModernButton save = ModernButton.primary("Save review");
        save.addActionListener(e -> {
            String reviewer = reviewerField.getText().trim();
            String comment = commentArea.getText().trim();
            int score = (Integer) scoreSpinner.getValue();

            if (reviewer.isEmpty() || comment.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill reviewer name and comment.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            system.addReview(project, reviewer, score, comment);
            if (onSaved != null) onSaved.run();
            dispose();
        });

        buttons.add(cancel);
        buttons.add(save);
        add(buttons, BorderLayout.SOUTH);
    }
}

