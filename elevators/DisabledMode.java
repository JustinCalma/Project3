package cecs277.elevators;

import cecs277.buildings.Floor;
import cecs277.elevators.Elevator.Direction;

public class DisabledMode implements OperationMode {

	// DISABLED MODE: THE ELEVATOR DOES NOT DO ANYTHING WHEN IT IS DISABLED
	
	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Direction targetDirection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Direction direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(Elevator elevator) {
		// TODO Auto-generated method stub
		
	}

}
