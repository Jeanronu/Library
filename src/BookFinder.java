import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BookFinder {
    private ArrayList<Book> books;

    public BookFinder() {
        this.books = new ArrayList<>();
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> loadBooks(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine(); // skip header row
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");        String ISBN10 = data[1];
            String title = data[2];
            String subtitle = data[3];
            String[] authors = data[4].split(";");
            String[] categories = data[5].split(";");
            String thumbnail = data[6];
            String description = data[7];
            int published = 0;
            if (data.length > 8 && !data[8].equals("")) {
                published = Integer.parseInt(data[8]);
            }
            double averageRating = 0.0;
            if (data.length > 9 && !data[9].equals("")) {
                averageRating = Double.parseDouble(data[9]);
            }
            int numPages = 0;
            if (data.length > 10) {
                numPages = Integer.parseInt(data[10]);
            }
            int numRatings = 0;
            if (data.length > 11) {
                numRatings = Integer.parseInt(data[11]);
            }
            Book book = new Book(ISBN10, title, subtitle, authors, categories, thumbnail, description, published, averageRating, numPages, numRatings);
            this.books.add(book);
        }
        reader.close();
        return this.books;
    }

    /**
     * Finds all books containing a given keyword in their title or subtitle
     * @param keyword the keyword to search for
     * @return an ArrayList of books containing the keyword in their title or subtitle
     */
    public ArrayList<Book> searchByTitleOrSubtitle(String keyword) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : this.books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getSubtitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    /**
     * Finds all books containing a given author name
     * @param authorName the author name to search for
     * @return an ArrayList of books containing the author name
     */
    public ArrayList<Book> searchByAuthor(String authorName) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : this.books) {
            for (String author : book.getAuthors()) {
                if (author.toLowerCase().contains(authorName.toLowerCase())) {
                    results.add(book);
                    break;
                }
            }
        }
        return results;
    }

    /**
     * Finds all books in a given category
     * @param category the category to search for
     * @return an ArrayList of books in the category
     */
    public ArrayList<Book> searchByCategory(String category) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : this.books) {
            for (String bookCategory : book.getCategories()) {
                if (bookCategory.toLowerCase().equals(category.toLowerCase())) {
                    results.add(book);
                    break;
                }
            }
        }
        return results;
    }

    /**
     * Finds all books published in a given year or later
     * @param year the year to search for
     * @return an ArrayList of books published in the year or later
     */
    public ArrayList<Book> searchByYear(int year) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : this.books) {
            if (book.getPublished() >= year) {
                results.add(book);
            }
        }
        return results;
    }

    public Book getBookByTitle(String title) {
        for (Book book : this.books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null; // no book with matching title was found
    }

    /**
     * Finds all books with a given minimum average rating
     * @param rating the minimum average rating to search for
     * @return an ArrayList of books with the minimum average rating or higher
     */
    public ArrayList<Book> searchByRating(double rating) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getAverageRating() >= rating) {
                results.add(book);
            }
        }
        return results;
    }
}