
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import twitter4j.GeoLocation;
import twitter4j.Location;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.*;

public class Main {
	
	
    public static void main(String[] args) {
    	
    	staticFileLocation("/resources/templates");
    	//externalStaticFileLocation("/var/www/public"); // Static files
    	
      ACSData myACSData = new ACSData();
      HashMap<String,HashMap<String, String>> ACSbyZIP = myACSData.getACSData();

      //useful zipcode to center coordinates map
      // http://maps.googleapis.com/maps/api/geocode/json?address=19143
      
      
      //https://dev.twitter.com/rest/reference/get/trends/place
      Twitter twitter = TwitterFactory.getSingleton();  
      Trends phillyTrends = null;
      ResponseList<Location> myLocs = null;
      GeoLocation phillyLoc = new GeoLocation(39.9526, -75.1652);
      GeoLocation coords19104 = new GeoLocation(39.9584, -75.1954);
      GeoLocation coords19143 = new GeoLocation(39.9413, -75.2187);
      GeoLocation coords19142 = new GeoLocation(39.9227, -75.2304);
//      Query q = new Query("q=the&geoloc=39.9526,-75.1652,5mi");
      Query q = new Query();
      QueryResult qr1 = null;
      QueryResult qr2 = null;
      QueryResult qr3 = null;
      try {
	      	phillyTrends = twitter.getPlaceTrends(2471217);
//	      phillyTrends = twitter.getPlaceTrends(1);
//	      myLocs = twitter.getClosestTrends(phillyLoc);
//	        q.setGeoCode(coords19104, 0.5, "mi");
//	      	qr1 = twitter.search(q);
//	        q.setGeoCode(coords19143, 0.5, "mi");
//	      	qr2 = twitter.search(q);
//	        q.setGeoCode(coords19142, 0.5, "mi");
	      	q = q.geoCode(coords19142, 0.5, "mi");
	      	qr3 = twitter.search(q);
	      System.out.println("IT WORKED IT WORKED!! :)");
      } catch (TwitterException e) {
	      System.out.println("Twitter request DID NOT WORK :(");
	      e.printStackTrace();
      }

      List<Status> tweets;

//      tweets = qr1.getTweets();
//      System.out.println("Printing "+tweets.size()+" tweets from 19104...");
//      for(int i =0; i<tweets.size(); i++){
//      	System.out.println(tweets.get(i).getText());
//      }
//
//      tweets = qr2.getTweets();
//      System.out.println("Printing "+tweets.size()+" tweets from 19143...");
//      for(int i =0; i<tweets.size(); i++){
//      	System.out.println(tweets.get(i).getText());
//      }
//
      tweets = qr3.getTweets();
      System.out.println("Printing "+tweets.size()+" tweets from 19142...");
      for(int i =0; i<tweets.size(); i++){
      	System.out.println(tweets.get(i).getText());
      }

      
//      for(int i = 0; i<myLocs.size(); i++){
//      	Location l = myLocs.get(i);
//      	System.out.println(l.getName() + " " + l.getWoeid());
//      }
      
    	get("/TESTZIP/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "test.mustache"), new MustacheTemplateEngine());
    	Trend[] myTrends = phillyTrends.getTrends();
    	
    	String firstTopic = "";
    	for(int i = 0; i<myTrends.length; i++){
    		firstTopic = firstTopic + myTrends[i].getName() + ", ";
    	}
    	
    	Map<String, Object> viewObjects = new HashMap<String, Object>();
    	viewObjects.put("ZIP", "88888");
    	viewObjects.put("atrend", firstTopic);
    	
    	
    	get("/TESTTWITTER/:zipcode", (request, response) -> new ModelAndView(viewObjects, "twitter.mustache"), new MustacheTemplateEngine());

    }
}
