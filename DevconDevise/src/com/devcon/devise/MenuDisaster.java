package com.devcon.devise;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MenuDisaster extends Fragment implements ClusterManager.OnClusterClickListener<Person>, ClusterManager.OnClusterInfoWindowClickListener<Person>, ClusterManager.OnClusterItemClickListener<Person>, ClusterManager.OnClusterItemInfoWindowClickListener<Person> {
	
	public MenuDisaster(){}
	private GoogleMap map;
	private Spinner spinnerDisasterType;
    public ClusterManager<Person> mClusterManager;

	final String APPLICATION_ID = "GBdi0gjuAzS7MilcllE4vgMpEaJ8NdFCGsLMIJci";
	final String CLIENT_KEY = "R3ww5CExVqUo9LCo13d4dO2mhNA1RHicuTcGpnLf";
	
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	List<ParseObject> obj;
	Bitmap bmp;
    
	private LatLngBounds PHILIPPINES = new LatLngBounds(new LatLng(4.6145711,119.6272661), new LatLng(19.966096,124.173694));
	private static View view;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        cd  =  new ConnectionDetector(getActivity());
        try {
            view = inflater.inflate(R.layout.layout_disaster, container, false);
            isInternetPresent = cd.isConnectingToInternet();
    		if (isInternetPresent) {
    			initializeMap();
    			Parse.initialize(getActivity(), APPLICATION_ID, CLIENT_KEY);
    		}else{
            	Toast.makeText(getActivity(), "Please check your internet connection and try again.", Toast.LENGTH_LONG).show();
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
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {
			initializeMap();
		}
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
            mImageView.setImageBitmap(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).snippet(person.snippet).title(person.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                if (profilePhotos.size() == 4) break;
                Drawable d = new BitmapDrawable(getResources(), p.profilePhoto);
                Drawable drawable = d;
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
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
		query.setLimit(1000);
		 try {
             obj = query.find();
         } catch (ParseException e) {
             Log.e("Error", e.getMessage());
             e.printStackTrace();
         }
		for (ParseObject geo : obj) {
			ParseGeoPoint g  = geo.getParseGeoPoint("points");
			ParseFile fileObject = (ParseFile)geo.get("image");
		     fileObject.getDataInBackground(new GetDataCallback() {
		         public void done(byte[] data, ParseException e) {
		           if (e == null) {
		        	   bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
		           } else {
		             Toast.makeText(getActivity(), "There was a problem downloading the data." , Toast.LENGTH_SHORT).show();
		           }
		         }
		       });
			LatLng latlng = new LatLng(g.getLatitude(), g.getLongitude());
			mClusterManager.addItem(new Person(latlng, geo.getString("title"), geo.getString("address"), bmp));
		}
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
