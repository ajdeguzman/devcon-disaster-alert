package com.devcon.devise;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class SubmitPhoto extends Activity {
	ImageView img;
	final String APP_ID = "GBdi0gjuAzS7MilcllE4vgMpEaJ8NdFCGsLMIJci";
	final String CLIENT_KEY = "R3ww5CExVqUo9LCo13d4dO2mhNA1RHicuTcGpnLf";
	Bitmap bitmap;
	EditText txtTitle, txtDescription;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);
		txtTitle = (EditText)findViewById(R.id.txtTitle);
		txtDescription = (EditText)findViewById(R.id.txtDescription);
		Parse.initialize(this, APP_ID, CLIENT_KEY);
	}
	private class LoadTask extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... arg0) {
			return null;
		}
		
	}
	@Override
	public void onStart(){
		super.onStart();
		img = (ImageView) findViewById(R.id.imageView1);
		bitmap = (Bitmap) getIntent().getParcelableExtra("Image");
		img.setImageBitmap(bitmap);
	}
	public void clickSubmit(View v){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    // get byte array here
	   byte[] bytearray= stream.toByteArray();
	   
	   ParseObject user = new ParseObject("Places");
	   user.put("title", txtTitle.getText().toString());
	   user.put("description", txtDescription.getText().toString());
	    if (bytearray != null){
	        ParseFile file = new ParseFile(txtTitle.getText().toString().toLowerCase()+".png",bytearray);
	        file.saveInBackground();
	        user.put("image",file);
	    }
	    user.saveInBackground();

	}
	public void clickCancel(View v){
		finish();
	}
}