package com.poros.smsgw.cluster;

import java.util.concurrent.LinkedBlockingQueue;

import com.poros.smsgw.ms.MessageItem;

public class MessageQueue {

	private static LinkedBlockingQueue<MessageItem> q = new LinkedBlockingQueue<MessageItem>();

	public static void putMessageQueue(MessageItem message) throws Exception {

		q.put(message);

	}

	public static MessageItem getMessage() {
		return q.poll();
	}

}
