package com.poros.smsgw.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.poros.smsgw.engine.MessageSenderEngineIntf;

public class SendMessagesTask extends BaseTask {

	private int threadCount = 5;
	private ExecutorService executor;

	public int getThreadCount() {
		return threadCount;
	}

	@Autowired
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public MessageSenderEngineIntf senderEngine;

	public MessageSenderEngineIntf getSenderEngine() {
		return senderEngine;
	}

	@Autowired
	public void setSenderEngine(MessageSenderEngineIntf senderEngine) {
		this.senderEngine = senderEngine;
	}

	public ExecutorService getExecuter() {
		if (executor == null || executor.isShutdown()) {
			executor = Executors.newFixedThreadPool(getThreadCount());
		}
		return executor;
	}

	@Override
	public void run() {
		logger.debug("Sending Message Started.");
		for (int i = 0; i < getThreadCount(); i++) {
			Runnable worker = new MessageSenderWorker(senderEngine);
			getExecuter().execute(worker);
		}
		logger.debug("Finished started threads");

	}
}
