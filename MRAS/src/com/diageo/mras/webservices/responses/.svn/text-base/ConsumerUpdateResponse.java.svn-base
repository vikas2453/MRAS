package com.diageo.mras.webservices.responses;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.diageo.mras.webservices.modals.Message;
import com.diageo.mras.webservices.modals.Reward;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ConsumerUpdateResponse {

	@XmlElement(name = "lastRefresh")
	private String lastRefresh;

	// @XmlElementWrapper(name="RewardList")
	@XmlElement
	private Collection<Reward> rewardList;

	@XmlElement(name = "MessageList")
	private Collection<Message> messageList;

	@XmlElement(name = "OfferList")
	private Collection<OfferResponse> offerList;

	public Collection<OfferResponse> getOfferList() {
		return offerList;
	}

	public void setOfferList(Collection<OfferResponse> offerList) {
		this.offerList = offerList;
	}

	public Collection<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(Collection<Message> messageList) {
		this.messageList = messageList;
	}

	public String getLastRefresh() {
		return lastRefresh;
	}

	public void setLastRefresh(String lastRefresh) {
		this.lastRefresh = lastRefresh;
	}

	public Collection<Reward> getRewardList() {
		return rewardList;
	}

	public void setRewardList(Collection<Reward> rewardList) {
		this.rewardList = rewardList;
	}

	public String toString() {
		return ("Reward List: " + rewardList + "\n" + "Message List:"
				+ messageList + "\n" + "lastRefresh: " + lastRefresh
				+ "\nofferList:" + offerList);
	}

}
