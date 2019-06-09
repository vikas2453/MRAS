package com.diageo.mras.webservices.modals;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="outlets1")
@XmlAccessorType(XmlAccessType.FIELD)
public class OuletInformation {
	@XmlElement(name = "bands")
	private ArrayList<OutletBandDetails> bands;
	@XmlElement(name = "outletName")
	private String outletName;
	@XmlElement(name = "longitude")
	private double longitude;
	@XmlElement(name = "lattitude")
	private double lattitude;
	@XmlElement(name = "address")
	private String address;
	@XmlElement(name = "town")
	private String town;
	@XmlElement(name = "countryId")
	private int countryId;
	@XmlElement(name = "county")
	private String county;
	@XmlElement(name = "sales_Org")
	private String sales_Org;
	@XmlElement(name = "plant")
	private String plant;
	@XmlElement(name = "channel")
	private String channel;
	@XmlElement(name = "phone")
	private String phone;
	@XmlElement(name = "email")
	private String email;
	@XmlElement(name = "website")
	private String website;
	@XmlElement(name = "faceBookURL")
	private String faceBookURL;
	@XmlElement(name = "twitterURL")
	private String twitterURL;
	@XmlElement(name = "photoURL")
	private String photoURL;
	@XmlElement(name = "pubRegion")
	private String pubRegion;
	@XmlElement(name = "doingMusic")
	private String doingMusic;
	@XmlElement(name = "ZipCode")
	private String ZipCode;
	
	
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	
	public ArrayList<OutletBandDetails> getList() {
		return bands;
	}
	public void setList(ArrayList<OutletBandDetails> bands) {
		this.bands = bands;
	}
	public String getOutletName() {
		return outletName;
	}
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLattitude() {
		return lattitude;
	}
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
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
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getSales_Org() {
		return sales_Org;
	}
	public void setSales_Org(String sales_Org) {
		this.sales_Org = sales_Org;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getFaceBookURL() {
		return faceBookURL;
	}
	public void setFaceBookURL(String faceBookURL) {
		this.faceBookURL = faceBookURL;
	}
	public String getTwitterURL() {
		return twitterURL;
	}
	public void setTwitterURL(String twitterURL) {
		this.twitterURL = twitterURL;
	}
	public String getPhotoURL() {
		return photoURL;
	}
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}
	public String getPubRegion() {
		return pubRegion;
	}
	public void setPubRegion(String pubRegion) {
		this.pubRegion = pubRegion;
	}
	public String getDoingMusic() {
		return doingMusic;
	}
	public void setDoingMusic(String doingMusic) {
		this.doingMusic = doingMusic;
	}
	
