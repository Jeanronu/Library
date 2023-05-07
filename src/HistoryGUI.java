import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
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
                } else if (selectedOption != null && selectedOption.equals("Recommendations")) {
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
        });

        menuPanel.add(menuBox);

        // Create a text field for searching
        searchBar = new JTextField(20);
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            // When the text field gains focus, the focusGained method is called, which sets the text of the text field to an empty string. This allows the user to start typing immediately without deleting any existing text in the field.
            public void focusGained(FocusEvent e) {
                searchBar.setText("");
            }
        });
        menuPanel.add(searchBar);
        // Create a button for searching
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            // Implement search functionality
        });
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
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
