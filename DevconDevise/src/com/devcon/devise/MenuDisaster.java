package com.devcon.devise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.devcon.devise.model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MenuDisaster extends Fragment implements ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person> {
	
	public MenuDisaster(){}
	private GoogleMap map;
	private Spinner spinnerDisasterType;
    private Random mRandom = new Random(1984);
    private ClusterManager<Person> mClusterManager;
    
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
		spinnerDisasterType = (Spinner) view.findViewById(R.id.spinView);
	    ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getActivity(), R.array.arr_view_map, R.layout.spinner_layout);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerDisasterType.setAdapter(adapter);
        return view;
    }
	private void initializeMap(){
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mClusterManager = new ClusterManager<Person>(getActivity(), map);
        mClusterManager.setRenderer(new PersonRenderer());
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        map.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
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
	private class PersonRenderer extends DefaultClusterRenderer<Person> {
        private final IconGenerator mIconGenerator = new IconGenerator(getActivity());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getActivity());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(getActivity(), map, mClusterManager);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View multiProfile = inflater.inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getActivity());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() > 1;
        }
    }
	private void addItems() {
        mClusterManager.addItem(new Person(position(), "Disaster 1", R.drawable.a));
        mClusterManager.addItem(new Person(position(), "Disaster 2", R.drawable.b));
        mClusterManager.addItem(new Person(position(), "Disaster 3", R.drawable.c));
        mClusterManager.addItem(new Person(position(), "Disaster 4", R.drawable.d));
        mClusterManager.addItem(new Person(position(), "Disaster 5", R.drawable.e));
        mClusterManager.addItem(new Person(position(), "Disaster 6", R.drawable.f));
        mClusterManager.addItem(new Person(position(), "Disaster 7", R.drawable.g));
        mClusterManager.addItem(new Person(position(), "Disaster 8", R.drawable.h));
        mClusterManager.addItem(new Person(position(), "Disaster 9", R.drawable.h));
    }
    private LatLng position() {
        return new LatLng(random(5.5158016,117.9349218), random(20.176906,123.5187749));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(getActivity(), cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {
    }

    @Override
    public boolean onClusterItemClick(Person item) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person item) {
    }
}
