package dk.dba.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;
import dk.dba.android.pojo.Ad;

public class SaxFeedParser {
	
	final URL feedUrl;
	static boolean useThisPicture = false;
	
	public SaxFeedParser(String feedUrl) {
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Ad> parse() {
		final Ad currentAd = new Ad();
		final List<Ad> ads = new ArrayList<Ad>();
		final String labelValue[] = new String[2];
		
		RootElement root = new RootElement("AdsResult");

		Element adElements = root.getChild("ads");
		Element adElement = adElements.getChild("ad");
		adElement.setEndElementListener(new EndElementListener() {
			public void end() {
				ads.add(currentAd.copy());
			}
		});
		Element title = adElement.getChild("title");
		title.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setTitle(body);
			}
		});
		Element description = adElement.getChild("description");
		description.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setDescription(body);
			}
		});
		Element price = adElement.getChild("price");
		price.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setPrice(body);
			}
		});
		
		Element address = adElement.getChild("ad-address");
		Element street = address.getChild("street");
		street.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setAddressStreet(body);
			}
		});
		Element zipcode = address.getChild("zip-code");
		zipcode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setAddressZipcode(body);
			}
		});
		Element city = address.getChild("city");
		city.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setAddressCity(body);
			}
		});
		Element phone = address.getChild("phone");
		phone.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setAddressPhone(body);
			}
		});
		Element longitude = address.getChild("longitude");
		longitude.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setAddressLongitude(body);
			}
		});
		Element latitude = address.getChild("latitude");
		latitude.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				currentAd.setAddressLatitude(body);
			}
		});
		
		Element pictures = adElement.getChild("pictures");
		Element picture = pictures.getChild("picture");
		picture.setElementListener(new ElementListener() {
			public void end() {
				useThisPicture = false;
			}
			
			public void start(Attributes attributes) {
				if(attributes.getValue("size-category").equals("Thumbnail"))
					useThisPicture = true;
			}
		});
		Element pictureLink = picture.getChild("link");
		pictureLink.setElementListener(new ElementListener() {
			public void start(Attributes attributes) {
				if(useThisPicture)
					currentAd.setThumbnail(attributes.getValue("href"));
			}
			public void end() {}
		});
		
		Element matrixData = adElement.getChild("matrixdata");
		Element matrix = matrixData.getChild("Matrix");
		matrix.setEndElementListener(new EndElementListener() {
			public void end() {
				currentAd.addToMatrixMap(labelValue[0], labelValue[1]);
			}
		});
		Element matrixLabel = matrix.getChild("label");
		matrixLabel.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				labelValue[0] = body;
			}
		});
		Element matrixValue = matrix.getChild("value");
		matrixValue.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				labelValue[1] = body;
			}
		});
		
		try {
			Xml.parse(getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return ads;
	}
}