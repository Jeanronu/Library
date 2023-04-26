import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Stack;

public class ViewHistory {
    private Stack<String> history;
    private String filename;
    private JLabel historyLabel;
    private JLabel titleLabel;

    public ViewHistory(String filename) {
        this.filename = filename;
        history = new Stack<>();
    }

    public void addBook(String bookTitle) {
        history.push(bookTitle);
        saveHistory();
    }

    public void clearHistory() {
        history.clear();
        saveHistory();
    }

    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        for (String bookTitle : history) {
            sb.append(bookTitle).append("\n");
        }
        return sb.toString();
    }

    public void loadHistory() {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                history.push(line);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHistory() {
        try {
            FileWriter fw = new FileWriter(filename);
            for (String bookTitle : history) {
                fw.write(bookTitle + "\n");
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}