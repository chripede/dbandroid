package dk.dba.android.util;

import android.content.Context;
import android.content.Intent;
import dk.dba.android.MainSearch;
import dk.dba.android.SearchList;
import dk.dba.android.Vip;
import dk.dba.android.pojo.Ad;

public class UIUtils {
	
    public static void goHome(Context context) {
        final Intent intent = new Intent(context, MainSearch.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    
    public static void doSearch(Context context, CharSequence searchTerm) {
    	final Intent intent = new Intent(context, SearchList.class);
    	intent.putExtra("dk.dba.android.SearchTerm", searchTerm);
    	context.startActivity(intent);
    }
    
    public static void showVip(Context context, Ad ad) {
    	final Intent intent = new Intent(context, Vip.class);
    	intent.putExtra("dk.dba.android.Ad", ad);
    	context.startActivity(intent);
    }

}
