package com.example.topic_test;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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

public class Fragment3 extends Fragment implements SurfaceHolder.Callback,ConnectionCallbacks,
OnConnectionFailedListener{
	final int End=3;
    final int Countdown=1;
    final int playVideo=0;
    final int videoplayEnd=4;
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
	
	View view;
	Button playgo,btn_go_list;
	Timer tm;
	TimerTask tmTask;
	int i;
	TextView txt;
	LinearLayout linear_Q,linear_A;
	
	String rand_query="SELECT QID,issue_video from test_quest where issue_video!='' AND QID not in("+
	"select QID from test_quest,("+
	"select *,SUBSTR(r_quest, 2) as Rstr from reply where r_quest like'z%') as R1 " +
	"where Rstr=QID  and issue_video!='' and r_name=r_who and r_name="+Hello_login.Iam+" group by(QID))ORDER BY(RAND()) LIMIT 1";
	// 
	String result;
	Youtube youtube_fragment;
	
	private SurfaceView surfaceview;   
     MediaPlayer mediaPlayer;
     MediaController mc;
    private SurfaceHolder surfaceHolder;  
    SurfaceHolder holder;
     RCamera mthread; 
     SharedPreferences add_video_index ;
     int video_index,recorder_lim_time=5;
      
     
     private ProgressBar progressBar;
 	private String FILE_UPLOAD_URL,filePath = null;
 	private TextView txtPercentage;
 	long totalSize = 0;
 	String query_add,query_update,query_able,thisQ,Iam,data_name,who;
 	FrameLayout framelayout;
 	String Server_Time;
 	View progress1;
 	public static boolean isRUN=false;
 	
 	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment3, container, false);
		 LayoutInflater factory=LayoutInflater.from(view.getContext());
		progress1=factory.inflate(R.layout.godlike_progress,null);
		txtPercentage = (TextView) progress1.findViewById(R.id.txtPercentage);
		progressBar = (ProgressBar) progress1.findViewById(R.id.progressBar);
		
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
		
        who=Iam=Hello_login.Iam;
        add_video_index = getActivity().getSharedPreferences("vv",Context.MODE_PRIVATE);
    	video_index = add_video_index.getInt("godlike", 0);
        query_able="select count(r_video) from reply where r_name=r_who and r_name='"+Hello_login.Iam+"'";
		playgo=(Button)view.findViewById(R.id.playGo);
		txt=(TextView)view.findViewById(R.id.txt_countdown);
		linear_Q=(LinearLayout)view.findViewById(R.id.linear_Q);
		linear_A=(LinearLayout)view.findViewById(R.id.linear_A);
		 
		framelayout=(FrameLayout)view.findViewById(R.id.youtube_fragment_godlike);
		framelayout.setVisibility(View.INVISIBLE);
		
		btn_go_list=(Button)view.findViewById(R.id.btn_looklist);
		btn_go_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent(view.getContext(), Combat_List.class);
				startActivity(it);
			}
		});
			
		playgo.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				
				result = DBConnector.executeQuery(query_able,view.getContext());
				
				try {
					JSONObject jsonData;
					jsonData = new JSONArray(result).getJSONObject(0);
					
if(jsonData.getInt("count(r_video)")>0)
{
	
	if(ResumableUpload.isRun==false && UploadService.isRun==false)
	{
				result = DBConnector.executeQuery(rand_query,view.getContext());
				
		if(result.equals("null\n\n\n\n"))
		{
			Toast.makeText(view.getContext(), "恭賀您!! 成就已解滿，請等待老師出新實戰題。", Toast.LENGTH_LONG).show();
			
		}else{
			jsonData = new JSONArray(result).getJSONObject(0);
			youtube_fragment.setVideoId(jsonData.getString("issue_video"));
			thisQ=jsonData.getString("QID");
			isRUN=true;
			playgo.setVisibility(View.GONE);
			btn_go_list.setVisibility(View.GONE);
			txt.setVisibility(View.VISIBLE);
			txt.setTextSize(400);
			
				if (tm == null) {  
		            tm = new Timer(); 
		            i=6;
		        }  
		  
		        if (tmTask == null) {  
		            tmTask = new TimerTask() {  
		                @Override  
		                public void run() {  
		                 Message msg = new Message();
		                	if(i!=1)
		                	{
		                		i--;
		   	       	      	  msg.what = 1;
		   	       	      	  handler.sendMessage(msg);
		   	       	      	 
		                	}
		                	else
		                	{
		                		msg.what = 0;
		     	       	      	  handler.sendMessage(msg);	
		                	}
		         
		                	
		                }  
		            };  
		        }  
		       
		        
				if(tm != null && tmTask != null )  
		            tm.schedule(tmTask, 1, 1000); 
		}	
	}			  
	else
	{Toast.makeText(view.getContext(), "請等待前一筆上傳作業完成  . . .", Toast.LENGTH_LONG).show();}
}else
{Toast.makeText(view.getContext(), "請在練習區完成一次有面試影片的面試紀錄，即可解鎖", Toast.LENGTH_LONG).show();}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
				
			});
		
		youtube_ok(savedInstanceState);
		return view;
	}
	

	@Override
    public void onPause() {
        super.onPause();
     
        if (tm != null) {  
            tm.cancel();  
            tm = null; 
            
        }  
        if(youtube_fragment!=null)
        {
        youtube_fragment.release();
        youtube_fragment=null;
        }
        if (tmTask != null) {  
           tmTask.cancel();  
            tmTask = null;  
        }     
      
      // mthread.destroy();
        Log.e("onPause", "stop");
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(
                    broadcastReceiver);
        }
       
        //playgo.setVisibility(View.VISIBLE);
        add_video_index.edit().putInt("godlike",video_index).commit();
    }

    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 if (mthread!=null) {  
	    		
  		   mthread.stopRecord();  
            
             mthread=null;  
      }
	}


	@Override
    public void onResume() {
        super.onResume();
        if(Hello_login.Iam==null)
		 {
			 getActivity().finish();
		 }
      
        if(youtube_fragment==null)
        {
        
        youtube_fragment=Youtube.newInstance();
		FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.youtube_fragment_godlike, youtube_fragment).commit();
        }
        File dir=Environment.getExternalStorageDirectory(); 
	       String path=dir.getPath()+"/面試App"; 
	       File file=new File(path); 
	       if(!file.exists()) 
	        file.mkdir(); 

	       if (broadcastReceiver == null)
	            broadcastReceiver = new UploadBroadcastReceiver();
	        IntentFilter intentFilter = new IntentFilter(
	                REQUEST_AUTHORIZATION_INTENT);
	        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(
	                broadcastReceiver, intentFilter);
	       
  surfaceview = (SurfaceView) view.findViewById(R.id.surfaceView1); 
	       holder = surfaceview.getHolder();
	       holder.addCallback(this);
	       mediaPlayer = new MediaPlayer();
	       holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
	    //   surfaceview.setVisibility(View.GONE);
    }
    
	
    private Handler handler = new Handler(){
 @Override
 public void handleMessage(Message msg) {
  // TODO Auto-generated method stub
  super.handleMessage(msg);
  switch (msg.what){
  case 1:
	  txt.setText(String.valueOf(i));
	  break;
  case 0:
	  tmTask.cancel();
	  txt.setVisibility(View.GONE);
	  
	 // surfaceview.setVisibility(View.INVISIBLE);
	  framelayout.setVisibility(View.VISIBLE);
      youtube_fragment.play();
      
      new Timer().schedule(new TimerTask() {
    	int time=youtube_fragment.getVideoSumTime();
		int i=1000;
		@Override
		public void run() {
		if(youtube_fragment.getCurrentTime()>100)
		{
			Log.e("", "開始");
			i+=1000;
			if(i>time+3000)
			{
			
			 Message msg = new Message();
  	      	  msg.what = 4;
  	      	  handler.sendMessage(msg);
			this.cancel();
			}
		}
		}
	},1000,1000);
      
	  break;
  case 4:
      surfaceview.setBackgroundColor(Color.alpha(0));
      youtube_fragment.release();
      
	  if (mthread==null)
            {  
         	  video_index++;
         	  mthread = new RCamera(recorder_lim_time*1000, surfaceview, surfaceHolder,"01+265",video_index);
         	  mthread.start();  
         	
         	new Timer().schedule(new timeOver(), (recorder_lim_time+3)*1000);
            }
	  
	  break;
  case 3:
	 show1("傳送面試過程");
	 surfaceview.setBackgroundColor(Color.alpha(255));
	 isRUN=false;
	 playgo.setVisibility(View.VISIBLE);
	 btn_go_list.setVisibility(View.VISIBLE);
  	 break;
  default:
  }
     }};
     
     public void show1(String vv)
     { 
    	
		
    	 Server_Time=new Server_Time().getServerTime();
    	 query_add="INSERT INTO reply (r_name, r_who,r_quest,r_time,r_text,r_video) VALUES " +
    	 		"("+Iam+","+who+",'z"+thisQ+"','"+Server_Time+"','面試實戰練習',";

		Intent uploadIntent = new Intent(view.getContext(), UploadService.class);
		mFileURI=getImageContentUri(view.getContext(), new File(RCamera.godlike_path));
        uploadIntent.setData(mFileURI);
        uploadIntent.putExtra(MainActivity.ACCOUNT_KEY, mChosenAccountName);
        uploadIntent.putExtra("query", query_add);
        int last=RCamera.godlike_path.lastIndexOf("/");
		data_name=RCamera.godlike_path.substring(last+1);
        Ytitle=Server_Time+"__"+data_name;
        uploadIntent.putExtra("Youtubetitle", Ytitle);
        getActivity().startService(uploadIntent);
        Toast.makeText(view.getContext(), "正在上傳面試檔案",
                Toast.LENGTH_LONG).show();
    	 
     }
     
     class timeOver extends TimerTask {  
    	  
         
         @Override  
         public void run() {  
            // video_Quest.seekTo(video_Quest.getDuration());
            // video_Quest.start();
        	 Message msg = new Message();
       	      	  msg.what = 3;
       	      	  handler.sendMessage(msg);
             this.cancel();
         }  
     }
     
     @Override  
	   public void surfaceChanged(SurfaceHolder holder, int format, int width,  
	           int height) {  
	         
	       surfaceHolder = holder;  
	   }  
	 
	   @Override  
	   public void surfaceCreated(SurfaceHolder holder) {  
		
	       surfaceHolder = holder;  
	   }  
	 
	   @Override  
	   public void surfaceDestroyed(SurfaceHolder holder) {  
	      
	       surfaceview = null;  
	       surfaceHolder = null;  
	      // mediarecorder = null;
	       if (mediaPlayer != null) mediaPlayer.release();
	   }

	   public static Uri getImageContentUri(Context context, java.io.File imageFile) {  
	       String filePath = imageFile.getAbsolutePath();  
	       Cursor cursor = context.getContentResolver().query(  
	               MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  
	               new String[] { MediaStore.Images.Media._ID },  
	               MediaStore.Images.Media.DATA + "=? ",  
	               new String[] { filePath }, null);  
	       if (cursor != null && cursor.moveToFirst()) {  
	           int id = cursor.getInt(cursor  
	                   .getColumnIndex(MediaStore.MediaColumns._ID));  
	           Uri baseUri = Uri.parse("content://media/external/images/media");  
	           return Uri.withAppendedPath(baseUri, "" + id);  
	       } else {  
	           if (imageFile.exists()) {  
	               ContentValues values = new ContentValues();  
	               values.put(MediaStore.Images.Media.DATA, filePath);  
	               return context.getContentResolver().insert(  
	                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
	           } else {  
	               return null;  
	           }  
	       }  
	   }  
	 
	
	public void youtube_ok(Bundle savedInstanceState)
	{
		
        
		credential = GoogleAccountCredential.usingOAuth2(
               view.getContext(), Arrays.asList(Auth.SCOPES));
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
                .getDefaultSharedPreferences(view.getContext());
        mChosenAccountName = sp.getString(ACCOUNT_KEY, null);
        getActivity().invalidateOptionsMenu();
    }

    private void saveAccount() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(view.getContext());
        sp.edit().putString(ACCOUNT_KEY, mChosenAccountName).commit();
    }
   
	public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, getActivity(),
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
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
            Toast.makeText(view.getContext(),
                    R.string.connection_to_google_play_failed, Toast.LENGTH_SHORT)
                    .show();

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {
                connectionResult.startResolutionForResult(getActivity(), 0);
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



	
	

	}

	

