package com.realtalkserver.test;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.Test;

import com.realtalkserver.util.UserManager;

public class UserManagerTest {

	@Test
	public void testFAddUser() {
		try {
			// Exception should not be thrown
			UserManager.fAddUser("TestUser", "TestPassword", "TestId");
		} catch (Exception e) {
			// Exception thrown: error
			assertTrue(false);
		}
	}

}
