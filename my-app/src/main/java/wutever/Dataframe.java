package wutever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Dataframe {

	private ArrayList<String> columns; //not including index
	private ArrayList<String> indicies;
	private Double[][] data; 
	
	public Dataframe(Collection<String> columns, Collection<String> indicies){
		this.columns = new ArrayList<String>(columns);
		this.indicies = new ArrayList<String>(indicies);		
		data = new Double[columns.size()][indicies.size()];		
	}
	
	public ArrayList<String> getColumns(){
		return columns;
	}
	
	public ArrayList<String> getIndicies(){
		return indicies;
	}
	
	public void setValue(String column, String index, Double dataVal){
		int indexIndex = indicies.indexOf(index);
		int columnIndex = columns.indexOf(column);
		data[columnIndex][indexIndex] = dataVal;
	}

	public Double getValue(String column, String index){
		int indexIndex = indicies.indexOf(index);
		int columnIndex = columns.indexOf(column);
		return data[columnIndex][indexIndex];
	}

	public Double[] getRow(String indexName){
		Double[] retVal = new Double[columns.size()];
		int indexIndex = indicies.indexOf(indexName);
		for(int i=0; i<columns.size(); i++){
			retVal[i] = data[i][indexIndex];
		}
		return retVal;
	}

	public Double[] getColumn(String columnName){
		Double[] retVal = new Double[indicies.size()];
		int columnIndex = columns.indexOf(columnName);
		for(int i=0; i<indicies.size(); i++){
			retVal[i] = data[columnIndex][i];
		}
		return retVal;
	}
	
	public Dataframe zScore(){
		Dataframe retVal = new Dataframe(columns, indicies);

		Double sum, sumSq, mean, stdDev;
		for(int i=0; i<columns.size(); i++){
			//calculate stats!
			sum = 0.0;
			sumSq = 0.0;
			for(int j=0; j<indicies.size(); j++){
				sum += data[i][j];
				sumSq += data[i][j] * data[i][j];
			}
			mean = sum/indicies.size();
			stdDev = Math.sqrt(sumSq/indicies.size());
			//fill out zscore data
			for(int j=0; j<indicies.size(); j++){
				retVal.setValue(columns.get(i), indicies.get(j), (data[i][j]-mean)/stdDev);
			}
		}

		return retVal;
	}
	
	public void quickPrint(){
		System.out.println(columns);
		for(int i = 0; i<indicies.size(); i++){
			List<Double> row = Arrays.asList(getRow(indicies.get(i)));
			System.out.println(row);
		}
	}

	
}
