package com.devcon.devise;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
	List<Address> addresses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);
		txtTitle = (EditText)findViewById(R.id.txtTitle);
		txtDescription = (EditText)findViewById(R.id.txtDescription);
        gps = new GPSTracker(SubmitPhoto.this);
		Parse.initialize(this, APP_ID, CLIENT_KEY);
	}
	public String getPlaceByLatLng(double lat, double longi){
		Geocoder geocoder = new Geocoder(SubmitPhoto.this, Locale.getDefault());
		String addressString = null;
		try {
			addresses = geocoder.getFromLocation(lat, longi, 1);
			StringBuilder place_name = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
					place_name.append(address.getAddressLine(i)).append("\n");
					place_name.append(address.getLocality()).append("\n");
					place_name.append(address.getPostalCode()).append("\n");
					place_name.append(address.getCountryName());
				}
			addressString = place_name.toString();

		} catch (IOException e) {
			 Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_LONG).show();
		}
		return addressString;
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
		   
		   Toast.makeText(getApplicationContext(), getPlaceByLatLng(latitude, longitude).toString()+"", Toast.LENGTH_LONG).show();

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
			showDone();
		}
	}
	public void showDone(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Disaster Photo successfully submitted.");
  
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	finish();
            	dialog.cancel();
            }
        });
  
        alertDialog.show();
    }
	public void clickCancel(View v){
		finish();
	}
}