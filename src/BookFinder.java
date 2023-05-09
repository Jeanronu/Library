import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The BookFinder class is responsible for loading a list of books from a file and providing methods for searching the books based on various criteria.
 * @author Jean Rojas
 */
public class BookFinder {
    private ArrayList<Book> books;

    /**
     * Creates a new instance of the BookFinder class.
     */
    public BookFinder() {
        this.books = new ArrayList<>();
    }

    /**
     * Sets the list of books.
     * @param books The ArrayList of Book objects to set.
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    /**
     * Loads a list of books from a CSV file.
     * @param filePath The path to the CSV file to load.
     * @return An ArrayList of Book objects loaded from the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public ArrayList<Book> loadBooks(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String[] files = {filePath};
        Library library = new Library(files);
        this.books = library.getLibrary();
        reader.close();
        return this.books;
    }

    /**
     * Returns the first book found in the list with a matching title.
     * @param title The title of the book to search for.
     * @return The first Book object found in the list with a matching title, or null if no match is found.
     */
    public Book getBookByTitle(String title) {
        for (Book book : this.books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null; // no book with matching title was found
    }
}