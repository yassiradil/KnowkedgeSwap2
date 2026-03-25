package knowledgeswap2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable, Displayable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String pdfFilePath;
    private final Student owner;
    private final ArrayList<Review> reviews;

    public Project(String title, String pdfFilePath, Student owner) {
        this.title = title == null ? "" : title.trim();
        this.pdfFilePath = pdfFilePath == null ? "" : pdfFilePath.trim();
        this.owner = owner;
        this.reviews = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath == null ? "" : pdfFilePath.trim();
    }

    public Student getOwner() {
        return owner;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        if (review != null) reviews.add(review);
    }

    public double calculateAverageScore() {
        if (reviews.isEmpty()) return 0.0;
        int total = 0;
        for (Review r : reviews) total += r.getScoreOutOf100();
        return (double) total / reviews.size();
    }

    @Override
    public String toDisplayString() {
        return title + " (Owner: " + (owner == null ? "-" : owner.getName()) + ")";
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}

