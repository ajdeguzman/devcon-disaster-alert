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
	private Spinner spinnerDisasterType;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View root = inflater.inflate(R.layout.layout_report, container, false);
        spinnerDisasterType = (Spinner) root.findViewById(R.id.spinnerTypeDisaster);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity(), R.array.arr_disaster_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisasterType.setAdapter(adapter);
        return root;
    }
}
