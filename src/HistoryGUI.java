import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class HistoryGUI extends JFrame {
    private ViewHistory history;
    private JLabel titleLabel;
    private JTextArea historyTextArea;

    public HistoryGUI() {
        super("Book History");

        // Create the view history object with the history label and filename
        String filename = "book_history.csv";
        history = new ViewHistory(filename);

        // Create the title label
        titleLabel = new JLabel("Book History");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();

        // Create a button to add a book to the history
        JButton addButton = new JButton("Add Book");
        addButton.setPreferredSize(new Dimension(100, 30));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookTitle = "New Book";
                history.addBook(bookTitle);
                historyTextArea.setText(history.getHistory());
            }
        });
        buttonPanel.add(addButton);

        // Create a button to clear the history
        JButton clearButton = new JButton("Clear History");
        clearButton.setPreferredSize(new Dimension(120, 30));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                history.clearHistory();
                historyTextArea.setText(history.getHistory());
            }
        });
        buttonPanel.add(clearButton);

        // Create a scrollable text area for the history
        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setBackground(Color.WHITE);
        historyTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(historyTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the components to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Load the history from the CSV file and update the text area
        history.loadHistory();
        historyTextArea.setText(history.getHistory());

        // Set the size and visibility of the frame
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
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