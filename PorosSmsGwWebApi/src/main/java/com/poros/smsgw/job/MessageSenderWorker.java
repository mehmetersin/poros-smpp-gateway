package com.poros.smsgw.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poros.smsgw.cluster.MessageQueue;
import com.poros.smsgw.engine.MessageSenderEngineIntf;
import com.poros.smsgw.ms.MessageItem;

public class MessageSenderWorker implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger("scheduleLogger");

	public MessageSenderEngineIntf senderEngine;

	public MessageSenderWorker(MessageSenderEngineIntf se) {
		senderEngine = se;
	}

	public MessageSenderEngineIntf getSenderEngine() {
		return senderEngine;
	}

	public void setSenderEngine(MessageSenderEngineIntf senderEngine) {
		this.senderEngine = senderEngine;
	}

	@Override
	public void run() {
		int count = 0;
		logger.debug("Thread {}.Sending Message Started.", Thread
				.currentThread().getName());

		while (true) {
			MessageItem ms = MessageQueue.getMessage();
			if (ms == null) {
				logger
						.debug(
								"Thread {} Sending sms is finished in queqe.Total sended message {}",
								Thread.currentThread().getName(), count);
				break;
			}
			if (count >=1000){
				logger.debug("{} Working Thread is finished.Processed Message Total sended message {}",
								Thread.currentThread().getName(), count);
			}
			
			count++;
			senderEngine.sendSMS(ms);
		}

		logger.debug("Thread {}.Sending Message Finished", Thread
				.currentThread().getName());

	}

}
