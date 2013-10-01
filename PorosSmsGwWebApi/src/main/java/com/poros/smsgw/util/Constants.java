package com.poros.smsgw.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	// public static final byte ENROUTE = (byte) 0x01;
	//
	// public static final byte DELIVERED = (byte) 0x02;
	//
	// public static final byte EXPIRED = (byte) 0x03;
	//
	// public static final byte DELETED = (byte) 0x04;
	//
	// public static final byte UNDELIVERABLE = (byte) 0x05;
	//
	// public static final byte ACCEPTED = (byte) 0x06;
	//
	// public static final byte UNKNOWN = (byte) 0x07;
	//
	// public static final byte REJECTED = (byte) 0x08;

	public static final int MessageState_NoInfo = 0;
	public static final int MessageState_Enroute = 1;
	public static final int MessageState_Delivered = 2;
	public static final int MessageState_Expired = 3;
	public static final int MessageState_Deleted = 4;
	public static final int MessageState_Undeliverable = 5;
	public static final int MessageState_Accepted = 6;
	public static final int MessageState_Unknown = 7;
	public static final int MessageState_Rejected = 8;

	public static final int SendingStatus_New = 1;
	public static final int SendingStatus_Processing = 2;
	public static final int SendingStatus_Finished_Failed = 3;
	public static final int SendingStatus_Finished_Success = 4 ;

	public static int RESULT_CODE_SUCCESS = 0;
	public static int RESULT_INVALID_USER = 1;
	public static int RESULT_SMS_SENDING_FAILED = 2;
	public static int RESULT_SMPP_CONNECTION_FAILED = 3;
	public static int RESULT_QUERY_MESSAGE_FAILED = 4;
	public static int RESULT_INVALID_MESSAGE_ID = 5;


}
