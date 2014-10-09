public class GPSLocatorActivity extends MapActivity {
    private MapView mapView;
    private MapController mapController;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    

        locationListener = new GPSLocationListener();

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 
            0, 
            0, 
            locationListener);

        mapView = (MapView) findViewById(R.id.mapView);

        // enable Street view by default
        mapView.setStreetView(true);

        // enable to show Satellite view
        // mapView.setSatellite(true);

        // enable to show Traffic on map
        // mapView.setTraffic(true);
        mapView.setBuiltInZoomControls(true);

        mapController = mapView.getController();
        mapController.setZoom(16); 
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private class GPSLocationListener implements LocationListener 
    {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                GeoPoint point = new GeoPoint(
                        (int) (location.getLatitude() * 1E6), 
                        (int) (location.getLongitude() * 1E6));

                /* Toast.makeText(getBaseContext(), 
                        "Latitude: " + location.getLatitude() + 
                        " Longitude: " + location.getLongitude(), 
                        Toast.LENGTH_SHORT).show();*/

                mapController.animateTo(point);
                mapController.setZoom(16);

                // add marker
                MapOverlay mapOverlay = new MapOverlay();
                mapOverlay.setPointToDraw(point);
                List<Overlay> listOfOverlays = mapView.getOverlays();
                listOfOverlays.clear();
                listOfOverlays.add(mapOverlay);
                String address = ConvertPointToLocation(point);
                Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
                mapView.invalidate();
            }
        }

        public String ConvertPointToLocation(GeoPoint point) {   
            String address = "";
            Geocoder geoCoder = new Geocoder(
                    getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(
                    point.getLatitudeE6()  / 1E6, 
                    point.getLongitudeE6() / 1E6, 1);

                if (addresses.size() > 0) {
                    for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                        address += addresses.get(0).getAddressLine(index) + " ";
                }
            }
            catch (IOException e) {                
                e.printStackTrace();
            }   

            return address;
        } 

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class MapOverlay extends Overlay
    {
        private GeoPoint pointToDraw;

        public void setPointToDraw(GeoPoint point) {
            pointToDraw = point;
        }

        public GeoPoint getPointToDraw() {
            return pointToDraw;
        }

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            super.draw(canvas, mapView, shadow);                   

            // convert point to pixels
            Point screenPts = new Point();
            mapView.getProjection().toPixels(pointToDraw, screenPts);

            // add marker
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.red);
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 24, null); // 24 is the height of image        
            return true;
        }
    } 
}