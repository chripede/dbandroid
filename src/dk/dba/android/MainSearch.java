package dk.dba.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import dk.dba.android.util.UIUtils;

public class MainSearch extends Activity {

	private Button searchButton;
	private EditText searchText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findAllViewsById();
		registerListeners();
	}

	private void findAllViewsById() {
		searchButton = (Button) findViewById(R.id.searchButton);
		searchText = (EditText) findViewById(R.id.searchText);
	}

	private void registerListeners() {
		searchText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER)
				{
					searchButton.performClick();
					return true;
				}
				return false;
			}
		});

		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.doSearch(MainSearch.this, searchText.getText());
			}
		});
	}
}