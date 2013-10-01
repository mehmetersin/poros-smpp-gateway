package com.poros.smsgw.ms;

import java.io.Serializable;
import java.sql.Timestamp;

import com.poros.smsgw.smpp.SmsMsgReq;
import com.poros.smsgw.util.Constants;

public class MessageItem implements Serializable {

	private int id;
	private int detailId;
	private String destination;
	private int sendingStatus = Constants.SendingStatus_New;
	private String transactionId;
	private int retryCount = 0;
	private int state = Constants.MessageState_NoInfo;
	private Timestamp processingDate;
	private Timestamp deliveryDate;
	
	
	private String text;
	private String originator;
	private int completed = 0;
	private Timestamp startDate;
	private int userId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getSendingStatus() {
		return sendingStatus;
	}
	public void setSendingStatus(int sendingStatus) {
		this.sendingStatus = sendingStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Timestamp getProcessingDate() {
		return processingDate;
	}
	public void setProcessingDate(Timestamp processingDate) {
		this.processingDate = processingDate;
	}
	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOriginator() {
		return originator;
	}
	public void setOriginator(String originator) {
		this.originator = originator;
	}
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	

	
	
	

}
