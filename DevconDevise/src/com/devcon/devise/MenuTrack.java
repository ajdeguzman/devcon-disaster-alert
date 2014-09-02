package com.devcon.devise;

import com.devcon.devise.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuTrack extends Fragment {
	
	public MenuTrack(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.layout_track, container, false);
         
        return rootView;
    }
}
