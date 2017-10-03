package com.example.topic_test;

import java.util.Arrays;
import java.util.GregorianCalendar;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.google.ytdl.MainActivity;
import com.google.ytdl.ResumableUpload;

import com.google.ytdl.UploadService;

public class Create_Ans extends Activity implements ConnectionCallbacks,
OnConnectionFailedListener,OnPreparedListener{

	 public static final String ACCOUNT_KEY = "accountName";
	    public static final String MESSAGE_KEY = "message";
	    public static String Ytitle;
	    public static final String YOUTUBE_WATCH_URL_PREFIX = "http://www.youtube.com/watch?v=";
	    static final String REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth";
	    static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param";
	    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
	    private static final int REQUEST_GMS_ERROR_DIALOG = 1;
	    private static final int REQUEST_ACCOUNT_PICKER = 2;
	    private static final int REQUEST_AUTHORIZATION = 3;
	    private static final int RESULT_PICK_IMAGE_CROP = 4;
	    private static final String TAG = "MainActivity";
	    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	    final JsonFactory jsonFactory = new GsonFactory();
	    GoogleAccountCredential credential;
	    private String mChosenAccountName;
	    private Uri mFileURI = null;
	    private UploadBroadcastReceiver broadcastReceiver;
	    private GoogleApiClient mGoogleApiClient;
	

	long totalSize = 0;;
	String query_add,query_update,thisQ,Iam,data_name,who,read,tmp_text,Myname;
	
	String Server_Time;
 	private TextView txtPercentage;
 	View progress1;
 	ProgressBar progressBar;
	ImageButton imbtn_pick,imbtn_del;
	Button btn_send;
 	EditText edt_rtext,edt_path;
 	VideoView vv_lookAns;
 	MediaController mCtrl;
 	TextView txt_title;
 	@Override
    protected void onResume() {
        super.onResume();
        if(Hello_login.Iam==null)
		 {
			 finish();
		 }
        if (broadcastReceiver == null)
            broadcastReceiver = new UploadBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(
                REQUEST_AUTHORIZATION_INTENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, intentFilter);
    }
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisQ=Is_every.thisQ;
	    Iam =Hello_login.Iam; 
	    Myname=Hello_login.name;
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create__ans);
		
			txt_title=(TextView)findViewById(R.id.txt_video_title);
			imbtn_pick=(ImageButton)findViewById(R.id.imbtn_Pick);
			btn_send=(Button)findViewById(R.id.btn_Send);
			imbtn_del=(ImageButton)findViewById(R.id.imbtn_del_video);
			edt_rtext=(EditText)findViewById(R.id.edt_rtext);
			edt_path=(EditText)findViewById(R.id.edt_path);
			progressBar=(ProgressBar)findViewById(R.id.progress_vv);
			vv_lookAns=(VideoView)findViewById(R.id.vv_lookAns);
			vv_lookAns.setOnPreparedListener(this);
			mCtrl=new MediaController(this);
		
		if(Is_every_teach.thisQ.indexOf('s')==-1)
		{
			thisQ =Is_every.thisQ;
			Iam  =Hello_login.Iam;
			who=Iam;
			read="update reply set pig_read='1' where r_name!=r_who AND r_who="+Hello_login.Iam+" AND r_quest='"+thisQ+"' AND pig_read='0'";
		}
		else
		{
			thisQ =Is_every_teach.thisQ.substring(1);
			Iam   =Hello_login.Iam;
			who   =Is_every_teach.Who;
			read="update reply set pig_read='1' where r_name=r_who AND r_who="+who+" AND r_quest='"+thisQ+"' AND pig_read='0'";
		}
		
		youtube_ok(savedInstanceState);
		btn_listener();
       }
	
	public void btn_listener()
	{
		btn_send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Server_Time = new Server_Time().getServerTime();
				 
			if(ResumableUpload.isRun==false && UploadService.isRun==false)	
			{	if (mFileURI != null) {
					
					tmp_text=edt_rtext.getText().toString();
					query_add="INSERT INTO reply (r_name, r_who,r_quest,r_time,r_text,r_video) VALUES ("+Iam+","+who+",'"+thisQ+"','"+Server_Time+"','"+tmp_text+"',";
					
                 	Intent uploadIntent = new Intent(Create_Ans.this, UploadService.class);
                 	
                     uploadIntent.setData(mFileURI);
                     uploadIntent.putExtra(MainActivity.ACCOUNT_KEY, mChosenAccountName);
                     uploadIntent.putExtra("query", query_add);
                     
                     if(data_name.indexOf("面試")==-1 && data_name.indexOf(Myname)==-1)
                    	 Ytitle="面試_"+Myname+"_"+data_name;
                     else if(data_name.indexOf("面試")==-1)
                    	 Ytitle="面試_"+data_name;
                     else if(data_name.indexOf(Myname)==-1)
                     {
                     	String[] array= new String[2];
                     	array=data_name.split("面試");
         				Ytitle=array[0]+"面試_"+Myname+"_"+array[1];
                     }else
                     {
                      Ytitle=data_name;
                     }
                     Ytitle=Server_Time+"__"+Ytitle;
                     uploadIntent.putExtra("Youtubetitle", Ytitle);
                     startService(uploadIntent);
                     teach_update_time();
                     Toast.makeText(Create_Ans.this, R.string.youtube_upload_started,
                             Toast.LENGTH_LONG).show();
                    
                     Ytitle="";
                     mFileURI = null;
     				edt_rtext.setText("");
     				edt_path.setText("");
     				imbtn_del.setVisibility(View.INVISIBLE);
     				vv_lookAns.setVisibility(View.INVISIBLE);
     				txt_title.setVisibility(View.VISIBLE);
					
					showAlert("面試答案正在上傳");
				
				}
			}
			
				if(mFileURI==null && !"".equals(edt_rtext.getText().toString().trim()))
				{
					query_add="INSERT INTO reply (r_name, r_who,r_quest,r_time,r_text) VALUES ("+Iam+","+who+",'"+thisQ+"','"+Server_Time+"','"+edt_rtext.getText()+"');";
					
					try {
						
			            DBConnector.executeQuery(query_add,Create_Ans.this);
			            teach_update_time();
			            showAlert("面試回答已傳送");
			            txtPercentage.setText("面試回答已傳送");
			            edt_rtext.setText("");
			            }
			         catch(Exception e) {
			        	 showAlert("失敗欸");
			            // Log.e("log_tag", e.toString());
			         }
				}else if(!ResumableUpload.isRun==false || !UploadService.isRun==false)
				{
					Toast.makeText(Create_Ans.this, "請等待前一筆上傳作業完成  . . .", Toast.LENGTH_LONG).show();
				}
				
				Reading();
			}
			
		});
		
     imbtn_pick.setOnClickListener( new OnClickListener(){
        	
            public void onClick(View arg0) {
            	Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, RESULT_PICK_IMAGE_CROP);
              }
        });
     
	}
	public void Reading()
    {
   	 DBConnector.executeQuery(read,this);
    }
	public void teach_update_time()
	{
		if(Is_every_teach.thisQ.indexOf('s')!=-1)
        {
        query_update="update reply set teach_when='"+Server_Time+"' where r_name=r_who AND r_who="+who+" AND r_quest='"+thisQ+"' AND teach_when<r_time";
        DBConnector.executeQuery(query_update,Create_Ans.this);
        }
	}
	public void del_video(View v){
		if(vv_lookAns.isPlaying())
		{
		vv_lookAns.stopPlayback();
		}
		edt_path.setText("");
		txt_title.setVisibility(View.VISIBLE);
		vv_lookAns.setVisibility(View.INVISIBLE);
		mFileURI = null;
		imbtn_del.setVisibility(View.INVISIBLE);
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
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    broadcastReceiver);
        }
        if (isFinishing()) {
            // mHandler.removeCallbacksAndMessages(null);
        }
        vv_lookAns.stopPlayback();
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);

	       if(version >= 5) {
	    	   overridePendingTransition(R.anim.addq_out_nono, R.anim.addq_out_down);
	    	   }
    }

	private void showAlert(String message) {
		LayoutInflater factory=LayoutInflater.from(this);
		progress1=factory.inflate(R.layout.godlike_progress,null);
		txtPercentage = (TextView) progress1.findViewById(R.id.txtPercentage);
		progressBar = (ProgressBar) progress1.findViewById(R.id.progressBar);
		txtPercentage.setText("");
		
		new AlertDialog.Builder(this).setView(progress1).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).show();	
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       
        switch (requestCode) {
        
        case RESULT_PICK_IMAGE_CROP:
            if (resultCode == RESULT_OK) {
             mFileURI = data.getData();
            
             String[] proj = { MediaStore.Images.Media.DATA };
             Cursor actualimagecursor = managedQuery(mFileURI,proj,null,null,null);
             int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
             actualimagecursor.moveToFirst();
             String img_path = actualimagecursor.getString(actual_image_column_index);
             vv_lookAns.setVideoPath(img_path);
             vv_lookAns.setVisibility(View.VISIBLE);
             vv_lookAns.requestFocus();
             imbtn_del.setVisibility(View.VISIBLE);
             progressBar.setVisibility(View.VISIBLE);
             edt_path.setText(img_path);
             int last=img_path.lastIndexOf("/");
				data_name=img_path.substring(last+1);
            }
            break;
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
                        connectionStatusCode, Create_Ans.this,
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
   private class UploadBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REQUEST_AUTHORIZATION_INTENT)) {
                Log.d(TAG, "Request auth received - executing the intent");
                Intent toRun = intent
                        .getParcelableExtra(REQUEST_AUTHORIZATION_INTENT_PARAM);
                startActivityForResult(toRun, REQUEST_AUTHORIZATION);
            }
        }
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create__ans, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(ACCOUNT_KEY, mChosenAccountName);
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
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		vv_lookAns.setMediaController(mCtrl);
		progressBar.setVisibility(View.GONE);
		txt_title.setVisibility(View.GONE);
		mp.start();
		
		}

	
}
