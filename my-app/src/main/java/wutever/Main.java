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
        HashMap<String,HashMap<String, String>> ACSbyZIP = myACSData.getACSData();
    	
    	/* 
    	 * This handles requests for ZIP code data pages
    	 * Such that the request is formatted like "GET /Data/12345" 
    	 * such that request.params(":zipcode") is '12345' 
    	*/ 
        get("/Data/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "Data.mustache"), new MustacheTemplateEngine());
    	
    	
    }
    
    public static void createInstagram(){
    	InstagramData igData = new InstagramData();
    }
    
	
}
