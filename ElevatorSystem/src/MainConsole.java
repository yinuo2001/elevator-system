import building.Building;

/**
 * The driver for the elevator system.
 * This class will create the elevator system model, view and controller. By calling the
 * start method on the controller, the elevator system will start running.
 */
public class MainConsole {

  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   * @param args the command line arguments, if any
   */
  public static void main(String[] args) {
    Building building = new Building(6, 8, 3);
    SwingElevatorView view = new SwingElevatorView("Elevator System");
    SwingElevatorController controller = new SwingElevatorController(view, building);
    controller.autoRun();
  }
}