package cecs277.passengers;

import java.util.Random;

import cecs277.Simulation;

public class JerkFactory implements PassengerFactory {
	private int weight;
	
	public JerkFactory() {
		this.weight = 2;
	}
	
	public JerkFactory(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String factoryName() {

		return "Jerk";
	}

	@Override
	public String shortName() {

		return "J";
	}

	@Override
	public int factoryWeight() {

		return this.weight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
				
		BoardingStrategy cb = new CapacityBoarding();
		
		return cb;
		
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		
		Random r = simulation.getRandom();

		int destination = (int)(r.nextInt(simulation.getBuilding().getFloorCount() - 1)) + 2;
				
		int duration = (int) (r.nextGaussian() * 1200 + 3600);
		
		TravelStrategy sdt = new SingleDestinationTravel(destination, duration);
		
		return sdt;
	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		
		EmbarkingStrategy de = new DisruptiveEmbarking();
		
		return de;
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		
		DebarkingStrategy at = new AttentiveDebarking();
		
		return at;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return this.weight;
	}
	
}
