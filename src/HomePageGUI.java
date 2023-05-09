import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Jean Rojas
 * Create a GUI for the HomePage, where it displays books
 */
public class HomePageGUI {
    private final JFrame frame;
    private final JPanel contentPane;
    private final ViewHistory history;
    private final JTextField searchField;
    private final JLabel messageLabel;
    private final JPanel bookPanelContainer;


    public HomePageGUI(JFrame frame) {
        this.frame = frame;
        // Create the components
        JLabel welcomeLabel = new JLabel("Welcome to the iLibrary");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        JComboBox<String> menuBox = new JComboBox<>(new String[]{"Home", "History", "Search", "Recommendations"});
        menuBox.addActionListener(new ActionListener() {
            @Override
            /**
             * This method handles the actions to be taken when a user clicks on an item in the menuBox.
             * It checks the selected option and performs the corresponding action.
             * @param e - the ActionEvent object that triggered the method call.
             */
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) menuBox.getSelectedItem();

                if (selectedOption != null && selectedOption.equals("History")) {
                    // Open the History window
                    closeWindow(); // Close the current window
                    HistoryGUI historyWindow = new HistoryGUI();
                    historyWindow.setVisible(true);
                }
                else if (selectedOption != null && selectedOption.equals("Search")) {
                    // Open the Search window
                    closeWindow(); // close the current window
                    SearchGUI searchgui = new SearchGUI();
                    searchgui.setVisible(true);
                }
                else if (selectedOption != null && selectedOption.equals("Recommendations")) {
                    // Open the Recommendations window
                    closeWindow(); // Close the current window
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
        });

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
                int option = JOptionPane.showOptionDialog(frame, categoryBox, "Select a Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
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
                    JOptionPane.showMessageDialog(frame, "Book not found", "Search Results", JOptionPane.INFORMATION_MESSAGE); // show message dialog
                } else {
                    messageLabel.setText("Showing " + results.size() + " results.");
                    displayBooksTable(results);
                }

                // Update the book panel container
                bookPanelContainer.revalidate();
                bookPanelContainer.repaint();
            }
        });

        messageLabel = new JLabel();
        bookPanelContainer = new JPanel();
        bookPanelContainer.setLayout(new BorderLayout());

        // Create five JPanels, each with a 200 x 400 size
        JPanel bookPanel1 = new JPanel();
        bookPanel1.setPreferredSize(new Dimension(200, 400));
        bookPanel1.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel2 = new JPanel();
        bookPanel2.setPreferredSize(new Dimension(200, 400));
        bookPanel2.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel3 = new JPanel();
        bookPanel3.setPreferredSize(new Dimension(200, 400));
        bookPanel3.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel4 = new JPanel();
        bookPanel4.setPreferredSize(new Dimension(200, 400));
        bookPanel4.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel5 = new JPanel();
        bookPanel5.setPreferredSize(new Dimension(200, 400));
        bookPanel5.setBackground(Color.LIGHT_GRAY);

        // Add the components to the content pane
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(welcomeLabel, FlowLayout.CENTER);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.add(menuBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        contentPane.add(searchPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel bookPanelContainer = new JPanel();
        bookPanelContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bookPanelContainer.add(bookPanel1);
        bookPanelContainer.add(bookPanel2);
        bookPanelContainer.add(bookPanel3);
        bookPanelContainer.add(bookPanel4);
        bookPanelContainer.add(bookPanel5);
        contentPane.add(bookPanelContainer);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        // Perform the book when the window is loaded
        history = new ViewHistory("book_history.csv");
        String[] files = {"Book_data/Book1.csv"};
        Library library;
        try {
            library = new Library(files);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Book1
        Book book1 = Main.randomBook(library);
        BookDisplay bookDisplay1;
        try {
            bookDisplay1 = new BookDisplay(book1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel1.setLayout(new BorderLayout());
        bookPanel1.add(bookDisplay1.rootPanel, BorderLayout.CENTER);

        /**
         * Creates 5 JButtons for adding the books to the user's history and adds it to bookPanel#.
         * The button has a fixed preferred size of 120x30 and triggers an ActionListener
         * that prompts the user to rate the book and add it to the history if it has not been added yet.
         * If the user cancels the rating, the book is not added to the history.
         * If the rating is invalid, the user is prompted to enter a number from 1-5.
         * If the book has already been added to the history, a message is displayed and the book is not added again.
         * The history text area is updated if it exists.
         *
         * @param book# the Book object to be added to the history
         */
        JButton addButton1 = new JButton("Add Book");
        addButton1.setPreferredSize(new Dimension(120, 30));
        addButton1.addActionListener(e -> {
            if (history.containsBook(book1)) {
                JOptionPane.showMessageDialog(frame, "This book is already in the history.");
            } else {
                book1.setRead(); // Call isRead on the book object before adding it to the history
                String rating;
                int ratingValue;
                while (true) {
                    rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                    if (rating == null) {
                        JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                        break;
                    }
                    try {
                        ratingValue = Integer.parseInt(rating);
                        if (ratingValue >= 1 && ratingValue <= 5) {
                            book1.setPersonalRating(ratingValue); // Set the book's rating
                            history.addBook(book1);
                            JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                            break;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                    }
                }
                if (HistoryGUI.historyTextArea != null) {
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel1.add(addButton1, BorderLayout.SOUTH);
        bookPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 2
        Book book2 = Main.randomBook(library);
        BookDisplay bookDisplay2;
        try {
            bookDisplay2 = new BookDisplay(book2);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel2.setLayout(new BorderLayout());
        bookPanel2.add(bookDisplay2.rootPanel, BorderLayout.CENTER);
        JButton addButton2 = new JButton("Add Book");
        addButton2.setPreferredSize(new Dimension(120, 30));
        addButton2.addActionListener(e -> {
            if (history.containsBook(book2)) {
                JOptionPane.showMessageDialog(frame, "This book is already in the history.");
            } else {
                book2.setRead(); // Call isRead on the book object before adding it to the history
                String rating;
                int ratingValue;
                while (true) {
                    rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                    if (rating == null) {
                        JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                        break;
                    }
                    try {
                        ratingValue = Integer.parseInt(rating);
                        if (ratingValue >= 1 && ratingValue <= 5) {
                            book2.setPersonalRating(ratingValue); // Set the book's rating
                            history.addBook(book2);
                            JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                            break;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                    }
                }
                if (HistoryGUI.historyTextArea != null) {
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel2.add(addButton2, BorderLayout.SOUTH);
        bookPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 3
        Book book3 = Main.randomBook(library);
        BookDisplay bookDisplay3;
        try {
            bookDisplay3 = new BookDisplay(book3);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel3.setLayout(new BorderLayout());
        bookPanel3.add(bookDisplay3.rootPanel, BorderLayout.CENTER);
        JButton addButton3 = new JButton("Add Book");
        addButton3.setPreferredSize(new Dimension(120, 30));
        addButton3.addActionListener(e -> {
            if (history.containsBook(book3)) {
                JOptionPane.showMessageDialog(frame, "This book is already in the history.");
            } else {
                book3.setRead(); // Call isRead on the book object before adding it to the history
                String rating;
                int ratingValue;
                while (true) {
                    rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                    if (rating == null) {
                        JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                        break;
                    }
                    try {
                        ratingValue = Integer.parseInt(rating);
                        if (ratingValue >= 1 && ratingValue <= 5) {
                            book3.setPersonalRating(ratingValue); // Set the book's rating
                            history.addBook(book3);
                            JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                            break;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                    }
                }if (HistoryGUI.historyTextArea != null) {
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel3.add(addButton3, BorderLayout.SOUTH);
        bookPanel3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 4
        Book book4 = Main.randomBook(library);
        BookDisplay bookDisplay4;
        try {
            bookDisplay4 = new BookDisplay(book4);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel4.setLayout(new BorderLayout());
        bookPanel4.add(bookDisplay4.rootPanel, BorderLayout.CENTER);
        JButton addButton4 = new JButton("Add Book");
        addButton4.setPreferredSize(new Dimension(120, 30));
        addButton4.addActionListener(e -> {
            if (history.containsBook(book4)) {
                JOptionPane.showMessageDialog(frame, "This book is already in the history.");
            } else {
                book4.setRead(); // Call isRead on the book object before adding it to the history
                String rating;
                int ratingValue;
                while (true) {
                    rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                    if (rating == null) {
                        JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                        break;
                    }
                    try {
                        ratingValue = Integer.parseInt(rating);
                        if (ratingValue >= 1 && ratingValue <= 5) {
                            book4.setPersonalRating(ratingValue); // Set the book's rating
                            history.addBook(book4);
                            JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                            break;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                    }
                }if (HistoryGUI.historyTextArea != null) {
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel4.add(addButton4, BorderLayout.SOUTH);
        bookPanel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 5
        Book book5 = Main.randomBook(library);
        BookDisplay bookDisplay5;
        try {
            bookDisplay5 = new BookDisplay(book5);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel5.setLayout(new BorderLayout());
        bookPanel5.add(bookDisplay5.rootPanel, BorderLayout.CENTER);
        JButton addButton5 = new JButton("Add Book");
        addButton5.setPreferredSize(new Dimension(120, 30));
        addButton5.addActionListener(e -> {
            if (history.containsBook(book5)) {
                JOptionPane.showMessageDialog(frame, "This book is already in the history.");
            } else {
                book5.setRead(); // Call isRead on the book object before adding it to the history
                String rating;
                int ratingValue;
                while (true) {
                    rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                    if (rating == null) {
                        JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                        break;
                    }
                    try {
                        ratingValue = Integer.parseInt(rating);
                        if (ratingValue >= 1 && ratingValue <= 5) {
                            book5.setPersonalRating(ratingValue); // Set the book's rating
                            history.addBook(book5);
                            JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                            break;
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                    }
                }if (HistoryGUI.historyTextArea != null) {
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel5.add(addButton5, BorderLayout.SOUTH);
        bookPanel5.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set the content pane and show the frame
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Displays the list of books in a table format using a JOptionPane.
     *
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
        JOptionPane.showMessageDialog(frame, scrollPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     Closes the current window.
     If the frame is not null, it is disposed of.
     */
    public void closeWindow() {
        if (this.frame != null) {
            this.frame.dispose();
        }
    }
    /**
     Returns the content pane of the window.
     @return the content pane of the window
     */
    public JPanel getContentPane() {
        return contentPane;
    }
    public static void main(String[] args) {
        // Set the content pane and show the frame
        JFrame frame = new JFrame("iLibrary");
        HomePageGUI homePage = new HomePageGUI(frame);
        frame.setContentPane(homePage.getContentPane());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}