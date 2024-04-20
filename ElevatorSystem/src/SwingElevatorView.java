import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This is the view for the elevator system. It is responsible for displaying the elevator system
 * and handling user input.
 */
public class SwingElevatorView extends JFrame {
  private final JButton exitButton;
  private final JButton restartButton;
  private final JButton stopButton;
  private final JButton requestButton;
  private final JLabel systemStatus;
  private final JTextField startFloor;
  private final JTextField destinationFloor;
  private final JLabel[][] grid = new JLabel[6][8];
  private final JLabel requestInformation;

  /**
   * Constructs a new SwingElevatorView. It uses the swing library to create a GUI for the elevator
   * system. The GUI consists of a grid of labels that represent the elevator system.
   * @param title the title of the window
   */
  public SwingElevatorView(String title) {
    super(title);
    // Set up the window.
    setSize(800, 400);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Set up the elevator panel, which is consisted of 8 elevators and 6 floors. Each elevator is
    // represented by a label. The 7th row is used to display individual elevator's status.
    JPanel elevatorPanel = new JPanel();
    elevatorPanel.setLayout(new GridLayout(6, 8));
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 8; j++) {
        grid[i][j] = new JLabel();
        grid[i][j].setOpaque(true);
        grid[i][j].setPreferredSize(new Dimension(100, 50));
        grid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
        grid[i][j].setVerticalAlignment(SwingConstants.CENTER);
        grid[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
        grid[i][j].setBackground(Color.WHITE);
        elevatorPanel.add(grid[i][j]);
      }
    }
    this.add(elevatorPanel);

    // Set up the text box, which allows users to enter two integers to make requests.
    JPanel requestPanel = new JPanel();
    this.startFloor = new JTextField(5);
    this.destinationFloor = new JTextField(5);
    requestPanel.add(startFloor);
    requestPanel.add(destinationFloor);
    this.requestButton = new JButton("Make request");
    requestPanel.add(requestButton);
    this.requestInformation = new JLabel();
    requestPanel.add(requestInformation);
    requestPanel.add(Box.createHorizontalGlue());
    this.add(requestPanel);

    // Set up the status panel, which contains the system status.
    JPanel statusPanel = new JPanel();
    this.systemStatus = new JLabel("System status: ");
    statusPanel.add(systemStatus);
    this.add(statusPanel);

    // Set up the bottom panel, which contains the step, start/restart, exit, and stop button.
    JPanel bottomPanel = new JPanel();
    this.restartButton = new JButton("Restart");
    bottomPanel.add(restartButton);
    this.stopButton = new JButton("Stop");
    bottomPanel.add(stopButton);
    this.exitButton = new JButton("Exit");
    bottomPanel.add(exitButton);
    this.add(bottomPanel);

    // Set up the layout.
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

    pack();
    setVisible(true);
  }

  /**
   * Adds the controller features to the view.
   * @param controllerFeatures the controller to be implemented to the view.
   */
  public void addFeatures(SwingElevatorController controllerFeatures) {
    // Set up the request button.
    this.requestButton.addActionListener(evt -> controllerFeatures.makeRequest(
        startFloor.getText(), destinationFloor.getText()));

    // Set up the exit button.
    this.exitButton.setActionCommand("Exit Button");
    this.exitButton.addActionListener(evt -> controllerFeatures.exitProgram());

    // Set up the stop button.
    this.stopButton.addActionListener(evt -> controllerFeatures.stop());

    // Set up the restart button.
    this.restartButton.addActionListener(evt -> controllerFeatures.restart());
  }

  /**
   * Displays the status of the elevator system.
   * @param status a string representing the status of the system. This method will display the
   *               status in the GUI. The status are the status of elevators, the status of requests
   *               and the status of the system.
   */
  public void displaySystemStatus(String status) {
    this.systemStatus.setText(status);
  }

  /**
   * Displays the status of each elevator. The text will be displayed in the lowest grid label,
   * while the background color will be changed to represent the status of the elevator.
   * @param index the index of the certain elevator
   * @param floor the floor of the certain elevator
   * @param direction the direction of the certain elevator
   */
  public void displayElevatorStatus(int index, int floor, String direction) {
    // If the elevator is moving up, change the background color to green. If the elevator is
    // moving down, change the background color to red. Otherwise, change the background color to
    // black.
    int floorIndex = 5 - floor;
    for (int i = 0; i < 6; i++) {
      grid[i][index].setBackground(Color.WHITE);
    }
    if ("^".equals(direction)) {
      grid[floorIndex][index].setBackground(Color.GREEN);
    } else if ("v".equals(direction)) {
      grid[floorIndex][index].setBackground(Color.RED);
    } else {
      grid[floorIndex][index].setBackground(Color.BLACK);
    }
  }

  /**
   * Displays an error message.
   * @param message a string representing the error message
   */
  public void displayError(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  public void displayRequestInformation(String upRequests, String downRequests) {
    this.requestInformation.setText("Up: " + upRequests + " Down: " + downRequests);
  }
}
a