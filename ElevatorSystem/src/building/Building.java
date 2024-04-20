package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.ArrayList;
import scanerzus.Request;


/**
 * This class represents a building. A building is composed of multiple elevators. It can control
 * over the elevators and process requests.
 */
public class Building implements BuildingInterface {

  private final ElevatorInterface[] elevators;
  private ElevatorSystemStatus elevatorSystemStatus;
  private final ArrayList<Request> upRequests;
  private final ArrayList<Request> downRequests;
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;


  /**
   * The constructor for the building.
   *
   * @param numberOfFloors the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity the capacity of the elevators in the building.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity) {
    if (numberOfFloors < 2) {
      throw new IllegalArgumentException("The number of floors must be greater than 1.");
    } else if (numberOfElevators < 1) {
      throw new IllegalArgumentException("There must be at least one elevator in the building.");
    } else if (elevatorCapacity < 1) {
      throw new IllegalArgumentException(
          "The elevator must be able to receive at least one person.");
    }
    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevators = new ElevatorInterface[numberOfElevators];
    for (int i = 0; i < numberOfElevators; i++) {
      this.elevators[i] = new Elevator(numberOfFloors, elevatorCapacity);
    }
    this.elevatorSystemStatus = ElevatorSystemStatus.outOfService;
    this.downRequests = new ArrayList<>();
    this.upRequests = new ArrayList<>();
  }

  @Override
  public boolean addRequest(Request request) throws IllegalStateException,
      IllegalArgumentException {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null.");
    }
    if (this.elevatorSystemStatus == ElevatorSystemStatus.outOfService) {
      return false;
    }
    // Now I know that the request is legal and the building will either be stopped or run.
    if (this.elevatorSystemStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator system is stopping now.");
    }
    // The building is running, so the request could be processed.
    if (request.getStartFloor() < request.getEndFloor()) {
      this.upRequests.add(request);
    } else {
      this.downRequests.add(request);
    }
    return true;
  }

  @Override
  public void step() {
    // If the elevator system is out of service, do nothing.
    if (this.elevatorSystemStatus == ElevatorSystemStatus.outOfService) {
      return;
    }
    for (ElevatorInterface elevator : this.elevators) {
      elevator.step();
    }
    // If the elevator system is stopping, check if all elevators are on the first floor after
    // stepping. If so, then the elevator system becomes out of service (finish stopping).
    if (this.elevatorSystemStatus == ElevatorSystemStatus.stopping) {
      for (ElevatorInterface elevator : this.elevators) {
        if (elevator.getCurrentFloor() != 0) {
          return;
        }
      }
      this.elevatorSystemStatus = ElevatorSystemStatus.outOfService;
    } else {
      // If the elevator system is running, distribute requests to elevators.
      this.distributeRequest();
    }
  }

  /**
   * This private method is used when the building steps, to distribute up and
   * down requests to all elevators.
   */
  private void distributeRequest() {
    if (this.upRequests.isEmpty() && this.downRequests.isEmpty()) {
      return;
    }
    for (ElevatorInterface elevator : this.elevators) {
      if (elevator.isTakingRequests()) {
        ArrayList<Request> distributedRequests;
        if (elevator.getCurrentFloor() == 0) {
          distributedRequests = this.getDistributedRequests(this.upRequests);
          elevator.processRequests(distributedRequests);
        } else if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
          distributedRequests = this.getDistributedRequests(this.downRequests);
          elevator.processRequests(distributedRequests);
        }
      }
    }
  }

  /**
   * This private method is used to get a list of requests to be distributed into one elevator.
   * @param requests the list of requests (up or down) stored in the building.
   * @return a list of requests to be distributed to one elevator.
   */
  private ArrayList<Request> getDistributedRequests(ArrayList<Request> requests) {
    ArrayList<Request> distributedRequests = new ArrayList<>();
    while (!requests.isEmpty() && distributedRequests.size() < this.elevatorCapacity) {
      distributedRequests.add(requests.remove(0));
    }
    return distributedRequests;
  }

  @Override
  public boolean startElevatorSystem() throws IllegalStateException {
    if (this.elevatorSystemStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("Elevator system is stopping now.");
    }
    if (this.elevatorSystemStatus == ElevatorSystemStatus.outOfService) {
      this.elevatorSystemStatus = ElevatorSystemStatus.running;
      for (ElevatorInterface elevator : this.elevators) {
        elevator.start();
      }
      return true;
    }
    return false;
  }

  @Override
  public void stopElevatorSystem() {
    for (ElevatorInterface elevator : this.elevators) {
      elevator.takeOutOfService();
    }
    this.elevatorSystemStatus = ElevatorSystemStatus.stopping;
    this.downRequests.clear();
    this.upRequests.clear();
  }

  @Override
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.numberOfElevators];
    for (int i = 0; i < this.numberOfElevators; i++) {
      elevatorReports[i] = this.elevators[i].getElevatorStatus();
    }
    return new BuildingReport(this.numberOfFloors, this.numberOfElevators,
        this.elevatorCapacity, elevatorReports, this.upRequests, this.downRequests,
        this.elevatorSystemStatus);
  }
}


