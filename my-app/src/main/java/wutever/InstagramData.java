/**
 * 
 */
package wutever;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.*;
import java.io.FileReader;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author brittanybinler
 *
 */


public class InstagramData{
	static String input; //for testing

	
	InstagramData(){
		input = "jsonTest.json"; //for testing
		parseJSON(input);
	}
	
	// http://maps.googleapis.com/maps/api/geocode/json?address=19146

	public static void parseJSON(String input){
		JSONParser parser = new JSONParser();
	  
			
	      try{
	         Object obj = parser.parse(input);
	         JSONArray array = (JSONArray)obj;
				
//	         System.out.println("The 2nd element of array");
//	         System.out.println(array.get(1));
//	         System.out.println();

	         JSONObject obj2 = (JSONObject)array.get(1);
	         System.out.println("Field \"1\"");
	         System.out.println(obj2.get("1"));    

	         input = "{}";
	         obj = parser.parse(input);
	         System.out.println(obj);

	         input = "[5,]";
	         obj = parser.parse(input);
	         System.out.println(obj);

	         input = "[5,,2]";
	         obj = parser.parse(input);
	         System.out.println(obj);
	         
	      }catch(ParseException pe){
			
	         System.out.println("position: " + pe.getPosition());
	         System.out.println(pe);
	      }

	}
	
//	public static void geocoding(String addr) throws Exception {
//	    // build a URL
//	    String s = "http://maps.google.com/maps/api/geocode/json?" +
//	                    "sensor=false&address=";
//	    s += URLEncoder.encode(addr, "UTF-8");
//	    URL url = new URL(s);
//	 
//	    // read from the URL
//	    Scanner scan = new Scanner(url.openStream());
//	    String str = new String();
//	    while (scan.hasNext())
//	        str += scan.nextLine();
//	    scan.close();
//	 
//	    // build a JSON object
//	    JSONObject obj = new JSONObject();
//	    if (! obj.getString("status").equals("OK"))
//	        return;
//	 
//	    // get the first result
//	    JSONObject res = obj.getJSONArray("results").getJSONObject(0);
//	    System.out.println(res.getString("formatted_address"));
//	    JSONObject loc =
//	        res.getJSONObject("geometry").getJSONObject("location");
//	    System.out.println("lat: " + loc.getDouble("lat") +
//	                        ", lng: " + loc.getDouble("lng"));
//	}
//	
	
	public String getJSONFromFile(String filename){
		filename = "jsonTest.json";
		
    	JSONParser parser = new JSONParser();
        try {
 
            Object obj = parser.parse(new FileReader(filename));
 
            JSONObject jsonObject = (JSONObject) obj;
 
            String geometry = (String) jsonObject.toJSONString();
//            String author = (String) jsonObject.get("Author");
//            JSONArray geometry = (JSONArray) jsonObject.get("geometry");
// 
            System.out.println("Geometry: " + geometry);
//            System.out.println("Author: " + author);
//            System.out.println("\nCompany List:");
//            Iterator<String> iterator = companyList.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
 
            return geometry;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
		
    }	
	
}


