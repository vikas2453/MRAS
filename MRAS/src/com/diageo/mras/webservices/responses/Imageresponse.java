package com.diageo.mras.webservices.responses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Imageresponse {

	byte[] image;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

}
