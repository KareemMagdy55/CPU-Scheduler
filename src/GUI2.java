import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI2 extends JFrame {
    private JTextField nameField;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTextField priorityNumberField;
    private JButton nextButton;
    int currentProcess = 0;
    ArrayList<Process> processes;

    public GUI2(int nProccess, int roundRobinQuantumTime, int contextSwitchingTime) {

        super("Process Information");
        processes = new ArrayList<>();


        nameField = new JTextField(20);
        arrivalTimeField = new JTextField(20);
        burstTimeField = new JTextField(20);
        priorityNumberField = new JTextField(20);

        JLabel nameLabel = new JLabel("Name:");
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        JLabel burstTimeLabel = new JLabel("Burst Time:");
        JLabel priorityNumberLabel = new JLabel("Priority Number:");

        JButton colorPickerButton = new JButton("Pick Color");
        colorPickerButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(GUI2.this, "Choose Color", Color.WHITE);
            if (color != null) {
                colorPickerButton.setBackground(color);
            }
        });

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentProcess++;
            String name = nameField.getText();
            String arrivalTime = arrivalTimeField.getText();
            String burstTime = burstTimeField.getText();
            String priorityNumber = priorityNumberField.getText();


            Process process = new Process();
            process.name = name;
            process.arrivalTime = Integer.parseInt(arrivalTime);
            process.burstTime = Integer.parseInt(burstTime);
            process.processNum = currentProcess + 1;
            process.priorityNumber = Integer.parseInt(priorityNumber);
            process.color = colorPickerButton.getBackground();

            processes.add(new Process(process));


            nameField.setText("");
            arrivalTimeField.setText("");
            burstTimeField.setText("");
            priorityNumberField.setText("");
            colorPickerButton.setBackground(Color.WHITE);

            if (currentProcess == nProccess) {

                dispose();

                Scheduler s = new SRTF();
                s.run(new ArrayList<>(processes));
                s.printStats();

                s = new SJF(contextSwitchingTime);
                s.run(new ArrayList<>(processes));
                s.printStats();
                System.out.println();

                s = new PriorityScheduler();
                s.run(new ArrayList<>(processes));
                s.printStats();
                System.out.println();

                s = new AG(roundRobinQuantumTime);
                s.run(new ArrayList<>(processes));
                s.printStats();


                System.exit(0);

            }
        });

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(arrivalTimeLabel);

        panel.add(arrivalTimeField);
        panel.add(burstTimeLabel);
        panel.add(burstTimeField);

        panel.add(priorityNumberLabel);
        panel.add(priorityNumberField);
        panel.add(new JLabel());

        panel.add(colorPickerButton);
        panel.add(new JLabel());
        panel.add(nextButton);

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(panel, BorderLayout.CENTER);

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

}
