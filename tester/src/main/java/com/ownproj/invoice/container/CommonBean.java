package com.ownproj.invoice.container;

import java.util.List;
import java.util.Map;



public class CommonBean {

	private String username;
	private String password;
	
	
	
	private String name;
	private String contact;
	private String place;
	private String email;
	private String productIDs;
	private Map<String, String> quantityIDMap;
	
	private List<List<String>> productDetails;
	private int grandTotal;
	private int status;
	private String htmlContent;
	
	private String pdfFullPath;
	private String pdfFileName;
	
	private String productId;
	private String productName;
	private String productPrice;
	private String gst;
	private String display;
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getGst() {
		return gst;
	}
	public void setGst(String gst) {
		this.gst = gst;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPdfFullPath() {
		return pdfFullPath;
	}
	public void setPdfFullPath(String pdfFullPath) {
		this.pdfFullPath = pdfFullPath;
	}
	public String getPdfFileName() {
		return pdfFileName;
	}
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}
	public int getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(int grandTotal) {
		this.grandTotal = grandTotal;
	}
	public List<List<String>> getProductDetails() {
		return productDetails;
	}
	public void setProductDetails(List<List<String>> productDetails) {
		this.productDetails = productDetails;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getProductIDs() {
		return productIDs;
	}
	public void setProductIDs(String productIDs) {
		this.productIDs = productIDs;
	}
	public Map<String, String> getQuantityIDMap() {
		return quantityIDMap;
	}
	public void setQuantityIDMap(Map<String, String> quantityIDMap) {
		this.quantityIDMap = quantityIDMap;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CommonBean [");
		if (username != null) {
			builder.append("username=");
			builder.append(username);
			builder.append(", ");
		}
		if (password != null) {
			builder.append("password=");
			builder.append(password);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (contact != null) {
			builder.append("contact=");
			builder.append(contact);
			builder.append(", ");
		}
		if (place != null) {
			builder.append("place=");
			builder.append(place);
			builder.append(", ");
		}
		if (email != null) {
			builder.append("email=");
			builder.append(email);
			builder.append(", ");
		}
		if (productIDs != null) {
			builder.append("productIDs=");
			builder.append(productIDs);
			builder.append(", ");
		}
		if (quantityIDMap != null) {
			builder.append("quantityIDMap=");
			builder.append(quantityIDMap);
			builder.append(", ");
		}
		if (productDetails != null) {
			builder.append("productDetails=");
			builder.append(productDetails);
			builder.append(", ");
		}
		builder.append("grandTotal=");
		builder.append(grandTotal);
		builder.append(", status=");
		builder.append(status);
		builder.append(", ");
		if (htmlContent != null) {
			builder.append("htmlContent=");
			builder.append(htmlContent);
			builder.append(", ");
		}
		if (pdfFullPath != null) {
			builder.append("pdfFullPath=");
			builder.append(pdfFullPath);
			builder.append(", ");
		}
		if (pdfFileName != null) {
			builder.append("pdfFileName=");
			builder.append(pdfFileName);
			builder.append(", ");
		}
		if (productId != null) {
			builder.append("productId=");
			builder.append(productId);
			builder.append(", ");
		}
		if (productName != null) {
			builder.append("productName=");
			builder.append(productName);
			builder.append(", ");
		}
		if (productPrice != null) {
			builder.append("productPrice=");
			builder.append(productPrice);
			builder.append(", ");
		}
		if (gst != null) {
			builder.append("gst=");
			builder.append(gst);
			builder.append(", ");
		}
		if (display != null) {
			builder.append("display=");
			builder.append(display);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
