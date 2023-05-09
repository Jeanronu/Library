import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Jean Rojas
 * A GUI class for displaying the books that the program recommendto the
 * user based on the books they have read.
 */
public class RecommendationGUI<T> extends JFrame {
    private RecommendationHeap<T> heap;
    private JList<String> recommendedBooks;
    private JPanel menuPanel;
    private JComboBox<String> menuBox;
    private JTextField searchField;
    private JLabel messageLabel;
    private JPanel bookPanelContainer;

    public RecommendationGUI(RecommendationHeap<T> heap, ViewHistory history) throws FileNotFoundException {
        this.heap = heap;

        if (heap == null) {
            JOptionPane.showMessageDialog(null, "Please add some books");
            JFrame frame = new JFrame("iLibrary");
            HomePageGUI homePage = new HomePageGUI(frame);
            frame.setContentPane(homePage.getContentPane());
            frame.pack();
            frame.setVisible(true);
            return;
        }

        // Set up the JFrame
        setTitle("Recommended Books");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the components
        JLabel recommendLabel = new JLabel("Recommendations");
        recommendLabel.setFont(new Font("Serif", Font.BOLD, 20));
        recommendLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(recommendLabel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridLayout(1, 5)); // change GridLayout to 1 row, 5 columns
        if (heap!=null) {
            for (int i = 0; i < 5; i++) { // create 5 book panels
                JPanel bookPanel = new JPanel(new BorderLayout());
                Book book = heap.getRecommendations(5).get(i); // get the ith recommended book
                BookDisplay bookDisplay;
                try {
                    bookDisplay = new BookDisplay(book);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                bookPanel.add(bookDisplay.rootPanel, BorderLayout.CENTER);
                JButton addButton = new JButton("Add Book");
                addButton.setPreferredSize(new Dimension(120, 30));
                addButton.addActionListener(e -> {
                    if (history.containsBook(book)) {
                        JOptionPane.showMessageDialog(this, "This book is already in the history.");
                    } else {
                        book.setRead(); // Call isRead on the book object before adding it to the history
                        String rating;
                        int ratingValue;
                        while (true) {
                            rating = JOptionPane.showInputDialog(this, "Please rate the book from 1-5:");
                            if (rating == null) {
                                JOptionPane.showMessageDialog(this, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                                break;
                            }
                            try {
                                ratingValue = Integer.parseInt(rating);
                                if (ratingValue >= 1 && ratingValue <= 5) {
                                    book.setPersonalRating(ratingValue); // Set the book's rating
                                    history.addBook(book);
                                    JOptionPane.showMessageDialog(this, "Your book has been added. <3");
                                    break;
                                } else {
                                    JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number from 1-5.");
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number from 1-5.");
                            }
                        }
                        if (HistoryGUI.historyTextArea != null) {
                            history.saveHistory(); // Save the history to the CSV file
                            HistoryGUI.historyTextArea.setText(history.getHistory());
                        }
                    }
                });
                bookPanel.add(addButton, BorderLayout.SOUTH);
                bookPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                centerPanel.add(bookPanel); // add the book panel to the center panel
            }
        } else {
            JLabel noRecommendationsLabel = new JLabel("No recommendations available.");
            noRecommendationsLabel.setFont(new Font("Serif", Font.BOLD, 16));
            noRecommendationsLabel.setHorizontalAlignment(JLabel.CENTER);
            centerPanel.add(noRecommendationsLabel);
        }

        // Create a panel for the menu and search components
        menuPanel = new JPanel();

        // Create a combo box with the menu options
        menuBox = new JComboBox<>(new String[]{"Recommendations","History", "Home", "Search"});
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) menuBox.getSelectedItem();
                assert selectedOption != null;
                switch (selectedOption) {
                    case "Home" -> {
                        // Redirect to the homepage
                        dispose(); // Close the current window
                        JFrame frame = new JFrame("iLibrary");
                        HomePageGUI homePage = new HomePageGUI(frame);
                        frame.setContentPane(homePage.getContentPane());
                        frame.pack();
                        frame.setVisible(true);
                    }
                    case "Search" -> {
                        // Open the Search window
                        dispose(); // close the current window
                        SearchGUI searchgui = new SearchGUI();
                        searchgui.setVisible(true);
                    }
                    case "History" -> {
                        // Open the History window
                        dispose(); // close the current window
                        HistoryGUI historyWindow = new HistoryGUI();
                        historyWindow.setVisible(true);
                    }
                }
            }
        });

