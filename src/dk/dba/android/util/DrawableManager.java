package dk.dba.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class DrawableManager {

	private final Map<String, Drawable> drawableMap;

	public DrawableManager() {
		drawableMap = new HashMap<String, Drawable>();
	}

	public Drawable fetchDrawable(String urlString) {
		if (drawableMap.containsKey(urlString)) {
			return drawableMap.get(urlString);
		}

		try {
			InputStream is = fetch(urlString);
			Drawable drawable = Drawable.createFromStream(is, "src");
			drawableMap.put(urlString, drawable);
			return drawable;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void fetchDrawableOnThread(final String urlString,
			final ImageView imageView) {
		if (drawableMap.containsKey(urlString)) {
			imageView.setImageDrawable(drawableMap.get(urlString));
			return;
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.what == 1)
					imageView.setImageDrawable((Drawable) message.obj);
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {
				Drawable drawable = fetchDrawable(urlString);
				Message message;
				if (drawable != null)
					message = handler.obtainMessage(1, drawable);
				else
					message = handler.obtainMessage(0);

				handler.sendMessage(message);
			}
		};
		thread.start();
	}

	private InputStream fetch(String urlString) throws MalformedURLException,
			IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		HttpResponse response = httpClient.execute(request);
		return response.getEntity().getContent();
	}

}
