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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author brittanybinler
 *
 */
public class InstagramJSONReader {
	private static final String access_token = "6468350.1677ed0.51442ec921ae44989cf30c72887d0cc0";
	private static final String stub = "https://api.instagram.com/v1/media/search?access_token=";
    private static InstagramJSONReader instance = new InstagramJSONReader();

   /**
    * Constructor
    */
   InstagramJSONReader(){
	   
   }

   /**
    * Getter method for instance of InstagramJSONReader
    * @return instance
    */
   public static InstagramJSONReader getInstance(){
      return instance;
   }
	
	
	/**
	 * Creates Instagram API URL using access token, latitude, longitude, and media search endpoint
	 * @param lnglat
	 * @return igURL
	 */
	private static synchronized String createURL(String[] lnglat){
		String lng = lnglat[0];
		String lat = lnglat[1];
		
		String igURL = stub + access_token + "&" + lat + "&" + lng + "&count=100"; 
		//System.out.println(igURL);
		
		return igURL;
	}
	
	
	/**
	 * Get latitude and longitude for a given zipcode, entered as a String using GoogleJSONReader class
	 * @param zipcode
	 * @throws JSONException
	 * @throws IOException
	 */
	private static synchronized String[] getLatLong(String zipcode) throws JSONException, IOException{
    	GoogleJSONReader g = GoogleJSONReader.getInstance();
    	System.out.println(zipcode);
    	String googleData = g.getLatitudeLongitude(zipcode);
    	
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
	   * Creates a hashmap with zipcodes as a key, and another hashmap with photo URLs for values
	   * @return photoURLS
	   * @throws JSONException
	   * @throws IOException
	   */
	  private static synchronized List<String> parse(String url) throws JSONException, IOException{  
		  
		JSONObject json = readJsonFromUrl(url);
		JSONArray data = json.getJSONArray("data");
		
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < data.length(); i++){ //data is a JSONArray of JSONObjects
			

			JSONObject temp = data.getJSONObject(i);
			JSONObject images = temp.getJSONObject("images");
			JSONObject image = images.getJSONObject("standard_resolution");
			String imageURL = image.getString("url");
			
			list.add(imageURL);
			
			System.out.println(imageURL);
			
		}
		
		return list;
	  }
	  
	  
	/**
	 * Creates a hashmap for a zipcode with urls for Instagram photos at that location  
	 * @param zipcode
	 * @return photos
	 * @throws JSONException
	 * @throws IOException
	 */
	public synchronized HashMap<String, List<String>> getZipcodePhotos(String zipcode) throws JSONException, IOException{
		
		HashMap<String, List<String>> photos = new HashMap<String, List<String>>();
		
		String[] tempLngLat = getLatLong(zipcode);
		String igURL = createURL(tempLngLat);
		List<String> photosforZip = parse(igURL);
		
		photos.put("photos", photosforZip);
		return photos;
	}
	
	
	
//	public static void main(String[] args) throws JSONException, IOException{
//		String[] tempLngLat = getLatLong("19146");
//		String igURL = createInstagramURL(access_token, tempLngLat);
//		HashMap<String, String> photosforZip = parse();
//		
//	}


}
