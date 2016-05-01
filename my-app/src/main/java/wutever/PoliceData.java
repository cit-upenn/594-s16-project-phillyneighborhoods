package wutever;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PoliceData {
	
	HashMap<String,HashMap<String, String>> CrimebyZIP;
	HashMap<String,HashMap<String, String>> CrimebyID;
	ArrayList<String> allZIPCodes;
	ArrayList<String> allDC_KEY;
	ArrayList<String> header;
	BufferedReader reader;
	int error;
	
	//this is the number of variables that are in the txt file, including the ZIP code
	int numberHeaders = 11; 
	
	String fileName;
	
	public PoliceData(ArrayList<String> AllZIPCodes){
		CrimebyZIP = new HashMap<String,HashMap<String, String>>();
		CrimebyID = new HashMap<String,HashMap<String, String>>();
		allZIPCodes = AllZIPCodes;
		allDC_KEY = new ArrayList<String>();
		header = new ArrayList<String>();
		fileName = "PPD_Crime_Incidents_2006-Present.txt";
		
		
		int check = openFile();
		//openFile() returns -1 if file isn't found
		if (check == -1){
			return;
		}
		try{
			readFileIntoAllUsers();
		}
		catch (IOException e) {
			System.out.println("Error.");
			return;
		}
		closeFile();
		
		
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

	
	private void readFileIntoAllUsers() throws IOException{
		String tempLine;
		
		System.out.println("Reading File");
		
		
		int checkHeader = 0;
		
		while((tempLine = this.reader.readLine()) != null){
			//System.out.println(tempLine);
			
			//first we parse out the header strings
			//so that we can use them as keys for the crime variables
			//in the HashMap for each crime code
			if(checkHeader == 0){
				String[] parts = tempLine.split(",",numberHeaders);
				for(int i = 0; i < numberHeaders; i++){
					header.add(parts[i]);
					//System.out.println("Header, " + parts[i]);
				}
				checkHeader = 2;
			}
			
			//then we read each of the lines of data for each crime code
			else{

				String[] parts = tempLine.split(",",numberHeaders);
				
				
				//if crime code isn't already in the list of crime keys
				if(allDC_KEY.contains(parts[6]) == false){
					//add zip code to list of all zip codes
					allDC_KEY.add(parts[6]);
					
					//create a new hash map for the crime ID code and add data to it
					HashMap<String,String> newMap = new HashMap<String,String>();
					for(int i = 0; i < header.size(); i++){
						newMap.put(header.get(i), parts[i]);
					}
					
					
					//attach the new hash map for the zip code to the hash map of all ACS data
					CrimebyID.put(parts[6], newMap);
					
					//System.out.println(parts[6] + " "+ newMap.get("SHAPE") + " " + newMap.get("TEXT_GENERAL_CODE"));
				}
			}
			
		}
		
		return;
	}
	
	private void closeFile(){
		
		/*for(String ZIPCode: allZIPCodes){
			Set<String> allDataForZip = CrimebyZIP.get(ZIPCode).keySet();
			for(String dataPoints: allDataForZip){
				//System.out.println("ZIP " + ZIPCode + " contains data point " + dataPoints + " with value " + ACSbyZIP.get(ZIPCode).get(dataPoints));
			}	
		}*/
		
		try {
			reader.close();
			return;
		}
		catch (Exception e) {
			return;
		}
	}
	
	public HashMap<String,HashMap<String, String>> getCrimeData(){
		return CrimebyZIP;
	}
	
	
}
