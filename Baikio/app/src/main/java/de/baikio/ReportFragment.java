package de.baikio;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;

    private Firebase myFirebaseRef;
    private long _reportCount;
    private int next;

    private EditText reportText;
    private TextView location;
    private EditText description;

    private OnFragmentInteractionListener mListener;

    private GoogleApiClient mGoogleApiClient;
    private Location lastLocation;


    private RadioGroup radioLevelGroup;
    private RadioButton radioLevelButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        myFirebaseRef = new Firebase("https://baikio.firebaseio.com/reports");
        buildGoogleApiClient();

        // other setup code
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_report,
                container, false);

        reportText= (EditText) mView.findViewById(R.id.reportText);
        location = (TextView) mView.findViewById(R.id.txtViewLocation);
        description = (EditText) mView.findViewById(R.id.edShortDescription);

        radioLevelGroup = (RadioGroup) mView.findViewById(R.id.radioLevel);


        Button button = (Button) mView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                String format = s.format(new Date());

                // get selected radio button from radioGroup
                int selectedId = radioLevelGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioLevelButton = (RadioButton) mView.findViewById(selectedId);



                report nReport = new report(reportText.getText().toString(), location.getText().toString(), radioLevelButton.getText().toString(), description.getText().toString());
                String reportName = "report" + format;
                myFirebaseRef.child(reportName).setValue(nReport);

                reportText.setText("Name");
                description.setText("short description");


                Toast.makeText(getActivity(), "Thanks for reporting :)!",
                        Toast.LENGTH_LONG).show();
            }
        });



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

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d("LOCATION", lastLocation.getLatitude() +" " + lastLocation.getLongitude());

        if (lastLocation != null) {
            Log.d("LOCATION", lastLocation.getLatitude() +" " + lastLocation.getLongitude());
            location.setText(String.valueOf(lastLocation.getLatitude()) + " " + String.valueOf(lastLocation.getLongitude()));
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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


    public int NextReportNumber() {

        myFirebaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                _reportCount = snapshot.getChildrenCount();
                System.out.println("There are " + (int)_reportCount + " reports");

                for (DataSnapshot reportSnapshot: snapshot.getChildren()) {
                    next++;
                    report xReport = reportSnapshot.getValue(report.class);
                    System.out.println(xReport.get_Title() + " - " + xReport.get_Location() + " - " + xReport.get_damageType());
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        int next = (int)_reportCount;
        System.out.println("next number will be" + next++);
        return  next;
    }
    public void getReports(){
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                _reportCount = snapshot.getChildrenCount();
                _reportCount ++;
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }


}
