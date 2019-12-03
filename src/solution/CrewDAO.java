package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import baseclasses.CabinCrew;
import baseclasses.Crew;
import baseclasses.DataLoadingException;
import baseclasses.ICrewDAO;
import baseclasses.Pilot;
import baseclasses.Pilot.Rank;

/**
 * The CrewDAO is responsible for loading data from JSON-based crew files It
 * contains various methods to help the scheduler find the right pilots and
 * cabin crew
 */
public class CrewDAO implements ICrewDAO {

	List<Crew> Crew = new ArrayList<>();
	List<CabinCrew> CabinCrew = new ArrayList<>();
	List<Pilot> Pilots = new ArrayList<>();

	/**
	 * Loads the crew data from the specified file, adding them to the currently loaded crew
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadCrewData(Path p) throws DataLoadingException {
		
		Pilot temp = new Pilot();
		CabinCrew tempCC = new CabinCrew();
		
		try {
			BufferedReader br = Files.newBufferedReader(p);
			String json=""; String line = "";
			while((line = br.readLine()) != null) {json=json+line;}
			
			JSONObject root = new JSONObject(json);
			
			JSONArray pilots = root.getJSONArray("pilots");
			JSONArray cabinCrew = root.getJSONArray("cabincrew");
			
			for(int i=0;i<pilots.length(); i++)
			{
				JSONObject pilot = pilots.getJSONObject(i);
				
				try
				{
					temp.setForename(pilot.get("forename").toString());
					temp.setSurname(pilot.get("surname").toString());
					temp.setRank(Rank.valueOf(pilot.get("rank").toString().toUpperCase()));
					temp.setHomeBase(pilot.get("homebase").toString());
					
					JSONArray pilotTypeRating = pilot.getJSONArray("typeRatings");
					for(int j=0;j<pilotTypeRating.length(); j++)
					{
						temp.setQualifiedFor(pilotTypeRating.get(j).toString());
					}
			
					Pilots.add(temp);
					temp = new Pilot();
				}
				catch(Exception e)
				{
					throw new DataLoadingException();
				}
			}
			

			for(int k=0;k<pilots.length(); k++)
			{
				JSONObject cabcrew = cabinCrew.getJSONObject(k);
				JSONArray qualifiedFor = cabcrew.getJSONArray("typeRatings");
				try
				{
					tempCC.setForename(cabcrew.get("forename").toString());
					tempCC.setSurname(cabcrew.get("surname").toString());
					tempCC.setHomeBase(cabcrew.get("homebase").toString());
					
					
					for(int j=0;j<qualifiedFor.length(); j++)
					{
						tempCC.setQualifiedFor(qualifiedFor.get(j).toString());
					}
					
					CabinCrew.add(tempCC);
					tempCC = new CabinCrew();
				}
				catch(Exception e)
				{
					throw new DataLoadingException();
				}
			}
		} 
		catch (IOException e) {
			throw new DataLoadingException();
		}
	}

	/**
	 * Returns a list of all the cabin crew based at the airport with the specified
	 * airport code
	 * 
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at the airport with the specified
	 *         airport code
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBase(String airportCode) {
		List<CabinCrew> ccHome = new ArrayList<>();
		
		for(CabinCrew c : CabinCrew)
		{
			if(c.getHomeBase().equals(airportCode))
			{
				ccHome.add(c); 
			}
		}

		return ccHome;
	}

	/**
	 * Returns a list of all the cabin crew based at a specific airport AND
	 * qualified to fly a specific aircraft type
	 * 
	 * @param typeCode    the type of plane to find cabin crew for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the cabin crew based at a specific airport AND
	 *         qualified to fly a specific aircraft type
	 */
	@Override
	public List<CabinCrew> findCabinCrewByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		List<CabinCrew> ccHomeAndType = new ArrayList<>();
		
		for(CabinCrew c : CabinCrew)
		{
			for(String type : c.getTypeRatings())
			{
				if(c.getHomeBase().equals(airportCode) && type.equals(typeCode))
				{
					ccHomeAndType.add(c); 
				}
			}
		}

