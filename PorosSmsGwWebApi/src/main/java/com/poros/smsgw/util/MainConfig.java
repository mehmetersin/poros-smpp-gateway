package com.poros.smsgw.util;


public class MainConfig {

	private int maxRetryCount;
	private int blackHourStartHour;
	private int blackHourStopHour;
	public int getMaxRetryCount() {
		return maxRetryCount;
	}
	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}
	public int getBlackHourStartHour() {
		return blackHourStartHour;
	}
	public void setBlackHourStartHour(int blackHourStartHour) {
		this.blackHourStartHour = blackHourStartHour;
	}
	public int getBlackHourStopHour() {
		return blackHourStopHour;
	}
	public void setBlackHourStopHour(int blackHourStopHour) {
		this.blackHourStopHour = blackHourStopHour;
	}

	

}
