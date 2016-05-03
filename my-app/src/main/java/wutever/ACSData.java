package wutever;

import java.util.*;
import java.io.*;

public class ACSData {

	static ACSData myACSData;
	HashMap<String,HashMap<String, Object>> ACSbyZIP;
	ArrayList<String> allZIPCodes;
	ArrayList<String> header;
	BufferedReader reader;
	int error;
	
	//this is the number of variables that are in the txt file, including the ZIP code
	int numberHeaders = 107; 
	
	String fileName;
	
	private ACSData(){
		ACSbyZIP = new HashMap<String,HashMap<String, Object>>();
		allZIPCodes = new ArrayList<String>();
		header = new ArrayList<String>();
		fileName = "ACSData.txt";
		
		
		int check = openFile();
		//openFile() returns -1 if file isn't found
		if (check == -1){
			return;
		}
		try{
			readFile();
		}
		catch (IOException e) {
			System.out.println("Error.");
			return;
		}
		closeFile();
	}
	
	public static ACSData initACSData(){
		if(myACSData == null){
			myACSData = new ACSData();
		}
		return myACSData;
	}
	
	private int openFile(){
		try {
			InputStream inputStream = new FileInputStream(this.fileName);
			DataInputStream stream = new DataInputStream(inputStream);
			InputStreamReader streamReader = new InputStreamReader(stream);
			this.reader = new BufferedReader(streamReader);
			return 0;
		}
		catch (IOException e) {
			System.out.println("Error reading file.");
			this.error = -1;
			return -1;
		}
	}

	
	private void readFile() throws IOException{
		String tempLine;
		
		System.out.println("Reading File");
		
		
		int checkHeader = 0;
		
		while((tempLine = this.reader.readLine()) != null){
			//System.out.println(tempLine);
			
			//first we parse out the header strings
			//so that we can use them as keys for the ACS variables
			//in the HashMap for each ZIP code
			if(checkHeader == 0){
				String[] parts = tempLine.split(",",numberHeaders);
				for(int i = 0; i < numberHeaders; i++){
					header.add(parts[i]);
					//System.out.println("Header, " + parts[i]);
				}
				checkHeader = 2;
			}
			
			//then we read each of the lines of data for each ZIP code
			else{

				String[] parts = tempLine.split(",",numberHeaders);
				
				
				//if zip code isn't already in ACSbyZip
				if(allZIPCodes.contains(parts[0]) == false){
					//add zip code to list of all zip codes
					allZIPCodes.add(parts[0]);
					
					//create a new hash map for the zip code and add data to it
					HashMap<String, Object> newMap = new HashMap<String, Object>();
					for(int i = 0; i < header.size(); i++){
						newMap.put(header.get(i), parts[i]);
					}
					
					
					//attach the new hash map for the zip code to the hash map of all ACS data
					ACSbyZIP.put(parts[0], newMap);
					
					//System.out.println(parts[0] + " "+ parts[1] + " " + newMap.get("popDen"));
				}
			}
			
		}
		
		return;
	}
	
	
	/**
	 * Finds zipcode based on parameter passed in 
	 * @param age
	 * @param married
	 * @param income
	 * @return
	 */
	public String[] findZipCode(String age, String married, String income){
		double avgMaxAge = 0.0;
		double avgMaxMarriage =0.0;
		double avgMaxIncome =0.0;
		
		String range = "";
		String code = "";
		String zipcode = "";
		String zipCodeMarriage = "";
		String marriageStatus = "";
		String zipCodeIncome = "";
		
		double tempAge = Double.parseDouble(age);
		if(married.equals("never married")){
			marriageStatus = "PCT_SE_T022_002";		
		}
		if(married.equals("married")){
			marriageStatus = "PCT_SE_T022_003";		
		}
		if(married.equals("separated")){
			marriageStatus = "PCT_SE_T022_004";		
		}
		if(married.equals("widowed")){
			marriageStatus = "PCT_SE_T022_005";		
		}
		if(married.equals("divorced")){
			marriageStatus = "PCT_SE_T022_006";		
		}
		
		
		if(tempAge <= 5){
			range ="PCT_SE_T005_003";
		}
		else if(tempAge >5 && tempAge <=9){
			range = "PCT_SE_T005_004"; 
		}
		else if(tempAge >9 && tempAge <=14){
			range = "PCT_SE_T005_005"; 
		}
		else if(tempAge >14 && tempAge <=17){
			range = "PCT_SE_T005_006"; 
		}
		else if(tempAge >17 && tempAge <=24){
			range = "PCT_SE_T005_007"; 
		}
		else if(tempAge >24 && tempAge <=34){
			range = "PCT_SE_T005_008"; 
		}
		else if(tempAge >34 && tempAge <=44){
			range = "PCT_SE_T005_009"; 
		}
		else if(tempAge >44 && tempAge <=54){
			range = "PCT_SE_T005_010"; 
		}
		else if(tempAge >54 && tempAge <=64){
			range = "PCT_SE_T005_011"; 
		}
		else if(tempAge >64 && tempAge <=74){
			range = "PCT_SE_T005_012"; 
		}
		else if(tempAge >74 && tempAge <=84){
			range = "PCT_SE_T005_013"; 
		}
		else if(tempAge >84){
			range = "PCT_SE_T005_014"; 
		}
		for(String ZIPCode: allZIPCodes){
			Set<String> allDataForZip = ACSbyZIP.get(ZIPCode).keySet();
			for(String dataPoints: allDataForZip){
				//System.out.println("ZIP " + ZIPCode + " contains data point " + dataPoints + " with value " + ACSbyZIP.get(ZIPCode).get(dataPoints));
				
				
				if(dataPoints.equals(range)){
					
				if(Double.parseDouble(ACSbyZIP.get(ZIPCode).get(dataPoints))>avgMaxAge){
					avgMaxAge = Double.parseDouble(ACSbyZIP.get(ZIPCode).get(dataPoints));

//					if(dataPoints.equals(range)){
					code = dataPoints;
					zipcode = ZIPCode;
					System.out.println("range: "+range);
					}
					}
		
			if(dataPoints.equals(marriageStatus)){
				if(Double.parseDouble(ACSbyZIP.get(ZIPCode).get(dataPoints))>avgMaxMarriage){
					avgMaxMarriage = Double.parseDouble(ACSbyZIP.get(ZIPCode).get(dataPoints));

//					if(dataPoints.equals(range)){
					code = dataPoints;
					zipCodeMarriage = ZIPCode;
					System.out.println("marriage range: "+marriageStatus);
				}
			}
			if(dataPoints.equals("SE_T083_001")){
				
				double diff1 = Double.parseDouble(ACSbyZIP.get(ZIPCode).get(dataPoints)) - Double.parseDouble(income);
				double diff2 = avgMaxIncome - Double.parseDouble(income);
				if(diff1>diff2){
					zipCodeIncome = ZIPCode;
				}
			}
				
					
				}
			}
		String[] zipArray = new String[3];
		zipArray[0] = zipCodeIncome;
		zipArray[1] = zipCodeMarriage;
		zipArray[2] = zipcode;
		return zipArray;
		}
		
	
	
	private void closeFile(){
		
		for(String ZIPCode: allZIPCodes){
			Set<String> allDataForZip = ACSbyZIP.get(ZIPCode).keySet();
			for(String dataPoints: allDataForZip){
				//System.out.println("ZIP " + ZIPCode + " contains data point " + dataPoints + " with value " + ACSbyZIP.get(ZIPCode).get(dataPoints));
			}
		}
		
		try {
			reader.close();
			return;
		}
		catch (Exception e) {
			return;
		}
	}
	
	public HashMap<String,HashMap<String, Object>> getACSData(){
		return ACSbyZIP;
	}
	
	public ArrayList<String> getAllZips(){
		return allZIPCodes;
	}
}
