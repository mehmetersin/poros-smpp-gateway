package com.poros.smsgw.smpp;

import java.util.Date;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.RelativeTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.poros.smsgw.ms.MessageItem;
import com.poros.smsgw.ms.MessageStoreAdapterIntf;
import com.poros.smsgw.smpp.gateway.AutoReconnectGateway;
import com.poros.smsgw.smpp.gateway.ConfigBean;
import com.poros.smsgw.smpp.gateway.SmsGateway;
import com.poros.smsgw.transmitter.TransmitterAdapterIntf;
import com.poros.smsgw.util.Constants;
import com.poros.smsgw.util.GwException;

public class SmppAdapterImpl implements TransmitterAdapterIntf {

	private static SmsGateway gateway = null;
	
	public MessageStoreAdapterIntf messageStoreAdapter;

	private static final Logger logger = LoggerFactory
			.getLogger(SmppAdapterImpl.class);

	private static RelativeTimeFormatter timeFormatter = new RelativeTimeFormatter();

	
	public void init(){
		try {
			getSmsGw();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public MessageStoreAdapterIntf getMessageStoreAdapter() {
		return messageStoreAdapter;
	}




	public void setMessageStoreAdapter(MessageStoreAdapterIntf messageStoreAdapter) {
		this.messageStoreAdapter = messageStoreAdapter;
	}




	public static SmsGateway getGateway() {
		return gateway;
	}




	public static void setGateway(SmsGateway gateway) {
		SmppAdapterImpl.gateway = gateway;
	}




	private SmsGateway getSmsGw() {
		if (gateway == null) {
			try {

				ConfigBean config = (ConfigBean) ConfigBean.getBeanFactory()
						.getBean("configBean");

				gateway = new AutoReconnectGateway(config.getRemoteipaddress(),
						Integer.valueOf(config.getRemoteport()).intValue(),
						new BindParameter(BindType.BIND_TRX, config
								.getSystemid(), config.getPassword(), config
								.getSystemtype(), TypeOfNumber.UNKNOWN,
								NumberingPlanIndicator.ISDN, null),config.getReconnectInterval());
				
				((AutoReconnectGateway) gateway).setMessageStoreAdapter(getMessageStoreAdapter());

			} catch (Exception e) {
				logger.error("error occured", e);
				throw new GwException(Constants.RESULT_SMPP_CONNECTION_FAILED);
			}
		}
		return gateway;
	}

	
	public int queryMessage(MessageItem request) {
		return 0;
			

//		logger.debug("Query Message Status {}");
//
//		try {
//			QuerySmResult response = getSmsGw().queryShortMessage(
//					request.getMessageTransactionId(),
//					TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN,
//					"+"+request.getSource());
//
//			int resultCode = response.getMessageState().value();
//			return resultCode;
//		} catch (Exception e) {
//			logger.error("Exception while sending sms", e);
//			throw new GwException(Constants.RESULT_QUERY_MESSAGE_FAILED);
//		}

	}

	
	@Override
	public String sendSMS(MessageItem request) {
		
		
		logger.debug("{} Sms sending started Id = {}", Thread.currentThread()
				.getName(), request.getId());
		try {
			String ti = getSmsGw().submitShortMessage(
					"CMT",
					TypeOfNumber.ALPHANUMERIC,
					NumberingPlanIndicator.ISDN ,
					request.getOriginator(),
					TypeOfNumber.INTERNATIONAL,
					NumberingPlanIndicator.ISDN ,
					request.getDestination(),
					new ESMClass(),
					(byte) 0,
					(byte) 1,
					null, //timeFormatter.format(new Date()),
					null,
					new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
					(byte) 0, 
					DataCoding.newInstance(0), (byte) 0,
					request.getText().getBytes());
			logger.debug("{} Sms sending finished Id = {}", Thread
					.currentThread().getName(), request.getId());
			return ti;
		} catch (Exception e) {
			logger.error("Exception while sending sms", e);
			throw new GwException(Constants.RESULT_SMS_SENDING_FAILED);
		}
	}

}
