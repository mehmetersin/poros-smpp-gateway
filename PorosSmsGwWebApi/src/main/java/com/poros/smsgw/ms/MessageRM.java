package com.poros.smsgw.ms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MessageRM implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int line) throws SQLException {
		MessageItem ms = new MessageItem();
		
		ms.setId(rs.getInt("Id"));
		ms.setDetailId(rs.getInt("detailId"));
		ms.setDestination(rs.getString("destination"));
		ms.setSendingStatus(rs.getInt("sendingStatus"));
		ms.setTransactionId(rs.getString("transactionId"));
		ms.setRetryCount(rs.getInt("retryCount"));
		ms.setState(rs.getInt("state"));
		ms.setProcessingDate(rs.getTimestamp("processingDate"));
		ms.setDeliveryDate(rs.getTimestamp("deliveryDate"));
		ms.setText(rs.getString("text"));
		ms.setOriginator(rs.getString("originator"));
		ms.setCompleted(rs.getInt("completed"));
		ms.setStartDate(rs.getTimestamp("startDate"));
		ms.setUserId(rs.getInt("userId"));
		ms.setSendingStatus(rs.getInt("sendingStatus"));
		return ms;
	}

}
