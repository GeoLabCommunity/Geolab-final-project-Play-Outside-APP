package geolab.playoutside.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GeoLab on 1/27/16.
 */
    public static class Detail_map_fragment extends Fragment 
    implements
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener,
    LocationListener {
        GoogleMap map;
        LatLng latlng;
        private LocationRequest lr;
        private LocationClient lc;
        MapFragment mapFragment;
        ImageView iv;
        private static View view;

        public Detail_map_fragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            if (view != null) {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null)
                    parent.removeView(view);
            }

            try {
                view = inflater.inflate(R.layout.XYZ, container,
                        false);

                mapFragment = ((MapFragment) this.getActivity()
                        .getFragmentManager().findFragmentById(R.id.map));
                iv = (ImageView) view.findViewById(R.id.iv);

                map = mapFragment.getMap();
                map.getUiSettings().setAllGesturesEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMyLocationEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(false);

                MapsInitializer.initialize(this.getActivity());
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(getActivity(), "Google Play Services missing !",
                        Toast.LENGTH_LONG).show();
            } catch (InflateException e) {
                Toast.makeText(getActivity(), "Problems inflating the view !",
                        Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "Google Play Services missing !",
                        Toast.LENGTH_LONG).show();
            }

            return view;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            lr = LocationRequest.create();
            lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            lc = new LocationClient(this.getActivity().getApplicationContext(),
                    this, this);
            lc.connect();
        }

        @Override
        public void onLocationChanged(Location l2) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(l2.getLatitude(), l2.getLongitude()), 15);
            map.animateCamera(cameraUpdate);
        }

        @Override
        public void onConnectionFailed(ConnectionResult arg0) {

        }

        @Override
        public void onConnected(Bundle connectionHint) {
            lc.requestLocationUpdates(lr, this);

        }

        @Override
        public void onDisconnected() {

        }
    }