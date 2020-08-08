package cecs277.passengers;

import cecs277.elevators.Elevator;

public class DisruptiveEmbarking implements EmbarkingStrategy {

	@Override
	public void enteredElevator(Passenger passenger, Elevator elevator) {
		
		if (elevator.requestDestinationFloor(passenger) > elevator.getCurrentFloor().getNumber()) {
			
			for(int i = elevator.requestDestinationFloor(passenger); i < elevator.getBuilding().getFloorCount(); i++) {
				
				if (!elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
					elevator.getRequestedFloor().add(i);
				}
			}
			
		} else if (elevator.requestDestinationFloor(passenger) < elevator.getCurrentFloor().getNumber()) {
						
			for(int i = elevator.requestDestinationFloor(passenger); i > 0; i--) {
				
				if (!elevator.getRequestedFloor().contains(passenger.getTravelStrategy().getDestination())) {
					elevator.getRequestedFloor().add(i);
				}
				
			}
			
		}
		
	}

}
