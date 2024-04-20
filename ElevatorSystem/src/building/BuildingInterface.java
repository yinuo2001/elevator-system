package building;

import scanerzus.Request;

/**
 * This interface is used to represent a building. It contains methods that are used to interact
 * with the building, including processing requests and controlling the elevators.
 */
public interface BuildingInterface {

  /**
   * This method is used to add a request to the building.
   * @param request the request to be added.
   * @return true if the building is running and the request is added, false if the building is out
   *         of service in which case requests cannot be added.
   * @throws IllegalStateException if the building is stopping.
   * @throws IllegalArgumentException if the request is null.
   */
  boolean addRequest(Request request) throws IllegalStateException, IllegalArgumentException;

  /**
   * This method is used to step the building, evoking the step methods in all the elevators if
   * the building is not out of service.
   */
  void step();

  /**
   * This method is used to start the elevator system and start all the elevators.
   * @return true if the elevator system is out of service, false if it's running.
   * @throws IllegalStateException if the elevator system is stopping.
   */
  boolean startElevatorSystem() throws IllegalStateException;

  /**
   * This method is used to stop the elevator system. Elevators will go to the first floor and stop
   * servicing requests. All elevators are in stopping mode and all requests are purged. This method
   * has no effect on a stopping or out of service elevator system.
   */
  void stopElevatorSystem();

  /**
   * This method is used to get the status of the elevator system by generating a BuildingReport
   * which contains the building information and the status of all elevators.
   * @return a BuildingReport object containing status of the elevator system.
   */
  BuildingReport getElevatorSystemStatus();
}
