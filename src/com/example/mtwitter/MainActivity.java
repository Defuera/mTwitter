package com.example.mtwitter;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static String oauth_consumer_key = "pJGXKDSxqxpRv2kHH98Gg";// "<Your Consumer Key>";
	final static String APISECRET = "5v4NFFvDlNSfxmVKFo0XW4DJpNWLWsBO7SDfSQtfeE";// "<Your Consumer Secret>";
	

	private final static String twitURL = "https://api.twitter.com/1.1/search/tweets.json?q=%40twitterapi";
	private final static String getTokenURL = "https://api.twitter.com/oauth2/token";
	private static String bearerToken="AAAAAAAAAAAAAAAAAAAAAMZrRgAAAAAAk2xhvhGX%2BzCQDuospyZOxSaNsQ0%3DL4XfqGUpCFmjujwbzNouKhvxEZONyIFJhzSTnqcEQ";
	
	private static List<Item> arrayOfList = new ArrayList<Item>();
	private ListView listView;
	
	public static Resources resourses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    
		System.out.println("--------------------------");
		System.out.println("---------NEW TEST---------");
		System.out.println("--------------------------");

		System.out.println(arrayOfList.size());

		listView = (ListView) findViewById(R.id.listview);

		if (arrayOfList.size() == 0 ) loadData();
		else{
			NewsRowAdapter objAdapter = new NewsRowAdapter(this, arrayOfList, listView);	
			listView.setAdapter(objAdapter);
		}
	}

	public void loadData(){
		if (Utils.isNetworkAvailable(MainActivity.this)) {
			System.out.println("Network is available");
			new MyTask(listView, arrayOfList, MainActivity.this).execute();
		} else {
			String msg = "No Network Connection!!!";
			showToast(msg);
			this.finish();
		}
	}

	public void showToast(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	    		System.out.println("--------------------------");
	    		System.out.println("--------REFRESHING--------");
	    		System.out.println("--------------------------");
	        	showToast("Refreshing ");
	        	ImageLoader.cache.clear();
	        	arrayOfList.clear();
	        	Item it = new Item();
	        	it.count = 0;
	        	it.totalCount = 0;
	        	MyTask.loadedItemsCounter = 0;
	        	loadData();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		MyTask.loadedItemsCounter = 0;
		System.out.println("onDestroy");
	}
	
//	 Fetches the first tweet from a given user's timeline
	private static String fetchTimelineTweet(String endPointUrl)
			throws IOException {
		HttpsURLConnection connection = null;

		try {
			URL url = new URL(endPointUrl);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "anyApplication");
			connection.setRequestProperty("Authorization", "Bearer " +	 bearerToken);
			connection.setUseCaches(false);
			
			System.out.println("endPointUrl "+endPointUrl);
			System.out.println("Authorization "+connection.getRequestProperty("Authorization"));
			System.out.println("User-Agent "+connection.getRequestProperty("User-Agent"));
			System.out.println("Host "+connection.getRequestProperty("Host"));
			
			System.out.println("RequestMethod "+connection.getRequestMethod());

			// Parse the JSON response into a JSON mapped object to fetch fields
			// from.
			System.out.println("CONN RESP CODE " + connection.getResponseCode()+" ok= "+HttpsURLConnection.HTTP_OK);
			readResponse(connection);

			return new String();
		} catch (MalformedURLException e) {
			throw new IOException("Invalid endpoint URL specified.", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	// Writes a request to a connection
	private static boolean writeRequest(HttpsURLConnection connection,
			String textBody) {
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					connection.getOutputStream()));
			wr.write(textBody);
			wr.flush();
			wr.close();

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	// Reads a response for a given connection and returns it as a string.
	private static String readResponse(HttpsURLConnection connection) {
		try {
			StringBuilder str = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(str);
				str.append(line + System.getProperty("line.separator"));
			}
			return str.toString();
		} catch (IOException e) {
			System.out.println("readResponse exception ");
			return new String();
		}
	}

}




