package knowledgeswap2.model;
//new project / link it to student(owner) / save PDF PATH /show+add reviews / calc average /display out put
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable, Displayable { // save&load&show
    private static final long serialVersionUID = 1L;//load id for java

    private String title;//p /var /type /store
    private String pdfFilePath; //show sys w is the PDF path
    private final Student owner; //type of obj from student
    private final ArrayList<Review> reviews; //list array to store var

    public Project(String title, String pdfFilePath, Student owner) {
        this.title = title == null ? "" : title.trim(); // short if title = null "" else trim spaces
        this.pdfFilePath = pdfFilePath == null ? "" : pdfFilePath.trim();// short if PATH = null "" else trim spaces
        this.owner = owner;
        this.reviews = new ArrayList<>(); //empty list Array at first without reviews
    }
    //Getters / Setters
    public String getTitle() {
        return title;
    }//title

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }//clean

    public String getPdfFilePath() {
        return pdfFilePath;
    }//PATH

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath == null ? "" : pdfFilePath.trim();
    } //clean

    public Student getOwner() {
        return owner;
    }//owner

    public List<Review> getReviews() {
        return reviews;
    }//view old reviews


    //LOGIC METHODS
    public void addReview(Review review) {
        if (review != null) reviews.add(review);
    } // add new reviews

    public double calculateAverageScore() {
        if (reviews.isEmpty()) return 0.0; //start with 0.0
        int total = 0; //new var (total)
        for (Review r : reviews) total += r.getScoreOutOf100(); //reviews for loop adding rev score to total
        return (double) total / reviews.size(); //average = total / number of reviews (double)
    }

    @Override //display output + short if "title (Owner: - or Ali)"
    public String toDisplayString() {
        return title + " (Owner: " + (owner == null ? "-" : owner.getName()) + ")";
    }


    @Override //display the text from toDisplayString (clean)
    public String toString() {
        return toDisplayString();
    }
}

