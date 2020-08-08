package cecs277.passengers;

import cecs277.elevators.Elevator;

public class AttentiveDebarking implements DebarkingStrategy {

	@Override
	public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {

		if (elevator.requestDestinationFloor(passenger) == elevator.getCurrentFloor().getNumber()) {
			return true;
		}
		return false;
	}

	@Override
	public void departedElevator(Passenger passenger, Elevator elevator) {

		elevator.callScheduleNextDestination(passenger);
	}

}
