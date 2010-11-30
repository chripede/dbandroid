package dk.dba.android;

import java.util.HashMap;
import java.util.Map;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import dk.dba.android.util.UIUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainSearch extends Activity {

	private Button searchButton;
	private EditText searchText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findAllViewsById();

		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.doSearch(MainSearch.this);
				
				// IntentIntegrator.initiateScan(MainSearch.this);
			}
		});
	}

	private void findAllViewsById() {
		searchButton = (Button) findViewById(R.id.searchButton);
		searchText = (EditText) findViewById(R.id.searchText);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {
			new UpcDatabaseTask(scanResult).execute();
		}
	}
	
	private class UpcDatabaseTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog dialog;
		private IntentResult scanResult;
		
		public UpcDatabaseTask(IntentResult result) {
			scanResult = result;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainSearch.this, "Henter data", "Vent venligst..."); 
		}

		@Override
		protected Void doInBackground(Void... params) {
			XMLRPCClient client = new XMLRPCClient("http://www.upcdatabase.com/xmlrpc");
			try {
				Map<String, String> rpcParams = new HashMap<String, String>();
				rpcParams.put("rpc_key", "cf25e34e3d92db42079e83f1b11e6b43f88bcfcf");
				rpcParams.put("upc", scanResult.getContents());
				HashMap result = (HashMap) client.call("lookup", rpcParams);
				String resultSize = result.get("size").toString();
				final String resultDesc = result.get("description").toString();
				MainSearch.this.runOnUiThread(new Runnable() {
					public void run() {
						searchText.setText(resultDesc);
					}
				});
			} catch (NullPointerException e) {
				Log.e("onActivityResult:NullPointerException", e.getMessage());
			} catch (XMLRPCException e) {
				Log.e("onActivityResult:XMLRPCException", e.getMessage());
			}
			return null;
		}
		
	}
}