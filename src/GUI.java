import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private JTextField nProcessesTextField;
    private JTextField roundRobinTextField;
    private JTextField contextSwithchingTimeTextField;

    public GUI() {
        super("CPU Scheduling Algorithm");

        nProcessesTextField = new JTextField(20);
        roundRobinTextField = new JTextField(20);
        contextSwithchingTimeTextField = new JTextField(20);

        JLabel nProcessesLabel = new JLabel("Number of Processes: ");
        JLabel roundRobinLabel = new JLabel("Round Robin QuantumTime: ");
        JLabel contextSwitchLabel = new JLabel("Context Switching Time:   ");

        JButton submitButton = new JButton("Submit");

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(nProcessesLabel);
        panel.add(nProcessesTextField);

        panel.add(roundRobinLabel);
        panel.add(roundRobinTextField);

        panel.add(contextSwitchLabel);
        panel.add(contextSwithchingTimeTextField);

        panel.add(new JLabel());
        panel.add(submitButton);

        submitButton.addActionListener(e -> {
            int nProcesses = Integer.parseInt(nProcessesTextField.getText());
            int roundRobin = Integer.parseInt(roundRobinTextField.getText());
            int contextSwitchTime = Integer.parseInt(contextSwithchingTimeTextField.getText());

            new GUI2(nProcesses, roundRobin, contextSwitchTime);

            dispose();

        });

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(panel, BorderLayout.CENTER);

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
