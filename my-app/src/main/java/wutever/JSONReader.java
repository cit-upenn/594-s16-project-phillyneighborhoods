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

	public GoogleJSONReader(String input){
		GoogleJSONReader.url = input;
	}
	
	public static String getURL(){
		return url;
	}

	  private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	  
	  public static String parse() throws JSONException, IOException{
		JSONObject json = readJsonFromUrl(getURL());

		String s =  json.toString();
		System.out.println(s.length());
		
		//get index of "location"
		int locationPosition = s.indexOf("location"); 
			System.out.println(locationPosition);
		
		//get index of "location_type"
		int locationTypePosition = s.indexOf("location_type"); 
		System.out.println(locationTypePosition);
		
		//get substring, e.g. location":{"lng":-75.18663959999999,"lat":39.9396284},"
		String latitudeLongitude = s.substring(locationPosition, locationTypePosition); 

		return latitudeLongitude;
	  }
	  
//	  public static void main(String[] args) throws IOException, JSONException {
//		
//
//	  	parse(); 
//	  }
	

}
