
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
    	
      ACSData myACSData = new ACSData();
      HashMap<String,HashMap<String, String>> ACSbyZIP = myACSData.getACSData();

      //useful zipcode to center coordinates map
      // http://maps.googleapis.com/maps/api/geocode/json?address=19143

      Twitter twitter = TwitterFactory.getSingleton();  
      GeoLocation coords19104 = new GeoLocation(39.9584, -75.1954);
      GeoLocation coords19143 = new GeoLocation(39.9413, -75.2187);
      GeoLocation coords19142 = new GeoLocation(39.9227, -75.2304);
      Query q = new Query();
    	q = q.geoCode(coords19142, 0.5, "mi");
      QueryResult qr = null;
      try {
	      qr = twitter.search(q);
	      System.out.println("IT WORKED IT WORKED!! :)");
      } catch (TwitterException e) {
	      System.out.println("Twitter request DID NOT WORK :(");
	      e.printStackTrace();
      }

      List<Status> tweets;

      tweets = qr.getTweets();
      System.out.println("Printing "+tweets.size()+" tweets from 19142...");
      for(int i =0; i<tweets.size(); i++){
      	System.out.println(tweets.get(i).getText());
      }
      
    	get("/TESTZIP/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "test.mustache"), new MustacheTemplateEngine());

    	Map<String, Object> viewObjects = new HashMap<String, Object>();
    	viewObjects.put("atrend", tweets.get(0).getText());
    	
    	get("/TESTTWITTER/:ZIP", (request, response) -> new ModelAndView(viewObjects, "twitter.mustache"), new MustacheTemplateEngine());

    	get("/hello/:ZIP", (request, response) -> {
        String msg =  "Hello: " + request.params(":name");
        ModelAndView mv = new ModelAndView(viewObjects, "twitter.mustache");
        MustacheTemplateEngine mte = new MustacheTemplateEngine();
        return mte.render(mv);
    	});
    	
    }
}
