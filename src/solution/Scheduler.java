package solution;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import baseclasses.*;

public class Scheduler implements IScheduler {

	@Override
	public Schedule generateSchedule(IAircraftDAO aircraft, 
			ICrewDAO crew, 
			IRouteDAO routes, 
			IPassengerNumbersDAO passengers,
			LocalDate startDate, 
			LocalDate endDate) {
		
		Schedule s = new Schedule(routes, startDate, endDate);
		
		List<Aircraft> aeroplanes = aircraft.getAllAircraft();
		List<CabinCrew> ccrew = crew.getAllCabinCrew();
		List<Pilot> pilots = crew.getAllPilots();
		List<Route> routesList = routes.getAllRoutes();
		
		List<FlightInfo> flights = s.getRemainingAllocations();
		
		for(FlightInfo f: flights)
		{
			Random rand = new Random(); 
			while(!s.isCompleted())
			{
				try {
					s.allocateAircraftTo(aeroplanes.get(rand.nextInt(aircraft.getNumberOfAircraft())), f);
				}
				catch (DoubleBookedException e) {
					e.printStackTrace();
				}
			}
			while(!s.isCompleted())
			{
				try {
					s.allocateCabinCrewTo(ccrew.get(rand.nextInt(crew.getNumberOfCabinCrew())), f);
				}
				catch (DoubleBookedException e) {
					e.printStackTrace();
				}
			}
			while(!s.isCompleted())
			{
				try {
					s.allocateFirstOfficerTo(pilots.get(rand.nextInt(crew.getNumberOfPilots())), f);
				}
				catch (DoubleBookedException e) {
					e.printStackTrace();
				}
			}
			while(!s.isCompleted())
			{
				try {
					s.allocateCaptainTo(pilots.get(rand.nextInt(crew.getNumberOfPilots())), f);
				}
				catch (DoubleBookedException e) {
					e.printStackTrace();
				}
			} 
			try {
				s.completeAllocationFor(f);
			} catch (InvalidAllocationException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	@Override
	public void setSchedulerRunner(SchedulerRunner arg0) {
		

	}

	@Override
	public void stop() {
		

	}

}
