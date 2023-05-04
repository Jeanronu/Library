import java.util.ArrayList;

public class BookFinder {
    private ArrayList<Book> books;

    public BookFinder(ArrayList<Book> books) {
        this.books = books;
    }

    /**
     * Finds all books containing a given keyword in their title or subtitle
     * @param keyword the keyword to search for
     * @return an ArrayList of books containing the keyword in their title or subtitle
     */
    public ArrayList<Book> searchByTitleOrSubtitle(String keyword) {
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : books) {
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
        for (Book book : books) {
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
        for (Book book : books) {
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
        for (Book book : books) {
            if (book.getPublished() >= year) {
                results.add(book);
            }
        }
        return results;
    }

    public Book getBookByTitle(String title) {
        for (Book book : books) {
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