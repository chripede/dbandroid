package dk.dba.android.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Ad implements Serializable {

	private String adId;
	private String title;
	private String description;
	private String price;
	private String addressStreet;
	private String addressZipcode;
	private String addressCity;
	private String addressPhone;
	private String addressLongitude;
	private String addressLatitude;
	private String thumbnail;
	private LinkedHashMap<String, String> matrixMap;

	public Ad() {
		matrixMap = new LinkedHashMap<String, String>();
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAddressZipcode() {
		return addressZipcode;
	}

	public void setAddressZipcode(String addressZipcode) {
		this.addressZipcode = addressZipcode;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressPhone() {
		return addressPhone;
	}

	public void setAddressPhone(String addressPhone) {
		this.addressPhone = addressPhone;
	}

	public String getAddressLongitude() {
		return addressLongitude;
	}

	public void setAddressLongitude(String addressLongitude) {
		this.addressLongitude = addressLongitude;
	}

	public String getAddressLatitude() {
		return addressLatitude;
	}

	public void setAddressLatitude(String addressLatitude) {
		this.addressLatitude = addressLatitude;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public HashMap<String, String> getMatrixMap() {
		return matrixMap;
	}

	public void addToMatrixMap(String label, String value) {
		matrixMap.put(label, value);
	}

	public Ad copy() {
		Ad copy = new Ad();
		copy.adId = adId;
		copy.title = title;
		copy.description = description;
		copy.price = price;
		copy.addressStreet = addressStreet;
		copy.addressZipcode = addressZipcode;
		copy.addressCity = addressCity;
		copy.addressPhone = addressPhone;
		copy.addressLongitude = addressLongitude;
		copy.addressLatitude = addressLatitude;
		copy.thumbnail = thumbnail;
		copy.matrixMap = (LinkedHashMap<String, String>) matrixMap.clone();

		adId = null;
		title = null;
		description = null;
		price = null;
		addressStreet = null;
		addressZipcode = null;
		addressCity = null;
		addressPhone = null;
		addressLongitude = null;
		addressLatitude = null;
		thumbnail = null;
		matrixMap.clear();

		return copy;
	}
}
