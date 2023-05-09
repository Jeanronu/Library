/**
 * Test main it does not do anything
 * DO NOT DELETE THIS FILE
 */

import java.awt.image.AreaAveragingScaleFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] files = {"Book_data/Book1.csv"};
        Library library = new Library(files);
        ArrayList<Book> bookLibrary = library.getLibrary();
        Stack<Book> readHistory = new Stack<>();
        for (int i = 0; i < 23; i++) {
            Book book = randomBook(library);
            book.setRead();
            book.setPersonalRating((int) (Math.random() * 5));
            readHistory.push(book);
            System.out.println(book);
            System.out.println(book.getPersonalRating());
        }
//        RecommendationHeap<BookNode> recommendationHeap = new RecommendationHeap<>(bookLibrary, readHistory);
//        for (int i = 0; i < 5; i++) {
//            BookNode max = (BookNode) recommendationHeap.removeMax();
//            Book book = max.getBook();
//            System.out.println(max.toString());
//////        }
//        String[] files = {"Book_data/Book1.csv"};
//        Library bookLibrary = new Library(files);
//
//        ArrayList<Book> library = bookLibrary.getLibrary(); // contains all the books from the Book csv file
//        LinkedBinarySearchTreeBook<ArrayList<Book>> booksOrganized = OrganizeBooks.organizeTitle(library); // tree of library books sorted by title
//        LinkedBinarySearchTreeBook<ArrayList<Book>> autOrganized = OrganizeBooks.organizeAuthor(library); // tree of library books sorted by author
//        LinkedBinarySearchTreeBook<ArrayList<Book>> catOrganized = OrganizeBooks.organizeCategories(library); // tree of library books sorted by category
//
//        ArrayList<Book> titles = SearchBooks.titleSearch("the nuclear age", library, booksOrganized); // searches for a book by a given title
//        System.out.println(titles.get(0)); // prints the first book in the array returned by titleSearch
//        System.out.println(titles.size());
//
//        ArrayList<Book> authors = SearchBooks.authorSearch("tim o'brien", library, autOrganized); // searches for a book by a given author
//        System.out.println(authors.get(0)); // prints the first book in the array returned by authorSearch
//
//        ArrayList<Book> categories = SearchBooks.categorySearch("fantasy fiction", library, catOrganized); // searches for a book by a given author
//        System.out.println(categories.get(5)); // prints the first book in the array returned by categorySearch

    }


    /**
     Returns a random Book object from the specified Library object.
     @param library The Library object to select a random book from.
     @return A randomly selected Book object from the Library.
     */

    public static Book randomBook(Library library) {
        int randomIndex = (int) (Math.random() * library.getSize());
        return library.getBook(randomIndex);
    }
}