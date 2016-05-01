package wutever;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		staticFileLocation("/resources/templates");
    	//externalStaticFileLocation("/var/www/public"); // Static files		
		
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
    	//PoliceData myPoliceData = new PoliceData(myACSData.getAllZips());
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
    	
        /*
         * calls the refresh function to refresh instagram photos
         */
        /*before((request, response) -> {
        	HashMap<String,HashMap<String, String>> retVal = refresh();
        	
        })*/;
        
    }
    
    public static void createInstagram(){
    	InstagramData igData = new InstagramData();
    }
    
    /*
     * This refreshes the API calls so that we get new Instagram pictures!
     */
    public static HashMap<String,HashMap<String, String>> refresh(){
    	HashMap<String,HashMap<String, String>> retVal = new HashMap<String,HashMap<String, String>>();
    	return retVal;
    }
	
}
