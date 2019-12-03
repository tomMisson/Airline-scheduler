package solution;

import java.nio.file.Paths;

import baseclasses.DataLoadingException;
import baseclasses.*;

/**
 * This class allows you to run the code in your classes yourself, for testing and development
 */
public class Main {

	public static void main(String[] args) {	
		IAircraftDAO aircraft = new AircraftDAO();
		IRouteDAO routes = new RouteDAO();
		ICrewDAO crew = new CrewDAO();
		
		try {
			aircraft.loadAircraftData(Paths.get("./data/aircraft.csv"));
			routes.loadRouteData(Paths.get("./data/routes.xml"));
			crew.loadCrewData(Paths.get("./data/crew.json"));
		}
		catch (DataLoadingException dle) {
			System.err.println("Error loading aircraft data");
			dle.printStackTrace();
		}
	}

}