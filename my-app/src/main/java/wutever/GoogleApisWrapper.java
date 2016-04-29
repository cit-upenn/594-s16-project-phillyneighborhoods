package wutever;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import twitter4j.JSONException;
import twitter4j.JSONObject;


public class GoogleApisWrapper {

	//do fuck all for starters... but changes are it will come in useful eventually
	GoogleApisWrapper(){}
	
	public static Double[] getLatLongForZip(String zipcode){
		Double[] latLong = new Double[2];
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String urlString = "http://maps.googleapis.com/maps/api/geocode/json?address="+zipcode;
		System.out.println("Requesting " + urlString);
		HttpGet httpGet = new HttpGet(urlString);
		CloseableHttpResponse response1 = null;
    try {
	    response1 = httpclient.execute(httpGet);
	    System.out.println(response1.getStatusLine());
	    HttpEntity entity1 = response1.getEntity();
      if (entity1 != null) {
        String retSrc = EntityUtils.toString(entity1); 
        // parsing JSON
        JSONObject result = new JSONObject(retSrc).getJSONArray("results").getJSONObject(0); //Convert String to JSON Object
        String latStr = result.getJSONObject("geometry").getJSONObject("location").getString("lat");
        String lngStr = result.getJSONObject("geometry").getJSONObject("location").getString("lng");
        latLong[0] = Double.parseDouble(latStr);
        latLong[1] = Double.parseDouble(lngStr);
      }    		    // do something useful with the response body
	    EntityUtils.consume(entity1); //Must fully consume to avoid resource leak
    } catch (ClientProtocolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
    } finally {
		    try {
	        response1.close();
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } //MUST close to avoid resource leak
		}
		return latLong;
	}
	
	
}
