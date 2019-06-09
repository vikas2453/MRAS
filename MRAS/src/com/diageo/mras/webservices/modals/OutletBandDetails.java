package com.diageo.mras.webservices.modals;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OutletBandDetails {
	
	//private long outletCode;
	private int bandNumber;
	private String pubBandPhoto;
	private String pubBandWebsite;
	private String pubBandName;
	private String pubBandTime;
	private String pubBandAbout;
	private String pubBandFaceBook;
	
	
	public int getBandNumber() {
		return bandNumber;
	}
	public void setBandNumber(int bandNumber) {
		this.bandNumber = bandNumber;
	}
	public String getPubBandPhoto() {
		return pubBandPhoto;
	}
	public void setPubBandPhoto(String pubBandPhoto) {
		this.pubBandPhoto = pubBandPhoto;
	}
	public String getPubBandWebsite() {
		return pubBandWebsite;
	}
	public void setPubBandWebsite(String pubBandWebsite) {
		this.pubBandWebsite = pubBandWebsite;
	}
	public String getPubBandName() {
		return pubBandName;
	}
	public void setPubBandName(String pubBandName) {
		this.pubBandName = pubBandName;
	}
	public String getPubBandTime() {
		return pubBandTime;
	}
	public void setPubBandTime(String pubBandTime) {
		this.pubBandTime = pubBandTime;
	}
	public String getPubBandAbout() {
		return pubBandAbout;
	}
	public void setPubBandAbout(String pubBandAbout) {
		this.pubBandAbout = pubBandAbout;
	}
	public String getPubBandFaceBook() {
		return pubBandFaceBook;
	}
	public void setPubBandFaceBook(String pubBandFaceBook) {
		this.pubBandFaceBook = pubBandFaceBook;
	}
	

}
