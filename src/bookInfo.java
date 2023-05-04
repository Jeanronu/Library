public class bookInfo {
    private String title;
    private String author;
    private String description;
    private double averageRating;
    private int numRatings;
    private String thumbnail;

    public bookInfo(String title, String author, String description, double averageRating, int numRatings, String thumbnail) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.averageRating = averageRating;
        this.numRatings = numRatings;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDescription() {
        return this.description;
    }

    public double getAverageRating() {
        return this.averageRating;
    }

    public int getNumRatings() {
        return this.numRatings;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }
}
