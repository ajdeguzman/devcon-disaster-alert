package com.devcon.devise;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MenuReport extends Fragment {
	
	public MenuReport(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View root = inflater.inflate(R.layout.layout_report, container, false);
        return root;
    }
}
