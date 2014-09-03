package com.devcon.devise;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MenuDisaster extends Fragment {
	
	public MenuDisaster(){}
	private GoogleMap map;
	private LatLngBounds PHILIPPINES = new LatLngBounds(new LatLng(5.5158016,117.9349218), new LatLng(20.176906,123.5187749));
	private static View view;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.layout_disaster, container, false);
            if(checkInternetConnection()){
            	initializeMap();
            }else{
            	Toast.makeText(getActivity(), "Cannot connect to server", Toast.LENGTH_LONG).show();
            }
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return view;
    }
	private void initializeMap(){
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnCameraChangeListener(new OnCameraChangeListener() {

		    @Override
		    public void onCameraChange(CameraPosition arg0) {
		        map.moveCamera(CameraUpdateFactory.newLatLngBounds(PHILIPPINES, 10));
		        map.setOnCameraChangeListener(null);
		    }
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		if(checkInternetConnection()){
        	initializeMap();
        }else{
        	Toast.makeText(getActivity(), "Cannot connect to server", Toast.LENGTH_LONG).show();
        }
	}
	   private boolean checkInternetConnection(){
		   boolean connected = false;
		      ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		      if (check != null) 
		      {
		         NetworkInfo[] info = check.getAllNetworkInfo();
		         if (info != null) 
		            for (int i = 0; i <info.length; i++) 
		            if (info[i].getState() == NetworkInfo.State.CONNECTED)
		            {
		            	connected = true;
		            }
		      }
		      else{
		         Toast.makeText(getActivity(), "Cannot connect to the server.", Toast.LENGTH_SHORT).show();
		          }
		      return connected;
		   }
}
