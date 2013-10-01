package com.poros.smsgw.transmitter;

import com.poros.smsgw.ms.MessageItem;

public interface TransmitterAdapterIntf {

	public String sendSMS(MessageItem message);
	

	public int queryMessage(MessageItem request);
}
