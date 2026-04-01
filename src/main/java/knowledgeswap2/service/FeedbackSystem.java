//Manages student&project list.// Search-add-delete (project) / add (review)
package knowledgeswap2.service;
// import the classes
import knowledgeswap2.model.Project;
import knowledgeswap2.model.Review;
import knowledgeswap2.model.Student;

import java.io.Serializable; //object can be saved and loaded
import java.util.ArrayList; //store students&projects.
import java.util.List; //general list type

public class FeedbackSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ArrayList<Student> students = new ArrayList<>(); //create a list to store students
    private final ArrayList<Project> projects = new ArrayList<>(); //same for projects


    public List<Project> getProjects() {
        return projects;
    } //returns the list of projects

        //search M for a students by ID.
    public Student findStudentById(int id) {
        for (Student s : students) if (s.getId() == id) return s;  //search loop if id==id return else null
        return null;
    }

    public void addAssignment(int studentId, String studentName, String majorCode, String projectTitle, String pdfFilePath) {
        Student student = findStudentById(studentId); //by id checks if the student already exists
        if (student == null) { //if null creates a new Student object
            student = new Student(studentId, studentName, majorCode);
            students.add(student); //adds it student to the student list.
        } else { //if exists not null&not empty --> update student name
            if (studentName != null && !studentName.trim().isEmpty()) student.setName(studentName);
            //same for major
            if (majorCode != null && !majorCode.trim().isEmpty()) student.setMajorCode(majorCode);
        }

        projects.add(new Project(projectTitle, pdfFilePath, student));
    }//creates a new Project object and adds it to the project list.

    public void deleteProject(Project p) {
        projects.remove(p); //delete a project from the project list.
    }

    //This method adds a new review to a project.
    public void addReview(Project project, String reviewerName, int score, String comment) {
        if (project == null) return; //if null, stop to avoid errors
        project.addReview(new Review(reviewerName, score, comment)); //creates a new Review object/ for the project
    }
}

