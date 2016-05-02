/**
 * 
 */
package wutever;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author brittanybinler
 *
 */
public class InstagramJSONReader {
//	static String url = "http://maps.googleapis.com/maps/api/geocode/json?address=19146";
	static String access_token = "6468350.1677ed0.51442ec921ae44989cf30c72887d0cc0";
	
	
	static String url;
	
	/**
	 * Getter method for String url
	 * @return url
	 */
	public static String getURL(){
		return url;
	}
	
	/**
	 * Setter method for String url
	 * @param input
	 */
	public static void setURL(String input){
		url = input;
	}
	
	/**
	 * Creates Instagram API URL using access token, latitude, longitude, and media search endpoint
	 * @param access_token
	 * @param lnglat
	 * @return igURL
	 */
	public static String createInstagramURL(String access_token, String[] lnglat){
		String lng = lnglat[0];
		String lat = lnglat[1];
		
		String igURL = "https://api.instagram.com/v1/media/search?access_token="
				+ access_token + "&" + lat + "&" + lng + "&count=100"; 
		//System.out.println(igURL);
		
		setURL(igURL);
		return igURL;
	}
	
	
	/**
	 * Get latitude and longitude for a given zipcode, entered as a String using GoogleJSONReader class
	 * @param zipcode
	 * @throws JSONException
	 * @throws IOException
	 */
	public static String[] getLatLong(String zipcode) throws JSONException, IOException{
    	String temp = "http://maps.googleapis.com/maps/api/geocode/json?address=" + zipcode;
    	GoogleJSONReader jr = new GoogleJSONReader(temp);
    	String googleData = jr.parse();
    	
		//split
		String lnglat[] = googleData.split(",", 2);
    	return lnglat;
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
	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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
	   * Creates a hashmap with zipcodes as a key, and another hashmap with photo URLs for values
	   * @return photoURLS
	   * @throws JSONException
	   * @throws IOException
	   */
	  public static HashMap<String, HashMap<String, String>> parse() throws JSONException, IOException{  
		JSONObject json = readJsonFromUrl(getURL());
		JSONArray data = json.getJSONArray("data");
		
		HashMap<String, HashMap<String, String>> photos = new HashMap<String, HashMap<String, String>>();
		
		
		
		
//		for(int i = 0; i < data.length(); i++){ //data is a JSONArray of JSONObjects
//			Object temp = data.;
//			
//			Iterator<?> keys = data.keys();
//			HashMap<String, String> map = new HashMap<String, String>();
//			System.out.println((String) keys.next());
////			while(keys.hasNext()){
////	            String key = (String) keys.next();
////	            String value = json.getString(key); 
////	            System.out.println(value);
////	            map.put(key, value);
////
////	        }
//	        System.out.println("map : "+ map);
//
//		}
//		
		
		
        

		String s =  json.toString();


        System.out.println("json : "+ data);
		

		
//		JSONObject images = data.getJSONObject("images");
		
//		System.out.println(images);
		
//		int firstDelimiter = s.indexOf("location"); 
//		int secondDelimiter = s.indexOf("location_type"); 
//		String latitudeLongitude = s.substring(firstDelimiter, secondDelimiter); 

		return photos;
	  }
	
	
	
//	public static void main(String[] args) throws JSONException, IOException{
//		String[] tempLngLat = getLatLong("19146");
//		String igURL = createInstagramURL(access_token, tempLngLat);
//		parse();
//		
//	}


}
