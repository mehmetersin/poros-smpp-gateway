package com.poros.smsgw.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRM implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int line) throws SQLException {
		User user = new User();
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setUserId(rs.getInt("ID"));
		return user;
	}
//
}
