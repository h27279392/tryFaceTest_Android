package com.example.topic_test;

import java.io.File;
import java.util.Arrays;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Add_Q extends Activity implements ConnectionCallbacks,
OnConnectionFailedListener,OnPreparedListener{
	
	 public static final String ACCOUNT_KEY = "accountName";
	    public static final String MESSAGE_KEY = "message";
	    public static String Ytitle;
	    public static final String YOUTUBE_WATCH_URL_PREFIX = "http://www.youtube.com/watch?v=";
	    static final String REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth";
	    static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param";
	    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
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
	
Spinner sper;
EditText edt;
Button btn;
String Iam,path,query_add,tmp_text,Myname;
String[] job_num,job_name;
Uri uri;
String data_name;
TextView text_path;
ImageButton btn_del;
VideoView Vv;
MediaController mCtrl;

	private ProgressBar progressBar;
	private TextView txtPercentage;
	View progress1;
	HttpPost R_time = new HttpPost("http://chentopic.no-ip.info/server_time.php");
	String Server_Time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_q);
		Iam=Hello_login.Iam;
		Myname=Hello_login.name;
		
		sper=(Spinner) findViewById(R.id.spier1);
		edt=(EditText)findViewById(R.id.edit1);
		btn=(Button)findViewById(R.id.button1);
		text_path=(TextView)findViewById(R.id.text_path);
		btn_del=(ImageButton)findViewById(R.id.del_video);
		mCtrl=new MediaController(this);
		Vv=(VideoView)findViewById(R.id.video_look);
		Vv.setOnPreparedListener(new OnPreparedListener(){
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Vv.setMediaController(mCtrl);
				mp.start();
			}});
		
		try {
            String result = DBConnector.executeQuery("SELECT * FROM job",this);
            JSONArray jsonArray = new JSONArray(result);
            job_name=new String[jsonArray.length()];
            job_num=new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonData = jsonArray.getJSONObject(i);
            	job_name    [i]=jsonData.getString("job_name");
            	job_num[i]=jsonData.getString("job_id");
            }
          ArrayAdapter  listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,job_name);
            sper.setAdapter(listAdapter);
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }
		
		youtube_ok(savedInstanceState);
		btn_listener();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(ACCOUNT_KEY, mChosenAccountName);
	}

	public void btn_listener(){
		
		btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				  try{
					  
					  
	 if("".equals(edt.getText().toString().trim()))
		{
		 Toast.makeText(Add_Q.this,"新增題目最基本的一定要有文字說明，題目影片愛放不放隨便你\n如果可以，最好別讓我一再的重複說明", Toast.LENGTH_SHORT).show();
		}
	 else	
		 {
		 
		 	if(mFileURI==null && !"".equals(edt.getText().toString().trim()))
		
	 		{
		   
		 		query_add="INSERT INTO test_quest (issue_text,who,belong) VALUES ('"+edt.getText()+"',"+Iam+","+job_num[sper.getSelectedItemPosition()]+");";
	            DBConnector.executeQuery(query_add,Add_Q.this);
	            showAlert("ok");
	            txtPercentage.setText("新題目:\n"+edt.getText()+"\n已傳送。");
	            edt.setText("");
	 		}else 
				if(ResumableUpload.isRun==false && UploadService.isRun==false)	
				{
		        	   if(!"".equals(edt.getText().toString().trim()) && mFileURI!=null)
		        	   {
		        		   tmp_text=edt.getText().toString();
		        		   query_add="INSERT INTO test_quest (issue_text,who,belong,issue_video) " +
					        		"VALUES ('"+edt.getText()+"',"+Iam+","+job_num[sper.getSelectedItemPosition()]+",";
		        		   
		                 	Intent uploadIntent = new Intent(Add_Q.this, UploadService.class);
		                     uploadIntent.setData(mFileURI);
		                     uploadIntent.putExtra(MainActivity.ACCOUNT_KEY, mChosenAccountName);
		                     uploadIntent.putExtra("query", query_add);
		                     
		                     if(data_name.indexOf("面試題目")==-1 && data_name.indexOf(Myname)==-1)
		                    	 Ytitle="面試題目_"+Myname+"_"+data_name;
		                     else if(data_name.indexOf("面試題目")==-1)
		                    	 Ytitle="面試題目_"+data_name;
		                     else if(data_name.indexOf(Myname)==-1)
		                     {
		                     	String[] array= new String[2];
		                     	array=data_name.split("面試題目");
		         				Ytitle=array[0]+"面試題目_"+Myname+"_"+array[1];
		                     }else
		                     {
		                    	 Ytitle=data_name;
		                     }
		                     Ytitle=new Server_Time().getServerTime()+"__"+Ytitle;
		                     uploadIntent.putExtra("Youtubetitle", Ytitle);
		                     startService(uploadIntent);
		                     Toast.makeText(Add_Q.this, R.string.youtube_upload_started,
		                             Toast.LENGTH_LONG).show();
							
							edt.setText("");
							mFileURI = null;
							text_path.setText("此題目未添加面試影片");
		        		   btn_del.setVisibility(View.GONE);
		        		   Vv.setVisibility(View.GONE);
		        		   showAlert("面試答案正在上傳");
		        		   
		        		}
				}	else if(!ResumableUpload.isRun==false || !UploadService.isRun==false)
					{
					Toast.makeText(Add_Q.this, "請等待前一筆上傳作業完成  . . .", Toast.LENGTH_LONG).show();
					}  
		 } 	    
		        	    
		           }  catch(Exception e) {
		        	   showAlert("有問題存在欸");
		        	   		txtPercentage.setText("失敗喔");
				         } 	
			}});
		
		
	
	
	}
	
	
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 mGoogleApiClient.disconnect();
	        if (broadcastReceiver != null) {
	            LocalBroadcastManager.getInstance(this).unregisterReceiver(
	                    broadcastReceiver);
	        }
	        if (isFinishing()) {
	            // mHandler.removeCallbacksAndMessages(null);
	        }
	        Vv.stopPlayback();
	        if(mFileURI==null)
	        {
	        	
	        }
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);

	       if(version >= 5) {
	    	   overridePendingTransition(R.anim.addq_out_nono, R.anim.addq_out_down);
	    	   }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add__q, menu);
		return true;
	}
	
