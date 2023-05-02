import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JComboBox<String> menuBox;

    public Main() {
        super("Main Window");

        // Create a menu with options
        menuBox = new JComboBox<>(new String[]{"Home", "Option 1", "Option 2"});
        JPanel panel = new JPanel();
        panel.add(menuBox);

        // Add an ActionListener to the menuBox
        menuBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) menuBox.getSelectedItem();
                if (selectedOption != null && selectedOption.equals("Home")) {
                    // Open the Home window
                } else if (selectedOption != null && selectedOption.equals("Option 1")) {
                    // Open the Option1 window
                    HistoryGUI option1Window = new HistoryGUI();
                    option1Window.setVisible(true);
                } else if (selectedOption != null && selectedOption.equals("Option 2")) {
                    // Open the Option2 window
                    HomePage option2Window = new HomePage();
                    option2Window.setVisible(true);
                }
            }
        });


        // Set up the main window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
