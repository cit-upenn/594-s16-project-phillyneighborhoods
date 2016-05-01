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
	
	public static String getURL(){
		return url;
	}
	
	public static void setURL(String input){
		url = input;
	}
	
	/**
	 * 
	 * @param access_token
	 * @param lnglat
	 * @return
	 */
	public static String createInstagramURL(String access_token, String[] lnglat){
		String lng = lnglat[0];
		String lat = lnglat[1];
		
		String stub = "https://api.instagram.com/v1/media/search?access_token="
				+ access_token + "&" + lat + "&" + lng + "&count=100"; 
		//System.out.println(stub);
		
		setURL(stub);
		return stub;
	}
	
	
	/**
	 * 
	 * @param input
	 * @throws JSONException
	 * @throws IOException
	 */
	public static String[] getLatLong(String zipcode) throws JSONException, IOException{
    	String temp = "http://maps.googleapis.com/maps/api/geocode/json?address=" + zipcode;
    	
    	GoogleJSONReader jr = new GoogleJSONReader(temp);
    	String googleData = jr.parse();
    	
		//trim 
		int firstDelimiter = googleData.indexOf("ln"); 
		int secondDelimiter = googleData.indexOf("}"); 
		String latitudeLongitude = googleData.substring(firstDelimiter, secondDelimiter); 
		//format latLong for Instagram
		latitudeLongitude = latitudeLongitude.replace("\"", "");
		latitudeLongitude = latitudeLongitude.replace(":", "=");
		//split
		String lnglat[] = latitudeLongitude.split(",", 2);
		
    	return lnglat;
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
		JSONArray data = json.getJSONArray("data");
		
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
		
        

		

//        System.out.println("json : "+ data);
		

		
//		JSONObject images = data.getJSONObject("images");
		
//		System.out.println(images);
		
//		int firstDelimiter = s.indexOf("location"); 
//		int secondDelimiter = s.indexOf("location_type"); 
//		String latitudeLongitude = s.substring(firstDelimiter, secondDelimiter); 

		return s;
	  }
	
	
//	
//	public static void main(String[] args) throws JSONException, IOException{
//		String[] tempLngLat = getLatLong("19146");
//		createInstagramURL(access_token, tempLngLat);
//		parse();
//		
//	}


}
