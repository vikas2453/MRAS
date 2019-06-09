package com.diageo.mras.webservices.modals;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a bean class for the Outlet entity.
 * 
 * @author Infosys Limited
 * @version 1.0
 */
@XmlRootElement
public class Outlet {
	private String outletname;
	private int outletid;
	private long outletCode;
	private String address;
	private String town;
	private String country;
	private int countryCode;
	private double latitude;
	private double longitude;
	private String Channel;
	private String outletFiveDigitCode;
	private String outletTwelveDigitCode;
	private String phoneNumber;
	private String zipCode;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	// phase 2
	private List<String> groups;
	private int checkins;
	private int redemptionTillNow;
	private int maxredemption;
	
	public int getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}


	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public int getCheckins() {
		return checkins;
	}

	public void setCheckins(int checkins) {
		this.checkins = checkins;
	}

	public int getRedemptionTillNow() {
		return redemptionTillNow;
	}

	public void setRedemptionTillNow(int redemptionTillNow) {
		this.redemptionTillNow = redemptionTillNow;
	}

	public int getMaxredemption() {
		return maxredemption;
	}

	public void setMaxredemption(int maxredemption) {
		this.maxredemption = maxredemption;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

/*	public int getCountryid() {
		return countryCode;
	}

	public void setCountryid(int countryCode) {
		this.countryCode = countryCode;
	}*/

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getChannel() {
		return Channel;
	}

	public void setChannel(String channel) {
		Channel = channel;
	}

	public int getOutletid() {
		return outletid;
	}

	public void setOutletid(int outletid) {
		this.outletid = outletid;
	}

	public String getOutletname() {
		return outletname;
	}

	public void setOutletname(String outletname) {
		this.outletname = outletname;
	}

	public String getOutletFiveDigitCode() {
		return outletFiveDigitCode;
	}

	public void setOutletFiveDigitCode(String outletFiveDigitCode) {
		this.outletFiveDigitCode = outletFiveDigitCode;
	}

	public String getOutletTwelveDigitCode() {
		return outletTwelveDigitCode;
	}

	public void setOutletTwelveDigitCode(String outletTwelveDigitCode) {
		this.outletTwelveDigitCode = outletTwelveDigitCode;
	}

	public long getOutlet_Ship_To() {
		return outletCode;
	}

	public void setOutlet_Ship_To(long outlet_Ship_To) {
		this.outletCode = outlet_Ship_To;
	}
	
	
	public String toString() {
		
		StringBuffer resultStringBuffer=new StringBuffer("{outletid: " + outletid  + ", outletCode:" + outletCode + ", latitude:" + latitude 
		+ ", longitude:" + longitude + ", countryCode:" + countryCode+ ", maxredemption:" + maxredemption);
		
		
	/*	String resultString="{outletid: " + outletid  + ", outletCode:" + outletCode + ", latitude:" + latitude 
		+ ", longitude:" + longitude + ", countryCode:" + countryCode+ ", maxredemption:" + maxredemption;
			*/
		
	
		
		if(outletname!=null && !outletname.isEmpty()){
			//resultString=resultString+", outletname:" + outletname;
			resultStringBuffer.append(", outletname:" + outletname);
		}
		if(address!=null && !address.isEmpty()){
			//resultString=resultString+", address:" + address;
			resultStringBuffer.append(", address:" + address);
		}
		if(Channel!=null && !Channel.isEmpty()){
			//resultString=resultString+", Channel:" + Channel;
			resultStringBuffer.append(", Channel:" + Channel);
		}
		if(phoneNumber!=null && !phoneNumber.isEmpty()){
			//resultString=resultString+", phoneNumber:" + phoneNumber;
			resultStringBuffer.append(", phoneNumber:" + phoneNumber);
		}
		if(zipCode!=null && !zipCode.isEmpty()){
			//resultString=resultString+", zipCode:" + zipCode;
			resultStringBuffer.append(", zipCode:" + zipCode);
		}
		if(town!=null && !town.isEmpty()){
			//resultString=resultString+", town:" + town;
			resultStringBuffer.append(", town:" + town);
		}
		if(country!=null && !country.isEmpty()){
			//resultString=resultString+", country:" + country;
			resultStringBuffer.append(", country:" + country);
		}
		
		resultStringBuffer.append("}");
		
		
		return resultStringBuffer.toString();
		
	}
	
	
	
}
