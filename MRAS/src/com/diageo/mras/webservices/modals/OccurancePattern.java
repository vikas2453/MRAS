package com.diageo.mras.webservices.modals;

public class OccurancePattern {
	private int reOccurencPatternType;
	private int occurrencePatternValue1;
	private int occurrencePatternValue2;
	private String days;
	private Offer offer;

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public int getReOccurencPatternType() {
		return reOccurencPatternType;
	}

	public void setReOccurencPatternType(int reOccurencPatternType) {
		this.reOccurencPatternType = reOccurencPatternType;
	}

	public int getOccurrencePatternValue1() {
		return occurrencePatternValue1;
	}

	public void setOccurrencePatternValue1(int occurrencePatternValue1) {
		this.occurrencePatternValue1 = occurrencePatternValue1;
	}

	public int getOccurrencePatternValue2() {
		return occurrencePatternValue2;
	}

	public void setOccurrencePatternValue2(int occurrencePatternValue2) {
		this.occurrencePatternValue2 = occurrencePatternValue2;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

}
