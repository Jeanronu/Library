import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Jean Rojas
 A GUI class for displaying the history of the books that the user has borrowed or returned.
 */

public class HistoryGUI extends JFrame {
    public static JTextArea historyTextArea;
    private JTextArea historyTextArea2;
    private final ViewHistory history;
    private JButton addButton;
    private JButton clearButton;
    private JComboBox<String> menuBox;
    private JTextField searchBar;
    private JButton searchButton;
    private JPanel buttonPanel;
    private JPanel menuPanel;
    private JLabel protitle;
    private JLabel histotitle;
    private JPanel mainPanel;
    private JPanel TopPanel;
    private JLabel messageLabel;
    private JPanel bookPanelContainer;

    /**
     * Constructs a new HistoryGUI object with the title "Book History".
     * Initializes the view history object with the history label and filename.
     * Loads the history and displays it in the text area.
     * Creates the title label, a panel for the buttons, a scrollable text area for the history,
     * a panel for the menu and search components, and a title for the history panel.
     * Sets the main panel as the content pane and sets the size of the window to 600x400.
     */
    public HistoryGUI() {
        super("Book History");

        // Create the view history object with the history label and filename
        historyTextArea = new JTextArea();
        history = new ViewHistory("book_history.csv");

        // Load the history and display it in the text area
        history.loadHistory();
        historyTextArea.setText(history.getHistory());
        historyTextArea.setEditable(false);
        historyTextArea.setBackground(Color.WHITE);
        historyTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        historyTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the title label
        protitle = new JLabel("Book History");
        protitle.setHorizontalAlignment(JLabel.CENTER);
        protitle.setFont(new Font("Arial", Font.BOLD, 24));

        // Create a panel for the buttons
        buttonPanel = new JPanel();

        // Create a button to clear the history
        clearButton = new JButton("Clear History");
        clearButton.setPreferredSize(new Dimension(120, 30));
        clearButton.addActionListener(new ActionListener() {
            /**
             * Clears the history and updates the text area.
             * @param e The action event.
             */
            public void actionPerformed(ActionEvent e) {
                history.clearHistory();
                historyTextArea.setText(history.getHistory());
            }
        });
        buttonPanel.add(clearButton);

        // Create a scrollable text area for the history
        JScrollPane scrollPane = new JScrollPane(historyTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create a panel for the menu and search components
        menuPanel = new JPanel();

        // Create a combo box with the menu options
        menuBox = new JComboBox<>(new String[]{"History", "Home", "Search", "Recommendations"});
        menuBox.addActionListener(new ActionListener() {
            /**
             * Handles the action event of selecting an option from the menu.
             * If the "Home" option is selected, redirects to the homepage.
             * If the "Search" option is selected, opens the search window.
             * If the "Recommendations" option is selected, opens the recommendations window.
             * @param e The action event.
             */
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

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText();
                String category = null;
                // Show a dialog to select the category
                String[] options = new String[] {"Category", "Author", "Title"};
                JComboBox<String> categoryBox = new JComboBox<>(options);
                int option = JOptionPane.showOptionDialog(mainPanel, categoryBox, "Select a Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
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
                }

                // Remove existing book panels
                bookPanelContainer.removeAll();
                if (results == null || results.isEmpty()) {
                    messageLabel.setText("No results found.");
                    bookPanelContainer.removeAll(); // clear any existing book panels
                    JOptionPane.showMessageDialog(mainPanel, "Book not found", "Search Results", JOptionPane.INFORMATION_MESSAGE); // show message dialog
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
        bookPanelContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        menuPanel.add(searchBar);
        menuPanel.add(searchButton);

        // Create a title for the history panel
        histotitle = new JLabel("History");
        histotitle.setFont(new Font("Arial", Font.BOLD, 20));

        // Create a panel for the top components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(protitle, BorderLayout.NORTH);
        topPanel.add(menuPanel, BorderLayout.CENTER);

        // Create the main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set the main panel as the content pane
        setContentPane(mainPanel);

        // Load the history and display it in the text area
        history.userHistory();
        historyTextArea.setText(history.userHistory());

        // Set the size of the window and make it visible
        setSize(1050, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // Set the alignment of the cell renderer to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Create a scroll pane for the table and set its dimensions
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Display the table in a JOptionPane with a plain message type
        JOptionPane.showMessageDialog(mainPanel, scrollPane, "Search Results", JOptionPane.PLAIN_MESSAGE);
    }

}
