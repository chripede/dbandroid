package dk.dba.android.util;

import java.util.ArrayList;
import java.util.List;

import dk.dba.android.pojo.Ad;

public class DbaSearchService {

	public static List<Ad> Search(String searchTerm, int pageSize, int startFrom) {
		return TestSearch();
		/*
		String url = String.format("http://api.dba.dk/public/v1/ads?q=%s&f=xml&ps=%s&pn=%s", searchTerm.replace(' ', '+'), pageSize, (startFrom / pageSize) + 1);
		SaxFeedParser parser = new SaxFeedParser(url);
		return parser.parse();
		*/
	}
	
	public static Ad GetAd(String adId) {
		String url = String.format("http://api.dba.dk/public/v1/ad/%s?f=xml", adId);
		SaxFeedParser parser = new SaxFeedParser(url);
		return parser.parse().get(0);
	}
	
	/***
	 * Testdata 
	 */
	private static List<Ad> TestSearch() {
		Ad ad = new Ad();
		ad.setAddressCity("�rhus");
		ad.setAddressLatitude("52.00000");
		ad.setAddressLongitude("20.1234");
		ad.setAddressPhone("12345678");
		ad.setAddressStreet("Testvej 213");
		ad.setAddressZipcode("8000");
		ad.setDescription("S�d hamster");
		ad.setPrice("40.0000");
		// ad.setThumbnail("http://i.dbastatic.dk/images/8/53/%5C70324453_08122010151101_5340_8.jpg");
		ad.setTitle("D�dt hamster");
		ad.addToMatrixMap("Navn", "Bent");
		ad.addToMatrixMap("K�n", "Nej");
		ad.addToMatrixMap("V�gt", "350 g");
		
		List<Ad> l = new ArrayList<Ad>();
		l.add(ad);
		return l;
	}
}
