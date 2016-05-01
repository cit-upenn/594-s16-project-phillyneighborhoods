package wutever;

import java.util.ArrayList;
import java.util.Collection;

public class Dataframe {

	private ArrayList<String> columns; //not including index
	private ArrayList<String> index;
	private ArrayList<ArrayList<Double>> data;
	
	public Dataframe(){
		columns = new ArrayList<String>();
		index = new ArrayList<String>();
		data = new ArrayList<ArrayList<Double>>();
	}
	
	public void setColumns(Collection<String> columns){
		for (String column : columns){
			columns.add(column);
			data.add(new ArrayList<Double>());
		}
	}

	public ArrayList<String> getColumns(){
		return columns;
	}
	
	//must be called after column setting
	public void setIndex(Collection<String> indicies) {
		for (String thisIndex : indicies){
			index.add(thisIndex);
		}		
	}

	public ArrayList<String> getIndex(){
		return index;
	}
	
	public void setValue(String indexVal, String column, Double dataVal){
		int indexIndex = index.indexOf(indexVal);
		int columnsIndex = columns.indexOf(column);
		data.get(columnsIndex).set(indexIndex, dataVal);
	}

	
}
