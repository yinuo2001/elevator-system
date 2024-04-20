package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import building.enums.Direction;
import building.enums.ElevatorSystemStatus;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;
import scanerzus.RequestInterface;

/**
 * This class is used to test the Building class. It contains tests for adding requests, starting
 * and stopping the elevator system, and getting the status of the elevator system.
 */
public class BuildingTest {

  private BuildingInterface testBuilding;
  private RequestInterface testUpRequest1;
  private RequestInterface testUpRequest2;
  private RequestInterface testDownRequest1;
  private RequestInterface testDownRequest2;

  /**
   * Set up the buildings and requests that are to be used for testing.
   */
  @Before
  public void setUp() {
    this.testBuilding = new Building(3, 3, 3);
    this.testUpRequest1 = new Request(0, 2);
    this.testUpRequest2 = new Request(0, 1);
    this.testDownRequest1 = new Request(2, 1);
    this.testDownRequest2 = new Request(1, 0);
  }

  /**
   * This test is used to test the different cases of report status when the building is started,
   * stopping or out of service.
   */
  @Test
  public void testReportStatus() {
    assertEquals(ElevatorSystemStatus.outOfService,
        testBuilding.getElevatorSystemStatus().getSystemStatus());
    assertTrue(this.testBuilding.startElevatorSystem());
    assertEquals(ElevatorSystemStatus.running,
        testBuilding.getElevatorSystemStatus().getSystemStatus());
    testBuilding.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
        testBuilding.getElevatorSystemStatus().getSystemStatus());
  }

  /**
   * This test is used to test adding one request into the building and if it could be distributed
   * to the appropriate elevator correctly.
   */
  @Test
  public void testProcessRequest() {
    assertTrue(this.testBuilding.startElevatorSystem());
    // Add an up request from floor 0 to floor 2
    assertTrue(this.testBuilding.addRequest((Request) this.testUpRequest1));
    assertTrue(this.testBuilding.addRequest((Request) this.testUpRequest2));
    assertEquals(2, this.testBuilding.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, this.testBuilding.getElevatorSystemStatus().getDownRequests().size());
    // In the beginning, the first elevator is taking requests.
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].isTakingRequests());
    // After one step, the request should be distributed to the first elevator.
    this.testBuilding.step();
    assertEquals(0, this.testBuilding.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, this.testBuilding.getElevatorSystemStatus().getDownRequests().size());
    // After the building successfully distributes the request to the first elevator, it stops
    // taking any requests.
    assertFalse(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].isTakingRequests());
    // Testing if the requests are distributed to the elevators successfully
    // and if they remain the same
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[0]);
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[1]);
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[2]);
  }

  /**
   * This test is used to test if the given up and down requests are stored correctly in order
   * inside the building.
   */
  @Test
  public void testStoreRequests() {
    assertTrue(this.testBuilding.startElevatorSystem());
    ArrayList<Request> expectedUpRequests = new ArrayList<>();
    ArrayList<Request> expectedDownRequests = new ArrayList<>();
    // When the building starts, there should not exist any requests in the building.
    assertEquals(expectedUpRequests,
        this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
    // Adding UpRequest1
    assertTrue(this.testBuilding.addRequest((Request) testUpRequest1));
    expectedUpRequests.add((Request) testUpRequest1);
    assertEquals(expectedUpRequests, this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
    // Adding DownRequest1
    assertTrue(this.testBuilding.addRequest((Request) testDownRequest1));
    expectedDownRequests.add((Request) testDownRequest1);
    assertEquals(expectedUpRequests, this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
    // Adding UpRequest2
    assertTrue(this.testBuilding.addRequest((Request) testUpRequest2));
    expectedUpRequests.add((Request) testUpRequest2);
    assertEquals(expectedUpRequests, this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
    // Adding DownRequest2
    assertTrue(this.testBuilding.addRequest((Request) testDownRequest2));
    expectedDownRequests.add((Request) testDownRequest2);
    assertEquals(expectedUpRequests, this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
    // Adding UpRequest1 again
    assertTrue(this.testBuilding.addRequest((Request) testUpRequest1));
    expectedUpRequests.add((Request) testUpRequest1);
    assertEquals(expectedUpRequests, this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
    // Adding 50 requests at a time
    for (int i = 0; i < 50; i++) {
      assertTrue(this.testBuilding.addRequest((Request) testDownRequest1));
      expectedDownRequests.add((Request) testDownRequest1);
    }
    assertEquals(expectedUpRequests, this.testBuilding.getElevatorSystemStatus().getUpRequests());
    assertEquals(expectedDownRequests,
        this.testBuilding.getElevatorSystemStatus().getDownRequests());
  }

  /**
   * This test is used to test allocating up requests to appropriate elevators based on their
   * capacity and current floors. The principle for giving requests to elevators is that
   * one elevator takes as many requests as it could and then the next elevator takes the turn.
   */
  @Test
  public void testRequestsAllocation() {
    assertTrue(this.testBuilding.startElevatorSystem());
    // Adding 4 up requests and 2 down requests to the building.
    // The first elevator should take three up requests, and the second one takes
    // one up request according to the elevator capacity and the principle that only
    // elevators with direction up could take up requests.
    for (int i = 0; i < 4; i++) {
      assertTrue(this.testBuilding.addRequest((Request) testUpRequest1));
    }
    assertTrue(this.testBuilding.addRequest((Request) testDownRequest1));
    assertTrue(this.testBuilding.addRequest((Request) testDownRequest2));
    // After one step, since all the elevators are on the first floor,
    // the up requests are distributed to the elevators.
    this.testBuilding.step();
    assertEquals(0, this.testBuilding.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(2, this.testBuilding.getElevatorSystemStatus().getDownRequests().size());
    // After the building successfully distributes the request to the first and second elevator,
    // they stop taking any requests. In comparison, the third elevator is still taking requests.
    assertFalse(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].isTakingRequests());
    assertFalse(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[1].isTakingRequests());
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[2].isTakingRequests());
    // Testing if the up requests are distributed to the first two elevator successfully
    for (int i = 0; i < 2; i++) {
      assertTrue(
          this.testBuilding.getElevatorSystemStatus()
              .getElevatorReports()[i].getFloorRequests()[0]);
      assertFalse(
          this.testBuilding.getElevatorSystemStatus()
              .getElevatorReports()[i].getFloorRequests()[1]);
      assertTrue(
          this.testBuilding.getElevatorSystemStatus()
              .getElevatorReports()[i].getFloorRequests()[2]);
    }
    // In comparison, the third elevator did not receive any requests to stop anywhere.
    for (int i = 0; i < 3; i++) {
      assertFalse(
          this.testBuilding.getElevatorSystemStatus()
              .getElevatorReports()[2].getFloorRequests()[i]);
    }
    // After waiting for 5 steps and moving up to the third floor (2 steps),
    // the third elevator takes the remaining down requests.
    for (int i = 0; i < 7; i++) {
      this.testBuilding.step();
    }
    // Assert that the third elevator accepts the requests and will stop at each floor.
    assertEquals(0, this.testBuilding.getElevatorSystemStatus().getDownRequests().size());
    assertFalse(this.testBuilding.getElevatorSystemStatus()
        .getElevatorReports()[2].isTakingRequests());
    for (int i = 0; i < 3; i++) {
      assertTrue(
          this.testBuilding.getElevatorSystemStatus()
              .getElevatorReports()[2].getFloorRequests()[i]);
    }
  }

  /**
   * This test is used to test the case when the started building is given the order to stop.
   * After it eventually stops, the building status will become outOfService and no more requests
   * could be added to the building. Also, the requests that are not distributed to the elevators
   * will be cleared.
   */
  @Test
  public void testOutOfService() {
    assertFalse(this.testBuilding.addRequest((Request) testUpRequest1));
    assertTrue(this.testBuilding.startElevatorSystem());
    assertFalse(this.testBuilding.startElevatorSystem());
    assertTrue(this.testBuilding.addRequest((Request) testUpRequest1));
    assertTrue(this.testBuilding.addRequest((Request) testDownRequest1));
    for (int i = 0; i < 3; i++) {
      this.testBuilding.step();
    }
    // All the elevators are going up.
    for (int i = 0; i < 3; i++) {
      assertEquals(Direction.UP,
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[i].getDirection());
    }
    // The first elevator takes the up request. Its door is opened after three steps.
    // It will go to the third floor.
    assertFalse(this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].isDoorClosed());
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[2]);
    assertEquals(1, this.testBuilding.getElevatorSystemStatus().getDownRequests().size());
    this.testBuilding.stopElevatorSystem();
    // After stopping the building, the requests are purged. The first elevator will no longer
    // stop at the third floor.
    assertFalse(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[2]);
    assertEquals(0, this.testBuilding.getElevatorSystemStatus().getDownRequests().size());
    assertEquals(ElevatorSystemStatus.stopping,
        this.testBuilding.getElevatorSystemStatus().getSystemStatus());
    // Test that all the elevators are going down after the building is stopping.
    for (int i = 0; i < 3; i++) {
      assertEquals(Direction.DOWN,
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[i].getDirection());
    }
    for (int i = 0; i < 3; i++) {
      this.testBuilding.step();
    }
    // After the building stops, the building status becomes outOfService and the elevators are out
    // of service as well. The elevators are on the first floor with doors opened.
    assertEquals(ElevatorSystemStatus.outOfService,
        this.testBuilding.getElevatorSystemStatus().getSystemStatus());
    for (int i = 0; i < 3; i++) {
      assertTrue(
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[i].isOutOfService());
      assertFalse(
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[i].isDoorClosed());
      assertEquals(0,
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[i].getCurrentFloor());
    }
    // The building is out of service, so no more requests could be added.
    assertFalse(this.testBuilding.addRequest((Request) testUpRequest1));
  }

  /**
   * This test will test when the stored requests exceed the maximum capacity of one elevator.
   * The building is supposed to give the requests to the elevators in order.
   */
  @Test
  public void testAllocateRequestsExceedingMaxCapacity() {
    assertTrue(this.testBuilding.startElevatorSystem());
    for (int i = 0; i < 3; i++) {
      assertTrue(
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].isTakingRequests());
      assertTrue(this.testBuilding.addRequest((Request) testUpRequest1));
    }
    this.testBuilding.step();
    // The first elevator is able to take three requests at most.
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[0]);
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].getFloorRequests()[2]);
    assertFalse(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[0].isTakingRequests());
    for (int i = 0; i < 3; i++) {
      assertTrue(
          this.testBuilding.getElevatorSystemStatus().getElevatorReports()[1].isTakingRequests());
      assertTrue(this.testBuilding.addRequest((Request) testUpRequest1));
    }
    this.testBuilding.step();
    // Since the first elevator is full, the second elevator is going to take next three requests.
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[1].getFloorRequests()[0]);
    assertTrue(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[1].getFloorRequests()[2]);
    assertFalse(
        this.testBuilding.getElevatorSystemStatus().getElevatorReports()[1].isTakingRequests());
  }

  /**
   * This test is used to test the toString method of BuildingReport.
   */
  @Test
  public void testBuildingReportToString() {
    this.testBuilding.startElevatorSystem();
    assertEquals(3, this.testBuilding.getElevatorSystemStatus().getNumFloors());
    assertEquals(3, this.testBuilding.getElevatorSystemStatus().getNumElevators());
    assertEquals(3, this.testBuilding.getElevatorSystemStatus().getElevatorCapacity());
    this.testBuilding.addRequest((Request) testUpRequest1);
    String expected = "Building Report:\n"
        + "Elevator system status: Running\n"
        + "Elevator reports: \n"
        + "Waiting[Floor 0, Time 5]\n"
        + "Waiting[Floor 0, Time 5]\n"
        + "Waiting[Floor 0, Time 5]\n"
        + "Up requests: [0->2]\n"
        + "Down requests: []\n";
    assertEquals(expected, this.testBuilding.getElevatorSystemStatus().toString());
  }
}