package dk.dba.android;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import dk.dba.android.pojo.Ad;

public class Vip extends Activity {

	private TextView itemTitle;
	private TextView itemPrice;
	private TextView itemDescription;
	private TextView itemContactInfo;
	private Gallery itemGallery;
	private Ad item;
	private ImageButton backButton;
	private TableLayout matrixTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip);

		item = (Ad) getIntent().getExtras().get("dk.dba.android.Ad");
		
		findAllViewsById();
		registerListeners();
		updateViewData();
		updateMatrixData();
		
		matrixTable.setShrinkAllColumns(true);
	}

	private void registerListeners() {
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void updateViewData() {
		itemTitle.setText(item.getTitle());
		itemPrice.setText("Kr. " + item.getPrice().replace(".0000", ""));
		itemDescription.setText(item.getDescription());
		itemContactInfo.setText(item.getAddressStreet() + "\n"
				+ item.getAddressZipcode() + " " + item.getAddressCity() + "\n"
				+ "Tlf: " + item.getAddressPhone());
	}
	
	private void updateMatrixData() {
		LayoutParams layout = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setMargins(0, 0, 0, 1);

		Iterator<Entry<String, String>> i = item.getMatrixMap().entrySet().iterator();
		while(i.hasNext()) {
			Map.Entry<String, String> pairs = (Map.Entry<String, String>) i.next();
			TableRow row = new TableRow(this);
			row.setBackgroundColor(Color.WHITE);
			
			TextView name = new TextView(this);
			name.setText(pairs.getKey());
			name.setTextColor(Color.BLACK);
			name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			name.setPadding(0, 0, 3, 0);
			row.addView(name);
			
			TextView value = new TextView(this);
			value.setText(pairs.getValue());
			value.setTextColor(Color.BLACK);
			row.addView(value);
			
			matrixTable.addView(row, layout);
		}
	}

	private void findAllViewsById() {
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		itemDescription = (TextView) findViewById(R.id.itemDescription);
		itemContactInfo = (TextView) findViewById(R.id.itemContactInfo);
		itemGallery = (Gallery) findViewById(R.id.itemGallery);
		backButton = (ImageButton) findViewById(R.id.searchButton);
		matrixTable = (TableLayout) findViewById(R.id.itemMatrixTableLayout);
	}
}
