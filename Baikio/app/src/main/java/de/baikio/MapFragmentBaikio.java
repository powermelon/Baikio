package de.baikio;



import android.content.Intent;
import android.net.Uri;


import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.ViewGroup;



import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.*;
import java.text.DateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragmentBaikio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragmentBaikio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragmentBaikio extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View mView;
    private FloatingActionButton fab;
    ListView classListView = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleApiClient gac;
    private Location miLocacion;
    private Button boton;
    private EditText direccion;
    private String tiempo;
    private LocationRequest mLocationRequest;
    private double latitud, longitud, latitudPedida,longitudPedida;
    private boolean encontraL,pidiendoUpdateLocacion;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragmentBaikio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_maps,
                container, false);



        final View actionB = mView.findViewById(R.id.action_b);

        FloatingActionButton actionC = new FloatingActionButton(getActivity());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) mView.findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);

        /*
        final FloatingActionButton removeAction = (FloatingActionButton) mView.findViewById(R.id.button_remove);
        removeAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FloatingActionsMenu) mView.findViewById(R.id.multiple_actions_down)).removeButton(removeAction);
            }
        });


        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));
        ((FloatingActionButton) mView.findViewById(R.id.setter_drawable)).setIconDrawable(drawable);

*/
        final FloatingActionButton actionA = (FloatingActionButton) mView.findViewById(R.id.action_a);
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
                Log.d("INFO", "A pressed");
                Intent intent = new Intent(getActivity(), CreateReportActivity.class);
            }
        });

        // Test that FAMs containing FABs with visibility GONE do not cause crashes
       // mView.findViewById(R.id.button_gone).setVisibility(View.GONE);

        /*
        final FloatingActionButton actionEnable = (FloatingActionButton) mView.findViewById(R.id.action_enable);
        actionEnable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                menuMultipleActions.setEnabled(!menuMultipleActions.isEnabled());
            }
        });
        */

       /*  FloatingActionsMenu rightLabels = (FloatingActionsMenu) mView.findViewById(R.id.right_labels);
        FloatingActionButton addedOnce = new FloatingActionButton(getActivity());
        addedOnce.setTitle("Added once");
        rightLabels.addButton(addedOnce);

        FloatingActionButton addedTwice = new FloatingActionButton(getActivity());
        addedTwice.setTitle("Added twice");
        rightLabels.addButton(addedTwice);
        rightLabels.removeButton(addedTwice);
        rightLabels.addButton(addedTwice);
*/
//Fragment fm = getFragmentManager().findFragmentById(R.id.map);
        MapFragment mapfrag = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);

        // MapFragment mapfrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //MapFragment mapfrag = (MapFragment) fm;
        //MapFragment mapfrag_old = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapfrag.getMapAsync(this);
        this.crearGoogleApiClient();
        this.createLocationRequest();

        this.boton=(Button) mView.findViewById(R.id.button);
        this.direccion=(EditText) mView.findViewById(R.id.editText);
        // Inflate the layout for this fragment

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    protected synchronized void crearGoogleApiClient(){
        this.gac = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        if(this.gac!=null){
            this.gac.connect();
        }
    }

    public void onConnected(Bundle conexion){
        miLocacion= LocationServices.FusedLocationApi.getLastLocation(this.gac);

        Log.d(this.miLocacion+"", "mi locacion");
        if(this.miLocacion!=null){
            this.latitud=this.miLocacion.getLatitude();
            this.longitud=this.miLocacion.getLongitude();
            if(this.encontraL==false){
                this.encontraL=true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.latitud, this.longitud),14));
                mMap.addMarker(new MarkerOptions().position(new LatLng(this.latitud,this.longitud)).title("Yo"));
            }


        }

    }

    protected void createLocationRequest() {
        //this.pidiendoUpdateLocacion=true;
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setInterval(10000);
        this.mLocationRequest.setFastestInterval(5000);
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(pidiendoUpdateLocacion){
            this.startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(this.gac, mLocationRequest, this);
    }


    public void onConnectionSuspended(int x){

    }

    public void onConnectionFailed(ConnectionResult x){

    }

    public void onLocationChanged(Location l){
        this.miLocacion=l;
        this.tiempo= DateFormat.getTimeInstance().format(new Date());
        this.updateUI();
    }

    private void updateUI() {
        this.latitud=this.miLocacion.getLatitude();
        this.longitud=this.miLocacion.getLongitude();
        /*if(destinoLat==this.latitud && destinoLong==this.longitud){
            this.stopUpdates();
        }*/
    }

    public void stopUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(this.gac, this);
    }

    public void onClick(View v){
        Geocoder code= new Geocoder(getActivity());
        if(code.isPresent()){
            Log.d("+++++++","esta entrando a las direccion");
        }
        try {
            Log.d("direccion", this.direccion.getText()+"");
            List<Address> direcciones = code.getFromLocationName(this.direccion.getText().toString()+",Guadalajara", 5);
            if(direcciones.size()>0) {
                Address ubicacion = direcciones.get(0);
                this.latitudPedida = ubicacion.getLatitude();
                this.longitudPedida = ubicacion.getLongitude();
                if (ubicacion != null) {
                    this.trazar();
                }
            }

        }
        catch (IOException e){
            Log.d("exception",e.toString() );
        }
    }

    public void trazar(){
        mMap.clear();
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(this.latitud, this.longitud))
                .add(new LatLng(this.latitudPedida, this.longitudPedida)));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap=map;
        /*map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction_arrow))
                .position(this.latitud, this.longitud)
                .flat(true)
                .rotation(245));*/
    }


}
