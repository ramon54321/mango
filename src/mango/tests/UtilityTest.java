package mango.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import mango.utilities.*;
import mango.dto.*;

public class UtilityTest {
	
	@Test
	public void validation1() {
		boolean valid = DataServices.validateCredentials("Ramon", "ramon").successfulLogin;
		assertEquals(true, valid);
	}
	
	@Test
	public void validation2() {
		boolean valid = DataServices.validateCredentials("Bob", "123").successfulLogin;
		assertEquals(false, valid);
	}
	
	@Test
	public void hash1() {
		String hash = DataServices.getHash("Hello12345");
		assertEquals(false, hash.equals("Hello12345"));
	}
	
	@Test
	public void hash2() {
		String hash = DataServices.getHash("Hello12345");
		assertEquals(true, hash.equals("40f87cdd28e39587cd8a22dfd0586269"));
	}
}
