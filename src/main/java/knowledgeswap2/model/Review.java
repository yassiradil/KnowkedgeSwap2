package knowledgeswap2.model;

import java.io.Serializable;

public class Review implements Serializable, Displayable {
    private static final long serialVersionUID = 1L;

    private final String reviewerName;
    private final int scoreOutOf100;
    private final String comment;

    public Review(String reviewerName, int scoreOutOf100, String comment) {
        this.reviewerName = reviewerName == null ? "" : reviewerName.trim();
        this.scoreOutOf100 = scoreOutOf100;
        this.comment = comment == null ? "" : comment.trim();
    }

    public String getReviewerName() {
        return reviewerName;
    } //get cuz its privet

    public int getScoreOutOf100() {
        return scoreOutOf100;
    }

    public String getComment() {
        return comment;
    }

    @Override //reuse + shortComment
    public String toDisplayString() {
        String shortComment = comment;
        if (shortComment.length() > 30) shortComment = shortComment.substring(0, 30) + "..."; //first 30 letters
        return reviewerName + " | " + scoreOutOf100 + "/100 | " + shortComment; //organize
    }

    @Override //reuse M
    public String toString() {
        return toDisplayString();
    }
}

