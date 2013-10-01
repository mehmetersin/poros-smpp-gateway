/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.poros.smsgw.smpp.gateway;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.bean.OptionalParameters;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.poros.smsgw.ms.MessageStoreAdapterIntf;
import com.poros.smsgw.util.Constants;

/**
 * This are implementation of {@link SmsGateway}. This gateway will reconnect
 * for a specified interval if the session are closed.
 * 
 * @author uudashr
 * 
 */
public class AutoReconnectGateway implements SmsGateway {
	private static final Logger logger = LoggerFactory
			.getLogger(AutoReconnectGateway.class);
	private SMPPSession session = null;
	private String remoteIpAddress;
	private int remotePort;
	private BindParameter bindParam;
	public MessageStoreAdapterIntf messageStoreAdapter;
	private long reconnectInterval = 10000; // 5 seconds

	/**
	 * Construct auto reconnect gateway with specified ip address, port and SMPP
	 * Bind parameters.
	 * 
	 * @param remoteIpAddress
	 *            is the SMSC IP address.
	 * @param remotePort
	 *            is the SMSC port.
	 * @param bindParam
	 *            is the SMPP Bind parameters.
	 * @throws IOException
	 */

	public AutoReconnectGateway() {

	}

	public MessageStoreAdapterIntf getMessageStoreAdapter() {
		return messageStoreAdapter;
	}

	public void setMessageStoreAdapter(
			MessageStoreAdapterIntf messageStoreAdapter) {
		this.messageStoreAdapter = messageStoreAdapter;
	}

	public AutoReconnectGateway(String remoteIpAddress, int remotePort,
			BindParameter bindParam, long reconnectInterval) throws IOException {
		this.remoteIpAddress = remoteIpAddress;
		this.remotePort = remotePort;
		this.bindParam = bindParam;
		this.reconnectInterval = reconnectInterval;
		session = newSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jsmpp.examples.gateway.Gateway#submitShortMessage(java.lang.String,
	 * org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator,
	 * java.lang.String, org.jsmpp.bean.TypeOfNumber,
	 * org.jsmpp.bean.NumberingPlanIndicator, java.lang.String,
	 * org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String,
	 * org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding, byte,
	 * byte[], org.jsmpp.bean.OptionalParameter[])
	 */
	public String submitShortMessage(String serviceType,
			TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
			String sourceAddr, TypeOfNumber destAddrTon,
			NumberingPlanIndicator destAddrNpi, String destinationAddr,
			ESMClass esmClass, byte protocolId, byte priorityFlag,
			String scheduleDeliveryTime, String validityPeriod,
			RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag,
			DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
			OptionalParameter... optionalParameters) throws PDUException,
			ResponseTimeoutException, InvalidResponseException,
			NegativeResponseException, IOException {

		return getSession().submitShortMessage(serviceType, sourceAddrTon,
				sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi,
				destinationAddr, esmClass, protocolId, priorityFlag,
				scheduleDeliveryTime, validityPeriod, registeredDelivery,
				replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage,
				optionalParameters);
	}

	/**
	 * Create new {@link SMPPSession} complete with the
	 * {@link SessionStateListenerImpl}.
	 * 
	 * @return the {@link SMPPSession}.
	 * @throws IOException
	 *             if the creation of new session failed.
	 */

	public static String openFileToString(byte[] _bytes) {
		String file_string = "";

		for (int i = 0; i < _bytes.length; i++) {
			file_string += (char) _bytes[i];
		}

		return file_string;
	}

	private static byte[] getValue(OptionalParameter param) {
		byte[] value = param.serialize();
		byte[] result = new byte[value.length - 4];
		System.arraycopy(value, 4, result, 0, value.length - 4);
		return result;
	}

