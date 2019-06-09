package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.diageo.mras.webservices.modals.PreferencesNew;

@XmlRootElement(name = "Consumer")
@XmlType(propOrder = { "consumerProfile", "preferences", "useraccount" })
public class Consumer {
	private ConsumerProfile consumerProfile;
	private PreferencesNew preferences;
	private UserAccount useraccount;
	
	
	public Consumer(){
		
	}
	
	public Consumer(ConsumerProfile consumerProfile,PreferencesNew preferences,UserAccount useraccount ){
		
		this.consumerProfile=consumerProfile;
		
		this.preferences=preferences;
		this.useraccount=useraccount;
		
		
	}

	@XmlElement(name = "UserAccount")
	public UserAccount getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(UserAccount useraccount) {
		this.useraccount = useraccount;
	}

	@XmlElement(name = "ConsumerProfile")
	public ConsumerProfile getConsumerProfile() {
		return consumerProfile;
	}

	public void setConsumerProfile(ConsumerProfile consumerProfile) {
		this.consumerProfile = consumerProfile;
	}

	@XmlElement(name = "Preferences")
	public PreferencesNew getPreferences() {
		return preferences;
	}

	public void setPreferences(PreferencesNew preferences) {
		this.preferences = preferences;
	}

}
