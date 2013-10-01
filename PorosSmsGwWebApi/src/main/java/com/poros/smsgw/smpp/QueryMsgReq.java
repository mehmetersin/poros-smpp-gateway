package com.poros.smsgw.smpp;

import java.io.Serializable;


public class QueryMsgReq extends BaseReq implements Serializable {

	private int messageId;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

}
