import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryGUI extends JFrame {
    public static JTextArea historyTextArea;
    private JTextArea historyTextArea2;
    private ViewHistory history;
    private JButton addButton;
    private JButton clearButton;
    private JComboBox<String> menuBox;
    private JTextField searchBar;
    private JButton searchButton;
    private JPanel buttonPanel;
    private JPanel topPanel;
    private JPanel menuPanel;
    private JLabel protitle;
    private JLabel histotitle;
    private JPanel mainPanel;
    private JPanel TopPanel;

    public HistoryGUI() {
        super("Book History");

        // Create the view history object with the history label and filename
        historyTextArea = new JTextArea();

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
            public void actionPerformed(ActionEvent e) {
                history.clearHistory();
                historyTextArea.setText(history.getHistory());
            }
        });
        buttonPanel.add(clearButton);

        // Create a button to add a book
        addButton = new JButton("Add Book");
        addButton.setPreferredSize(new Dimension(120, 30));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = BookDisplay.getBook();
                if (history.containsBook(book)) {
                    JOptionPane.showMessageDialog(mainPanel, "This book is already in the history.");
                } else {
                    book.setRead(); // Call isRead on the book object before adding it to the history
                    history.addBook(book);
                    history.saveHistory(); // Save the history to the CSV file
                    historyTextArea.setText(history.getHistory());
                }
            }
        });
        buttonPanel.add(addButton);

        // Create a scrollable text area for the history
        JScrollPane scrollPane = new JScrollPane(historyTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create a panel for the menu and search components
        menuPanel = new JPanel();

        // Create a combo box with the menu options
        menuBox = new JComboBox<>(new String[]{"History", "Home", "Search"});
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
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchBar.getText();
                // Implement search functionality
            }
        });
        menuPanel.add(searchButton);

        // Create a title for the history panel
        histotitle = new JLabel("History");
        histotitle.setFont(new Font("Arial", Font.BOLD, 20));

        // Create a panel for the top components
        topPanel = new JPanel(new BorderLayout());
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
        history.loadHistory();
        historyTextArea.setText(history.getHistory());

        // Set the size of the window and make it visible
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
