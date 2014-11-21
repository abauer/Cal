package com.example.michael.cal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import android.os.IBinder;

/**
 * Created by michael on 10/21/14.
 */
public class TrainingFragment extends PlaceholderFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ToggleButton mTakingDataButton;
    private ToggleButton mWalkButton;
    private Button mNewRoomButton;
    boolean bound = false;

    NthSense sensorService;
    dataServiceWiFi mWifiService;

    public static TrainingFragment newInstance(int sectionNumber) {
        TrainingFragment fragment = new TrainingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_training, container, false);

        mTakingDataButton = (ToggleButton) rootView.findViewById(R.id.take_data1);
        mWalkButton = (ToggleButton) rootView.findViewById(R.id.walkingToggle1);
        mNewRoomButton = (Button) rootView.findViewById(R.id.room);

        mTakingDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorService.set_is_takingData(mTakingDataButton.isChecked());
            }
        });

        mWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorService.set_is_walking(mWalkButton.isChecked());
            }
        });

        mNewRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiService.setRoomId(randomNumber());
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), NthSense.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        intent = new Intent(getActivity(), dataServiceWiFi.class);
        getActivity().bindService(intent, mConnectionWifi, Context.BIND_AUTO_CREATE);
        bound=true;
    }

    public void onDestroy() {
        if(bound) {                                                                                 //Michael claims "possibly dangerous" DO NOT STORE NEAR OPEN FLAMES
            getActivity().unbindService(mConnection);
            getActivity().unbindService(mConnectionWifi);
            bound = false;
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private int randomNumber(){
        return (int) (Math.random()*2000);
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            NthSense.NthBinder binder = (NthSense.NthBinder) service;
            sensorService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    private ServiceConnection mConnectionWifi = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            dataServiceWiFi.dataBinder binder = (dataServiceWiFi.dataBinder) service;
            mWifiService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

}
