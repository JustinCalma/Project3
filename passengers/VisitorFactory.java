package cecs277.passengers;

import java.util.Random;

import cecs277.Simulation;

public class VisitorFactory implements PassengerFactory {
	private int weight;
	
	public VisitorFactory() {
		this.weight = 10;
	}
	
	public VisitorFactory(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String factoryName() {

		return "Visitor";
	}

	@Override
	public String shortName() {

		return "V";
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
		
		EmbarkingStrategy re = new ResponsibleEmbarking();
		
		return re;
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
