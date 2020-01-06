package solution;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.CSS;

import baseclasses.Aircraft;
import baseclasses.CabinCrew;
import baseclasses.Crew;
import baseclasses.DoubleBookedException;
import baseclasses.FlightInfo;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.InvalidAllocationException;
import baseclasses.Pilot;
import baseclasses.QualityScoreCalculator;
import baseclasses.Route;
import baseclasses.Schedule;
import baseclasses.SchedulerRunner;

public class Scheduler implements IScheduler {

	@Override
	public Schedule generateSchedule(IAircraftDAO aircraft, ICrewDAO crew, IRouteDAO route, IPassengerNumbersDAO passenger,
			LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		
		Schedule s = new Schedule(route, startDate, endDate);
		s.sort();
				
		List<Aircraft> aircrafts = aircraft.getAllAircraft();
		List<Crew> crewMember = crew.getAllCrew();
		List<Route> routes = route.getAllRoutes();
		List<CabinCrew> c;
				
		Random rand = new Random(); 
		int passes = 0;
		int lastRemaining = 0;
		Boolean needsReset = false;
		
		int maxPasses = 999999999; 
		
		
		while(!s.isCompleted()) {
			
			if(s.getRemainingAllocations().size() == lastRemaining) {
				passes++;
			} else {
				lastRemaining = s.getRemainingAllocations().size();
				passes = 0;
			}
			
			if(passes>maxPasses) {
				passes = 0;
				needsReset = true;
			}
			
			if(needsReset) {
				s = new Schedule(route, startDate, endDate);
				needsReset = false;
			}
			
			List<FlightInfo> unallocatedFlights = s.getRemainingAllocations();

			for(FlightInfo flight : unallocatedFlights) {
				Route r = flight.getFlight();
							

				try {
					s.allocateAircraftTo(aircrafts.get(rand.nextInt(aircrafts.size())), flight);
				} catch (DoubleBookedException e1) {
			
				}
	
				try {
					s.allocateCabinCrewTo(crew.getAllCabinCrew().get(rand.nextInt(crew.getAllCabinCrew().size())), flight);
				} catch (DoubleBookedException e1) {
			
				}
				
				try {
					s.allocateCaptainTo(crew.getAllPilots().get(rand.nextInt(crew.getAllPilots().size())), flight);
				} catch (DoubleBookedException e1) {
				
				}
			
				try {
					s.allocateFirstOfficerTo(crew.getAllPilots().get(rand.nextInt(crew.getAllPilots().size())), flight);
				} catch (DoubleBookedException e1) {
				
				}
				
				
				try {
					s.completeAllocationFor(flight);
				} catch (InvalidAllocationException e) {
					
				}
			}
			System.out.println(s.getCompletedAllocations().size() + " Done | Left: " + s.getRemainingAllocations().size());
		}
		
		QualityScoreCalculator score = new QualityScoreCalculator(aircraft, crew, passenger, s);
						
		System.out.println("\nFinished Loading Schedule! With Score : " + score.calculateQualityScore());
		
		for(String des : score.describeQualityScore()) {
			System.out.println(des);
		}
		
		System.out.println(s.getCompletedAllocations().size() + " Done | Left: " + s.getRemainingAllocations().size());
		
		return s;
	}

	@Override
	public void setSchedulerRunner(SchedulerRunner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
