
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.*;

public class Main {
	
    public static void main(String[] args) {
    	
    	staticFileLocation("/resources/templates");
    	
      ACSData myACSData = new ACSData();
      HashMap<String,HashMap<String, String>> ACSbyZIP = myACSData.getACSData();
      
    	get("/TESTZIP/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "test.mustache"), new MustacheTemplateEngine());

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
}
