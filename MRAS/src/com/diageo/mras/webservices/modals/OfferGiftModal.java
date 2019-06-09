package com.diageo.mras.webservices.modals;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferGiftModal {
	@XmlElement(name = "offersGiftedTo")
	private ArrayList<String> offersGiftedTo;

	@XmlElement(name = "unRegisteredPhoneNumbers")
	private ArrayList<String> unRegisteredPhoneNumbers;

	public ArrayList<String> getOffersGiftedTo() {
		return offersGiftedTo;
	}

	public void setOffersGiftedTo(ArrayList<String> offersGiftedTo) {
		this.offersGiftedTo = offersGiftedTo;
	}

	public ArrayList<String> getUnRegisteredPhoneNumbers() {
		return unRegisteredPhoneNumbers;
	}

	public void setUnRegisteredPhoneNumbers(
			ArrayList<String> unRegisteredPhoneNumbers) {
		this.unRegisteredPhoneNumbers = unRegisteredPhoneNumbers;
	}

}
