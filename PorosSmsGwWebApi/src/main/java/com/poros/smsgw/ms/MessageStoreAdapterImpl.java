package com.poros.smsgw.ms;

import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.poros.smsgw.util.Constants;

public class MessageStoreAdapterImpl implements MessageStoreAdapterIntf {

	private SimpleJdbcInsert insertTemplate;
	private JdbcTemplate selectTemplate;

	private String selectForProcess = "select * from messages m, message_detail d where m.detailid=d.id and d.completed=0 "
			+ "and m.sendingStatus="
			+ Constants.SendingStatus_New
			+ " and d.startDate <= ?" + " and ROWNUM < ?";

	private String updateSingleMessageState = "update MESSAGES set sendingStatus=?,transactionId=? where ID =?";
	private String updateSingleMessageError = "update MESSAGES set sendingStatus=?,retryCount=retryCount+1 where ID =?";

	private String updateMessageProcessing = "update MESSAGES set sendingStatus=?,processingDate=? where ID =?";

	private String updateMessageState = "update MESSAGES set state=?,sendingStatus=?,deliverydate=? where transactionId =?";

	private String selectMessageItem = "select * from MESSAGES where ID=?";

	private String selectMessageDetail = "select * from MESSAGE_DETAIL where ID=?";

	private String updateFixPendingMessageItem = "update MESSAGES set sendingStatus=? where processingDate<? and sendingStatus=?";

	public void setDataSource(DataSource dataSource) {
		this.insertTemplate = new SimpleJdbcInsert(dataSource);
		if (insertTemplate.getTableName() != "MESSAGES") {
			insertTemplate.setTableName("MESSAGES");
			insertTemplate.setGeneratedKeyName("ID");
		}
		insertTemplate.compile();
		selectTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public int insertMessage(MessageItem message) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
				message);

		keyHolder = insertTemplate.executeAndReturnKeyHolder(namedParameters);
		return keyHolder.getKey().intValue();
	}

	@Override
	public java.util.List getNewMessages() {
		return (java.util.List) selectTemplate.query(selectForProcess,
				new Object[] { new Timestamp(new Date().getTime()), 500 },
				new MessageRM());
	}

	@Override
	public int fixPendingMessages() {
		Date twohoursago = new Date();
		twohoursago.setHours(twohoursago.getHours() - 4);
		// TODO move this configuration to config file
		return selectTemplate.update(updateFixPendingMessageItem,
				new Object[] { Constants.SendingStatus_New,
						new Timestamp(twohoursago.getTime()),
						Constants.SendingStatus_Processing });
	}

	@Override
	public MessageItem queryMessage(int id) {
		return (MessageItem) selectTemplate.queryForObject(selectMessageItem,
				new Object[] { id }, new MessageRM());
	}

	@Override
	public void updateMessageState(int id, int state, String messageTi,
			boolean isError) {
		if (isError) {
			selectTemplate.update(updateSingleMessageError, new Object[] {
					state, id });
		} else {
			selectTemplate.update(updateSingleMessageState, new Object[] {
					state, messageTi, id });
		}

	}

	@Override
	public void updateMessageSendingStatus(int id, int status) {
		Date now = new Date();
		selectTemplate.update(updateMessageProcessing, new Object[] { status,
				new Timestamp(now.getTime()), id });
	}

	@Override
	public void updateFinalMessageState(String id, int state, int status) {
		try {
			int a = selectTemplate.update(updateMessageState, new Object[] {
					state, status, new Timestamp(new Date().getTime()),id });
			System.out.println("effected"+a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
