package com.example.sok.navigationdrawer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.sok.navigationdrawer.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapFragment extends SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};

    private int mCurMapTypeIndex = 0;
    private boolean mResolvingError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_map, container, false);

        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v != null) {
            layout.addView(v, 0);
        }
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();

        mCurMapTypeIndex = MAP_TYPES[1];

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                if (map != null) {
                    mMap = map;
                }
                initListeners();
                getTeamInfo();
            }
        });
    }

    private void getTeamInfo() {
        //TODO запрос на сервер для получения инфы о команде
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    private void initListeners() {
        if (mMap != null) {
            mMap.setOnMarkerClickListener(MapFragment.this);
            mMap.setOnMapLongClickListener(MapFragment.this);
            mMap.setOnInfoWindowClickListener(MapFragment.this);
            mMap.setOnMapClickListener(MapFragment.this);
        }
    }

    private void removeListeners() {
        if (mMap != null) {
            mMap.setOnMarkerClickListener(null);
            mMap.setOnMapLongClickListener(null);
            mMap.setOnInfoWindowClickListener(null);
            mMap.setOnMapClickListener(null);
        }
    }

    private void initCamera(Location location) {
        final CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            mMap.setMapType(MAP_TYPES[mCurMapTypeIndex]);
            mMap.setMyLocationEnabled(true);
            mMap.stopAnimation();
            UiSettings mapSettings = mMap.getUiSettings();
            mapSettings.setCompassEnabled(true);
            mapSettings.setMyLocationButtonEnabled(true);
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setRotateGesturesEnabled(true);
            mapSettings.setZoomGesturesEnabled(true);
            mapSettings.setScrollGesturesEnabled(true);
            mapSettings.setIndoorLevelPickerEnabled(false);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        initCamera(mCurrentLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) return; // Already attempting to resolve an error.
        if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(), "Clicked on marker", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        if (mMap != null) {
            MarkerOptions options = new MarkerOptions().position(latLng);
            options.title(getAddressFromLatLng(latLng));
            options.icon(BitmapDescriptorFactory.defaultMarker()); //replace with custom marker
            mMap.addMarker(options);
        }
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        if (mMap != null) {
            MarkerOptions options = new MarkerOptions().position(latLng);
            options.title(getAddressFromLatLng(latLng));
            options.icon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
            mMap.addMarker(options);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == Activity.RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.setCallback(new ErrorDialogFragment.ErrorDialogCallback() {
            @Override
            public void onDialogDismissed() {
                mResolvingError = false;
            }
        });
        dialogFragment.show(getActivity().getSupportFragmentManager(), "errordialog");
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private ErrorDialogCallback mCallback;

        public interface ErrorDialogCallback {
            void onDialogDismissed();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            mCallback.onDialogDismissed();
        }

        public void setCallback(ErrorDialogCallback callback) {
            this.mCallback = callback;
        }
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());
        String address = "";
        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            //TODO handle
        }
        return address;
    }

    private void toggleTraffic() {
        if (mMap != null) {
            mMap.setTrafficEnabled(!mMap.isTrafficEnabled());
        }
    }

    private void cycleMapType() {
        if (mCurMapTypeIndex < MAP_TYPES.length - 1) {
            mCurMapTypeIndex++;
        } else {
            mCurMapTypeIndex = 0;
        }
        if (mMap != null) {
            mMap.setMapType(MAP_TYPES[mCurMapTypeIndex]);
        }
    }

    private void drawCircle(final LatLng location) {
        if (mMap != null) {
            CircleOptions options = new CircleOptions();
            options.center(location);
            options.radius(10); //in meters
            options.fillColor(getResources().getColor(R.color.maps_fill_color));
            options.strokeColor(getResources().getColor(R.color.maps_stroke_color));
            options.strokeWidth(10);
            mMap.addCircle(options);
        }
    }

    //draws image on map
    private void drawOverlay(final LatLng location, final int width, final int height) {
        if (mMap != null) {
            GroundOverlayOptions options = new GroundOverlayOptions();
            options.position(location, width, height);
            options.image(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
            mMap.addGroundOverlay(options);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) { //connection state doesn't matter
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListeners();
    }
}