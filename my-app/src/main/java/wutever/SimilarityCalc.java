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
		final HashMap<String,HashMap<String, String>> dataz = new ACSData().getACSData();
		ArrayList<String> zips = new ArrayList<String>(new TreeSet<String>(dataz.keySet()));	
		return zips;
	}
	
	public static Double[][] doit(){
		final HashMap<String,HashMap<String, String>> dataz = new ACSData().getACSData();
		ArrayList<String> zips = new ArrayList<String>(new TreeSet<String>(dataz.keySet()));	

		ArrayList<String> columns = new ArrayList<String>();
		columns.add("SE_T002_002"); //population density
		columns.add("SE_T083_001"); //Per capita income (2014 dollars)
		columns.add("PCT_SE_T150_005"); //percent bachelor degree
		columns.add("SE_T098_001"); //Median year structure built
		columns.add("PCT_SE_T130_006"); //Moved from abroad
		
		/////////////////////////////////////////////
		//Retrieve data as arrays from ACSdata
		
		Double[][] data = new Double[columns.size()][zips.size()];		
		for(int i=0; i<columns.size(); i++){
			for(int j=0; j<zips.size(); j++){
				String zip = zips.get(j);
				String column = columns.get(i);
				System.out.println("Processing zip " + zip + " and column "+ column);
				HashMap<String, String> zipDataz = dataz.get(zip);
				String dataStr = zipDataz.get(column);

				if(dataStr!=null){
					data[i][j] = Double.parseDouble(dataStr);					
				}
				else{
					data[i][j] = null;
				}
			}			
		}
		
		/////////////////////////////////////////////
		//Calculate mean, stddev and zscore of data
		
		Double[][] dataZscored = new Double[columns.size()][zips.size()];
		Double sum, sumSq, mean, stdDev;
		for(int i=0; i<columns.size(); i++){
			//calculate stats!
			sum = 0.0;
			sumSq = 0.0;
			for(int j=0; j<zips.size(); j++){
				sum += data[i][j];
				sumSq += data[i][j] * data[i][j];
			}
			mean = sum/zips.size();
			stdDev = Math.sqrt(sumSq/zips.size());
			//fill out zscore data
			for(int j=0; j<zips.size(); j++){
				dataZscored[i][j] = (data[i][j]-mean)/stdDev;
			}
			//print the results
			System.out.println(columns.get(i) + " " + new ArrayList<Double>(Arrays.asList(data[i])));
			System.out.println(columns.get(i) + " " + new ArrayList<Double>(Arrays.asList(dataZscored[i])));
		}

		/////////////////////////////////////////////
		//Calculate mean, stddev and zscore of data
		
		Double[][] zipSimilarity = new Double[zips.size()][zips.size()];
		String zipI, zipJ, column;
		Double zipDiff, dist;
		for(int i = 0; i<zips.size(); i++){
			for(int j = 0; j<zips.size(); j++){
				zipI = zips.get(i);
				zipJ = zips.get(j);
				sumSq = 0.0;
				//calculate similarity for this zip pair
				for(int k = 0; k<columns.size();k++){
					column = columns.get(k);
					try {
	          zipDiff = dataZscored[k][i] - dataZscored[k][j];
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
