package dk.dba.android;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dk.dba.android.pojo.Ad;
import dk.dba.android.util.DbaSearchService;
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
					loadData(searchAdapter, searchTerm, 10, searchAdapter.getCount(), searchListHandler.obtainMessage());
				}
			}
		});
		
		searchList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Ad ad = searchAdapter.getItem(position);
				UIUtils.showVip(SearchList.this, ad);
			}
		});
	}

	private void createHandlers() {
		searchListHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case MESSAGE_MORE_DATA:
					searchAdapter.AddData((List<Ad>) msg.obj);
					searchAdapter.notifyDataSetChanged();
					loadingData = false;
					break;
				case MESSAGE_NO_MORE_DATA:
					searchAdapter.AddData((List<Ad>) msg.obj);
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
	
	private void loadData(final SearchAdapter searchAdapter, final String searchTerm, final int pageSize, final int startFrom, final Message message) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					List<Ad> ads = DbaSearchService.Search(searchTerm, pageSize, startFrom); 
					message.obj = ads;
	
					if(ads.size() < pageSize)
						message.what = MESSAGE_NO_MORE_DATA;
					else
						message.what = MESSAGE_MORE_DATA;
				} catch (Exception e) {
					message.what = MESSAGE_ERROR;
				}
				
				message.sendToTarget();
			}
		};
		thread.start();
	}

	private static class SearchAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private DrawableManager drawableManager;
		private List<Ad> ads;
		private Context context;
		
		public SearchAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
			drawableManager = new DrawableManager();
			ads = new ArrayList<Ad>();
		}

		public int getCount() {
			return ads.size();
		}

		public Ad getItem(int position) {
			return ads.get(position);
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
			
			Ad ad = ads.get(position);

			holder.price.setText("Kr. " + ad.getPrice().replace(".0000", ""));
			holder.description.setText(ad.getDescription());
			holder.location.setText(ad.getAddressZipcode() + " " + ad.getAddressCity());
			
			if(ad.getThumbnail() != null)
				drawableManager.fetchDrawableOnThread(ads.get(position).getThumbnail(), holder.image);

			return convertView;
		}
		
		public void AddData(List<Ad> ads) {
			this.ads.addAll(ads);
		}

		static class ViewHolder {
			TextView description;
			TextView price;
			TextView location;
			ImageView image;
		}
	}
	
}
