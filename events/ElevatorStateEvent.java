package cecs277.events;

import cecs277.Simulation;
import cecs277.elevators.Elevator;

/**
 * A simulation event that sets an elevator's state and calls its tick() method.
 */
public class ElevatorStateEvent extends SimulationEvent {
	private Elevator.ElevatorState mNewState;
	private Elevator mElevator;
	
	public ElevatorStateEvent(long scheduledTime, Elevator.ElevatorState newState, Elevator elevator, int priority) {
		super(scheduledTime, priority);
		mNewState = newState;
		mElevator = elevator;
	}
	@Override
	public void execute(Simulation sim) {
		mElevator.setState(mNewState);
		mElevator.tick();
	}
	
	@Override
	public String toString() {
		
		// PROJECT 3: Logger
		return "" + mElevator;
		
	}
}
