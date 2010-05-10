package edu.brown.cs32.siliclone.database.client.test;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.brown.cs32.siliclone.accounts.User;
import edu.brown.cs32.siliclone.database.client.DataServiceException;
import edu.brown.cs32.siliclone.database.client.UserService;
import edu.brown.cs32.siliclone.database.client.UserServiceAsync;


public class UserServiceTest extends GWTTestCase{
	public String getModuleName() {
		return "edu.brown.cs32.siliclone.Siliclone";
	}
	
	public void testUserAccount() {
		//to test rpc methods, chain together and wait for last one to finish
		
		final User u = new User("name", "password", "email");
		
		final UserServiceAsync service = GWT.create(UserService.class);
		
		
		
		
		

		final AsyncCallback<Void> deleteAccountTest = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {
				fail("delete test error : " + caught.getMessage());
			}
			public void onSuccess(Void result) {
				System.out.println("account deleted");
				finishTest();
			}
		};
		
		
		final AsyncCallback<Void> deleteGroupTest = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {
				fail("delete group test error : " + caught.getMessage());
			}
			public void onSuccess(Void result) {
				System.out.println("group deleted");
				try {
					service.remove(u, deleteAccountTest);
				} catch (DataServiceException e) {
					fail(e.getMessage());
				}
			}
		};
		
		final AsyncCallback<List<String>> checkGroupTest = new AsyncCallback<List<String>>(){
			public void onFailure(Throwable caught) {
				fail("check group test error : " + caught.getMessage());
			}
			public void onSuccess(List<String> result) {
				System.out.println("group checked");
				assertTrue(result.contains("group"));
				service.removeGroup("group", deleteGroupTest);
			}
		};
		
		
		final AsyncCallback<Void> createGroupTest = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {
				fail("create group test error : " + caught.getMessage());
			}
			public void onSuccess(Void result) {
				System.out.println("group created");
				try {
					service.getOwnedGroups(checkGroupTest);
				} catch (DataServiceException e) {
					fail(e.getMessage());
				}
			}
		};
		
		
		final AsyncCallback<User> loginTest = new AsyncCallback<User>(){
			public void onFailure(Throwable caught) {
				fail("login test error : " + caught.getMessage());
			}
			public void onSuccess(User result) {
				System.out.println("logged in");
				assertEquals(result.getName(), "name");
				assertNull(result.getPassword());
				assertEquals(result.getEmail(), "email");
				try {
					service.createGroup("group", createGroupTest);
				} catch (DataServiceException e) {
					fail(e.getMessage());
				}
			}
		};		
		
		
		
		final AsyncCallback<Void> logoutTest = new AsyncCallback<Void>(){
			public void onFailure(Throwable caught) {
				fail("logout test error : "  + caught.getMessage());
			}
			public void onSuccess(Void result) {
				System.out.println("logged out");
				try {
					service.login(u, loginTest);
				} catch (DataServiceException e) {
					fail(e.getMessage());
				}
			}
		};
		
		
		AsyncCallback<User> registerTest = new AsyncCallback<User>(){
			public void onFailure(Throwable caught) {
				fail("register test error : " + caught.getMessage());
			}
			public void onSuccess(User result) {
				System.out.println("registered");
				assertEquals(result.getName(), "name");
				assertNull(result.getPassword());
				assertEquals(result.getEmail(), "email");
				service.logout(logoutTest);
			}
		};

		try {
			System.out.println("about to register");
			service.register(u, registerTest);
		} catch (DataServiceException e1) {
			fail("shouldn't catch here ");
			e1.printStackTrace();
		}
		
		
		delayTestFinish(1000);
		
		/*
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
		
		try {
			System.out.println(service.getOwnedGroups());
		} catch (DataServiceException e1) {
			e1.printStackTrace();
		}
		
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
		}*/
	}

}
