package wutever;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author brittanybinler
 *
 */
public class GoogleJSONReader {
	
	private static final String stub = "http://maps.googleapis.com/maps/api/geocode/json?address=";
   private static GoogleJSONReader instance = new GoogleJSONReader();
   static String url = "";

   /**
    * Constructor
    */
   private GoogleJSONReader(){
	   
   }

   /**
    * Getter method for instance of JSON Reader
    * @return instance
    */
   public static GoogleJSONReader getInstance(){
      return instance;
   }
	
	/**
	 * Getter method for stub
	 * @return stub
	 */
	public static String getURL(){
		return url;
	}
	
	/**
	 * Create URL
	 * @param zipcode
	 */
	private static synchronized String createURL(String zipcode){
		url = stub + zipcode;
		return url;
	}

	/**
	 * StringBuilder class reads
	 * @param rd
	 * @return stringBuilder.toString()
	 * @throws IOException
	 */
	 private static synchronized String readAll(Reader reader) throws IOException {
	    StringBuilder stringBuilder = new StringBuilder();
	    int next;
	    while ((next = reader.read()) != -1) {
	    	stringBuilder.append((char) next);
	    }
	    return stringBuilder.toString();
	 }

	 /**
	  * Reads JSON from a given URL, entered as a String
	  * @param url
	  * @return json
	  * @throws IOException
	  * @throws JSONException
	  */
	  private static synchronized JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream inputStream = new URL(url).openStream();
	    
	    try {
	      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
	      String jsonText = readAll(reader);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	    	inputStream.close();
	    }
	  }
	  
	  /**
	   * Retrieves and formats latitude and longitude from JSON
	   * @return latitudeLongitude
	   * @throws JSONException
	   * @throws IOException
	   */
	  private static synchronized String parse() throws JSONException, IOException{
		JSONObject json = readJsonFromUrl(getURL());
		
		String s =  json.toString();
		//System.out.println(json);
		
		if(json.get("status") != "ZERO_RESULTS"){
			

			//get index of "location"
			int firstDelimiter = s.indexOf("location"); 
//				System.out.println(firstDelimiter);
			
			//get index of "location_type"
			int secondDelimiter = s.indexOf("location_type"); 
//			System.out.println(secondDelimiter);
			
			//get substring, e.g. location":{"lng":-75.18663959999999,"lat":39.9396284},"
			String latitudeLongitude = s.substring(firstDelimiter, secondDelimiter); 
			
			//format substring
			int thirdDelimiter = s.indexOf("lng"); 
			int fourthDelimiter = s.indexOf("}"); 
			latitudeLongitude = s.substring(thirdDelimiter, fourthDelimiter); 
			latitudeLongitude = latitudeLongitude.replace("\"", "");
			latitudeLongitude = latitudeLongitude.replace(":", "=");
			//System.out.println(latitudeLongitude);
			return latitudeLongitude;
		}
		
		System.out.println("No latitude/longitude found.");
		return null;
	  }
	  
	  public synchronized String getLatitudeLongitude(String zipcode) throws JSONException, IOException{
		  createURL(zipcode);
		  String latlong = parse();
		  return latlong;
	  }
	  
//	  public static void main(String[] args) throws IOException, JSONException {
//	  	parse(); 
//	  }
	

}
