package com.devcon.devise;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MenuReport extends Fragment {
	private static final int PICK_IMAGE = 1;
	private static final int TAKE_PICTURE = 1;
	public MenuReport(){}
	private ImageButton imgBrowse, imgTake, imgMap;
	ProgressDialog progress;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View root = inflater.inflate(R.layout.layout_report, container, false);
        imgBrowse = (ImageButton)root.findViewById(R.id.imgBrowse);
        imgMap = (ImageButton)root.findViewById(R.id.imgMap);
        imgTake = (ImageButton)root.findViewById(R.id.imgTake);
        imgBrowse.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
			}
        	
        }); 
        imgTake.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE);
			}
        	
        });
        imgMap.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("position", 2);
					i.putExtras(bundle);
					startActivity(i);
			}
        });
        return root;
    }
	private class MyTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected void onPostExecute(String str) {
			progress.hide();
		}
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Getting location details...");
			progress.isIndeterminate();
			progress.show();
		}
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(requestCode == PICK_IMAGE) {
	    	Bitmap photo = (Bitmap) data.getExtras().get("data");
	    	Intent i = new Intent(getActivity(), SubmitPhoto.class);
	    	i.putExtra("Image", photo);
	    	startActivity(i);
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
}
