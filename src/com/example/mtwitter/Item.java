package com.example.mtwitter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;

public class Item {

	private String dateString;
	private String userName;
	private String imageURL;
	private String text;
	private SimpleDateFormat simpleD;
	private Bitmap imageBmp;
	private long uniqId;
	
	public boolean imageUpdated = false;
	
	public int count;
	public static int totalCount=0;

//	public Item(Date datePosted, String message, String username,
//			String imageurl, String twitterHumanFriendlyDate) {
//		
//		date = datePosted;
//		text = message;
//		userName = username;
//		imageURL = imageurl;
//		
//
//	}

	public Item() {
		count = ++totalCount;
//		System.out.println("Item createrd "+count);
//		this.get`qa
	}

	public long getUniqId() {
		return uniqId;
	}
	
	public void setUniqId(long uniqId2) {
		this.uniqId = uniqId2;
	}
	
	public String getDate() {
		return dateString;
	}

	public void setDate(String dateString) {
		this.dateString = fomatDate(dateString);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Bitmap getImageBmp(){
		return imageBmp;
		
	}
	
	public void setImageBmp(Bitmap bitmap) {
		this.imageBmp = bitmap;
		
	}
	
	private String fomatDate(String dateString2) {
		Date date = new Date();
		simpleD = new SimpleDateFormat("EEE MMM dd kk:mm:ss Z yyyy");

		try {
			date = simpleD.parse(dateString2);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		simpleD = new SimpleDateFormat("dd MMM kk:mm:ss");
		return simpleD.format(date);
	}

}
