package knowledgeswap2.service;

import knowledgeswap2.model.Project;
import knowledgeswap2.model.Review;
import knowledgeswap2.model.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedbackSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Project> projects = new ArrayList<>();

    public List<Student> getStudents() {
        return students;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Student findStudentById(int id) {
        for (Student s : students) if (s.getId() == id) return s;
        return null;
    }

    public void addAssignment(int studentId, String studentName, String majorCode, String projectTitle, String pdfFilePath) {
        Student student = findStudentById(studentId);
        if (student == null) {
            student = new Student(studentId, studentName, majorCode);
            students.add(student);
        } else {
            if (studentName != null && !studentName.trim().isEmpty()) student.setName(studentName);
            if (majorCode != null && !majorCode.trim().isEmpty()) student.setMajorCode(majorCode);
        }

        projects.add(new Project(projectTitle, pdfFilePath, student));
    }

    public void deleteProject(Project p) {
        projects.remove(p);
    }

    public void addReview(Project project, String reviewerName, int score, String comment) {
        if (project == null) return;
        project.addReview(new Review(reviewerName, score, comment));
    }
}

