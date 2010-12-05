package dk.dba.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dk.dba.android.util.DrawableManager;
import dk.dba.android.util.UIUtils;

public class SearchList extends Activity {

	private final static int MESSAGE_MORE_DATA = 0;
	private final static int MESSAGE_NO_MORE_DATA = 1;
	private final static int MESSAGE_ERROR = 2;

	private ImageButton homeButton;
	private ListView searchList;
	private String searchTerm;
	private SearchAdapter searchAdapter;
	private View footerView;
	private boolean loadingData = false;
	private Handler searchListHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);

		searchTerm = getIntent().getExtras().get("dk.dba.android.SearchTerm").toString();
		searchAdapter = new SearchAdapter(this);
		footerView = View.inflate(this, R.layout.search_list_item_progress, null);
		
		findAllViewsById();
		registerListeners();
		createHandlers();

		searchList.addFooterView(footerView);
		searchList.setAdapter(searchAdapter);
	}

	private void findAllViewsById() {
		homeButton = (ImageButton) findViewById(R.id.homeButton);
		searchList = (ListView) findViewById(R.id.searchListView);
	}

	private void registerListeners() {
		homeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UIUtils.goHome(SearchList.this);
			}
		});
		
		searchList.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				return;
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				boolean loadMore = totalItemCount > 0 
					&& !loadingData
					&& firstVisibleItem + visibleItemCount >= totalItemCount;
				if(loadMore) {
					loadingData = true;
					loadData(searchAdapter, searchTerm, searchListHandler.obtainMessage());
				}
			}
		});
	}

	private void createHandlers() {
		searchListHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case MESSAGE_MORE_DATA:
					// TODO: Tilføj data fra message til dataset på searchAdapter
					searchAdapter.notifyDataSetChanged();
					loadingData = false;
					break;
				case MESSAGE_NO_MORE_DATA:
					// TODO: Tilføj data fra message til dataset på searchAdapter
					searchAdapter.notifyDataSetChanged();
					searchList.removeFooterView(footerView);
					break;
				case MESSAGE_ERROR:
					searchList.removeFooterView(footerView);
					Toast.makeText(SearchList.this, "Der er sket en fejl", Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
	}
	
	private void loadData(final SearchAdapter searchAdapter, final String searchTerm, final Message message) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				// TODO: Kald webservice her
				message.what = MESSAGE_MORE_DATA;
				message.what = MESSAGE_NO_MORE_DATA;
				message.what = MESSAGE_ERROR;
				
				message.sendToTarget();
			}
		};
		thread.start();
	}

	private static class SearchAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private DrawableManager drawableManager;
		
		public SearchAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			drawableManager = new DrawableManager();
		}

		public int getCount() {
			return COUNTRIES.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_list_item, null);
				holder = new ViewHolder();
				holder.price = (TextView) convertView.findViewById(R.id.itemPrice);
				holder.description = (TextView) convertView.findViewById(R.id.itemDescription);
				holder.location = (TextView) convertView.findViewById(R.id.itemLocation);
				holder.image = (ImageView) convertView.findViewById(R.id.itemImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.price.setText("Kr. 1234");
			holder.description.setText("Beskrivelse af kjalsdh aslkdj hasdklha sdklah adsklh sdaklfh weoprusdkfæ asdfkljsdhfdsfhsdlkjf ddjfhsd " + COUNTRIES[position]);
			holder.location.setText(COUNTRIES[position]);
			drawableManager.fetchDrawableOnThread("http://i.dbastatic.dk/images/8/65/%5C70176665_06032010163617_8408_8.jpg", holder.image);

			return convertView;
		}

		static class ViewHolder {
			TextView description;
			TextView price;
			TextView location;
			ImageView image;
		}
	}

	private static final String[] COUNTRIES = new String[] { "Afghanistan",
			"Albania", "Algeria", "American Samoa", "Andorra", "Angola",
			"Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
			"Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
			"Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
			"Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
			"Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil",
			"British Indian Ocean Territory", "British Virgin Islands",
			"Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
			"Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
			"Central African Republic", "Chad", "Chile", "China",
			"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
			"Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
			"Cuba", "Cyprus", "Czech Republic",
			"Democratic Republic of the Congo", "Denmark", "Djibouti",
			"Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
			"El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
			"Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
			"Finland", "Former Yugoslav Republic of Macedonia", "France",
			"French Guiana", "French Polynesia", "French Southern Territories",
			"Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
			"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
			"Guinea", "Guinea-Bissau", "Guyana", "Haiti",
			"Heard Island and McDonald Islands", "Honduras", "Hong Kong",
			"Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
			"Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
			"Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
			"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
			"Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
			"Malawi", "Malaysia", "Maldives", "Mali", "Malta",
			"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
			"Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
			"Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
			"Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
			"New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
			"Niue", "Norfolk Island", "North Korea", "Northern Marianas",
			"Norway", "Oman", "Pakistan", "Palau", "Panama",
			"Papua New Guinea", "Paraguay", "Peru", "Philippines",
			"Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
			"Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe",
			"Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
			"Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
			"Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
			"Sierra Leone", "Singapore", "Slovakia", "Slovenia",
			"Solomon Islands", "Somalia", "South Africa",
			"South Georgia and the South Sandwich Islands", "South Korea",
			"Spain", "Sri Lanka", "Sudan", "Suriname",
			"Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
			"Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
			"The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
			"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
			"Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
			"Ukraine", "United Arab Emirates", "United Kingdom",
			"United States", "United States Minor Outlying Islands", "Uruguay",
			"Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
			"Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
			"Zambia", "Zimbabwe" };
}
