package wutever;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import twitter4j.GeoLocation;
import twitter4j.JSONArray;
import twitter4j.JSONObject;
//import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import wutever.ACSData;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.json.JSONException;

public class Main {
	
    public static void main(String[] args) throws JSONException, IOException {
    	
    	staticFileLocation("/resources/templates");
    	Spark.staticFileLocation("/public");
    	
       

    	/*NOTE! I removed createACSData() and put this back into main() because of weird
    	 * static variable errors when I tried to make myACSData and ACSbyZIP static class variables
    	 * 
         * This reads in the .txt data downloaded from the American Community Survey and brings the data
         * into a HashMap of HashMaps such that the ZIP code is the key to the outer HashMap
         * For each ZIP code, there's a HashMap wherein each ACS mnemonic is the key and the data value is the value
         * 
         * Mnemonic example: PCT_SE_T005_003 is the % Total Population: Male: Under 5 Years
         * All mnemonic description pairs are stored in ACSMetadata.csv
         * 
         * Example of a call to get PCT_SE_T005_003 for 19148: ACSbyZIP.get(19148).get(PCT_SE_T005_003)
         */
    	ACSData myACSData = ACSData.initACSData();
		HashMap<String,HashMap<String, Object>> ACSbyZIP = myACSData.getACSData();
    	
        
        
        /*
         * This is the home page
         */
        get("/", (request, response) -> new ModelAndView(null, "Home.mustache"), new MustacheTemplateEngine());
        
        /*
         * This is the quiz page
         */
        get("/Quiz", (request, response) -> new ModelAndView(null, "Quiz.mustache"), new MustacheTemplateEngine());
        
        /*
         * This is the about page
         */
        get("/About", (request, response) -> new ModelAndView(null, "About.mustache"), new MustacheTemplateEngine());
        
        /*
         * This page has information about all of Philadelphia
         * we're sending Zip code data for all the zip codes to this page
         * you can access data via Mustache by first secifying the zip code and then specifying the mnemonic
         *  For example, {{19148.SE_T002_002}} accesses the population density for 19148
         */
        get("/PhillyStats", (request, response) -> new ModelAndView(ACSbyZIP, "PhillyStats.mustache"), new MustacheTemplateEngine());
        
    	
        get("/SimilarityGraph", (request, response) -> new ModelAndView(new HashMap<String, Object>(), "SimilarityGraph.mustache"), new MustacheTemplateEngine());

        get("/GoogleMap", (request, response) -> new ModelAndView(new HashMap<String, Object>(),"Map.mustache"), new MustacheTemplateEngine());
        
        
        get("/Quiz", (request, response) -> new ModelAndView(new HashMap<String, Object>(),"Quiz.mustache"), new MustacheTemplateEngine());

         post("/submitform", (request, response) -> {
            Set <String> queryParams = request.queryParams();
            //System.out.println("filename " + request.queryParams("filename").toCharArray().toString());
         StringBuilder str = new StringBuilder();
         str.append("Request Parameters are <br/>");
         for(String param : queryParams){
          System.out.println("param " + param);
          System.out.println("param value " + request.queryParams("firstname"));
          str.append(param).append(" ").append(request.queryParams(param)).append("<br />");
         }
         String[] zipArray  = myACSData.findZipCode(request.queryParams("age"), request.queryParams("maritalstatus"), request.queryParams("income"));
         StringBuilder str1 = new StringBuilder();
         str1.append("Your top 3 zipcodes are: " ).append(zipArray[0]).append(",").append(zipArray[1]).append(",").append(zipArray[2]);
         return str1;
});
        
        get("/similarity.json", (request, response) -> {
        	response.type("json");
        	//VERTICIES
        	JSONArray nodes = new JSONArray();
        	ArrayList<String> zips = SimilarityCalc.getZips();
        	for(int i = 0; i<zips.size(); i++){
//        		if(!(zips.get(i).equals("19102"))){
          		JSONObject o = new JSONObject();
          		o.put("name", zips.get(i));
          		o.put("group", 1);
          		nodes.put(o);        			
//        		}
        	}
        	
        	//EDGES
        	Double[][] sims = SimilarityCalc.doit();
        	JSONArray links = new JSONArray();
        	for(int i = 0; i<zips.size(); i++){
          	for(int j = 0; j<i; j++){
	        		Double distance = sims[i][j];
	        		Double threshold = 0.45;
	        		if(distance < threshold && !zips.get(i).equals("19102") && !zips.get(j).equals("19102")){
		        		JSONObject o = new JSONObject();
		        		o.put("source", i);
		        		o.put("target", j);
		        		o.put("value", (int)((threshold-distance)*10));
		        		links.put(o);
	        		}
          	}
        	}
        	
        	//COMBINE AND RETURN
        	JSONObject js = new JSONObject();
        	js.put("nodes", nodes);
        	js.put("links", links);
        	return js.toString(2);
        });

        
        /* 
    	 * This handles requests for ZIP code data pages
    	 * Such that the request is formatted like "GET /Data/12345" 
    	 * such that request.params(":zipcode") is '12345' 
    	*/ 
        //get("/Data/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "Data.mustache"), new MustacheTemplateEngine());
        
        get("/Data/:ZIP", (request, response) -> {
    		//initiate values for use with mustache template
        	//Map<String, Object> viewObjects = new HashMap<String, Object>();    		
        	
    		
      	//get latitude and longitude from GoogleAPI
      	Double[] latLong = GoogleApisWrapper.getLatLongForZip(request.params(":ZIP"));
      	//viewObjects.put("lat", latLong[0]);    		
      	//viewObjects.put("lng", latLong[1]);    		

      	//get tweets from twitter API
      	GeoLocation coords = new GeoLocation(latLong[0], latLong[1]);
      	Twitter twitter = TwitterFactory.getSingleton();  
        Query q = new Query();
      	q = q.geoCode(coords, 0.5, "mi");
        QueryResult qr = null;
        try {
  	      qr = twitter.search(q);
        } catch (TwitterException e) {
  	      System.out.println("Twitter request DID NOT WORK :(");
  	      e.printStackTrace();
        }
        List<Status> tweets;
        tweets = qr.getTweets();
        
        List<String> parsedTweets = new ArrayList<String>();
        
        for(int i = 0; i < tweets.size(); i++){
        	parsedTweets.add(tweets.get(i).getText());
        }
        
        ACSbyZIP.get(request.params(":ZIP")).put("Tweets",parsedTweets);
        
    		
        ModelAndView mv = new ModelAndView(ACSbyZIP.get(request.params(":ZIP")), "Data.mustache");
        MustacheTemplateEngine mte = new MustacheTemplateEngine();
        return mte.render(mv);
    	});
        

        
        get("/IG/:ZIP", (request, response) -> {
        	String zip = request.params(":ZIP");
        	InstagramJSONReader ig = new InstagramJSONReader();

          	//get photos from InstagramJSONReader,  zip: {"photo" : url}
    		Map<String, List<String>> igViewObjects = ig.getZipcodePhotos(zip);
		
            ModelAndView mv = new ModelAndView(igViewObjects, "instagram.mustache");
            MustacheTemplateEngine mte = new MustacheTemplateEngine();
            return mte.render(mv);
        	}
            
            );
        

        	
    	
    }
    
 
    
    /*
     * This refreshes the API calls so that we get new Instagram pictures!
     */
    public static HashMap<String,HashMap<String, String>> refresh(){
    	HashMap<String,HashMap<String, String>> retVal = new HashMap<String,HashMap<String, String>>();
    	return retVal;
    }
	
}
