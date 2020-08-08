package cecs277.elevators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cecs277.buildings.Building;
import cecs277.buildings.Floor;
import cecs277.elevators.Elevator.Direction;
import cecs277.elevators.Elevator.ElevatorState;

/**
 * An ActiveMode elevator is handling at least one floor request.
 */
public class ActiveMode implements OperationMode {
	
	// TODO: implement this class.
	// An active elevator cannot be dispatched, and will ignore direction requests from its current floor. (Only idle
	//    mode elevators observe floors, so an ActiveMode elevator will never observe directionRequested.)
	// The bulk of your Project 2 tick() logic goes here, except that you will never be in IDLE_STATE when active.
	// If you used to schedule a transition to IDLE_STATE, you should instead schedule an operation change to
	//    IdleMode in IDLE_STATE.
	// Otherwise your code should be almost identical, except you are no longer in the Elevator class, so you need
	//    to use accessors and mutators instead of directly addressing the fields of Elevator.

	@Override
	public String toString() {
		return "Active";
	}

	// Active elevators cannot be dispatched to floors
	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		// TODO Auto-generated method stub
		return false;
	}

	// Active elevators cannot be dispatched to floors
	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Direction targetDirection) {
		// TODO Auto-generated method stub
		
	}

	// Active elevators will never observe directionRequested
	@Override
	public void directionRequested(Elevator elevator, Floor floor, Direction direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(Elevator elevator) {

		// State DOORS OPEN
        if (elevator.getState() == ElevatorState.DOORS_OPEN) {
        	// Number of passengers waiting in the elevator and on the floor
        	int numPassElevBefore = elevator.getPassengerCount();
        	int numPassFloorBefore = elevator.getCurrentFloor().getWaitingPassengers().size();
        	
        	// Notify all observers by triggering the elevatorDoorsOpened() method
        	List<ElevatorObserver> temp = new ArrayList<>(elevator.getObservers());
        	for (ElevatorObserver i : temp) {
        		i.elevatorDoorsOpened(elevator);
        	}
        	
        	// Transition to DOORS_CLOSING in 1 + x sec where x is half of the passenger count rounded down
        	int numPassElevAfter = elevator.getPassengerCount();
        	int numPassFloorAfter = elevator.getCurrentFloor().getWaitingPassengers().size();
        	
        	int numPassJoinedElev = numPassFloorBefore - numPassFloorAfter;
        	int numPassLeftElev = numPassJoinedElev + numPassElevBefore - numPassElevAfter;
        	
        	elevator.scheduleStateChange(ElevatorState.DOORS_CLOSING, (int)(Math.floor(numPassJoinedElev + numPassLeftElev) / 2) + 1);
        }

        // State Doors_Opening
        else if (elevator.getState() == ElevatorState.DOORS_OPENING) {
        	elevator.scheduleStateChange(ElevatorState.DOORS_OPEN, 2);

        }
        
        // State DOORS CLOSING
        else if (elevator.getState() == ElevatorState.DOORS_CLOSING) {
        	// If there is another request in our direction

        	if ((elevator.nextRequestUp(elevator.getCurrentFloor().getNumber()) != -1 && elevator.getCurrentDirection() == Direction.MOVING_UP) 
        			|| (elevator.nextRequestDown(elevator.getCurrentFloor().getNumber()) != -1 && elevator.getCurrentDirection() == Direction.MOVING_DOWN)) { 
        		elevator.scheduleStateChange(ElevatorState.ACCELERATING, 2);
       		
        	} else if (elevator.getCurrentDirection() == Direction.MOVING_UP && elevator.nextRequestUp(elevator.getCurrentFloor().getNumber()) == -1 && 
        			elevator.nextRequestDown(elevator.getCurrentFloor().getNumber()) != -1) {

        		elevator.setCurrentDirection(Direction.MOVING_DOWN);
            	elevator.scheduleStateChange(ElevatorState.DOORS_OPENING, 2);
            		
        	} else if (elevator.getCurrentDirection() == Direction.MOVING_DOWN && elevator.nextRequestDown(elevator.getCurrentFloor().getNumber()) == -1 && 
        			elevator.nextRequestUp(elevator.getCurrentFloor().getNumber()) != -1) {
        		elevator.setCurrentDirection(Direction.MOVING_UP);
        		elevator.scheduleStateChange(ElevatorState.DOORS_OPENING, 2);

        	} else if (elevator.getRequestedFloor().isEmpty()) {
        		elevator.setCurrentDirection(Direction.NOT_MOVING);
        		
        		// PROJECT 3: 10.4
        		elevator.scheduleModeChange(new IdleMode(), ElevatorState.IDLE_STATE, 2); 
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
        	
        	// "Clear" the current floor from the requested floor list
        	if (!elevator.getRequestedFloor().isEmpty() && elevator.getRequestedFloor().contains(elevator.getCurrentFloor().getNumber())) {

        		int a = elevator.getRequestedFloor().indexOf(elevator.getCurrentFloor().getNumber());
        		
        		elevator.getRequestedFloor().remove(a);
        		
        	}
           
            if (elevator.getCurrentFloor().directionIsPressed(elevator.getCurrentDirection()) || (elevator.nextRequestUp(elevator.getCurrentFloor().getNumber()) != -1 && elevator.getCurrentDirection() == Direction.MOVING_UP)
            		|| (elevator.nextRequestDown(elevator.getCurrentFloor().getNumber()) != -1 && elevator.getCurrentDirection() == Direction.MOVING_DOWN)) {
            	elevator.setCurrentDirection(elevator.getCurrentDirection());
            	
            } else if ((elevator.getCurrentDirection() == Direction.MOVING_UP && elevator.nextRequestUp(elevator.getCurrentFloor().getNumber()) == -1 )
            		&& !elevator.checkLeaving()) { 
            	
            	elevator.setCurrentDirection(Direction.MOVING_DOWN);
            	
            } else if ((elevator.getCurrentDirection() == Direction.MOVING_DOWN && elevator.nextRequestDown(elevator.getCurrentFloor().getNumber()) == -1 )
            		&& !elevator.checkLeaving()) { 
            	
        		elevator.setCurrentDirection(Direction.MOVING_UP);
        		
            } else {

            	elevator.setCurrentDirection(Direction.NOT_MOVING);
            }
            // Alert all observers and change to DOORS_OPENING
            List<ElevatorObserver> temp = new ArrayList<>(elevator.getObservers());
            for (ElevatorObserver i : temp) {
                i.elevatorDecelerating(elevator);
            }              
            elevator.scheduleStateChange(ElevatorState.DOORS_OPENING, 3);

        }
            
        // State Moving
        else if (elevator.getState() == ElevatorState.MOVING) {
        	
        	if (elevator.getCurrentDirection() == Direction.MOVING_UP) {
        		elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() + 1)); 
        		
        	} else if (elevator.getCurrentDirection() == Direction.MOVING_DOWN && elevator.getCurrentFloor().getNumber() != 1) {
        		elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() - 1)); 
        	}
            // if Elevator floor requests contains the next floor or next floor pressed the same direction 
            if (elevator.checkLeaving() || elevator.getCurrentFloor().directionIsPressed(elevator.getCurrentDirection()) || elevator.getRequestedFloor().contains(elevator.getCurrentFloor().getNumber())) {

            	elevator.scheduleStateChange(ElevatorState.DECELERATING, 2);

            } else {

            	elevator.scheduleStateChange(ElevatorState.MOVING, 2);

            }
        }            
	
	}
}
