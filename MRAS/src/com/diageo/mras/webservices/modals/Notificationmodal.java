package com.diageo.mras.webservices.modals;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Notificationmodal {

	String deviceId;
	String NotificationContentEnglish;
	String NotificationContentRegional;
	int devicetype;
	String name;
	int offerId;


	public Notificationmodal() {

	}

	public Notificationmodal(String deviceId, int devicetype,
			String notificationContentEnglish,
			String notificationContentRegional) {

		this.deviceId = deviceId;
		NotificationContentEnglish = notificationContentEnglish;
		NotificationContentRegional = notificationContentRegional;
		this.devicetype = devicetype;
	}
	
	public Notificationmodal(String deviceId, int devicetype,
			String notificationContentEnglish,
			String notificationContentRegional,String name,int offerId) {

		this.deviceId = deviceId;
		NotificationContentEnglish = notificationContentEnglish;
		NotificationContentRegional = notificationContentRegional;
		this.devicetype = devicetype;
		this.name=name;
		this.offerId=offerId;
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(int devicetype) {
		this.devicetype = devicetype;
	}

	public String getNotificationContentEnglish() {
		return NotificationContentEnglish;
	}

	public void setNotificationContentEnglish(String notificationContentEnglish) {
		NotificationContentEnglish = notificationContentEnglish;
	}

	public String getNotificationContentRegional() {
		return NotificationContentRegional;
	}

	public void setNotificationContentRegional(
			String notificationContentRegional) {
		NotificationContentRegional = notificationContentRegional;
	}

	public String toString() {
		return ("Device ID: " + deviceId + " NotificationContentEnglish:" + NotificationContentEnglish+
				" NotificationContentRegional"+NotificationContentRegional +
				"\n");
	}

	public boolean equals(Object o) {		
		if (!(o instanceof Notificationmodal))
			return false;

		Notificationmodal t = (Notificationmodal) o;

		return this.deviceId.equals(t.deviceId) && this.offerId==t.offerId 
		    ;

	}

	public int hashCode(){
		return 9;
	}
}
