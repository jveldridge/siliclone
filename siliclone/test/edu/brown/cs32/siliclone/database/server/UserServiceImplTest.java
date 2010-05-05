package edu.brown.cs32.siliclone.database.server;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.DataServiceException;


public class UserServiceImplTest {

	@Test
	public void testUserAccount() {
		UserServiceImpl service = new UserServiceImpl();
		
		User u = new User();
		u.setName("testuser");
		u.setPassword("password");
		u.setEmail("email@email.com");
		
		try {
			u = service.register(u);
		} catch (DataServiceException e) { 
			fail("User registration failed.");
		}
		assertNull(u.getPassword());
		System.out.println("Successfully registered new user.");
		
		service.logout();
		u.setPassword("password");
		u.setEmail(null);
		try {
			u = service.login(u);
		} catch (DataServiceException e) {
			fail("User login failed.");
		}
		assertEquals(u.getEmail(), "email@email.edu");
		
		try {
			service.remove(u);
		} catch (DataServiceException e) {
			fail("Failed to delete user account.");
		}
	}
}
