package cecs277.passengers;

import cecs277.elevators.Elevator;
import cecs277.elevators.Elevator.Direction;

public class ClumsyEmbarking implements EmbarkingStrategy {

	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		
		if (elevator.requestDestinationFloor(passenger) > elevator.getCurrentFloor().getNumber() && elevator.getCurrentDirection() == Direction.MOVING_UP) {
			
			// PROJECT 3: Other Fixes
			if (elevator.getCurrentFloor().getNumber() != elevator.requestDestinationFloor(passenger) - 1 && !elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
				elevator.getRequestedFloor().add(elevator.requestDestinationFloor(passenger) - 1);
			}
			
			if (!elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
				// When adding a passenger to an elevator, the addPassenger() will request the passenger's desired destination floor
				elevator.getRequestedFloor().add(elevator.requestDestinationFloor(passenger));
			}
			
		} else if (elevator.requestDestinationFloor(passenger) < elevator.getCurrentFloor().getNumber() && elevator.getCurrentDirection() == Direction.MOVING_DOWN) {
			
			// PROJECT 3: Other Fixes
			if (elevator.getCurrentFloor().getNumber() != elevator.requestDestinationFloor(passenger) + 1 && !elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
				elevator.getRequestedFloor().add(elevator.requestDestinationFloor(passenger) + 1);
			}
			
			if (!elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
				// When adding a passenger to an elevator, the addPassenger() will request the passenger's desired destination floor
				elevator.getRequestedFloor().add(elevator.requestDestinationFloor(passenger));
			}
			
		}
		
	}

}
