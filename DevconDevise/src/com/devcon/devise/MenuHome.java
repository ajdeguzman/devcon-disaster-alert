package com.devcon.devise;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MenuHome extends Fragment {
	
	public MenuHome(){}
	private MenuItem menuItem;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.layout_home, container, false);
        return rootView;
    }
	
		
}
