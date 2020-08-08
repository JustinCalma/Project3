package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ResponsibleEmbarking implements EmbarkingStrategy {
	
	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {

		if (!elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
			elevator.getRequestedFloor().add(passenger.getTravelStrategy().getDestination());
		} 
		
	}

}