	public String toString(){
		
		String resultstring="{\"longitude\":" + "\""+longitude + "\"" + ", \"lattitude\":" + "\""	+ lattitude + "\""+ ", \"countryId\":" + "\""+ countryId+ "\"";		
		StringBuffer resultStringBuffer =new StringBuffer(resultstring);		
		/*return(
				(
						outletName.isEmpty()||outletName==null)?resultStringBuffer.append(""):resultStringBuffer.append(", outletName:" + outletName)
				.append((address.isEmpty()||address==null)?resultStringBuffer.append(""):resultStringBuffer.append(", address:" + address))
				.append((town.isEmpty()||town==null)?resultStringBuffer.append(""):resultStringBuffer.append(", town:" + town))
				.append((county.isEmpty()||county==null)?resultStringBuffer.append(""):resultStringBuffer.append(", county:" + county))
				.append((sales_Org.isEmpty()||sales_Org==null)?resultStringBuffer.append(""):resultStringBuffer.append(", sales_Org:" + sales_Org))
				.append((plant.isEmpty()||plant==null)?resultStringBuffer.append(""):resultStringBuffer.append(", plant:" + plant))
				.append((channel.isEmpty()||channel==null)?resultStringBuffer.append(""):resultStringBuffer.append(", channel:" + channel))
				.append((phone.isEmpty()||phone==null)?resultStringBuffer.append(""):resultStringBuffer.append(", phone:" + phone))
				.append((email.isEmpty()||email==null)?resultStringBuffer.append(""):resultStringBuffer.append(", email:" + email))
				.append((website.isEmpty()||website==null)?resultStringBuffer.append(""):resultStringBuffer.append(", website:" + website))
				.append((faceBookURL.isEmpty()||faceBookURL==null)?resultStringBuffer.append(""):resultStringBuffer.append(", faceBookURL:" + faceBookURL))
				.append((twitterURL.isEmpty()||twitterURL==null)?resultStringBuffer.append(""):resultStringBuffer.append(", twitterURL:" + twitterURL))
				.append((photoURL.isEmpty()||photoURL==null)?resultStringBuffer.append(""):resultStringBuffer.append(", photoURL:" + photoURL))
				.append((pubRegion.isEmpty()||pubRegion==null)?resultStringBuffer.append(""):resultStringBuffer.append(", pubRegion:" + pubRegion))
				.append((doingMusic.isEmpty()||doingMusic==null)?resultStringBuffer.append(""):resultStringBuffer.append(", doingMusic:" + doingMusic))
				.append((ZipCode.isEmpty()||ZipCode==null)?resultStringBuffer.append(""):resultStringBuffer.append(", ZipCode:" + ZipCode)
			   )
			).toString();	*/	
		
		
		if(outletName!=null && !outletName.isEmpty()){
			//resultString=resultString+", outletname:" + outletname;
			resultStringBuffer.append(", \"outletname\":"+ "\"" + outletName+ "\"");
		}
		if(address!=null && !address.isEmpty()){
			//resultString=resultString+", address:" + address;
			resultStringBuffer.append(", \"address\":" + "\""+ address+ "\"");
		}
		if(county!=null && !county.isEmpty()){
			//resultString=resultString+", address:" + address;
			resultStringBuffer.append(", \"county\":" + "\""+ county+ "\"");
		}		
		if(sales_Org!=null && !sales_Org.isEmpty()){
			//resultString=resultString+", outletname:" + outletname;
			resultStringBuffer.append(", \"sales_Org\":" + "\""+ sales_Org+ "\"");
		}
		if(plant!=null && !plant.isEmpty()){
			//resultString=resultString+", outletname:" + outletname;
			resultStringBuffer.append(", \"plant\":" + "\""+ plant+ "\"");
		}
		if(email!=null && !email.isEmpty()){
			//resultString=resultString+", outletname:" + outletname;
			resultStringBuffer.append(", \"email\":" + "\""+ email+ "\"");
		}
		if(website!=null && !website.isEmpty()){
			//resultString=resultString+", outletname:" + outletname;
			resultStringBuffer.append(", \"website\":"+ "\"" + website+ "\"");
		}
		if(channel!=null && !channel.isEmpty()){
			//resultString=resultString+", Channel:" + Channel;
			resultStringBuffer.append(", \"channel\":"+ "\"" + channel+ "\"");
		}
		if(phone!=null && !phone.isEmpty()){
			//resultString=resultString+", phoneNumber:" + phoneNumber;
			resultStringBuffer.append(", \"phone\":"+ "\"" + phone+ "\"");
		}
		if(ZipCode!=null && !ZipCode.isEmpty()){
			//resultString=resultString+", zipCode:" + zipCode;
			resultStringBuffer.append(", \"zipCode\":" + "\""+ ZipCode+ "\"");
		}
		if(town!=null && !town.isEmpty()){
			//resultString=resultString+", town:" + town;
			resultStringBuffer.append(", \"town\":"+ "\"" + town+ "\"");
		}
		if(pubRegion!=null && !pubRegion.isEmpty()){
			//resultString=resultString+", country:" + country;
			resultStringBuffer.append(", \"pubRegion\":" + "\""+ pubRegion+ "\"");
		}
		if(faceBookURL!=null && !faceBookURL.isEmpty()){
			//resultString=resultString+", town:" + town;
			resultStringBuffer.append(", \"faceBookURL\":" + "\""+ faceBookURL+ "\"");
		}
		if(twitterURL!=null && !twitterURL.isEmpty()){
			//resultString=resultString+", country:" + country;
			resultStringBuffer.append(", \"twitterURL\":" + "\""+ twitterURL+ "\"");
		}if(photoURL!=null && !photoURL.isEmpty()){
			//resultString=resultString+", country:" + country;
			resultStringBuffer.append(", \"photoURL\":"+ "\"" + photoURL+ "\"");
		}
		if(doingMusic!=null && !doingMusic.isEmpty()){
			//resultString=resultString+", town:" + town;
			resultStringBuffer.append(", \"doingMusic\":"+ "\"" + doingMusic+ "\"");
		}
		
		resultStringBuffer.append("}");
		
		
		return resultStringBuffer.toString();
		
		
		
	
	}
	
	
	

}
