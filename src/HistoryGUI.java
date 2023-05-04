import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class HistoryGUI extends JFrame {
    private JTextArea historyTextArea;
    private ViewHistory history;
    private JButton addButton;
    private JButton clearButton;
    private JComboBox<String> menuBox;
    private JTextField searchBar;
    private JButton searchButton;
    private JPanel buttonPanel;
    private JPanel TopPanel;
    private JPanel menuPanel;
    private JLabel protitle;
    private JLabel histotitle;
    private JPanel mainPanel;

    public HistoryGUI() {
        super("Book History");

        // Create the view history object with the history label and filename
        String filename = "book_history.csv";
        history = new ViewHistory(filename);

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
                history.addBook(book);
                history.saveHistory(); // Save the history to the CSV file
                historyTextArea.setText(history.getHistory());
                book.isRead();
            }
        });
        buttonPanel.add(addButton);


        // Create a scrollable text area for the history
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setBackground(Color.WHITE);
        historyTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(historyTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create a panel for the menu and search components
        menuPanel = new JPanel();

        // Create a combo box with the menu options
        menuBox = new JComboBox<>(new String[]{"History", "Home", "Search"});
        menuBox.addActionListener(new ActionListener() {
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
                }
            }
        });

        menuPanel.add(menuBox);

        // Create a text field for searching
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            // When the text field gains focus, the focusGained method is called, which sets the text of the text field to an empty string. This clears the set message from the text field.
            public void focusGained(FocusEvent e) {
                searchBar.setText("");
            }
        });
        menuPanel.add(searchBar);

        // Create a button for running searches
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the search text field
                String searchText = searchBar.getText();

                // Perform search (replace this with your own search function)
                // ...

                // Display search results
                JOptionPane.showMessageDialog(mainPanel,
                        "You searched for \"" + searchText + "\".");
            }
        });
        menuPanel.add(searchButton);

        // Create a main panel to hold the components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the top panel to hold the title and menu panels
        TopPanel = new JPanel();
        TopPanel.setLayout(new BorderLayout());
        TopPanel.add(protitle, BorderLayout.NORTH);
        TopPanel.add(menuPanel, BorderLayout.SOUTH);

        // Add the components to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(TopPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Load the history from the CSV file and update the text area
        history.loadHistory();
        historyTextArea.setText(history.getHistory());

        // Set the size and visibility of the frame
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        // Check if the CSV file exists and create it if it doesn't
        String filename = "book_history.csv";
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Create and show the history GUI
        new HistoryGUI();
    }
}