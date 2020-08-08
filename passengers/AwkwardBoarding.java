package cecs277.passengers;

import cecs277.elevators.Elevator;

public class AwkwardBoarding implements BoardingStrategy {
	private int threshold;
	
	public AwkwardBoarding(int threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		
		if (elevator.getPassengerCount() <= threshold) {
			return true;
		} else {
			this.threshold = threshold + 2;
		}
		
		return false;
	}

}
