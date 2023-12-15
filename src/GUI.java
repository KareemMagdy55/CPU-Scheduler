import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    public GUI() {
        // Set the title of the frame
        super("CPU Scheduling Algorithm");

        // Create text fields
        textField1 = new JTextField(20);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);

        // Create labels
        JLabel label1 = new JLabel("Number of Processes: ");
        JLabel label2 = new JLabel("Round Robin QuantumTime: ");
        JLabel label3 = new JLabel("Context Switching Time:   ");

        // Create submit button
        JButton submitButton = new JButton("Submit");

        // Create panel to hold components
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Add components to the panel
        panel.add(label1);
        panel.add(textField1);
        panel.add(label2);
        panel.add(textField2);
        panel.add(label3);
        panel.add(textField3);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(submitButton);

        // Add ActionListener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform actions when the submit button is clicked
                int input1 = Integer.parseInt(textField1.getText());
                int input2 = Integer.parseInt(textField2.getText());
                int input3 = Integer.parseInt(textField3.getText());

                // You can process the input values as needed


                new GUI2(input1, input2, input3);

                dispose();

            }
        });

        // Set layout for the content pane
        getContentPane().setLayout(new BorderLayout());

        // Add the panel to the content pane
        getContentPane().add(panel, BorderLayout.CENTER);

        // Set frame properties
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
