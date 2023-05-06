import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class HomePageGUI {
    private JFrame frame;
    private JPanel contentPane;
    private ViewHistory history;

    public HomePageGUI(JFrame frame) {
        this.frame = frame;
        // Create the components
        JLabel welcomeLabel = new JLabel("Welcome to the iLibrary");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JComboBox<String> menuBox = new JComboBox<>(new String[]{"Home", "History", "Search", "Recommendations"});
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
                // SearchGUI searchWindow = new SearchGUI();
                // searchWindow.setVisible(true);
                } else if (selectedOption != null && selectedOption.equals("Recommendations")) {
                    // Open the Recommendations window
                    closeWindow(); // close the current window
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
        Library library = null;
        try {
            library = new Library(files);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Book1
        Book book1 = Main.randomBook(library);
        BookDisplay bookDisplay1 = null;
        try {
            bookDisplay1 = new BookDisplay(book1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel1.setLayout(new BorderLayout());
        bookPanel1.add(bookDisplay1.rootPanel, BorderLayout.CENTER);
        JButton addButton1 = new JButton("Add Book");
        addButton1.setPreferredSize(new Dimension(120, 30));
        addButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = book1;
                if (history.containsBook(book)) {
                    JOptionPane.showMessageDialog(frame, "This book is already in the history.");
                } else {
                    book.setRead(); // Call isRead on the book object before adding it to the history
                    String rating = "";
                    int ratingValue = 0;
                    while (true) {
                        rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                        if (rating == null) {
                            JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                            break;
                        }
                        try {
                            ratingValue = Integer.parseInt(rating);
                            if (ratingValue >= 1 && ratingValue <= 5) {
                                book.setPersonalRating(ratingValue); // Set the book's rating
                                history.addBook(book);
                                JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                                break;
                            } else {
                                JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    }
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel1.add(addButton1, BorderLayout.SOUTH);
        bookPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 2
        Book book2 = Main.randomBook(library);
        BookDisplay bookDisplay2 = null;
        try {
            bookDisplay2 = new BookDisplay(book2);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel2.setLayout(new BorderLayout());
        bookPanel2.add(bookDisplay2.rootPanel, BorderLayout.CENTER);
        JButton addButton2 = new JButton("Add Book");
        addButton2.setPreferredSize(new Dimension(120, 30));
        addButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = book2;
                if (history.containsBook(book)) {
                    JOptionPane.showMessageDialog(frame, "This book is already in the history.");
                } else {
                    book.setRead(); // Call isRead on the book object before adding it to the history
                    history.addBook(book);
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel2.add(addButton2, BorderLayout.SOUTH);
        bookPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 3
        Book book3 = Main.randomBook(library);
        BookDisplay bookDisplay3 = null;
        try {
            bookDisplay3 = new BookDisplay(book3);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel3.setLayout(new BorderLayout());
        bookPanel3.add(bookDisplay3.rootPanel, BorderLayout.CENTER);
        JButton addButton3 = new JButton("Add Book");
        addButton3.setPreferredSize(new Dimension(120, 30));
        addButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = book3;
                if (history.containsBook(book)) {
                    JOptionPane.showMessageDialog(frame, "This book is already in the history.");
                } else {
                    book.setRead(); // Call isRead on the book object before adding it to the history
                    history.addBook(book);
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel3.add(addButton3, BorderLayout.SOUTH);
        bookPanel3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 4
        Book book4 = Main.randomBook(library);
        BookDisplay bookDisplay4 = null;
        try {
            bookDisplay4 = new BookDisplay(book4);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel4.setLayout(new BorderLayout());
        bookPanel4.add(bookDisplay4.rootPanel, BorderLayout.CENTER);
        JButton addButton4 = new JButton("Add Book");
        addButton4.setPreferredSize(new Dimension(120, 30));
        addButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = book4;
                if (history.containsBook(book)) {
                    JOptionPane.showMessageDialog(frame, "This book is already in the history.");
                } else {
                    book.setRead(); // Call isRead on the book object before adding it to the history
                    String rating = "";
                    int ratingValue = 0;
                    while (true) {
                        rating = JOptionPane.showInputDialog(frame, "Please rate the book from 1-5:");
                        if (rating == null) {
                            JOptionPane.showMessageDialog(frame, "Sorry but for you to add the book to your list, you have to rate it. Thank you <3");
                            break;
                        }
                        try {
                            ratingValue = Integer.parseInt(rating);
                            if (ratingValue >= 1 && ratingValue <= 5) {
                                book.setPersonalRating(ratingValue); // Set the book's rating
                                history.addBook(book);
                                JOptionPane.showMessageDialog(frame, "Your book have been added. <3");
                                break;
                            } else {
                                JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid rating. Please enter a number from 1-5.");
                        }
                    }
                    history.saveHistory(); // Save the history to the CSV file
                    HistoryGUI.historyTextArea.setText(history.getHistory());
                }
            }
        });
        bookPanel4.add(addButton4, BorderLayout.SOUTH);
        bookPanel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Book 5
        Book book5 = Main.randomBook(library);
        BookDisplay bookDisplay5 = null;
        try {
            bookDisplay5 = new BookDisplay(book5);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        bookPanel5.setLayout(new BorderLayout());
        bookPanel5.add(bookDisplay5.rootPanel, BorderLayout.CENTER);
        JButton addButton5 = new JButton("Add Book");
        addButton5.setPreferredSize(new Dimension(120, 30));
        addButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book book = book5;
                if (history.containsBook(book)) {
                    JOptionPane.showMessageDialog(frame, "This book is already in the history.");
                } else {
                    book.setRead(); // Call isRead on the book object before adding it to the history
                    history.addBook(book);
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}