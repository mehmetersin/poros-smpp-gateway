package com.poros.smsgw.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.poros.smsgw.ms.MessageItem;
import com.poros.smsgw.ms.MessageStoreAdapterIntf;
import com.poros.smsgw.smpp.gateway.ConfigBean;
import com.poros.smsgw.transmitter.TransmitterAdapterIntf;
import com.poros.smsgw.util.Constants;
import com.poros.smsgw.util.MainConfig;

public class MessageSenderEngine implements MessageSenderEngineIntf {

	public MessageStoreAdapterIntf messageStoreAdapter;
	private TransmitterAdapterIntf smppAdapter;
	private MainConfig mainConfig;

	public MessageStoreAdapterIntf getMessageStoreAdapter() {
		return messageStoreAdapter;

	}

	public MainConfig getMainConfig() {
		return mainConfig;
	}

	public void setMainConfig(MainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	@Autowired
	public void setMessageStoreAdapter(
			MessageStoreAdapterIntf messageStoreAdapter) {
		this.messageStoreAdapter = messageStoreAdapter;
	}

	public TransmitterAdapterIntf getSmppAdapter() {
		return smppAdapter;
	}

	@Autowired
	public void setSmppAdapter(TransmitterAdapterIntf smppAdapter) {
		this.smppAdapter = smppAdapter;
	}

	@Async
	@Override
	public void sendSMS(MessageItem message) {
		try {
			String ti = smppAdapter.sendSMS(message);
			messageStoreAdapter.updateMessageState(message.getId(),
					Constants.SendingStatus_Finished_Success, ti, false);
		} catch (Exception e) {

			if (message.getRetryCount() > Integer.valueOf(getMainConfig()
					.getMaxRetryCount())) {
				messageStoreAdapter.updateMessageState(message.getId(),
						Constants.SendingStatus_Finished_Failed, null, true);
			} else {
				messageStoreAdapter.updateMessageState(message.getId(),
						Constants.SendingStatus_New, null, true);
			}

		}

	}

}
