package wutever;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import wutever.ACSData;

import java.util.*;

public class Main {
	
    public static void main(String[] args) {
    	
    	staticFileLocation("/resources/templates");
    	Spark.staticFileLocation("/public");
    	
    
    	
    	createInstagram();


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
    	ACSData myACSData = new ACSData();
      HashMap<String,HashMap<String, String>> ACSbyZIP = myACSData.getACSData();
    	
        
        
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
        
    	/* 
    	 * This handles requests for ZIP code data pages
    	 * Such that the request is formatted like "GET /Data/12345" 
    	 * such that request.params(":zipcode") is '12345' 
    	*/ 
        get("/Data/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "Data.mustache"), new MustacheTemplateEngine());
    	
        get("/similarity", (request, response) -> new ModelAndView(new HashMap<String, Object>(), "similarity.mustache"), new MustacheTemplateEngine());
    		
        
        get("/TWITTER/:ZIP", (request, response) -> {
    		//initiate values for use with mustache template
    		Map<String, Object> viewObjects = new HashMap<String, Object>();    		

    		
      	//get latitude and longitude from GoogleAPI
      	Double[] latLong = GoogleApisWrapper.getLatLongForZip(request.params(":ZIP"));
      	viewObjects.put("lat", latLong[0]);    		
      	viewObjects.put("lng", latLong[1]);    		

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
        String tweetsString = "Tweets:";
        for(int i =0; i<tweets.size(); i++){
        	tweetsString += tweets.get(i).getText();
        }
      	viewObjects.put("alltweets", tweetsString);    		
    		
        ModelAndView mv = new ModelAndView(viewObjects, "twitter.mustache");
        MustacheTemplateEngine mte = new MustacheTemplateEngine();
        return mte.render(mv);
    	});
    	
    }
    
    public static void createInstagram(){
    	InstagramJSONReader igData = new InstagramJSONReader();
    }
    
    /*
     * This refreshes the API calls so that we get new Instagram pictures!
     */
    public static HashMap<String,HashMap<String, String>> refresh(){
    	HashMap<String,HashMap<String, String>> retVal = new HashMap<String,HashMap<String, String>>();
    	return retVal;
    }
	
}
