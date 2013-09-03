package com.example.mtwitter;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

//

@SuppressLint("NewApi")
public class ImageLoader {

	 private static final ExecutorService service =	Executors.newFixedThreadPool(4);
//	private static final ExecutorService service = new MyPoolExecutor(1);

	public static final Map<String, BitmapDrawable> cache = new LinkedHashMap<String, BitmapDrawable>() {

		private static final int MAX_ENTRIES = 100;

		@Override
		protected boolean removeEldestEntry(
				Map.Entry<String, BitmapDrawable> eldest) {
			return size() > MAX_ENTRIES;

		}

	};

	public static void loadImageAsync(String url, ImageView imageView,
			int position, ListView listView) {
		imageView.clearAnimation();

		if (cache.containsKey(url)) {
			synchronized (cache) {
				imageView.setImageDrawable(cache.get(url));
			}

		} else {
			Context ctx = imageView.getContext();

			// imageView.setImageDrawable(null);
			LoadTask loadTask = new LoadTask();
			loadTask.url = url;
			loadTask.imageView = imageView;
			loadTask.position = position;
			loadTask.listView = listView;
			loadTask.anim = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.clockwise_rotation);

			imageView.setImageResource(android.R.drawable.ic_popup_sync);
			imageView.startAnimation(loadTask.anim);

			service.submit(loadTask);
		}
	}

	private static class LoadTask implements Runnable {

		private ProgressBar pb;
		private String url;
		private ImageView imageView;
		private int position;
		private ListView listView;
		BitmapDrawable bitmapDrawable;
		private Animation anim;

		@Override
		public void run() {

			InputStream is;
			try {
				is = (new URL(url)).openStream();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			bitmapDrawable = new BitmapDrawable(imageView.getContext()
					.getResources(), is); // must be final??

			synchronized (cache) {
				cache.put(url, bitmapDrawable);
			}

			((Activity) imageView.getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					int first = listView.getFirstVisiblePosition();
					int last = listView.getLastVisiblePosition();
					if (getPosition() < first || getPosition() > last) {
						return;
					}

					// pb.setVisibility(View.GONE);
					// synchronized(anim){
					anim.cancel();
					// }
					imageView.clearAnimation();
					imageView.setImageDrawable(bitmapDrawable);
				}

			});
		}

		public int getPosition() {
			return position;
		}

	}

}
