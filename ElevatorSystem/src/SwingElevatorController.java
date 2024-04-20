import building.Building;
import java.util.Objects;
import java.util.Timer;
import scanerzus.Request;

/**
 * This is the controller for the elevator system. It is responsible for handling user input and
 * updating the view.
 */
public class SwingElevatorController {
  private final SwingElevatorView view;
  private final Building building;

  public SwingElevatorController(SwingElevatorView view, Building building) {
    this.view = view;
    this.building = building;
  }

  /**
   * Renew the status of all individual elevators.
   */
  private void showAllElevatorStatus() {
    for (int i = 0; i < this.building.getElevatorSystemStatus().getNumElevators(); i++) {
      this.view.displayElevatorStatus(i,
          this.building.getElevatorSystemStatus().getElevatorReports()[i].getCurrentFloor(),
          this.building.getElevatorSystemStatus().getElevatorReports()[i].getDirection().toString()
      );
    }
  }

  /**
   * Exit the program.
   */
  public void exitProgram() {
    System.exit(0);
  }

  /**
   * Make a request to the elevator system.
   * @param startFloor the floor where the request starts
   * @param destinationFloor the floor where the request ends
   * @throws NumberFormatException if the input is not an integer
   */
  public void makeRequest(String startFloor, String destinationFloor) throws NumberFormatException {
    try {
      if (startFloor.isEmpty() || destinationFloor.isEmpty()) {
        this.view.displayError("Please enter two integers.");
        return;
      }
      String systemStatus = this.building.getElevatorSystemStatus().getSystemStatus().toString();
      if ("Out Of Service".equals(systemStatus) || "Stopping".equals(systemStatus)) {
        this.view.displayError("The system is not taking requests."
            + "Please restart it before making new requests.");
        return;
      }
      int start = Integer.parseInt(startFloor);
      int destination = Integer.parseInt(destinationFloor);
      // Check if the request is valid.
      if (start < 0 || start > 5
          || destination < 0 || destination > 5 || start == destination) {
        this.view.displayError(
            "Invalid request. Please enter two different integers in range 0-5.");
        return;
      }
      this.building.addRequest(new Request(start, destination));
      this.view.displayRequestInformation(
          this.building.getElevatorSystemStatus().getUpRequests().toString(),
          this.building.getElevatorSystemStatus().getDownRequests().toString());
    } catch (NumberFormatException e) {
      this.view.displayError("Invalid input. Please enter two integers.");
    }
  }

  /**
   * Restart the elevator system. Only works if the system is out of service.
   */
  public void restart() {
    String systemStatus = this.building.getElevatorSystemStatus().getSystemStatus().toString();
    if (Objects.equals(systemStatus, "Out Of Service")) {
      this.building.startElevatorSystem();
      this.view.displaySystemStatus(
          this.building.getElevatorSystemStatus().getSystemStatus().toString());
      showAllElevatorStatus();
    } else {
      String message = "The system is " + systemStatus
          + ". It cannot be restarted now. Please wait until it is out of service.";
      this.view.displayError(message);
    }
  }

  /**
   * Use the timer to enable the elevator system to run automatically.
   */
  public void autoRun() {
    this.building.startElevatorSystem();
    this.view.addFeatures(this);
    Timer timer = new Timer();
    timer.schedule(new java.util.TimerTask() {
      @Override
      public void run() {
        building.step();
        view.displaySystemStatus(building.getElevatorSystemStatus().getSystemStatus().toString());
        showAllElevatorStatus();
        view.displayRequestInformation(
            building.getElevatorSystemStatus().getUpRequests().toString(),
            building.getElevatorSystemStatus().getDownRequests().toString());
      }
    }, 0, 1000);
  }

  /**
   * Stop the elevator system.
   */
  public void stop() {
    String systemStatus = this.building.getElevatorSystemStatus().getSystemStatus().toString();
    if ("Out Of Service".equals(systemStatus) || "Stopping".equals(systemStatus)) {
      this.view.displayError("The system is " + systemStatus + ". It cannot be stopped now.");
      return;
    }
    this.building.stopElevatorSystem();
    this.view.displaySystemStatus(
        this.building.getElevatorSystemStatus().getSystemStatus().toString());
    showAllElevatorStatus();
  }
}
