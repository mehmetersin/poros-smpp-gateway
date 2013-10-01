package com.poros.smsgw.job;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTask extends TimerTask {

	public static final Logger logger = LoggerFactory
			.getLogger("scheduleLogger");

	@Override
	public abstract void run();

}
