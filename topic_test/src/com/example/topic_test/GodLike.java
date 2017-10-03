package com.example.topic_test;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.ytdl.Auth;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class GodLike extends FragmentActivity implements ViewPager.OnPageChangeListener,ConnectionCallbacks,
OnConnectionFailedListener{
ImageButton playgo;
private ViewPager myViewPager;
private PagerAdapter myPagerAdapter;


public static final String ACCOUNT_KEY = "accountName";
public static final String MESSAGE_KEY = "message";
public static String Ytitle;
public static final String YOUTUBE_WATCH_URL_PREFIX = "http://www.youtube.com/watch?v=";
static final String REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth";
static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param";
private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;

private static final int REQUEST_ACCOUNT_PICKER = 2;
private static final int REQUEST_AUTHORIZATION = 3;

private static final String TAG = "MainActivity";
final HttpTransport transport = AndroidHttp.newCompatibleTransport();
final JsonFactory jsonFactory = new GsonFactory();
GoogleAccountCredential credential;
private String mChosenAccountName;
private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_god_like);
		
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	        .detectDiskReads()  
	        .detectDiskWrites()  
	        .detectNetwork()  
	        .penaltyLog()  
	        .build());  
	        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
	        .detectLeakedSqlLiteObjects()   
	        .penaltyLog()  
	        .penaltyDeath()  
	        .build());  
		
		new AlertDialog.Builder(this).setMessage("請做足準備，再來挑戰面試實戰模式\n\n這裡會隨機抽取一題\n\n在你看完題目10秒鐘後\n\n開始進行限時1分鐘的回答").setPositiveButton("Ok", null).show();
	/*	
		*/
		this.intialiseViewPager();
		youtube_ok(savedInstanceState);
	}

	private void intialiseViewPager() {
		
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Fragment3.class.getName()));
		//fragments.add(Fragment.instantiate(this, Fragment4.class.getName()));
		
		this.myPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		this.myViewPager = (ViewPager) super.findViewById(R.id.viewpager);
		this.myViewPager.setAdapter(this.myPagerAdapter);
		this.myViewPager.setOnPageChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.god_like, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		 
		super.onPause();
		 mGoogleApiClient.disconnect();
	        
	}

	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(Fragment3.isRUN==true)    
	    {    if (keyCode == KeyEvent.KEYCODE_BACK)
	        {
	            
	    	Toast.makeText(GodLike.this, "面試正在進行，請勿離開。", Toast.LENGTH_LONG).show();
	            return true;
	        }
	    }   
	        return super.onKeyDown(keyCode, event);
	    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Hello_login.isrun="F";
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 if(Hello_login.Iam==null)
		 {
			 finish();
		 }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void youtube_ok(Bundle savedInstanceState)
	{
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_PROFILE)
        .build();
        mGoogleApiClient.connect();
        
		credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(Auth.SCOPES));
        credential.setBackOff(new ExponentialBackOff());
        if (savedInstanceState != null) {
            mChosenAccountName = savedInstanceState.getString(ACCOUNT_KEY);
        } else {
            loadAccount();
        }
        credential.setSelectedAccountName(mChosenAccountName);
        
	}
	
	
	private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }
	
	private void loadAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        mChosenAccountName = sp.getString(ACCOUNT_KEY, null);
        invalidateOptionsMenu();
    }

    private void saveAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        sp.edit().putString(ACCOUNT_KEY, mChosenAccountName).commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       
        switch (requestCode) {
       
        case REQUEST_GOOGLE_PLAY_SERVICES:
            if (resultCode == Activity.RESULT_OK) {
                haveGooglePlayServices();
            } else {
                checkGooglePlayServicesAvailable();
                mGoogleApiClient.connect();
            }
            break;
        case REQUEST_AUTHORIZATION:
            if (resultCode != Activity.RESULT_OK) {
                chooseAccount();
            }
            break;
        case REQUEST_ACCOUNT_PICKER:
            if (resultCode == Activity.RESULT_OK && data != null
                    && data.getExtras() != null) {
                String accountName = data.getExtras().getString(
                        AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    mChosenAccountName = accountName;
                    credential.setSelectedAccountName(accountName);
                    saveAccount();
                }
            }else
            {
            	chooseAccount();
            }
            break;
        }
        
    }
	public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, GodLike.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }
	private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        }
    }
	private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
            Toast.makeText(this,
                    R.string.connection_to_google_play_failed, Toast.LENGTH_SHORT)
                    .show();

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.toString(), e);
            }
        }
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(ACCOUNT_KEY, mChosenAccountName);
	}

}
