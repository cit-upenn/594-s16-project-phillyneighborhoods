/**
 * 
 */
package wutever;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.json.JSONException;


/**
 * @author brittanybinler
 *
 */
public class InstagramData {
	static String url = "http://maps.googleapis.com/maps/api/geocode/json?address=19146";

	static String access_token = "6468350.1677ed0.51442ec921ae44989cf30c72887d0cc0";
	
	/**
	 * 
	 * @param access_token
	 * @param lnglat
	 * @return
	 */
	public static String createInstagramURL(String access_token, String[] lnglat){
		String lng = lnglat[0];
		String lat = lnglat[1];
		
		String stub1 = "https://api.instagram.com/v1/media/search?access_token="
				+ access_token + "&" + lat + "&" + lng; 
//		System.out.println(stub1);
		return stub1;
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
	
	//public static void main(String[] args) throws JSONException, IOException{
	//	String[] tempLngLat = getLatLong("19146");
	//	createInstagramURL(access_token, tempLngLat);
	//}


}
