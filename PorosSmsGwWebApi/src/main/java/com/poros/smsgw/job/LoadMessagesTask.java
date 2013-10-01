package com.poros.smsgw.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.poros.smsgw.cluster.MessageQueue;
import com.poros.smsgw.ms.MessageItem;
import com.poros.smsgw.ms.MessageStoreAdapterIntf;
import com.poros.smsgw.smpp.gateway.ConfigBean;
import com.poros.smsgw.util.Constants;
import com.poros.smsgw.util.MainConfig;

public class LoadMessagesTask extends BaseTask {

	public MessageStoreAdapterIntf messageStoreAdapter;
	public MainConfig mainConfig;

	public MainConfig getMainConfig() {
		return mainConfig;
	}

	public void setMainConfig(MainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	public MessageStoreAdapterIntf getMessageStoreAdapter() {
		return messageStoreAdapter;
	}

	@Autowired
	public void setMessageStoreAdapter(
			MessageStoreAdapterIntf messageStoreAdapter) {
		this.messageStoreAdapter = messageStoreAdapter;
	}

	public static void main(String[] args) {
		System.out.println(new Date());
	}

	@Override
	public void run() {
		int blackHourStart = getMainConfig().getBlackHourStartHour();
		int blackHourStop = getMainConfig().getBlackHourStopHour();

		if (new Date().getHours() < blackHourStop
				|| new Date().getHours() > blackHourStart) {
			logger.debug("System is in blackhours.Loading messages is not working Black Start Hour:"
					+ blackHourStart + " Stop Hour:" + blackHourStop);
			return;
		}

		List messages = messageStoreAdapter.getNewMessages();
		logger.debug("Loading messages started. Message Count = {}",
				messages.size());

		for (Iterator iterator = messages.iterator(); iterator.hasNext();) {
			MessageItem ms = (MessageItem) iterator.next();

			try {
				MessageQueue.putMessageQueue(ms);
				messageStoreAdapter.updateMessageSendingStatus(ms.getId(),
						Constants.SendingStatus_Processing);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		logger.debug("Loading Messages Finished");

	}
}
