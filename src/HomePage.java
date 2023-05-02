import javax.swing.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class HomePage extends JFrame {
    private JComboBox Browse;
    private JTextField searchTextField;
    private JButton searchButton;
    private JLabel welcome;
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel SearchPanel;
    private JLabel stories;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JPanel StoriesPanel;
    private JButton button4;
    private JButton button5;

    public HomePage() {
        Browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stories.setText("" + Browse.getSelectedItem() + " stories~");
            }
        });

        searchTextField.addFocusListener(new FocusAdapter() {
            @Override
            // When the text field gains focus, the focusGained method is called, which sets the text of the text field to an empty string. This clears the set message from the text field.
            public void focusGained(FocusEvent e) {
                searchTextField.setText("");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the search text field
                String searchText = searchTextField.getText();

                // Perform search (replace this with your own search function)
                // ...

                // Display search results
                JOptionPane.showMessageDialog(mainPanel,
                        "You searched for \"" + searchText + "\".");
            }
        });

        Browse.addActionListener(new ActionListener() {
            @Override
            // Change the "Trend stories" by the "option" you pick
            public void actionPerformed(ActionEvent e) {
                stories.setText("" + Browse.getSelectedItem() + " stories~");
            }
        });

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HistoryGUI();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("iLibrary");
        frame.setContentPane(new HomePage().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}