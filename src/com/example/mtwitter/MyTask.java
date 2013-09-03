package com.example.mtwitter;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.HttpsURLConnection;

class MyTask extends AsyncTask<String, Void, String> {

	private static final String ARRAY_NAME = "statuses";
	private static final String DATE = "created_at";
	private static final String USERNAME = "name";
	private static final String IMAGE_URL = "profile_image_url";
	private static final String TEXT = "text";

	private final static String twitURL = "https://api.twitter.com/1.1/search/tweets.json?q=%40twitterapi";
//	private final static String getTokenURL = "https://api.twitter.com/oauth2/token";
	private static String bearerToken = "AAAAAAAAAAAAAAAAAAAAAMZrRgAAAAAAk2xhvhGX%2BzCQDuospyZOxSaNsQ0%3DL4XfqGUpCFmjujwbzNouKhvxEZONyIFJhzSTnqcEQ";
	final static String oauth_consumer_key = "pJGXKDSxqxpRv2kHH98Gg";// "<Your Consumer Key>";
	final static String oauth_consumer_secret = "5v4NFFvDlNSfxmVKFo0XW4DJpNWLWsBO7SDfSQtfeE";// "<Your Consumer Secret>";

	private MainActivity activity; // looks dummy to pass an activity to
	// current class..

	List<Item> arrayOfList;
	ListView listView;
	private NewsRowAdapter objAdapter;
	private ExecutorService executor;

	private int itemsNumber = 0;
	protected static int loadedItemsCounter = 0;

	private static long maxId;

	public MyTask(ListView listView, List<Item> arrayOfList,
			MainActivity activity) {
		this.listView = listView;
		this.arrayOfList = arrayOfList;
		this.activity = activity;
		
	}

	ProgressDialog pDialog;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if (arrayOfList.size() == 0) {
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

	}

	@Override
	protected String doInBackground(String... params) {
		try {			 
			return fetchTimelineTweet(twitURL);
			} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (null != pDialog && pDialog.isShowing()) {
			pDialog.dismiss();
		}

		if (null == result || result.length() == 0) {
			showToast("No data found from web!!!");
			activity.finish();
		} else {

			try {
				JSONObject mainJson = new JSONObject(result);
				JSONArray jsonArray = mainJson.getJSONArray(ARRAY_NAME);
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject objJson = jsonArray.getJSONObject(i);

					Item objItem = new Item();

					objItem.setDate(objJson.getString(DATE));
					objItem.setUserName(objJson.getJSONObject("user").getString(USERNAME));
					objItem.setImageURL(objJson.getJSONObject("user").getString(IMAGE_URL));
					objItem.setText(objJson.getString(TEXT));
					arrayOfList.add(objItem);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			setAdapterToListview();

		}

		loadedItemsCounter++;
	}

	public void setAdapterToListview() {
		objAdapter = new NewsRowAdapter(activity, arrayOfList, listView);	
		listView.setAdapter(objAdapter);
			
	}

	public void showToast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

	private static String fetchTimelineTweet(String endPointUrl)
			throws IOException {
		HttpsURLConnection connection = null;

		try {
			URL url = new URL(endPointUrl);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "anyApplication");
			connection.setRequestProperty("Authorization", "Bearer "+ bearerToken);
			connection.setUseCaches(false);

			String str = readResponse(connection);

			return str;
		} catch (MalformedURLException e) {
			throw new IOException("Invalid endpoint URL specified.", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	// Encodes the consumer key and secret to create the basic authorization key
	private static String encodeKeys(String consumerKey, String consumerSecret) {
		try {
			String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
			String encodedConsumerSecret = URLEncoder.encode(consumerSecret,
					"UTF-8");

			String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
			byte[] encodedBytes = Base64.encodeBase64(fullKey.getBytes());

			return new String(encodedBytes);
		} catch (UnsupportedEncodingException e) {
			return new String();
		}
	}

	// Constructs the request for requesting a bearer token and returns that
	// token as a string
	private static String requestBearerToken(String endPointUrl)
			throws IOException {
		HttpsURLConnection connection = null;
		String encodedCredentials = encodeKeys(oauth_consumer_key, oauth_consumer_secret);

		try {
			URL url = new URL(endPointUrl);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Host", "api.twitter.com");
			connection.setRequestProperty("User-Agent", "anyApplication");
			connection.setRequestProperty("Authorization", "Basic "
					+ encodedCredentials);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			connection.setRequestProperty("Content-Length", "29");
			connection.setUseCaches(false);
			
			System.out.println("requestToken connection 10 " + connection.getResponseCode());

			writeRequest(connection, "grant_type=client_credentials");

			// Parse the JSON response into a JSON mapped object to fetch fields
			// from.

			JSONObject obj = new JSONObject(readResponse(connection));
			
			if (obj != null) {
				String tokenType = (String) obj.get("token_type");
				String token = (String) obj.get("access_token");

				return ((tokenType.equals("bearer")) && (token != null)) ? token
						: "";
			}

		} catch (MalformedURLException e) {
			throw new IOException("Invalid endpoint URL specified.", e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			return new String();
		}
	}

	// Writes a request to a connection
	private static boolean writeRequest(HttpsURLConnection connection,
			String textBody) {
		try {
			BufferedWriter wr = null;

			wr = new BufferedWriter(new OutputStreamWriter(
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
		
		StringBuilder str = new StringBuilder();
		String result = null;
			try {
				

				BufferedReader br = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String line = "";
				while ((line = br.readLine()) != null) {
					str.append(line + System.getProperty("line.separator"));
				}
				result = str.toString();
				
			} catch (IOException e) {
			}
		return result;
	}

}