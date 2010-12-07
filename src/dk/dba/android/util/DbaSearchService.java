package dk.dba.android.util;

import java.util.List;

import dk.dba.android.pojo.Ad;

public class DbaSearchService {

	public static List<Ad> Search(String searchTerm, int pageSize, int startFrom) {
		String url = String.format("http://api.dba.dk/public/v1/ads?q=%s&f=xml&ps=%s&pn=%s", searchTerm.replace(' ', '+'), pageSize, (startFrom / pageSize) + 1);
		SaxFeedParser parser = new SaxFeedParser(url);
		return parser.parse();
	}	
}
