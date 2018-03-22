package com.g2eandroid.routebetween_twolocation;

import android.*;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Activity_DynamicRoute extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    //TextView tvDistanceDuration;
    JSONParser jsonParser = new JSONParser();
    Context context;
    JSONArray responcearray;
    String strname, strbloodgroup;
    Dialog dialog1;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;



    // Waypoints
    String waypoints = "";


    // ---------------------------Current location-----------------------
    private static final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    TextView textempname, textempid;
    Button butcall, butwaiting, butpickup, butleave;
    double lat1;
    double lon1;
    private LocationManager locationManager;
    // ---------------------------Current location-----------------------
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {

            map.clear();
            markerPoints.clear();

            starttracking();

            handler.postDelayed(this, 30000);

        }
    };

    private void starttracking() {


        Log.e("testing","responserarray = "+responcearray);
        for (int i = 0; i < responcearray.length(); i++) {

            JSONObject post = null;
            try {
                post = responcearray.getJSONObject(i);
                double latitudes2 = Double.parseDouble(post.getString("latitude"));
                double longitudes2 = Double.parseDouble(post.getString("longitude"));


                if(i==2)
                    waypoints = "waypoints=";
                waypoints += latitudes2 + "," + longitudes2 + "|";

                // LatLng latlan = new LatLng(latitudes2,longitudes2);

                //markerPoints.add(latlan);


                map.addMarker(new MarkerOptions()
                        .title(strname)
                        .snippet(strbloodgroup)
                        .position(new LatLng(latitudes2, longitudes2
                        ))


                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );


                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        dialog1.setContentView(R.layout.customdialogbox);
                        dialog1.setTitle("Details");
                        dialog1.setCancelable(false);

                        ImageView imgcancel = (ImageView) dialog1.findViewById(R.id.imgcancel);
                        textempname = (TextView) dialog1.findViewById(R.id.textname);
                        textempid = (TextView) dialog1.findViewById(R.id.textempid);

                        butcall = (Button) dialog1.findViewById(R.id.call);
                        butwaiting = (Button) dialog1.findViewById(R.id.waiting);
                        butpickup = (Button) dialog1.findViewById(R.id.pickup);
                        butleave = (Button) dialog1.findViewById(R.id.absent);


                        imgcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });

                        butcall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        butwaiting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        butpickup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        butleave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });


                        dialog1.show();

                             /*   Intent intent = new Intent(context,Activity.class);
                                Log.e("testing","Name="+strname+" Blood group="+strbloodgroup);
                                Log.e("testing","Name=2"+marker.getTitle()+" position="+marker.getPosition());
                                Log.e("testing","Id="+marker.getId());

                                startActivity(intent);*/

                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        double lat2=Double.parseDouble("12.952372");
        double lon2=Double.parseDouble("77.620341");

        LatLng latlont11 = new LatLng(lat1,lon1);
        LatLng latlont22 = new LatLng(lat2,lon2);






        map.addMarker(new MarkerOptions()
                //.title(strname)
                //.snippet(strbloodgroup)
                .position(new LatLng(lat1, lon1
                ))


                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        );

        map.addMarker(new MarkerOptions()
                // .title(strname)
                //.snippet(strbloodgroup)
                .position(new LatLng(lat2, lon2
                ))


                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        LatLng dest = new LatLng(lat1, lon1);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(dest, 17.0f));

        String url = getDirectionsUrl(latlont11, latlont22);


        Log.e("testing","btndraewwww"+url);





        DownloadTask downloadTask = new DownloadTask();


        Log.e("testing","btndraewwww"+downloadTask);

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_route);

        //-------------------- Marshmallow Permission------------------------
        checkLocationPermission();
        //-------------------- Marshmallow Permission------------------------

        context = this;
        dialog1 = new Dialog(context);
       // tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);


        // ---------------------------Current location-----------------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone
        // ---------------------------Current location-----------------------



        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting reference to Button


        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        new LoadSpinnerdata().execute();

        handler.postDelayed(runnable, 10000);

        // Enable MyLocation Button in the Map
