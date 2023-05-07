import java.util.ArrayList;

/**
 * @author Jenny Le
 * This class creates binary search trees based on values such as title, author, or category.
 * No user interaction is necessary.
 * The compareTo method compares the title, author, or category alphabetically.
 */

public class OrganizeBooks {
    private ArrayList<Book> library;
    /**
     * This is a method for the OrganizeBooks class that organizes Book objects by title alphabetically
     * @param library An arrayList of books, which is the library database
     * @return An ArrayList of Books which contains the sorted tree sortedTitle
     */
    public static LinkedBinarySearchTreeBook<ArrayList<Book>> organizeTitle(ArrayList<Book> library){
        LinkedBinarySearchTreeBook<ArrayList<Book>> sortedTitle = new LinkedBinarySearchTreeBook<>(); // New binary tree where sorted books by title will be inserted
        for(int i = 0; i< library.size(); i++){
            sortedTitle.insertTitle(library.get(i)); // all books from the library are inserted alphabetically
        }
        // There is now a BST sorted by title called sortedTitle
        return sortedTitle;
    }

    /**
     * This is a method for the OrganizeBooks class that organizes Book objects by author alphabetically
     * @param library An arrayList of books, which is the library database
     * @return An ArrayList of Books which contains the sorted tree sortedAuthor
     */
    public static LinkedBinarySearchTreeBook<ArrayList<Book>> organizeAuthor(ArrayList<Book> library){
        LinkedBinarySearchTreeBook<ArrayList<Book>> sortedAuthor = new LinkedBinarySearchTreeBook<>(); // New binary tree where sorted books by author will be inserted
        for(int i = 0; i < library.size(); i++){
            sortedAuthor.insertAuthor(library.get(i)); // all books from the library are inserted alphabetically
        }
        // There is now a BST sorted by author called sortedAuthor
        return sortedAuthor;
    }

    /**
     * This is a method for the OrganizeBooks class that organizes Book objects by category alphabetically
     * @param library An arrayList of books, which is the library database
     * @return An ArrayList of Books which contains the sorted tree sortedCategory
     */
    public static LinkedBinarySearchTreeBook<ArrayList<Book>> organizeCategories(ArrayList<Book> library){
        LinkedBinarySearchTreeBook<ArrayList<Book>> sortedCategories = new LinkedBinarySearchTreeBook<>(); // New binary tree where sorted books by category will be inserted
        for(int i = 0; i < library.size(); i++){
            sortedCategories.insertCategory(library.get(i)); // all books from the library are inserted alphabetically
        }
        // There is now a BST sorted by category called sortedCategories
        return sortedCategories;
    }



}