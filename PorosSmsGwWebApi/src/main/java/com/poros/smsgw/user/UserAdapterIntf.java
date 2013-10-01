package com.poros.smsgw.user;


public interface UserAdapterIntf {

	public void createUser(User user);
	public void deleteUser(String username);
	public User getUser(String username);
	public boolean checkUser(String username,String password);
	
}
