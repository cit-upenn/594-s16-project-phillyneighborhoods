package wutever;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class SimilarityCalcTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDoit() {
		ArrayList<String> zips = SimilarityCalc.getZips();
		Double[][] similarities = SimilarityCalc.doit();
		assertEquals(zips.size(), 47);
		assertEquals(similarities[6][6], new Double(0.0), 0.01);
	}
	

}
