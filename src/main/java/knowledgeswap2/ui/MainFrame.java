package knowledgeswap2.ui;

import knowledgeswap2.model.Major;
import knowledgeswap2.model.Project;
import knowledgeswap2.model.Review;
import knowledgeswap2.service.FeedbackSystem;
import knowledgeswap2.storage.DataStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    private final FeedbackSystem system;
    private final DataStorage storage;

    // Add assignment form
    private JTextField studentNameField;
    private JTextField studentIdField;
    private JComboBox<Major> majorCombo;
    private JTextField projectTitleField;
    private JTextField pdfPathField;
    private String selectedPdfPath = "";

    // Lists + filters
    private JComboBox<Object> myMajorFilter;
    private DefaultListModel<Project> myProjectsModel;
    private JList<Project> myProjectsList;

    private JComboBox<Object> studentsMajorFilter;
    private DefaultListModel<Project> studentsProjectsModel;
    private JList<Project> studentsProjectsList;

    // Details area (right)
    private EllipsisLabel detailsTitle;
    private JLabel detailsStudent;
    private JLabel detailsAvg;
    private DefaultListModel<Review> reviewsModel;
    private JList<Review> reviewsList;

    public MainFrame(FeedbackSystem system, DataStorage storage) {
        this.system = system;
        this.storage = storage;

        setTitle("Knowledge Swap");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        Image icon = AppIcons.loadAppIcon(64);
        if (icon != null) {
            setIconImage(icon);
        }

        buildUi();
        reloadAll();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndExit();
            }
        });
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(UiTheme.windowBg());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(true);
        header.setBackground(UiTheme.windowBg());
        header.setBorder(new EmptyBorder(14, 14, 8, 14));

        JLabel appTitle = new JLabel("Knowledge Swap", AppIcons.loadAppIconIcon(28), SwingConstants.LEFT);
        appTitle.setFont(appTitle.getFont().deriveFont(Font.BOLD, 20f));
        appTitle.setForeground(UiTheme.textColor());

        ModernButton save = ModernButton.secondary("Save");
        save.addActionListener(e -> saveNowWithMessage());

        header.add(appTitle, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(true);
        right.setBackground(UiTheme.windowBg());
        right.add(save);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JComponent buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(0, 14, 14, 14));

        tabs.addTab("My Assignments", buildMyAssignmentsTab());
        tabs.addTab("Students Assignments", buildStudentsAssignmentsTab());

        return tabs;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(6, 14, 10, 14));
        footer.setOpaque(true);
        footer.setBackground(UiTheme.windowBg());

        JLabel file = new JLabel("Data: " + storage.getDataFile().getAbsolutePath());
        file.setForeground(UiTheme.mutedText());
        footer.add(file, BorderLayout.WEST);
        return footer;
    }

    private JPanel buildMyAssignmentsTab() {
        JPanel left = buildAddAssignmentCard();
        JPanel right = buildMyAssignmentsListCard();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(620); // make New Assignment wider
        split.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(UiTheme.windowBg());
        container.add(split, BorderLayout.CENTER);
        return container;
    }

    private JPanel buildStudentsAssignmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UiTheme.windowBg());

        JPanel leftList = buildStudentsAssignmentsListCard();
        JPanel rightDetails = buildRightDetailsPanel();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftList, rightDetails);
        split.setDividerLocation(420);
        split.setBorder(BorderFactory.createEmptyBorder());

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAddAssignmentCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UiTheme.cardBg());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        JLabel title = new JLabel("New Assignment");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setForeground(UiTheme.textColor());

        studentNameField = new JTextField();
        studentIdField = new JTextField();
        majorCombo = new JComboBox<>(Major.allMajors().toArray(new Major[0]));
        majorCombo.setSelectedIndex(0);
        projectTitleField = new JTextField();

        pdfPathField = new JTextField("No PDF selected");
        pdfPathField.setEditable(false);
        pdfPathField.setFocusable(false);
        pdfPathField.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        pdfPathField.setHighlighter(null);
        pdfPathField.setSelectionColor(pdfPathField.getBackground());
        pdfPathField.setSelectedTextColor(pdfPathField.getForeground());
        pdfPathField.setBackground(UiTheme.cardBg());
        pdfPathField.setForeground(UiTheme.mutedText());
        pdfPathField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        pdfPathField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { choosePdf(); }
        });

        ModernButton choosePdf = ModernButton.secondary("Browse");
        choosePdf.addActionListener(e -> choosePdf());

        ModernButton add = ModernButton.primary("Add");
        add.addActionListener(e -> addAssignment());

        ModernButton clear = ModernButton.secondary("Clear");
        clear.addActionListener(e -> clearForm());

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(title, c);
        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 1;
        card.add(new JLabel("Student name"), c);
        c.gridx = 1; c.gridy = 1;
        card.add(studentNameField, c);

        c.gridx = 0; c.gridy = 2;
        card.add(new JLabel("Student ID"), c);
        c.gridx = 1; c.gridy = 2;
        card.add(studentIdField, c);

        c.gridx = 0; c.gridy = 3;
        card.add(new JLabel("Major"), c);
        c.gridx = 1; c.gridy = 3;
        card.add(majorCombo, c);

        c.gridx = 0; c.gridy = 4;
        card.add(new JLabel("Project title"), c);
        c.gridx = 1; c.gridy = 4;
        card.add(projectTitleField, c);

        c.gridx = 0; c.gridy = 5;
        card.add(new JLabel("PDF"), c);
        c.gridx = 1; c.gridy = 5;
        card.add(pdfPathField, c);

        c.gridx = 0; c.gridy = 6;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(choosePdf);
        buttons.add(clear);
        buttons.add(add);
        c.gridwidth = 2;
        c.insets = new Insets(10, 6, 0, 6);
        card.add(buttons, c);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UiTheme.windowBg());
        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel buildMyAssignmentsListCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UiTheme.cardBg());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);

        JLabel title = new JLabel("My Assignments");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setForeground(UiTheme.textColor());

        myMajorFilter = buildMajorFilterCombo();
        myMajorFilter.addActionListener(e -> reloadAll());

        top.add(title, BorderLayout.WEST);
        JPanel filterBox = new JPanel(new BorderLayout(6, 0));
        filterBox.setOpaque(false);
        filterBox.add(myMajorFilter, BorderLayout.CENTER);
        top.add(filterBox, BorderLayout.SOUTH);

        myProjectsModel = new DefaultListModel<>();
        myProjectsList = new JList<>(myProjectsModel);
        myProjectsList.setCellRenderer(new ProjectCardRenderer());
        myProjectsList.setFixedCellHeight(-1);
        myProjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myProjectsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() < 2 || !SwingUtilities.isLeftMouseButton(e)) return;
                Project p = myProjectsList.getSelectedValue();
                if (p == null) return;
                if (p.getReviews().isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "This assignment has no reviews yet.",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                new ProjectScoresDialog(MainFrame.this, p).setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(myProjectsList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(UiTheme.cardBg());

        card.add(top, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        EllipsisLabel hint = new EllipsisLabel("Tip: select an assignment then choose an action.");
        hint.setForeground(UiTheme.mutedText());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        ModernButton openPdf = ModernButton.secondary("Open PDF");
        openPdf.addActionListener(e -> openPdfForSelected(myProjectsList));

        ModernButton viewScores = ModernButton.secondary("View reviews");
        viewScores.addActionListener(e -> openScoresForSelected());

        ModernButton delete = ModernButton.danger("Delete");
        delete.addActionListener(e -> deleteSelectedAssignment());

        actions.add(openPdf);
        actions.add(viewScores);
        actions.add(delete);

        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0; bc.gridy = 0;
        bc.weightx = 1;
        bc.fill = GridBagConstraints.HORIZONTAL;
        bc.anchor = GridBagConstraints.WEST;
        bottom.add(hint, bc);

        bc.gridx = 1;
        bc.weightx = 0;
        bc.fill = GridBagConstraints.NONE;
        bc.anchor = GridBagConstraints.EAST;
        bottom.add(actions, bc);

        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildStudentsAssignmentsListCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UiTheme.cardBg());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);

        JLabel title = new JLabel("Students Assignments");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setForeground(UiTheme.textColor());

        studentsMajorFilter = buildMajorFilterCombo();
        studentsMajorFilter.addActionListener(e -> reloadAll());

        top.add(title, BorderLayout.WEST);
        JPanel filterBox = new JPanel(new BorderLayout(6, 0));
        filterBox.setOpaque(false);
        filterBox.add(studentsMajorFilter, BorderLayout.CENTER);
        top.add(filterBox, BorderLayout.SOUTH);

        studentsProjectsModel = new DefaultListModel<>();
        studentsProjectsList = new JList<>(studentsProjectsModel);
        studentsProjectsList.setCellRenderer(new ProjectCardRenderer());
        studentsProjectsList.setFixedCellHeight(-1);
        studentsProjectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsProjectsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showProjectDetails(studentsProjectsList.getSelectedValue());
        });

        JScrollPane scroll = new JScrollPane(studentsProjectsList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(UiTheme.cardBg());

        card.add(top, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildRightDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UiTheme.windowBg());

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UiTheme.cardBg());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.border(), 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        card.add(buildDetailsHeader(), BorderLayout.NORTH);
        card.add(buildReviewsArea(), BorderLayout.CENTER);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDetailsHeader() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        detailsTitle = new EllipsisLabel("Select an assignment from the list");
        detailsTitle.setFont(detailsTitle.getFont().deriveFont(Font.BOLD, 18f));
        detailsTitle.setForeground(UiTheme.textColor());

        detailsStudent = new JLabel("-");
        detailsStudent.setForeground(UiTheme.mutedText());

        detailsAvg = new JLabel("-");
        detailsAvg.setFont(detailsAvg.getFont().deriveFont(Font.BOLD, 14f));
        detailsAvg.setForeground(UiTheme.textColor());

        ModernButton openPdf = ModernButton.secondary("Open PDF");
        openPdf.addActionListener(e -> openPdfForSelected(studentsProjectsList));

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(detailsTitle, c);
        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 1;
        panel.add(detailsStudent, c);
        c.gridx = 1; c.gridy = 1;
        panel.add(detailsAvg, c);

        c.gridx = 1; c.gridy = 2;
        panel.add(openPdf, c);

        panel.add(new JSeparator(SwingConstants.HORIZONTAL), new GridBagConstraints() {{
            gridx = 0; gridy = 3; gridwidth = 2;
            fill = HORIZONTAL;
            insets = new Insets(8, 0, 8, 0);
        }});

        return panel;
    }

    private JPanel buildReviewsArea() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);

        JLabel title = new JLabel("Reviews");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setForeground(UiTheme.textColor());

        ModernButton addReviewBtn = ModernButton.primary("Add review");
        addReviewBtn.addActionListener(e -> openAddReviewDialog());

        top.add(title, BorderLayout.WEST);
        top.add(addReviewBtn, BorderLayout.EAST);

        reviewsModel = new DefaultListModel<>();
        reviewsList = new JList<>(reviewsModel);
        reviewsList.setCellRenderer(new ReviewCardRenderer());
        reviewsList.setFixedCellHeight(-1);
        reviewsList.setVisibleRowCount(14);
        reviewsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reviewsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() < 2 || !SwingUtilities.isLeftMouseButton(e)) return;
                Review r = reviewsList.getSelectedValue();
                if (r == null) return;
                new ReviewDetailsDialog(MainFrame.this, r).setVisible(true);
            }
        });

        JScrollPane scroll = new JScrollPane(reviewsList);
        scroll.setBorder(BorderFactory.createLineBorder(UiTheme.border(), 1, true));
        scroll.getViewport().setBackground(UiTheme.cardBg());

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ---------- logic helpers ----------

    private JComboBox<Object> buildMajorFilterCombo() {
        JComboBox<Object> combo = new JComboBox<>();
        combo.addItem("All majors");
        for (Major m : Major.allMajors()) combo.addItem(m);
        return combo;
    }

    private static String selectedMajorCodeFrom(JComboBox<Object> combo) {
        if (combo == null) return "";
        Object sel = combo.getSelectedItem();
        if (sel instanceof Major) return ((Major) sel).getCode();
        return "";
    }

    private static boolean matchesMajor(Project p, String majorCode) {
        if (majorCode == null || majorCode.trim().isEmpty()) return true;
        String ownerMajor = p.getOwner() == null ? "" : p.getOwner().getMajorCode().trim();
        return ownerMajor.equalsIgnoreCase(majorCode.trim());
    }

    private void reloadAll() {
        reloadMyList();
        reloadStudentsList();
    }

    private void reloadMyList() {
        if (myProjectsModel == null) return;
        String code = selectedMajorCodeFrom(myMajorFilter);
        myProjectsModel.clear();
        for (Project p : system.getProjects()) if (matchesMajor(p, code)) myProjectsModel.addElement(p);
        if (!myProjectsModel.isEmpty()) myProjectsList.setSelectedIndex(0);
    }

    private void reloadStudentsList() {
        if (studentsProjectsModel == null) return;
        String code = selectedMajorCodeFrom(studentsMajorFilter);
        studentsProjectsModel.clear();
        for (Project p : system.getProjects()) if (matchesMajor(p, code)) studentsProjectsModel.addElement(p);
        if (!studentsProjectsModel.isEmpty()) studentsProjectsList.setSelectedIndex(0);
        else showProjectDetails(null);
    }

    private void showProjectDetails(Project p) {
        if (p == null) {
            detailsTitle.setFullText("Select an assignment from the list");
            detailsStudent.setText("-");
            detailsAvg.setText("-");
            reviewsModel.clear();
            return;
        }

        detailsTitle.setFullText(p.getTitle());
        String major = p.getOwner() == null ? "" : p.getOwner().getMajorCode().trim();
        if (!major.isEmpty()) {
            detailsStudent.setText("Student: " + p.getOwner().getName() + " (ID: " + p.getOwner().getId() + ")  •  " + major);
        } else {
            detailsStudent.setText("Student: " + p.getOwner().getName() + " (ID: " + p.getOwner().getId() + ")");
        }

        detailsAvg.setText(ScoreStyle.averageText(p));
        ScoreStyle.applyAverageColors(detailsAvg, p);

        reviewsModel.clear();
        for (Review r : p.getReviews()) reviewsModel.addElement(r);
    }

    private void openAddReviewDialog() {
        Project p = studentsProjectsList.getSelectedValue();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Please select an assignment first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new AddReviewDialog(this, system, p, () -> {
            showProjectDetails(p);
            studentsProjectsList.repaint();
            myProjectsList.repaint();
        }).setVisible(true);
    }

    private void choosePdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose PDF Assignment");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF files (*.pdf)", "pdf"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                String name = file.getName().toLowerCase();
                if (!name.endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(this, "Please choose a PDF file only.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selectedPdfPath = file.getAbsolutePath();
                pdfPathField.setText(selectedPdfPath);
                pdfPathField.setToolTipText(selectedPdfPath);
            }
        }
    }

    private void addAssignment() {
        String studentName = studentNameField.getText().trim();
        String studentIdText = studentIdField.getText().trim();
        String majorCode = majorCombo.getSelectedItem() == null ? "" : ((Major) majorCombo.getSelectedItem()).getCode();
        String projectTitle = projectTitleField.getText().trim();

        if (studentName.isEmpty() || studentIdText.isEmpty() || majorCode.trim().isEmpty() || projectTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill Student Name, Student ID, Major, and Project Title.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId;
        try {
            studentId = Integer.parseInt(studentIdText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Student ID must be a number (example: 123).",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedPdfPath == null || selectedPdfPath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please choose a PDF file for the assignment.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        system.addAssignment(studentId, studentName, majorCode, projectTitle, selectedPdfPath);
        JOptionPane.showMessageDialog(this, "Assignment added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        clearForm();
        reloadAll();
    }

    private void clearForm() {
        studentNameField.setText("");
        studentIdField.setText("");
        majorCombo.setSelectedIndex(0);
        projectTitleField.setText("");
        pdfPathField.setText("No PDF selected");
        pdfPathField.setToolTipText(null);
        selectedPdfPath = "";
        studentNameField.requestFocus();
    }

    private void deleteSelectedAssignment() {
        Project p = myProjectsList.getSelectedValue();
        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select an assignment from \"My Assignments\" to delete.",
                    "Delete assignment",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String major = p.getOwner() == null ? "" : p.getOwner().getMajorCode().trim();
        int result = JOptionPane.showConfirmDialog(this,
                "Delete this assignment and all its reviews?\n\n" +
                        "Title: " + p.getTitle() + "\n" +
                        "Student: " + p.getOwner().getName() + " (ID: " + p.getOwner().getId() + ")" +
                        (major.isEmpty() ? "" : "\nMajor: " + major),
                "Confirm delete",
                JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION) return;

        system.deleteProject(p);
        reloadAll();
        showProjectDetails(null);

        JOptionPane.showMessageDialog(this, "Assignment deleted.", "Delete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openPdfForSelected(JList<Project> list) {
        Project p = list == null ? null : list.getSelectedValue();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Please select an assignment first.", "Open PDF", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        openPdfForProject(p);
    }

    private void openScoresForSelected() {
        Project p = myProjectsList.getSelectedValue();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Please select an assignment first.", "View reviews", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (p.getReviews().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This assignment has no reviews yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new ProjectScoresDialog(this, p).setVisible(true);
    }

    private void openPdfForProject(Project p) {
        String path = p.getPdfFilePath();
        if (path == null || path.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No PDF selected for this assignment.", "PDF", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File f = new File(path);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "PDF file not found.\n" + f.getAbsolutePath(), "PDF Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(f);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open PDF.\nReason: " + ex.getMessage(), "PDF Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveNowWithMessage() {
        try {
            storage.save(system);
            JOptionPane.showMessageDialog(this,
                    "Saved successfully.\nFile: " + storage.getDataFile().getAbsolutePath(),
                    "Save",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Save failed: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAndExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Do you want to save before exit?",
                "Exit",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.CANCEL_OPTION) return;
        if (choice == JOptionPane.YES_OPTION) {
            try {
                storage.save(system);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        dispose();
        System.exit(0);
    }
}