	private SMPPSession newSession() throws IOException {

		SMPPSession tmpSession = new SMPPSession(remoteIpAddress, remotePort,
				bindParam);
		tmpSession.setTransactionTimer(3000L);

		tmpSession.addSessionStateListener(new SessionStateListenerImpl());

		// Set listener to receive deliver_sm

		tmpSession.setMessageReceiverListener(new MessageReceiverListener() {
			public void onAcceptDeliverSm(DeliverSm deliverSm)
					throws ProcessRequestException {

				if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm
						.getEsmClass())) {
					try {

						logger.debug("Message {},{}",
								deliverSm.getOptionalParametes());
						String messageId = null;
						int messageState = 0;
						for (int i = 0; i < deliverSm.getOptionalParametes().length; i++) {
							OptionalParameter p = deliverSm
									.getOptionalParametes()[i];
							if (p.tag == Tag.RECEIPTED_MESSAGE_ID.code()) {
								messageId =  ((org.jsmpp.bean.OptionalParameter.OctetString) p).getValueAsString() ;
								//messageId = new String(getValue(p));
								logger.debug("message id :" + messageId);
								continue;
							}
							if (p.tag == Tag.MESSAGE_STATE.code()) {
								messageState = Integer
										.valueOf(((org.jsmpp.bean.OptionalParameter.Byte) p)
												.getValue());
								logger.debug("message state :" + p.toString());
								continue;
							}

						}

						// DeliveryReceipt delReceipt = deliverSm.
						// .getShortMessageAsDeliveryReceipt();

						if (messageId != null && messageState != -1) {
							getMessageStoreAdapter().updateFinalMessageState(
									messageId,
									messageState,Constants.SendingStatus_Finished_Success);
							logger.debug("Message State updated :{}:{}:",
									messageId, messageState);
						}

					} catch (Exception e) {
						logger.error("Failed getting delivery receipt", e);
					}
				} else {
					logger.debug("Receiving message but type is not delivery receipt : "
							+ new String(deliverSm.getShortMessage()));
				}
			}

			public void onAcceptAlertNotification(
					AlertNotification alertNotification) {
			}

			public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
					throws ProcessRequestException {
				return null;
			}

		});

		return tmpSession;
	}

	/**
	 * Get the session. If the session still null or not in bound state, then IO
	 * exception will be thrown.
	 * 
	 * @return the valid session.
	 * @throws IOException
	 *             if there is no valid session or session creation is invalid.
	 */
	private SMPPSession getSession() throws IOException {
		if (session == null) {
			logger.info("Initiate session for the first time to "
					+ remoteIpAddress + ":" + remotePort);
			session = newSession();
		} else if (!session.getSessionState().isBound()) {
			throw new IOException("We have no valid session yet");
		}
		return session;
	}

	/**
	 * Reconnect session after specified interval.
	 * 
	 * @param timeInMillis
	 *            is the interval.
	 */
	private void reconnectAfter(final long timeInMillis) {
		new Thread() {
			@Override
			public void run() {
				logger.info("Schedule reconnect after " + timeInMillis
						+ " millis");
				try {
					Thread.sleep(timeInMillis);
				} catch (InterruptedException e) {
				}

				int attempt = 0;
				while (session == null
						|| session.getSessionState()
								.equals(SessionState.CLOSED)) {
					try {
						logger.info("Reconnecting attempt #" + (++attempt)
								+ "...");
						session = newSession();
					} catch (IOException e) {
						logger.error("Failed opening connection and bind to "
								+ remoteIpAddress + ":" + remotePort, e);
						// wait for a second
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ee) {
						}
					}
				}
			}
		}.start();
	}

	/**
	 * This class will receive the notification from {@link SMPPSession} for the
	 * state changes. It will schedule to re-initialize session.
	 * 
	 * @author uudashr
	 * 
	 */
	private class SessionStateListenerImpl implements SessionStateListener {
		public void onStateChange(SessionState newState, SessionState oldState,
				Object source) {
			if (newState.equals(SessionState.CLOSED)) {
				logger.info("Session closed");
				reconnectAfter(reconnectInterval);
			}
		}
	}

	@Override
	public QuerySmResult queryShortMessage(String messageId,
			TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
			String sourceAddr) throws Exception {
		return getSession().queryShortMessage(messageId, sourceAddrTon,
				sourceAddrNpi, sourceAddr);
	}

}
