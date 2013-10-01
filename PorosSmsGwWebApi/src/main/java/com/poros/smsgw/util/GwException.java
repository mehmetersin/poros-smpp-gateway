package com.poros.smsgw.util;

public class GwException extends RuntimeException {

	private int resultCode = Constants.RESULT_CODE_SUCCESS;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public GwException() {

	}

	public GwException(int resultCode) {
		this.resultCode = resultCode;
	}

}
