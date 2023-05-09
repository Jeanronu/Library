import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

    /**
     * @author Jean Rojas
     * A GUI class for displaying the Search of the books that the user is looking.
     */
public class SearchGUI extends JFrame {
    private final JComboBox<String> menuBox;
    private final JTextField searchBar;
    private ViewHistory history;
    private final JButton searchButton;
    private final JPanel mainPanel;
    private final JLabel messageLabel;
    private final JPanel bookPanelContainer;
    private final JLabel protitle;

    public SearchGUI() {
        super("SearchGUI");

        // Create the title label
        protitle = new JLabel("What Book are you Looking for?");
        protitle.setHorizontalAlignment(JLabel.CENTER);
        protitle.setFont(new Font("Arial", Font.BOLD, 24));
        protitle.setHorizontalAlignment(JLabel.CENTER);

        // Create a panel for the menu and search components
        JPanel menuPanel = new JPanel();

        // Create a combo box with the menu options
        menuBox = new JComboBox<>(new String[]{"Search", "Home", "History", "Recommendations"});
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
                    case "History" -> {
                        // Open the History window
                        dispose(); // close the current window
                        HistoryGUI historyWindow = new HistoryGUI();
                        historyWindow.setVisible(true);
                    }
                    case "Recommendations" -> {
                        // Open the Recommendations window
                        dispose(); // close the current window
                        try {
                            RecommendationGUI<String> recommendationWindow = new RecommendationGUI<>(new RecommendationHeap<>(new Library(new String[]{"Book_data/Book1.csv"}), history.loadHistory()), new ViewHistory("book_history.csv"));
                            recommendationWindow.setVisible(true);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        menuPanel.add(menuBox);

        // Create a text field for searching
        searchBar = new JTextField("Search", 20);
        searchBar.setForeground(Color.GRAY);
        searchBar.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Search")) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Search");
                }
            }
        });

        JPanel searchResultsPanel = new JPanel();
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText();
                String category = null;
                String[] options = new String[] {"Category", "Author", "Title"};
                JComboBox<String> categoryBox = new JComboBox<>(options);
                int option = JOptionPane.showOptionDialog(mainPanel, categoryBox, "Select a Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (option == JOptionPane.OK_OPTION) {
                    category = (String) categoryBox.getSelectedItem();
                }
                String[] files = {"Book_data/Book1.csv"};
                history = new ViewHistory("book_history.csv");
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

                switch(category) {
                    case "Category":
                        results = SearchBooks.categorySearch(searchText, library, catOrganized);
                        break;
                    case "Title":
                        results = SearchBooks.titleSearch(searchText, library, booksOrganized);
                        break;
                    case "Author":
                        results = SearchBooks.authorSearch(searchText, library, autOrganized);
                        break;
                    default:
                        break;
                }

                mainPanel.removeAll();

                // Repaint the main panel to show the search results
                mainPanel.revalidate();
                mainPanel.repaint();

                menuPanel.add(searchBar);
                menuPanel.add(searchButton);

                // Create a panel for the top components
                JPanel topPanel = new JPanel(new BorderLayout());
                topPanel.add(protitle, BorderLayout.NORTH);
                topPanel.add(menuPanel, BorderLayout.CENTER);

                // Create the main panel
                mainPanel.add(topPanel, BorderLayout.NORTH);

                JPanel centerPanel = new JPanel();
                centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                centerPanel.add(Box.createVerticalGlue());
                centerPanel.add(Box.createVerticalGlue());
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                searchResultsPanel.removeAll();
                searchResultsPanel.setLayout(new GridLayout(0, 5, 5, 10)); // Set the search results panel to use a grid layout with 5 columns
                if (results == null || results.isEmpty()) {
                    messageLabel.setText("No results found.");
                    bookPanelContainer.removeAll(); // clear any existing book panels
                    JOptionPane.showMessageDialog(mainPanel, "Book not found", "Search Results", JOptionPane.INFORMATION_MESSAGE); // show message dialog
                } else {
                    int numBooks = results.size();
                    if (numBooks < 5) {
                        // If there are less than 5 books, center the book panel container horizontally
                        JPanel bookPanelContainer = new JPanel();
                        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.X_AXIS));
                        bookPanelContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                        for (Book book : results) {
                            try {
                                BookDisplay bookDisplay = new BookDisplay(book);
                                JPanel bookPanel = new JPanel();
                                bookPanel.setPreferredSize(new Dimension(200, 400));
                                bookPanel.setBackground(Color.LIGHT_GRAY);
                                bookPanel.setLayout(new BorderLayout());
                                bookPanel.add(bookDisplay.getRootPanel(), BorderLayout.CENTER);
                                JButton addButton = new JButton("Add Book");
                                addButton.setPreferredSize(new Dimension(120, 30));
                                addButton.addActionListener(i -> {
                                    if (history.containsBook(book)) {
                                        JOptionPane.showMessageDialog(mainPanel, "This book is already in the history.");
                                    } else {
                                        book.setRead(); // Call isRead on the book object before adding it to the history
                                        String rating;
                                        int ratingValue;
                                        while (true) {
                                            rating = JOptionPane.showInputDialog(mainPanel, "Please rate the book from 1-5:");
                                            if (rating == null) {
                                                JOptionPane.showMessageDialog(mainPanel, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                                                break;
                                            }
                                            try {
                                                ratingValue = Integer.parseInt(rating);
                                                if (ratingValue >= 1 && ratingValue <= 5) {
                                                    book.setPersonalRating(ratingValue); // Set the book's rating
                                                    history.addBook(book);
                                                    JOptionPane.showMessageDialog(mainPanel, "Your book have been added. <3");
                                                    break;
                                                } else {
                                                    JOptionPane.showMessageDialog(mainPanel, "Invalid rating. Please enter a number from 1-5.");
                                                }
                                            } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(mainPanel, "Invalid rating. Please enter a number from 1-5.");
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
                                bookPanelContainer.add(bookPanel);
                                bookPanelContainer.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        searchResultsPanel.add(bookPanelContainer);
                    } else {
                        // If there are 5 or more books, add the book panels normally using the grid layout
                        for (Book book : results) {
                            try {
                                BookDisplay bookDisplay = new BookDisplay(book);
                                JPanel bookPanel = new JPanel();
                                bookPanel.setPreferredSize(new Dimension(200, 400));
                                bookPanel.setBackground(Color.LIGHT_GRAY);
                                bookPanel.setLayout(new BorderLayout());
                                bookPanel.add(bookDisplay.getRootPanel(), BorderLayout.CENTER);
                                JButton addButton = new JButton("Add Book");
                                addButton.setPreferredSize(new Dimension(120, 30));
                                addButton.addActionListener(i -> {
                                    if (history.containsBook(book)) {
                                        JOptionPane.showMessageDialog(mainPanel, "This book is already in the history.");
                                    } else {
                                        book.setRead(); // Call isRead on the book object before adding it to the history
                                        String rating;
                                        int ratingValue;
                                        while (true) {
                                            rating = JOptionPane.showInputDialog(mainPanel, "Please rate the book from 1-5:");
                                            if (rating == null) {
                                                JOptionPane.showMessageDialog(mainPanel, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                                                break;
                                            }
                                            try {
                                                ratingValue = Integer.parseInt(rating);
                                                if (ratingValue >= 1 && ratingValue <= 5) {
                                                    book.setPersonalRating(ratingValue); // Set the book's rating
                                                    history.addBook(book);
                                                    JOptionPane.showMessageDialog(mainPanel, "Your book have been added. <3");
                                                    break;
                                                } else {
                                                    JOptionPane.showMessageDialog(mainPanel, "Invalid rating. Please enter a number from 1-5.");
                                                }
                                            } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(mainPanel, "Invalid rating. Please enter a number from 1-5.");
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
                                searchResultsPanel.add(bookPanel);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }

                // Create a JScrollPane for the search results panel and add it to the main panel
                JScrollPane scrollPane = new JScrollPane(searchResultsPanel);
                mainPanel.add(scrollPane);
            }
        });

        messageLabel = new JLabel();
        bookPanelContainer = new JPanel();
        bookPanelContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        menuPanel.add(searchBar);
        menuPanel.add(searchButton);

        // Create a panel for the top components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(protitle, BorderLayout.NORTH);
        topPanel.add(menuPanel, BorderLayout.CENTER);

        // Create the main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Set the size of the window and make it visible
        setSize(1050, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
