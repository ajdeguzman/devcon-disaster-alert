package com.devcon.devise;

import com.devcon.devise.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuAbout extends Fragment {
	
	public MenuAbout(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.layout_about, container, false);
         
        return rootView;
    }
}
