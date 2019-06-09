package com.diageo.mras.webservices.modals;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VoucherIssueCountModal {
	@XmlElement(name = "maxVouchersTobeIssued")
	private int maxVouchersTobeIssued;
	@XmlElement(name = "voucherIssuedTillNow")
	private int voucherIssuedTillNow;

	public int getMaxRedemeption() {
		return maxVouchersTobeIssued;
	}

	public void setMaxRedemeption(int maxRedemeption) {
		this.maxVouchersTobeIssued = maxRedemeption;
	}

	public int getRedemptionTillNow() {
		return voucherIssuedTillNow;
	}

	public void setRedemptionTillNow(int redemptionTillNow) {
		this.voucherIssuedTillNow = redemptionTillNow;
	}
}
