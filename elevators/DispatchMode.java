package cecs277.elevators;

import java.util.ArrayList;
import java.util.List;

import cecs277.buildings.Floor;
import cecs277.elevators.Elevator.Direction;
import cecs277.elevators.Elevator.ElevatorState;

/**
 * A DispatchMode elevator is in the midst of a dispatch to a target floor in order to handle a request in a target
 * direction. The elevator will not stop on any floor that is not its destination, and will not respond to any other
 * request until it arrives at the destination.
 */
public class DispatchMode implements OperationMode {
	// The destination floor of the dispatch.
	private Floor mDestination;
	// The direction requested by the destination floor; NOT the direction the elevator must move to get to that floor.
	private Elevator.Direction mDesiredDirection;
	
	public DispatchMode(Floor destination, Elevator.Direction desiredDirection) {
		mDestination = destination;
		mDesiredDirection = desiredDirection;
	}
	
	// TODO: implement the other methods of the OperationMode interface.
	// Only Idle elevators can be dispatched.
	// A dispatching elevator ignores all other requests.
	// It does not check to see if it should stop of floors that are not the destination.
	// Its flow of ticks should go: IDLE_STATE -> ACCELERATING -> MOVING -> ... -> MOVING -> DECELERATING.
	//    When decelerating to the destination floor, change the elevator's direction to the desired direction,
	//    announce that it is decelerating, and then schedule an operation change in 3 seconds to
	//    ActiveOperation in the DOORS_OPENING state.
	// A DispatchOperation elevator should never be in the DOORS_OPENING, DOORS_OPEN, or DOORS_CLOSING states.
	
	
	@Override
	public String toString() {
		return "Dispatching to " + mDestination.getNumber() + " " + mDesiredDirection;
	}

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
				
		// Idle state
        if (elevator.getState() == ElevatorState.IDLE_STATE) {
        	
        	// PROJECT 3: Additional Work
        	if (elevator.getBuilding().getHasBeenDisabled() == false && mDestination.getNumber() != 1) {
        		elevator.getBuilding().setHasBeenDisabled(true);
        		elevator.disable(new DisabledMode(), ElevatorState.IDLE_STATE, 0);
        		elevator.enable(this, ElevatorState.IDLE_STATE, 300);
        	}
        	        	
        	elevator.scheduleStateChange(ElevatorState.ACCELERATING, 0);
        	
        	if (mDesiredDirection == Direction.MOVING_UP) {
        		elevator.setCurrentDirection(Direction.MOVING_DOWN);
        		
        	} else if (mDesiredDirection == Direction.MOVING_DOWN) {
            	elevator.setCurrentDirection(Direction.MOVING_UP);
        	}
        	
        }        

        // State ACCELERATING
        else if (elevator.getState() == ElevatorState.ACCELERATING){
        	// Remove the elevator as an observer of the current floor
        	elevator.getCurrentFloor().removeObserver(elevator);
        	// Transition to MOVING state
        	elevator.scheduleStateChange(ElevatorState.MOVING, 3);
        	
        }

        // State DECELERATING
        else if (elevator.getState() == ElevatorState.DECELERATING) {

        	// PROJECT 3: 10.5
            elevator.setCurrentDirection(mDesiredDirection);
            
            // Alert all observers and change to DOORS_OPENING
            List<ElevatorObserver> temp = new ArrayList<>(elevator.getObservers());
            for (ElevatorObserver i : temp) {
                i.elevatorDecelerating(elevator);
            }
            
            elevator.scheduleModeChange(new ActiveMode(), Elevator.ElevatorState.DOORS_OPENING, 3);

        }
            
        // State Moving
        else if (elevator.getState() == ElevatorState.MOVING) {
        	
        	if (elevator.getCurrentDirection() == Direction.MOVING_UP) {
        	
        	} else if (elevator.getCurrentDirection() == Direction.MOVING_DOWN && elevator.getCurrentFloor().getNumber() != 1) {
        		        		
        		elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() - 1)); 
        	}
          
        	// if Elevator floor requests contains the next floor or next floor pressed the same direction 
            if (mDestination.getNumber() == elevator.getCurrentFloor().getNumber() || elevator.getCurrentFloor().directionIsPressed(elevator.getCurrentDirection())) {
            	
            	elevator.scheduleStateChange(ElevatorState.DECELERATING, 2);

            } else {
            	            	
            	elevator.scheduleStateChange(ElevatorState.MOVING, 2);
            }
        }            
	
		
	}
	
}
