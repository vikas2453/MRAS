package com.diageo.mras.webservices.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.diageo.mras.webservices.modals.Offer;
import com.diageo.mras.webservices.modals.OutletWithoutCampaignId;

@XmlRootElement
public class Getalloffers {
	List<Offer> offerList;

	public List<Offer> getOfferList() {
		return offerList;
	}

	public void setOfferList(List<Offer> offerList) {
		this.offerList = offerList;
	}

	
	}
