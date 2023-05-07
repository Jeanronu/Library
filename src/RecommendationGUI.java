import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RecommendationGUI<T> extends JFrame {
    private RecommendationHeap<T> heap;
    private JList<String> recommendedBooks;
    private JPanel menuPanel;
    private JComboBox<String> menuBox;
    private JTextField searchBar;
    private JButton searchButton;

    public RecommendationGUI(RecommendationHeap<T> heap, ViewHistory history) throws FileNotFoundException {
        this.heap = heap;

        // Set up the JFrame
        setTitle("Recommended Books");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the components
        JLabel recommendLabel = new JLabel("Recommendations");
        recommendLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(recommendLabel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        DefaultListModel<String> model = new DefaultListModel<>();
        recommendedBooks = new JList<>(model); // initialize recommendedBooks before calling updateRecommendedBooks
        updateRecommendedBooks(); // call updateRecommendedBooks after initializing recommendedBooks
        JScrollPane scrollPane = new JScrollPane(recommendedBooks);
        centerPanel.add(scrollPane);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            dispose();
        });
        bottomPanel.add(closeButton);

        // Create a panel for the menu and search components
        menuPanel = new JPanel();

        // Create a combo box with the menu options
        menuBox = new JComboBox<>(new String[]{"Recommendations","History", "Home", "Search"});
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) menuBox.getSelectedItem();
                if (selectedOption.equals("Home")) {
                    // Redirect to the homepage
                    dispose(); // Close the current window
                    JFrame frame = new JFrame("iLibrary");
                    HomePageGUI homePage = new HomePageGUI(frame);
                    frame.setContentPane(homePage.getContentPane());
                    frame.pack();
                    frame.setVisible(true);
                } else if (selectedOption.equals("Search")) {
                    // Open the Option2 window
                } else if (selectedOption != null && selectedOption.equals("History")) {
                    // Open the History window
                    dispose(); // close the current window
                    HistoryGUI historyWindow = new HistoryGUI();
                    historyWindow.setVisible(true);
                }
            }
        });

        menuPanel.add(menuBox);

        // Create a text field for searching
        searchBar = new JTextField(20);
        searchBar.setText("Search"); // set default text to "Search"
        searchBar.setForeground(Color.GRAY); // set default text color to gray
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Search")) {
                    searchBar.setText(""); // clear the text field
                    searchBar.setForeground(Color.BLACK); // set the text color back to black
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText("Search"); // set the default text back to "Search"
                    searchBar.setForeground(Color.GRAY); // set the text color back to gray
                }
            }
        });
        menuPanel.add(searchBar);

        // Create a button for searching
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchBar.getText();
                // Implement search functionality
            }
        });
        menuPanel.add(searchButton);

        // Add the components to the JFrame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(menuPanel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Display the JFrame
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void updateRecommendedBooks() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Book book : heap.getRecommendations(5)) {
            model.addElement(book.getTitle()); // or any other book information you want to display
        }
        recommendedBooks.setModel(model);
    }

    public static void main(String[] args) {
        try {
            // Create a Library object with the path to the library files
            Library library = new Library(new String[]{"Book_data/Book1.csv"});
            ViewHistory history = new ViewHistory("book_history.csv");

            // Create a RecommendationHeap object and set its library and history stack
            RecommendationHeap<String> heap = new RecommendationHeap<>(library, history.loadHistory());

            // Create a RecommendationGUI object with the heap and history objects
            RecommendationGUI<String> gui = new RecommendationGUI<>(heap, history);

            // Display the RecommendationGUI
            SwingUtilities.invokeLater(() -> gui.setVisible(true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}