package com.devcon.devise;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MenuReport extends Fragment {
	private static final int PICK_IMAGE = 1;
	public MenuReport(){}
	private ImageButton imgBrowse;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View root = inflater.inflate(R.layout.layout_report, container, false);
        imgBrowse = (ImageButton)root.findViewById(R.id.imgBrowse);
        imgBrowse.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
			}
        	
        });
        return root;
    }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
	        Uri _uri = data.getData();

	        //User had pick an image.
	        Cursor cursor = getActivity().getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();

	        //Link to the image
	        final String imageFilePath = cursor.getString(0);
	        cursor.close();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
}