//        map.setMyLocationEnabled(true);

        // Setting onclick event listener for the map
       /* map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already 10 locations with 8 waypoints and 1 start location and 1 end location.
                // Upto 8 waypoints are allowed in a query for non-business users
                if(markerPoints.size()>=10){
                    markerPoints.clear();
                    map.clear();
                    //return;
                }
                // Adding new item to the ArrayList
                markerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                *//**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED and
                 * for the rest of markers, the color is AZURE
                 *//*
                if(markerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else if(markerPoints.size()==2){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }else{
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                // Add new marker to the Google Map Android API V2
                map.addMarker(options);
            }
        });*/

        // The map will be cleared on long click
      /*  map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                // Removes all the points from Google Map
                map.clear();

                // Removes all the points in the ArrayList
                markerPoints.clear();
            }
        });*/

        // Click event handler for Button btn_draw
      /*  btnDraw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String str_origin = "origin="+"12.525919"+","+"76.273863";
                String str_dest = "origin="+"12.597494"+","+"76.316796";

                double lat1=Double.parseDouble("12.966605");
                double lon1=Double.parseDouble("77.624032");

                double lat2=Double.parseDouble("12.952372");
                double lon2=Double.parseDouble("77.620341");

                LatLng latlont11 = new LatLng(lat1,lon1);
                LatLng latlont22 = new LatLng(lat2,lon2);






                map.addMarker(new MarkerOptions()
                        //.title(strname)
                        //.snippet(strbloodgroup)
                        .position(new LatLng(lat1, lon1
                        ))


                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );

                map.addMarker(new MarkerOptions()
                       // .title(strname)
                        //.snippet(strbloodgroup)
                        .position(new LatLng(lat2, lon2
                        ))


                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );

                String url = getDirectionsUrl(latlont11, latlont22);


                Log.e("testing","btndraewwww"+url);

                DownloadTask downloadTask = new DownloadTask();


                Log.e("testing","btndraewwww"+downloadTask);

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

               *//* // Checks, whether start and end locations are captured
                if(markerPoints.size() >= 2){
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);
                    Log.e("testing","origiin in button = "+origin);
                    Log.e("testing","dest in button = "+dest);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);


                    Log.e("testing","btndraewwww"+url);

                    DownloadTask downloadTask = new DownloadTask();


                    Log.e("testing","btndraewwww"+downloadTask);

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }*//*
            }
        });*/
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
    //-------------------- Marshmallow Permission------------------------

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Activity_DynamicRoute.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    //-------------------- Marshmallow Permission------------------------


    // ---------------------------Current location-----------------------
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
       // mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
       // mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        Log.e("testing","Latitude = "+location.getLatitude());
        Log.e("testing","longitude = "+location.getLongitude());
       // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Log.e("testing", "testvendorlocation====================");
        lat1=location.getLatitude();
        lon1=location.getLongitude();



    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // ---------------------------Current location-----------------------

    private String getDirectionsUrl(LatLng origin,LatLng dest){





        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        //String str_origin = "origin="+"12.525919"+","+"76.273863";
        //String str_dest = "origin="+"12.597494"+","+"76.316796";


     /*   // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
       *//* for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }
*//*
        for (int i = 0; i < responcearray.length(); i++) {

            JSONObject post = null;
            try {
                post = responcearray.getJSONObject(i);
                double latitudes2 = Double.parseDouble(post.getString("latitude"));
                double longitudes2 = Double.parseDouble(post.getString("longitude"));


                if(i==2)
                    waypoints = "waypoints=";
                waypoints += latitudes2 + "," + longitudes2 + "|";

               // LatLng latlan = new LatLng(latitudes2,longitudes2);

                //markerPoints.add(latlan);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        String sensor = "sensor=false";
        // Building the parameters to the web service
        Log.e("testing","origin in url = "+str_origin);
        Log.e("testing","dest in url = "+str_dest);
        Log.e("testing","waypoints in url = "+waypoints);
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){

            //    Log.d("Exception while downloading url", e.toString());

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;

            Log.e("testing","pointssss"+points);

            PolylineOptions lineOptions = null;

            // Traversing through all the routes

            String distance = "";
            String duration = "";

            Log.e("testing","distance--heading"+distance);
            Log.e("testing","distance--heading"+duration);

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){

                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");

                        Log.e("testing","point--distance"+distance);
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        Log.e("testing","point--duration"+duration);
                        continue;
                    }

                    if (point.get("lat")!= null && point.get("lng") != null) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));


                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED);

                Log.e("testing","points = "+points);
            }


            //tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);

            Log.e("testing","tvDistanceDuration--heading"+distance);
            Log.e("testing","tvDistanceDuration--heading"+duration);

            // Drawing polyline in the Google Map for the i-th route

            map.addPolyline(lineOptions);

            Log.e("testing","addpolyine = "+lineOptions);
        }
    }

    class LoadSpinnerdata extends AsyncTask<String, String, String> {
        String responce;

        String status;
        String Message;

        String strresult1, strresult2, strresult3;
        String img;
        String textname, textRent, textDeposit;
        // String glbarrstr_package_cost[];
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        public String doInBackground(String... args) {
            // Create an array

            // Retrieve JSON Objects from the given URL address
            List<NameValuePair> userpramas = new ArrayList<NameValuePair>();
            // Log.e("testing", "user_id value=" + id);
            //  userpramas.add(new BasicNameValuePair(End_Urls.CARTDETAILS_Userid, id));

            userpramas.add(new BasicNameValuePair("pincode", "560037"));
            JSONObject json = jsonParser.makeHttpRequest("http://androidappfirst.com/bloodbunk/check_donor_by_pincode.php", "POST", userpramas);



            Log.e("testing", "json result = " + json);
            if (json == null) {

                Log.e("testing", "jon11111111111111111");
                // Toast.makeText(getActivity(),"Data is not Found",Toast.LENGTH_LONG);

                return responce;
            }else {
                Log.e("testing", "jon2222222222222");

                try {
                    status = json.getString("status");
                    Message = json.getString("response");

                    String arrayresponce = json.getString("donor_details");


                    Log.e("testing", "adapter value=" + arrayresponce);

                    responcearray = new JSONArray(arrayresponce);
                    Log.e("testing", "responcearray value=" + responcearray);





                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return responce;
            }
        }

        @Override
        protected void onPostExecute(String responce) {
            super.onPostExecute(responce);
            pDialog.dismiss();
            Log.e("testing", "result is = " + responce);



            if (status.equals("Success")){


                for (int i = 0; i < responcearray.length(); i++) {

                    JSONObject post = null;
                    try {
                        post = responcearray.getJSONObject(i);
                        double latitudes2 = Double.parseDouble(post.getString("latitude"));
                        double longitudes2 = Double.parseDouble(post.getString("longitude"));
                        strname = post.getString("name");
                        strbloodgroup = post.getString("bloodgrp");



                        LatLng dest2 = new LatLng(latitudes2, longitudes2);
                        map.addMarker(new MarkerOptions()
                                .title(strname)
                                .snippet(strbloodgroup)
                                .position(new LatLng(latitudes2, longitudes2
                                ))


                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        );


                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                              /*  dialog1.setContentView(R.layout.customdialogbox);
                                dialog1.setTitle("Details");








                                dialog1.show();*/

                             /*   Intent intent = new Intent(context,Activity.class);
                                Log.e("testing","Name="+strname+" Blood group="+strbloodgroup);
                                Log.e("testing","Name=2"+marker.getTitle()+" position="+marker.getPosition());
                                Log.e("testing","Id="+marker.getId());

                                startActivity(intent);*/

                            }
                        });
                        // map.animateCamera(CameraUpdateFactory.newLatLngZoom(dest2, 13.0f));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }else{
                Toast.makeText(context, Message, Toast.LENGTH_LONG).show();


            }



        }




    }




}