        // Create a text field for searching
        searchField = new JTextField("Search", 20);
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
            }
        });

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                String category = null;
                // Show a dialog to select the category
                String[] options = new String[] {"Category", "Author", "Title"};
                JComboBox<String> categoryBox = new JComboBox<>(options);
                int option = JOptionPane.showOptionDialog(centerPanel, categoryBox, "Select a Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (option == JOptionPane.OK_OPTION) {
                    category = (String) categoryBox.getSelectedItem();
                }

                String[] files = {"Book_data/Book1.csv"};
                Library bookLibrary = null;
                try {
                    bookLibrary = new Library(files);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                ArrayList<Book> library = bookLibrary.getLibrary(); // contains all the books from the Book csv file
                LinkedBinarySearchTreeBook<ArrayList<Book>> booksOrganized = OrganizeBooks.organizeTitle(library); // tree of library books sorted by title
                LinkedBinarySearchTreeBook<ArrayList<Book>> autOrganized = OrganizeBooks.organizeAuthor(library); // tree of library books sorted by author
                LinkedBinarySearchTreeBook<ArrayList<Book>> catOrganized = OrganizeBooks.organizeCategories(library); // tree of library books sorted by category
                ArrayList<Book> results = null;
                ArrayList<LinkedBinarySearchTreeBook<ArrayList<Book>>> treeList = new ArrayList<>();
                treeList.add(booksOrganized);
                treeList.add(autOrganized);
                treeList.add(catOrganized);

                switch (Objects.requireNonNull(category)) {
                    case "Category" -> results = SearchBooks.categorySearch(searchText, library, catOrganized);
                    case "Title" -> results = SearchBooks.titleSearch(searchText, library, booksOrganized);
                    case "Author" -> results = SearchBooks.authorSearch(searchText, library, autOrganized);
                }

                // Remove existing book panels
                bookPanelContainer.removeAll();
                if (results == null || results.isEmpty()) {
                    messageLabel.setText("No results found.");
                    bookPanelContainer.removeAll(); // clear any existing book panels
                    JOptionPane.showMessageDialog(centerPanel, "Book not found", "Search Results", JOptionPane.INFORMATION_MESSAGE); // show message dialog
                } else {
                    messageLabel.setText("Showing " + results.size() + " results.");
                    displayBooksTable(results);
                }

                // Update the book panel container
                bookPanelContainer.revalidate();
                bookPanelContainer.repaint();
            }
        });

        // Add the element to the panel
        topPanel.add(recommendLabel, BorderLayout.NORTH);
        menuPanel.add(menuBox, BorderLayout.SOUTH);
        menuPanel.add(searchField, BorderLayout.SOUTH);
        menuPanel.add(searchButton, BorderLayout.SOUTH);
        topPanel.add(menuPanel, BorderLayout.CENTER);

        messageLabel = new JLabel();
        bookPanelContainer = new JPanel();
        bookPanelContainer.setLayout(new BorderLayout());

        // Add the components to the JFrame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Display the JFrame
        pack();
        setSize(1050, 500);
        setVisible(true);
    }

    /**
     * Displays the list of books in a table format using a JOptionPane.
     * @param books the list of books to be displayed in the table
     */
    public void displayBooksTable(ArrayList<Book> books) {
        // Create the data array and column names for the table
        Object[][] data = new Object[books.size()][3];
        String[] columnNames = {"Title", "Author", "Category"};

        // Populate the data array with the book information
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = String.join(", ", book.getAuthors());
            data[i][2] = String.join(", ", book.getCategories());
        }

        // Create the JTable using the data array and column names
        JTable table = new JTable(data, columnNames);

        // Set the row height and auto resize mode for the table
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        // Create a scroll pane for the table and set its dimensions
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Display the table in a JOptionPane with a plain message type
        JOptionPane.showMessageDialog(menuPanel, scrollPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
    }
}