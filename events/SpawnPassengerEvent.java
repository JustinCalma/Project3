package cecs277.events;

import cecs277.buildings.Building;
import cecs277.passengers.*;
import cecs277.Simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A simulation event that adds a new random passenger on floor 1, and then schedules the next spawn event.
 */
public class SpawnPassengerEvent extends SimulationEvent {
	private static long SPAWN_MEAN_DURATION = 10_800;
	private static long SPAWN_STDEV_DURATION = 3_600;

	// After executing, will reference the Passenger object that was spawned.
	private Passenger mPassenger;
	private Building mBuilding;
	
	public SpawnPassengerEvent(long scheduledTime, Building building, int priority) {
		super(scheduledTime, priority);
		mBuilding = building;
	}
	
	@Override
	public String toString() {
		
		// PROJECT 3: Logger
		return "Adding " + mPassenger.getName() + " " + mPassenger.getId() + " [-> " + mPassenger.getTravelStrategy().getDestination() + "] from floor 1.";

	}
	
	@Override
	public void execute(Simulation sim) {
		Random rand = mBuilding.getSimulation().getRandom();
		
		// PROJECT 3: Spawning Passengers
		int total = 0;
		for(PassengerFactory pf : sim.getPassengerFactories()) {
			total += pf.factoryWeight();
		}
		
		int r = rand.nextInt(total);
		
		int sum = 0;
		int index = 0;
		for (int i = 0; i < sim.getPassengerFactories().size(); i++) {
			sum = sim.getPassengerFactories().get(i).factoryWeight() + sum;
			if (r < sum) {
				index = i;
				break;
			}
		}
		
		PassengerFactory pf = sim.getPassengerFactories().get(index);
		
		TravelStrategy ts = pf.createTravelStrategy(sim);
		BoardingStrategy bs = pf.createBoardingStrategy(sim);
		EmbarkingStrategy eb = pf.createEmbarkingStrategy(sim);
		DebarkingStrategy ds = pf.createDebarkingStrategy(sim);
		
		mPassenger = new Passenger(pf.factoryName(), pf.shortName(), ts, bs, eb, ds);
		
		mBuilding.getFloor(1).addWaitingPassenger(mPassenger);

		/*
		 TODO: schedule the new SpawnPassengerEvent with the simulation. Construct a new SpawnPassengerEvent
		 with a scheduled time that is X seconds in the future, where X is a uniform random integer from
		 1 to 30 inclusive.
		*/
				
		int schedTime = (int)(rand.nextInt(30) + 1);
		
		SpawnPassengerEvent SPE = new SpawnPassengerEvent(schedTime + sim.currentTime(), this.mBuilding, 3);
		
		sim.scheduleEvent(SPE);
		
	}
	
	
	private Passenger getVisitor() {
		/*
		 TODO: construct a VisitorPassenger and return it.
		 The visitor should have a random destination floor that is not floor 1 (generate a random int from 2 to N).
		 The visitor's visit duration should follow a NORMAL (GAUSSIAN) DISTRIBUTION with a mean of 1 hour
		 and a standard deviation of 20 minutes.
		 */
		Random r = mBuilding.getSimulation().getRandom();
		// Look up the documentation for the .nextGaussian() method of the Random class.

		int destination = (int)(r.nextInt(mBuilding.getFloorCount() - 1)) + 2;
				
		int duration = (int) (r.nextGaussian() * 1200 + 3600);
						
		TravelStrategy sdt = new SingleDestinationTravel(destination, duration);
		
		BoardingStrategy cb = new CapacityBoarding();
		
		EmbarkingStrategy re = new ResponsibleEmbarking();
		
		DebarkingStrategy at = new AttentiveDebarking();
		
		Passenger visitor = new Passenger("Visitor", "V", sdt, cb, re, at);

		return visitor; // Return the constructed VistorPassenger
		}
	
	private Passenger getWorker() {
		/*
		TODO: construct and return a WorkerPassenger. A Worker requires a list of destinations and a list of durations.
		To generate the list of destinations, first generate a random number from 2 to 5 inclusive. Call this "X",
		how many floors the worker will visit before returning to floor 1.
		X times, generate an integer from 2 to N (number of floors) that is NOT THE SAME as the previously-generated floor.
		Add those X integers to a list.
		To generate the list of durations, generate X integers using a NORMAL DISTRIBUTION with a mean of 10 minutes
		and a standard deviation of 3 minutes.
		 */
		Random r = mBuilding.getSimulation().getRandom();
		
		// "X" the amount of floors the worker will visit before returning to floor 1
		int x = (int)(r.nextInt(5) + 2);
		
		//Creating the ArrayList for destinations
		ArrayList<Integer> destinations = new ArrayList<>();
		// Generate an int from 2 to N that is not the same as the previous generated num
		for (int i = 0; i < x; i++) {
			int prevDestination = 0;
			int randDestination = (int)(r.nextInt(mBuilding.getFloorCount() - 1) + 2);
			// Keep generating random num until it isn't the same as the previous num
			while(randDestination == prevDestination) {
				randDestination = (int)(r.nextInt(mBuilding.getFloorCount() - 1) + 2);
			}
			// Set new prevDestination for next iteration
			prevDestination = randDestination;
			// Adding randDestination into destinations ArrayList
			destinations.add(randDestination);
		}
		
		// Creating the ArrayList for duration
		ArrayList<Long> durations = new ArrayList<>();
		// Generate the list of durations
		for (int i = 0; i < x; i++) {
			int duration = (int)(r.nextGaussian() * 180 + 600);
			durations.add((long) duration);
		}
		
		TravelStrategy ts = new MultipleDestinationTravel(destinations, durations);
		
		BoardingStrategy bs = new ThresholdBoarding(3);
		
		EmbarkingStrategy eb = new ResponsibleEmbarking();
		
		DebarkingStrategy ds = new AttentiveDebarking();
		
		
		Passenger worker = new Passenger("Worker", "W", ts, bs, eb, ds);
		
		return worker;
	}
}
