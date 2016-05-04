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
	
	public static ArrayList<String> getZips(){
		ACSData acs = ACSData.initACSData();
		final HashMap<String,HashMap<String, Object>> dataz = acs.getACSData(); 
		ArrayList<String> zips = new ArrayList<String>(new TreeSet<String>(dataz.keySet()));	
		return zips;
	}
	
	public static Double[][] doit(){
		ACSData acs = ACSData.initACSData();
		final HashMap<String,HashMap<String, Object>> dataz = acs.getACSData();
		ArrayList<String> zips = new ArrayList<String>(new TreeSet<String>(dataz.keySet()));	

		ArrayList<String> columns = new ArrayList<String>();
		columns.add("SE_T002_002"); //population density
		columns.add("SE_T083_001"); //Per capita income (2014 dollars)
		columns.add("PCT_SE_T150_005"); //percent bachelor degree
		columns.add("SE_T098_001"); //Median year structure built
		columns.add("PCT_SE_T130_006"); //Moved from abroad
		
		/////////////////////////////////////////////
		//create dataframe using ACSdata
		
		Dataframe df = new Dataframe(columns, zips);
		for(int i=0; i<columns.size(); i++){
			for(int j=0; j<zips.size(); j++){
				String zip = zips.get(j);
				String column = columns.get(i);

				HashMap<String, Object> zipDataz = dataz.get(zip);
				String dataStr = zipDataz.get(column).toString();

				if(dataStr!=null){
					df.setValue(column, zip, Double.parseDouble(dataStr));
				}
				else{
					df.setValue(column, zip, null);
				}
			}			
		}
		
		Dataframe dfz = df.zScore();
		
		/////////////////////////////////////////////
		//Calculate distance between zscored data
		
		Double[][] zipSimilarity = new Double[zips.size()][zips.size()];
		String zipI, zipJ, column;
		Double sumSq, zipDiff, dist;
		for(int i = 0; i<zips.size(); i++){
			for(int j = 0; j<zips.size(); j++){
				zipI = zips.get(i);
				zipJ = zips.get(j);
				sumSq = 0.0;
				//calculate similarity for this zip pair
				for(int k = 0; k<columns.size();k++){
					column = columns.get(k);
					try {
	          zipDiff = dfz.getValue(column, zipI) - dfz.getValue(column, zipJ);
						sumSq += (zipDiff * zipDiff);
          } catch (Exception e) {
	          e.printStackTrace();
          }
				}
				dist = Math.sqrt(sumSq);
				zipSimilarity[i][j] = dist;
			}			
			System.out.println(Arrays.asList(zipSimilarity[i]));
		}
	
		return zipSimilarity;
	}
		
	
}
