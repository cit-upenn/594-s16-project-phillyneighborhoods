package wutever;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

public class Main {
	
	
    public static void main(String[] args) {
    	
    	staticFileLocation("/resources/templates");
    	//externalStaticFileLocation("/var/www/public"); // Static files
    	
    	Map map = new HashMap();
        map.put("name", "Sam");
        
        ACSData myACSData = new ACSData();
        HashMap<String,HashMap<String, String>> ACSbyZIP = myACSData.getACSData();
        
       
    	// matches "GET /TESTZIP/12345" 
    	// request.params(":zipcode") is '12345' 
    	get("/TESTZIP/:zipcode", (request, response) -> new ModelAndView(ACSbyZIP.get(request.params(":zipcode")), "test.mustache"), new MustacheTemplateEngine());
    	
    	
    }
}
