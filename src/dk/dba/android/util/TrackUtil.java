package dk.dba.android.util;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class TrackUtil {

	private static GoogleAnalyticsTracker tracker;
	
	public static GoogleAnalyticsTracker getTracker(Context context) {
		if(tracker == null) {
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.start("UA-20335081-1", context);
		}
		return tracker;
	}
}
