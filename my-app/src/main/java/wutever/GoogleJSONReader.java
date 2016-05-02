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

	private static String url = "http://maps.googleapis.com/maps/api/geocode/json?address=19146";
//	String temp = "http://maps.googleapis.com/maps/api/geocode/json?address=19146";

	/**
	 * Constructor 
	 * @param input
	 */
	public GoogleJSONReader(String input){
		GoogleJSONReader.url = input;
	}
	
	/**
	 * Getter method for URL
	 * @return url
	 */
	public static String getURL(){
		return url;
	}

	/**
	 * StringBuilder class reads
	 * @param rd
	 * @return stringBuilder.toString()
	 * @throws IOException
	 */
	 private static String readAll(Reader reader) throws IOException {
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
	  private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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
	  private static String parse() throws JSONException, IOException{
		JSONObject json = readJsonFromUrl(getURL());

		String s =  json.toString();
		//System.out.println(s.length());
		
		//get index of "location"
		int firstDelimiter = s.indexOf("location"); 
//			System.out.println(firstDelimiter);
		
		//get index of "location_type"
		int secondDelimiter = s.indexOf("location_type"); 
//		System.out.println(secondDelimiter);
		
		//get substring, e.g. location":{"lng":-75.18663959999999,"lat":39.9396284},"
		String latitudeLongitude = s.substring(firstDelimiter, secondDelimiter); 
		
		//format substring
		int thirdDelimiter = s.indexOf("lng"); 
		int fourthDelimiter = s.indexOf("}"); 
		latitudeLongitude = s.substring(thirdDelimiter, fourthDelimiter); 
		latitudeLongitude = latitudeLongitude.replace("\"", "");
		latitudeLongitude = latitudeLongitude.replace(":", "=");
		
//		System.out.println(latitudeLongitude);
		return latitudeLongitude;
	  }
	  
	  public static String getLatitudeLongitude() throws JSONException, IOException{
		  String latlong = parse();
		  return latlong;
	  }
	  
//	  public static void main(String[] args) throws IOException, JSONException {
//		
//
//	  	parse(); 
//	  }
	

}
