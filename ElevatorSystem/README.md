## 1. About / Overview
This project implements a building system, which consists of some elevators.
The building system is a simulation of a real building system, which is used to control elevators and transport people between floors.
The project is implemented in java and uses java swing to visualize the building system.

## 2. List of Features
### 2.1. Model (Building)
- step: move the whole building for one step, including distributing requests to elevators and change the state of the building if necessary
- addRequest: add a request into the building system
- startElevatorSystem: start the building and start the elevators
- stopElevatorSystem: stop the building and stop the elevators
- getElevatorSystemStatus: get a building report, which can be used to represent the building's status in the view

### 2.2. View
- uses a JFrame to visualize the building system
- uses JPanels to format the GUI, including building state, requests and multiple buttons to interact
- uses grids to represent the elevators, floors, and directions (up: green, down: red, stop: black)
- uses two textboxes to read user input and make requests
- uses buttons to restart, stop, and exit the building system

### 2.3. Controller
- uses a controller to interact with the model and view
- uses a timer to update the view automatically every 1 second

## 3. How to Run
User can double-click the jar file to run the program. The program will start automatically.

## 4. How to Use the Program
After opening the jar file, the building runs automatically. The user can interact with the building system by using the two textboxes and buttons. User can make requests and controls the building system by clicking on the buttons. User can exit the system by clicking on the exit button.

## 5. Design / Model Changes
- When implementing the model, I deleted the getter methods in the original design, because I found that building report contains all the information that the user needs. I also added a private method to distribute the requests to the appropriate elevators.
- In the implementation of view and controller, I did not make any changes to the original design.

## 6. Assumptions
- The requests could be made by a certain method already implemented in the project (it turns out that it could not in the end, so I created requests in the controller).

## 7. Limitations
- When the building system is out of service, some elevators' color may still be red (indicating they are moving down) instead of black, because I did not enable the view to be updated while the system is stopped.
- I did not show the specified status of each elevator, because there is not enough space to show all the details and I did not think up of a good way to conclude them.
- After successfully making a request, the textboxes will not be cleared automatically.

## 8. Citations
- How to use swing timers. How to Use Swing Timers (The JavaTM Tutorials > Creating a GUI With Swing > Using Other Swing Features). (n.d.). https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/misc/timer.html 