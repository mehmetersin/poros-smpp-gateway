package com.poros.smsgw.ms;

import java.util.List;

public interface MessageStoreAdapterIntf {

	public int insertMessage(MessageItem message);

	public List<MessageItem> getNewMessages();

	public void updateMessageState( int id,int state,String messageTi,boolean isError);
	
	public MessageItem queryMessage(int id);

	public void updateMessageSendingStatus(int id, int status);


	public int fixPendingMessages();

	void updateFinalMessageState(String id, int state,int status);

}
