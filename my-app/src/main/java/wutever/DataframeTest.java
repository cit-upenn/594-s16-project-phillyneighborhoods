package wutever;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class DataframeTest {

	Dataframe df;
	
	@Before
	public void setUp() throws Exception {
		String[] columns = {"age", "weight", "salary"}; 
		String[] indicies = {"bob", "sally", "moe"}; 
		df = new Dataframe(Arrays.asList(columns), Arrays.asList(indicies));	
		Double seed=3.9;
		for(int i=0; i<columns.length; i++){
			for(int j=0; j<indicies.length; j++){
				seed = Math.round(((seed*seed)%9.6) * 100.0) / 100.0;
//				System.out.println("" + i + " " + j + " " + seed);
				df.setValue(columns[i], indicies[j], seed);
			}
		}
	}

	@Test
	public void testGetColumns() {		
		df.quickPrint();
		assertEquals(df.getColumns().get(0), "age");
	}

	@Test
	public void testGetIndicies() {
		assertEquals(df.getIndicies().get(2), "moe");
	}


	@Test
	public void testGetValue() {
		assertEquals(df.getValue("age", "bob"), 5.61, 0.0001);
	}


	@Test
	public void testZScore() {
		df.zScore().quickPrint();
	}

}
