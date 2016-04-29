import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class GoogleApisWrapperTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetLatLongForZip() {
		Double[] latLong;
		latLong= GoogleApisWrapper.getLatLongForZip("19143");
		assertEquals(latLong[0], 39.9412, 0.0001);
		assertEquals(latLong[1], -75.2187, 0.0001);

		latLong = GoogleApisWrapper.getLatLongForZip("18972");
		assertEquals(latLong[0], 40.5434, 0.0001);
		assertEquals(latLong[1], -75.1195, 0.0001);
	
	}

}
