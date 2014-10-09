package com.devcon.devise;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class SubmitPhoto extends Activity {
	ImageView img;
	final String APP_ID = "GBdi0gjuAzS7MilcllE4vgMpEaJ8NdFCGsLMIJci";
	final String CLIENT_KEY = "R3ww5CExVqUo9LCo13d4dO2mhNA1RHicuTcGpnLf";
	Bitmap bitmap;
	EditText txtTitle, txtDescription;
	private ProgressDialog pd;
	GPSTracker gps;
	double latitude,longitude;
	Geocoder geocoder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);
		txtTitle = (EditText)findViewById(R.id.txtTitle);
		txtDescription = (EditText)findViewById(R.id.txtDescription);
        gps = new GPSTracker(SubmitPhoto.this);
		Parse.initialize(this, APP_ID, CLIENT_KEY);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		img = (ImageView) findViewById(R.id.imageView1);
		bitmap = (Bitmap) getIntent().getParcelableExtra("Image");
		img.setImageBitmap(bitmap);
	}
	public void clickSubmit(View v){
		SubmitPhotoTask spt = new SubmitPhotoTask();
		spt.execute();
	}

	private class SubmitPhotoTask extends AsyncTask<String,String,String>{
		@Override
		protected String doInBackground(String... arg0) {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    // get byte array here
		   byte[] bytearray= stream.toByteArray();
		   if(gps.canGetLocation()){
	             
	            latitude = gps.getLatitude();
	            longitude = gps.getLongitude();
	         }else{
	            gps.showSettingsAlert();
	        }
		   ParseObject user = new ParseObject("Places");
		   ParseGeoPoint location = new ParseGeoPoint();
		   location.setLatitude(latitude);
		   location.setLongitude(longitude);
		   user.put("title", txtTitle.getText().toString());
		   user.put("description", txtDescription.getText().toString());
		   user.put("points", location);
		    if (bytearray != null){
		        ParseFile file = new ParseFile(txtTitle.getText().toString().toLowerCase()+".png",bytearray);
		        file.saveInBackground();
		        user.put("image",file);
		    }
		    user.saveInBackground();
			return "";
		}
		protected void onPreExecute(){
			pd = new ProgressDialog(SubmitPhoto.this);
			pd.setMessage("Submitting Report...");
			pd.show();
			
		}
		@Override
		protected void onPostExecute(String str){
			pd.hide();
		}
	}
	public void clickCancel(View v){
		finish();
	}
}