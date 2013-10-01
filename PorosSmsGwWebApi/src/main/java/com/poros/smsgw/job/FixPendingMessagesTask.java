package com.poros.smsgw.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.poros.smsgw.ms.MessageStoreAdapterIntf;

public class FixPendingMessagesTask extends BaseTask {

	public MessageStoreAdapterIntf messageStoreAdapter;

	public MessageStoreAdapterIntf getMessageStoreAdapter() {
		return messageStoreAdapter;
	}

	@Autowired
	public void setMessageStoreAdapter(
			MessageStoreAdapterIntf messageStoreAdapter) {
		this.messageStoreAdapter = messageStoreAdapter;
	}

	@Override
	public void run() {

		logger.debug("FixPendingMessagesTask Task Started.");

		int count = messageStoreAdapter.fixPendingMessages();

		logger.debug("FixPendingMessagesTask Task Finished. Number of Messages Effected:"
				+ count);

	}
}