		return ccHomeAndType;
	}

	/**
	 * Returns a list of all the cabin crew currently loaded who are qualified to
	 * fly the specified type of plane
	 * 
	 * @param typeCode the type of plane to find cabin crew for
	 * @return a list of all the cabin crew currently loaded who are qualified to
	 *         fly the specified type of plane
	 */
	@Override
	public List<CabinCrew> findCabinCrewByTypeRating(String typeCode) {
		List<CabinCrew> ccType = new ArrayList<>();
		
		for(CabinCrew c : CabinCrew)
		{
			for(String type : c.getTypeRatings())
			{
				if(type.equals(typeCode))
				{
					ccType.add(c); 
				}
			}
		}
		return ccType;
	}

	/**
	 * Returns a list of all the pilots based at the airport with the specified
	 * airport code
	 * 
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at the airport with the specified
	 *         airport code
	 */
	@Override
	public List<Pilot> findPilotsByHomeBase(String airportCode) {
		List<Pilot> byHome = new ArrayList<>();
		
		for(Pilot p : Pilots)
		{
			if(p.getHomeBase().equals(airportCode))
			{
				byHome.add(p); 
			}
		}
		return byHome;
	}

	/**
	 * Returns a list of all the pilots based at a specific airport AND qualified to
	 * fly a specific aircraft type
	 * 
	 * @param typeCode    the type of plane to find pilots for
	 * @param airportCode the three-letter airport code of the airport to check for
	 * @return a list of all the pilots based at a specific airport AND qualified to
	 *         fly a specific aircraft type
	 */
	@Override
	public List<Pilot> findPilotsByHomeBaseAndTypeRating(String typeCode, String airportCode) {
		List<Pilot> byHomeAndType = new ArrayList<>();
		
		for(Pilot p : Pilots)
		{
			for(String type : p.getTypeRatings())
			{
				if(p.getHomeBase().equals(airportCode) && type.equals(typeCode))
				{
					byHomeAndType.add(p); 
				}
			}
		}

		return byHomeAndType;
	}

	/**
	 * Returns a list of all the pilots currently loaded who are qualified to fly
	 * the specified type of plane
	 * 
	 * @param typeCode the type of plane to find pilots for
	 * @return a list of all the pilots currently loaded who are qualified to fly
	 *         the specified type of plane
	 */
	@Override
	public List<Pilot> findPilotsByTypeRating(String typeCode) {
		
		List<Pilot> byType = new ArrayList<>();
		
		for(Pilot p : Pilots)
		{
			for(String type: p.getTypeRatings())
			{
				if(type.equals(typeCode))
				{
					byType.add(p); 
				}
			}
		}

		return byType;
	}

	/**
	 * Returns a list of all the cabin crew currently loaded
	 * 
	 * @return a list of all the cabin crew currently loaded
	 */
	@Override
	public List<CabinCrew> getAllCabinCrew() {
		return CabinCrew;
	}

	/**
	 * Returns a list of all the crew, regardless of type
	 * 
	 * @return a list of all the crew, regardless of type
	 */
	@Override
	public List<Crew> getAllCrew() {
		Crew.addAll(Pilots);
		Crew.addAll(CabinCrew);
		return Crew;
	}

	/**
	 * Returns a list of all the pilots currently loaded
	 * 
	 * @return a list of all the pilots currently loaded
	 */
	@Override
	public List<Pilot> getAllPilots() {
		return Pilots;
	}

	@Override
	public int getNumberOfCabinCrew() {
		
		return CabinCrew.size();
	}

	/**
	 * Returns the number of pilots currently loaded
	 * 
	 * @return the number of pilots currently loaded
	 */
	@Override
	public int getNumberOfPilots() {
		return Pilots.size();
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		Crew = new ArrayList<>();
		CabinCrew = new ArrayList<>();
		Pilots = new ArrayList<>();
	}

}
