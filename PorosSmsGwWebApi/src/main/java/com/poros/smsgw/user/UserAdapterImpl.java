package com.poros.smsgw.user;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserAdapterImpl implements UserAdapterIntf {

	final String INSERT_SQL = "insert into USERS (username,password) values(?,?)";
	final String DELETE_SQL = "delete from USERS where username=?";
	final String SELECT_USER_WITH_USERNAME = "select * from USERS where username=?";

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public boolean checkUser(String username, String password) {
		User user = getUser(username);
		if (user == null) {
			return false;
		}
		if (password.equals(user.getPassword())) {
			return true;
		}
		return false;
	}

	@Override
	public void createUser(User user) {
		jdbcTemplate.update(INSERT_SQL, new Object[] { user.getUsername(),
				user.getPassword() });
	}

	@Override
	public void deleteUser(String username) {
		jdbcTemplate.update(DELETE_SQL, new Object[] { username });

	}

	@Override
	public User getUser(String username) {
		return (User) jdbcTemplate.queryForObject(SELECT_USER_WITH_USERNAME,
				new Object[] { username }, new UserRM());

	}

}
