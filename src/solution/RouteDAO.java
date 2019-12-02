package solution;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import baseclasses.DataLoadingException;
import baseclasses.IRouteDAO;
import baseclasses.Route;

/**
 * The RouteDAO parses XML files of route information, each route specifying
 * where the airline flies from, to, and on which day of the week
 */
public class RouteDAO implements IRouteDAO {

	List<Route> routes = new ArrayList<Route>();
	
	/**
	 * Finds all flights that depart on the specified day of the week
	 * @param dayOfWeek A three letter day of the week, e.g. "Tue"
	 * @return A list of all routes that depart on this day
	 */
	@Override
	public List<Route> findRoutesByDayOfWeek(String dayOfWeek) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Finds all of the flights that depart from a specific airport on a specific day of the week
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @param dayOfWeek the three letter day of the week code to searh for, e.g. "Tue"
	 * @return A list of all routes from that airport on that day
	 */
	@Override
	public List<Route> findRoutesByDepartureAirportAndDay(String airportCode, String dayOfWeek) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Finds all of the flights that depart from a specific airport
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @return A list of all of the routes departing the specified airport
	 */
	@Override
	public List<Route> findRoutesDepartingAirport(String airportCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Finds all of the flights that depart on the specified date
	 * @param date the date to search for
	 * @return A list of all routes that dpeart on this date
	 */
	@Override
	public List<Route> findRoutesbyDate(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns The full list of all currently loaded routes
	 * @return The full list of all currently loaded routes
	 */
	@Override
	public List<Route> getAllRoutes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns The number of routes currently loaded
	 * @return The number of routes currently loaded
	 */
	@Override
	public int getNumberOfRoutes() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Loads the route data from the specified file, adding them to the currently loaded routes
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadRouteData(Path arg0) throws DataLoadingException {
		
		Route temp = new Route();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(arg0.toString());
			Element root = doc.getDocumentElement();
			
			NodeList routesList = root.getElementsByTagName("Route");
			
			for(int i=0; i<routesList.getLength(); i++)
			{
				NodeList route = routesList.item(i).getChildNodes();
				
				for(int j=0; j<route.getLength(); j++)
				{
					try {
						System.out.println(route.item(j).getTextContent());
//						switch(route.item(j).getNodeName()) {
//							case "FlightNumber":
//								System.out.println(Integer.parseInt(route.item(j).getTextContent()));
//						
//							case "DayOfWeek":
//								System.out.println(route.item(j).getTextContent());
//								
//							case "DepartureTime":
//								System.out.println(LocalTime.parse(route.item(j).getTextContent(), formatter));
//								
//							case "DepartureAirportCode":
//								System.out.println(route.item(j).getTextContent());
//								
//							case "DepartureAirport":
//								System.out.println(route.item(j).getTextContent());
//								
//							case "ArrivalTime":
//								System.out.println(LocalTime.parse(route.item(j).getTextContent()));
//								
//							case "ArrivalAirport":
//								System.out.println(route.item(j).getTextContent());
//						
//							case "ArrivalAirportCode":
//								System.out.println(route.item(j).getTextContent());
//						
//							case "Duration":
//								System.out.println(java.time.Duration.parse(route.item(j).getTextContent()));
//						}
					}
					catch(Exception e)
					{
						System.err.println(e);
					}
					
					routes.add(temp);
					temp = new Route();
				}
			}
		}
		catch(ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println("Error Parsing XML: "+e);
		}
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
