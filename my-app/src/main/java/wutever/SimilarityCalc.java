package wutever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

//pick my top five stats to calc with
//SE_T002_002 - population density
//SE_T083_001 - Per capita income (2014 dollars)
//PCT_SE_T150_005 - percent bachelor degree
//SE_T098_001 - Median year structure built
//PCT_SE_T130_006 - Moved from abroad
/////////////////////////////////////////////
//SE_T057_001 - median household income
//PCT_SE_T095_003 - vacant house percent
//SE_T101_001 - Median value (house)
//PCT_SE_T033_006 - % Population 16 Years and over: In labor force: Civilian: Unemployed
//would like median age!!...BUT oh well


public class SimilarityCalc {
	
	public static void doit(){
		final HashMap<String,HashMap<String, String>> dataz = new ACSData().getACSData();
		ArrayList<String> zips = new ArrayList<String>(new TreeSet(dataz.keySet()));	

		ArrayList<String> columns = new ArrayList<String>();
		columns.add("SE_T002_002"); //population density
		columns.add("SE_T083_001"); //Per capita income (2014 dollars)
		columns.add("PCT_SE_T150_005"); //percent bachelor degree
		columns.add("SE_T098_001"); //Median year structure built
		columns.add("PCT_SE_T130_006"); //Moved from abroad
		
		Double[][] data = new Double[columns.size()][zips.size()];
		
		for(int i=0; i<zips.size(); i++){
			String zip = zips.get(i);
			for(int j=0; j<columns.size(); j++){
				String column = columns.get(j);
				System.out.println("Processing zip " + zip + " and column "+ column);
				HashMap<String, String> zipDataz = dataz.get(zip);
				String dataStr = zipDataz.get(column);

				if(dataStr!=null){
					data[j][i] = Double.parseDouble(dataStr);					
				}
				else{
					data[j][i] = null;
				}
			}			
		}
		
		for(int i =0; i<columns.size(); i++){
			System.out.println(columns.get(i) + " " + new ArrayList<Double>(Arrays.asList(data[i])));
		}
	}
		
	
}
