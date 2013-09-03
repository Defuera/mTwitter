package com.example.mtwitter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsRowAdapter extends ArrayAdapter<Item> {
	private Activity activity;
	private ListView listView;

	public NewsRowAdapter(Activity act, List<Item> arrayList, ListView listView) {
		super(act, R.layout.row, arrayList);
		this.activity = act;
		this.listView = listView;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		// Обеспечить вызов данного метода только когда закончены тестовые
		// вызовы (mesurements)

//		System.out.println("convertView "+convertwiew == null ? convertwiew.getId() : null+" parent "+ parent + " position " +position);
//		View view = convertview;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row, null);

			
		}

		Item item = (Item) getItem(position);
		TextView twUserName = (TextView) view.findViewById(R.id.twusername);
		TextView twDate = (TextView) view.findViewById(R.id.twdate);
		ImageView twImage = (ImageView) view.findViewById(R.id.twimage);
		TextView twText = (TextView) view.findViewById(R.id.twtext);

		twUserName.setText(item.getUserName());
		twDate.setText(item.getDate());
		twText.setText(item.getText());

		ImageLoader.loadImageAsync(item.getImageURL(), twImage, position, listView);

		return view;
	}

}
