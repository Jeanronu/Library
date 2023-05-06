import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BookDisplay {
    JPanel rootPanel;
    private JLabel bookImage;
    private JLabel bookTitle;
    private JLabel bookAuthor;
    private JLabel bookCategories;
    private JTextArea bookDescription;
    private static Book book;

    public BookDisplay(Book book) throws IOException {
        this.book = book;
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        // Set up book image
        bookImage = new JLabel();
        bookImage.setHorizontalAlignment(SwingConstants.CENTER);
        bookImage.setVerticalAlignment(SwingConstants.TOP);
        rootPanel.add(bookImage, BorderLayout.NORTH);

        // Set up book information panel
        JPanel bookInfoPanel = new JPanel();
        bookInfoPanel.setLayout(new BoxLayout(bookInfoPanel, BoxLayout.Y_AXIS));
        bookTitle = new JLabel(book.getTitle());
        bookTitle.setFont(new Font(bookTitle.getFont().getName(), Font.BOLD, 18));
        bookTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookInfoPanel.add(bookTitle);
        bookAuthor = new JLabel(String.join(", ", book.getAuthors()));
        bookAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookInfoPanel.add(bookAuthor);
        bookCategories = new JLabel(String.join("; ", book.getCategories()));
        bookCategories.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookInfoPanel.add(bookCategories);
        bookInfoPanel.add(Box.createVerticalStrut(10));
        bookDescription = new JTextArea(book.getDescription());
        bookDescription.setWrapStyleWord(true);
        bookDescription.setLineWrap(true);
        bookDescription.setEditable(false);
        bookDescription.setBackground(rootPanel.getBackground());
        bookDescription.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // add margin
        JScrollPane scrollPane = new JScrollPane(bookDescription);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bookInfoPanel.add(scrollPane);
        rootPanel.add(bookInfoPanel, BorderLayout.CENTER);
        rootPanel.setPreferredSize(new Dimension(190, 290)); // set preferred size

        // Load book image
        try {
            URL imageUrl = new URL(book.getThumbnail());
            Image image = ImageIO.read(imageUrl);
            int height = 150;
            ImageIcon icon = new ImageIcon(image.getScaledInstance(100, height, Image.SCALE_SMOOTH));
            bookImage.setIcon(icon);
        } catch (MalformedURLException ex) {
            URL invimageUrl = new URL("https://th.bing.com/th/id/R.543d6deeaf347069284e4b50a382c43d?rik=%2fhJ4nntH8qf0ow&riu=http%3a%2f%2fegyptianstreets.com%2fwp-content%2fuploads%2f2017%2f07%2f404.jpg&ehk=GmgY30HfAgjnFyI%2ffugEffJ11Tv%2bc8G6%2bCVUtin8EKo%3d&risl=&pid=ImgRaw&r=0");
            Image invimage = ImageIO.read(invimageUrl);
            int height = 150;
            ImageIcon icon2 = new ImageIcon(invimage.getScaledInstance(-1, height, Image.SCALE_SMOOTH));
            bookImage.setIcon(icon2);
        } catch (IOException ex) {
            bookImage.setText("Error loading image");
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public static Book getBook() {
        return book;
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("BookDisplay");

        String[] files = {"Book_data/Book1.csv"};
        Book test = null;
        Library library = new Library(files);
        test = Main.randomBook(library);

        BookDisplay bookDisplay = new BookDisplay(test); // create an instance of BookDisplay with the random book
        frame.add(bookDisplay.getRootPanel()); // add the root panel of the BookDisplay instance to the frame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
