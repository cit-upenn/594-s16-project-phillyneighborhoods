package wutever;

import java.util.*;
import java.io.*;

public class ACSData {

	static ACSData myACSData;
	HashMap<String,HashMap<String, String>> ACSbyZIP;
	ArrayList<String> allZIPCodes;
	ArrayList<String> header;
	BufferedReader reader;
	int error;
	
	//this is the number of variables that are in the txt file, including the ZIP code
	int numberHeaders = 107; 
	
	String fileName;
	
	private ACSData(){
		ACSbyZIP = new HashMap<String,HashMap<String, String>>();
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
					HashMap<String,String> newMap = new HashMap<String,String>();
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
	
	public HashMap<String,HashMap<String, String>> getACSData(){
		return ACSbyZIP;
	}
	
	public ArrayList<String> getAllZips(){
		return allZIPCodes;
	}
}
