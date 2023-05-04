import java.io.*;
import java.util.*;

public class ViewHistory {
    private String filename = "book_history.csv";
    private Stack<Book> historyStack;

    public ViewHistory(String filename) {
        this.filename = filename;
        historyStack = new Stack<>();
        loadHistory();
    }

    public void addBook(Book book) {
        historyStack.push(book);
        saveHistory();
    }

    public void clearHistory() {
        while (!historyStack.isEmpty()) {
            historyStack.pop();
        }
        saveHistory();
    }

    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        for (Book book : historyStack) {
            sb.append(book.getTitle()).append("\n");
        }
        return sb.toString();
    }

    void loadHistory() {
        try {
            FileReader fileReader = new FileReader(filename);
            CSVReader csvReader = new CSVReader();
            ArrayList<String[]> lines = csvReader.read(fileReader);
            ArrayList<Book> books = new ArrayList<>();
            BookFinder bookFinder = new BookFinder(books);
            for (String[] line : lines) {
                Book book = bookFinder.getBookByTitle(line[0]);
                if (book != null) {
                    historyStack.push(book);
                }
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveHistory() {
        try {
            List<String> titles = new ArrayList<>();
            FileReader fileReader = new FileReader(filename);
            CSVReader csvReader = new CSVReader();
            ArrayList<String[]> lines = csvReader.read(fileReader);
            for (String[] line : lines) {
                titles.add(line[0]);
            }
            fileReader.close();

            FileWriter writer = new FileWriter(filename, true);
            for (Book book : historyStack) {
                if (!titles.contains(book.getTitle())) {
                    writer.write(book.getTitle() + "\n");
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}