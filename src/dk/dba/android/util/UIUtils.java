package dk.dba.android.util;

import dk.dba.android.MainSearch;
import dk.dba.android.SearchList;
import android.content.Context;
import android.content.Intent;

public class UIUtils {
	
    public static void goHome(Context context) {
        final Intent intent = new Intent(context, MainSearch.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    
    public static void doSearch(Context context) {
    	final Intent intent = new Intent(context, SearchList.class);
    	context.startActivity(intent);
    }

}
