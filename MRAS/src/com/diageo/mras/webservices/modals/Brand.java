package com.diageo.mras.webservices.modals;

import java.util.List;

public class Brand {

	private String brandName;
	private List<String> products;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (products != null) {
			for (String s : products) {
				sb.append(s);
			}
		}
		return sb.toString();
	}
	public String getString() {
		
		String resultstring="{brandName:" + brandName+",";
		StringBuffer sb = new StringBuffer(resultstring);
		
		if (products != null) {
			sb.append("products:[");
			for (String s : products) {
				sb.append(s+",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
		}
		sb.append("}");
		return sb.toString();	
		
	}

}
