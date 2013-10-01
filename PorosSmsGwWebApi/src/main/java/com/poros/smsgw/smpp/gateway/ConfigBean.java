package com.poros.smsgw.smpp.gateway;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class ConfigBean {

	private String remoteipaddress = null;
	private String remoteport = "90";
	private String systemid;
	private String password;
	private String systemtype;
	private String addressrange;
	private long reconnectInterval = 10000; // 5 seconds
	public String getRemoteipaddress() {
		return remoteipaddress;
	}
	public void setRemoteipaddress(String remoteipaddress) {
		this.remoteipaddress = remoteipaddress;
	}
	public String getRemoteport() {
		return remoteport;
	}
	public void setRemoteport(String remoteport) {
		this.remoteport = remoteport;
	}
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSystemtype() {
		return systemtype;
	}
	public void setSystemtype(String systemtype) {
		this.systemtype = systemtype;
	}
	public String getAddressrange() {
		return addressrange;
	}
	public void setAddressrange(String addressrange) {
		this.addressrange = addressrange;
	}
	public long getReconnectInterval() {
		return reconnectInterval;
	}
	public void setReconnectInterval(long reconnectInterval) {
		this.reconnectInterval = reconnectInterval;
	}

	public static XmlBeanFactory getBeanFactory() {

		return new XmlBeanFactory(new ClassPathResource(
				"application-context.xml"));
	}
	
	
	
}
