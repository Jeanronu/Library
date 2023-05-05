import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;

public class HomePageGUI {
    private JFrame frame;
    private JPanel contentPane;

    public HomePageGUI(JFrame frame) {
        this.frame = frame;
        // Create the components
        JLabel welcomeLabel = new JLabel("Welcome to the iLibrary");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JComboBox<String> menuBox = new JComboBox<>(new String[]{"Home", "History", "Search"});
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) menuBox.getSelectedItem();
                if (selectedOption != null && selectedOption.equals("Home")) {
                    // do nothing
                } else if (selectedOption != null && selectedOption.equals("History")) {
                    // Open the History window
                    closeWindow(); // close the current window
                    HistoryGUI historyWindow = new HistoryGUI();
                    historyWindow.setVisible(true);
                } else if (selectedOption != null && selectedOption.equals("Search")) {
                    // Open the Search window
                    closeWindow(); // close the current window
//                    SearchGUI searchWindow = new SearchGUI();
//                    searchWindow.setVisible(true);
                }
            }
        });

        JTextField searchField = new JTextField("Search", 20);
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

        JPanel bookPanel1 = new JPanel();
        bookPanel1.setPreferredSize(new Dimension(200, 300));
        bookPanel1.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel2 = new JPanel();
        bookPanel2.setPreferredSize(new Dimension(200, 300));
        bookPanel2.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel3 = new JPanel();
        bookPanel3.setPreferredSize(new Dimension(200, 300));
        bookPanel3.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel4 = new JPanel();
        bookPanel4.setPreferredSize(new Dimension(200, 300));
        bookPanel4.setBackground(Color.LIGHT_GRAY);
        JPanel bookPanel5 = new JPanel();
        bookPanel5.setPreferredSize(new Dimension(200, 300));
        bookPanel5.setBackground(Color.LIGHT_GRAY);

        // Add the components to the content pane
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(welcomeLabel);
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
        String[] files = {"Book_data/Book1.csv"};
        Library library = null;
        try {
            library = new Library(files);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Book 1
        Book book1 = Main.randomBook(library);
        BookDisplay bookDisplay1 = null;
        try {
            bookDisplay1 = new BookDisplay(book1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel1.removeAll();
        bookPanel1.add(bookDisplay1.rootPanel);
        bookPanel1.revalidate();
        bookPanel1.repaint();

        // Book 2
        Book book2 = Main.randomBook(library);
        BookDisplay bookDisplay2 = null;
        try {
            bookDisplay2 = new BookDisplay(book2);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel2.removeAll();
        bookPanel2.add(bookDisplay2.rootPanel);
        bookPanel2.revalidate();
        bookPanel2.repaint();

        // Book 3
        Book book3 = Main.randomBook(library);
        BookDisplay bookDisplay3 = null;
        try {
            bookDisplay3 = new BookDisplay(book3);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel3.removeAll();
        bookPanel3.add(bookDisplay3.rootPanel);
        bookPanel3.revalidate();
        bookPanel3.repaint();

        // Book 4
        Book book4 = Main.randomBook(library);
        BookDisplay bookDisplay4 = null;
        try {
            bookDisplay4 = new BookDisplay(book4);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel4.removeAll();
        bookPanel4.add(bookDisplay4.rootPanel);
        bookPanel4.revalidate();
        bookPanel4.repaint();

        // Book 5
        Book book5 = Main.randomBook(library);
        BookDisplay bookDisplay5 = null;
        try {
            bookDisplay5 = new BookDisplay(book5);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel5.removeAll();
        bookPanel5.add(bookDisplay5.rootPanel);
        bookPanel5.revalidate();
        bookPanel5.repaint();

        // Set the content pane and show the frame
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public void closeWindow() {
        if (this.frame != null) {
            this.frame.dispose();
        }
    }

    public JPanel getContentPane() {
        return contentPane;
    }
    public static void main(String[] args) {
        // Set the content pane and show the frame
        JFrame frame = new JFrame("iLibrary");
        HomePageGUI homePage = new HomePageGUI(frame);
        frame.setContentPane(homePage.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }
}