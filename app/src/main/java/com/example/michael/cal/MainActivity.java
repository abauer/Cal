package com.example.michael.cal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.ServiceConnection;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.michael.cal.CalSQL.CalSqlAdapter;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, AccelFragment.AccelerometerInterface{
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static CalSqlAdapter calSqlAdapter;

    private boolean isTakingData, isWalking;
    public String timeStamp;
    private String GoogleAccountEmail;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static CalSqlAdapter getAdapter(){
        return calSqlAdapter;
    }
    public static CalSqlAdapter setAdapter(CalSqlAdapter c){
       return calSqlAdapter = c;
    }
    public static final String TAG = "CalNsd";

    //CalNsdManager mNsdManager;
   // CalNsdConnection mConnection;

    private Handler mUpdateHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (!pref.getBoolean("doneTraining", false)) {
            Log.i("Daniel","Should be entering Training");
            startActivity(TrainingActivity.class);
        }
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout)); // Set up the drawer.
        isWalking = false;

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
                Log.d(TAG, chatLine);
            }
        };
        getGoogleAccountEmail();

      //  mConnection = new CalNsdConnection(mUpdateHandler);

       // mNsdManager = new CalNsdManager(this);
       // mNsdManager.initializeNsd();
    }
    public String getGoogleAccountEmail() {
        if (GoogleAccountEmail == null) {
            AccountManager am = (AccountManager) this.getSystemService(this.ACCOUNT_SERVICE);
            Account[] accounts = am.getAccounts();
            for (Account a : accounts) {
                if (a.type.equals("com.google")) {
                    GoogleAccountEmail = a.name;
                    return GoogleAccountEmail;
                }
            }
            return null;
        }
        return GoogleAccountEmail;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("timeStamp", timeStamp);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, boolean fromSavedInstanceState) {
        if (!fromSavedInstanceState) {                                                              // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            if (position == 0) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TrainingFragment.newInstance(position + 1))
                        .commit();
            } else if (position == 4) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CalNetworkFragment.newInstance(position + 1))
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
            }
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);                                           // Only show items in the action bar relevant to this screen
            restoreActionBar();                                                                     // if the drawer is not showing. Otherwise, let the drawer
            return true;                                                                            // decide what to show in the action bar.
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();                                                                  // Handle action bar item clicks here. The action bar will
        switch (id){                                                                                // automatically handle clicks on the Home/Up button, so long
            case R.id.action_settings:                                                              // as you specify a parent activity in AndroidManifest.xml.
                showPopup();
                return true;
            case R.id.action_advertise:
                do_advertise();
                return true;
            case R.id.action_discover:
                do_discover();
                return true;
            case R.id.action_connect:
                do_connect();
                return true;
            case R.id.action_send:
                //mConnection.sendMessage(GoogleAccountEmail);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void do_advertise() {
      //  if(mConnection.getLocalPort() > -1) {
        //    mNsdManager.registerService(mConnection.getLocalPort());
       // }
       // else
        //    Log.d(TAG, "ServerSocket isn't bound.");
    }
    public void do_discover() {
    //    mNsdManager.discoverServices();
    }
    public void do_connect() {
      //  NsdServiceInfo service = mNsdManager.getChosenServiceInfo();
        //if (service != null) {
         //   Log.d(TAG, "Connecting. Sending " + GoogleAccountEmail);
          //  mConnection.connectToServer(service.getHost(), service.getPort());
          //  mConnection.sendMessage(GoogleAccountEmail);
      //  }
      //  else
      //      Log.d(TAG, "No service to connect to!");
    }
    @Override
    protected void onResume(){
        super.onResume();
     //   if(mNsdManager != null)
      //      mNsdManager.discoverServices();
    }

    @Override
    protected void onPause(){
       // if(mNsdManager != null)
        //    mNsdManager.stopDiscovery();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
       // mNsdManager.tearDown();
       // mConnection.tearDown();
        super.onDestroy();
    }

    @Override
    public void setIsTakingData(boolean state) {
        isTakingData = state;
    }

    @Override
    public void setWalkingState(boolean state) {
        isWalking = state;
        Context context = getApplicationContext();
        String walkingState;
        if (isWalking)
            walkingState = "walking!";
        else
            walkingState = "Not Walking!";
        Toast toast = Toast.makeText(context, walkingState , Toast.LENGTH_SHORT );
        toast.show();

    }

    private void showPopup(){
        int a = LinearLayout.LayoutParams.WRAP_CONTENT;
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_settings, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,a,a,true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 40);
        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) { popupWindow.dismiss();}});
        Button btnRetrain = (Button)popupView.findViewById(R.id.retrain);
        btnRetrain.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("MyPref", 0).edit();
                editor.putBoolean("doneTraining", false);
                editor.commit();
                popupWindow.dismiss();
            }});
    }

    private void startActivity(Class c){
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