public void Go_Record(View v)
{
	Intent i = new Intent();
	i.setAction("android.media.action.VIDEO_CAMERA");
	
	startActivity(i);
}
public void look_fk_video(View v)
{
    Intent intent = new Intent( Intent.ACTION_PICK );
    intent.setType( "video/*" );
    Intent destIntent = Intent.createChooser( intent, "選擇檔案" );
    startActivityForResult( destIntent, RESULT_PICK_IMAGE_CROP);
}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
public void del_fk_video(View v)
{	if(Vv.isPlaying())
	{
	Vv.stopPlayback();
	}
	Vv.setVisibility(View.GONE);
	btn_del.setVisibility(View.GONE);
	mFileURI=null;
	text_path.setText("此題目未添加面試影片");
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
             path = actualimagecursor.getString(actual_image_column_index);
             Vv.setVisibility(View.VISIBLE);
             Vv.setVideoPath(path);
             Vv.requestFocus();
             text_path.setText("影片路徑: "+path);
             int last=path.lastIndexOf("/");
				data_name=path.substring(last+1);
				btn_del.setVisibility(View.VISIBLE);
            }
            break;
        case REQUEST_AUTHORIZATION:
            if (resultCode != Activity.RESULT_OK) {
                chooseAccount();
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
	
	public void youtube_ok(Bundle savedInstanceState)
	{
		
		
		credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(Auth.SCOPES));
        credential.setBackOff(new ExponentialBackOff());
        if (savedInstanceState != null) {
            mChosenAccountName = savedInstanceState.getString(ACCOUNT_KEY);
        } else {
            loadAccount();
        }
        credential.setSelectedAccountName(mChosenAccountName);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_PROFILE)
        .build();
        mGoogleApiClient.connect();
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
	
    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, Add_Q.this,
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
						// do nothing
						
					}
				}).show();	
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
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

}
