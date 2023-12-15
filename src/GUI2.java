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

    public GUI2(int procesnum, int roundRobinQuantumTime, int contextSwitchingTime) {
        // Set the title of the frame

        super("Process Information");
        processes = new ArrayList<>() ;


        // Create text fields
        nameField = new JTextField(20);
        arrivalTimeField = new JTextField(20);
        burstTimeField = new JTextField(20);
        priorityNumberField = new JTextField(20);
        // Create labels
        JLabel nameLabel = new JLabel("Name:");
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        JLabel burstTimeLabel = new JLabel("Burst Time:");
        JLabel priorityNumberLabel = new JLabel("Priority Number:");

        // Create color picker
        JButton colorPickerButton = new JButton("Pick Color");
        colorPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open color picker dialog and set the background color of a text field
                Color color = JColorChooser.showDialog(GUI2.this, "Choose Color", Color.WHITE);
                if (color != null) {
                    nameField.setBackground(color);
                }
            }
        });

        // Create next button
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentProcess++;
                // Get values from text fields
                String name = nameField.getText();
                String arrivalTime = arrivalTimeField.getText();
                String burstTime = burstTimeField.getText();
                String priorityNumber = priorityNumberField.getText();



                Process process = new Process() ;
                process.name = name;
                process.arrivalTime = Integer.parseInt(arrivalTime);
                process.burstTime = Integer.parseInt(burstTime);
                process.processNum= currentProcess + 1;
                process.priorityNumber = Integer.parseInt(priorityNumber);
                process.color = nameField.getBackground().getRGB();

                processes.add(new Process(process));


                // Clear text fields for the next input
                nameField.setText("");
                arrivalTimeField.setText("");
                burstTimeField.setText("");
                priorityNumberField.setText("");
                nameField.setBackground(Color.WHITE); // Reset color

                // You can save the data, perform calculations, etc., as needed for your application
                if (currentProcess == procesnum) {

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


                    // terminate the program
                    System.exit(0);

                }
            }
        });

        // Create panel to hold components
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        // Add components to the panel
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(arrivalTimeLabel);
        panel.add(arrivalTimeField);
        panel.add(burstTimeLabel);
        panel.add(burstTimeField);
        panel.add(priorityNumberLabel);
        panel.add(priorityNumberField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(colorPickerButton);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(nextButton);

        // Set layout for the content pane
        getContentPane().setLayout(new BorderLayout());

        // Add the panel to the content pane
        getContentPane().add(panel, BorderLayout.CENTER);

        // Set frame properties
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

}
