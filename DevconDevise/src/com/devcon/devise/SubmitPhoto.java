package com.devcon.devise;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SubmitPhoto extends Activity {
	ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_photo);
	}
	@Override
	public void onStart(){
		super.onStart();
		img = (ImageView) findViewById(R.id.imageView1);
		Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("Image");
		img.setImageBitmap(bitmap);
	}
	public void clickCancel(View v){
		finish();
	}
}
