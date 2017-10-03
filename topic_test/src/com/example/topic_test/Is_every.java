package com.example.topic_test;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.ytdl.Auth;

public class Is_every extends YouTubeBaseActivity implements OnItemClickListener,OnInitializedListener {
	public static String thisQ,thisQQ;
	Spinner sper1;
	View view;
	ListView listview;
	String[] item,QID,isTeach,is_new,v_item;
	private ArrayAdapter<String> listAdapter;
	MyCustomAdapter mlist;
	Intent	it1;
	String result,ans;
	Timer tm;
	TimerTask tmTask;
	YouTubePlayerView youTubeView;
	private YouTubePlayer player;
	String play_videoid;
	int sel_class,sel_job;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sel_job=Class_job.select_job;
		sel_class=Class_job.select_class;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 //      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	  //             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.is_every);
	
		it1=new Intent(this,Testgo.class);
	/*	Bundle bundle =this.getIntent().getExtras();
	    String uname = bundle.getString("uname");
		setTitle(uname);*/
		
		sper1=(Spinner)findViewById(R.id.sp1);
		
		listview=(ListView)findViewById(R.id.listview_is_every);
		
		 
		
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
       
	        int goPos = 0;     
        try {
            String result = DBConnector.executeQuery("SELECT * FROM class,job where job_class=class_index and class_index='"+sel_class+"'",this);
            JSONArray jsonArray = new JSONArray(result);
            item=new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonData = jsonArray.getJSONObject(i);
            	
            	if(jsonData.getInt("job_id")==sel_job)
            	{	goPos=i;	}
            	
            	item[i]=jsonData.getString("job_name");
            }
           
            listAdapter = new ArrayAdapter(this, R.layout.myspinner ,item);
            sper1.setAdapter(listAdapter);
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }
      
		listview.setOnItemClickListener(this);
		
	sper1.setSelection(goPos);
	listview.setDividerHeight(20);
	listview.setPadding(20, 8, 20, 0);
	}

	


    @Override
    public void onResume() {
        super.onResume();
        if(Hello_login.Iam==null)
		 {
			 finish();
		 }
        if (tm == null) {  
            tm = new Timer();  
        }  
  
        if (tmTask == null) {  
            tmTask = new TimerTask() {  
                @Override  
                public void run() {  
                 
                	try {
            			 	 result = DBConnector.executeQuery("select name,QID,issue_text,issue_video,is_new FROM job,member," +
            					 		"test_quest LEFT JOIN (select r_quest,count(r_who) as is_new from reply where r_who!=r_name AND pig_read=0 AND r_who="+Hello_login.Iam+" group by r_quest)as new1 " +
            					 		"ON r_quest=QID  where UID=who AND belong=job_id AND job_name=\""+sper1.getSelectedItem().toString()+"\" ORDER BY QID ASC",Is_every.this);	
            			 	
            		
   	       	    
   	       	            if(!result.equals(ans)){
   	       	            	if(result.equals("null\n\n\n\n"))
   	       	            	{
   	       	            	listview.setVisibility(View.INVISIBLE);
   	       	            	
   	       	            	}else{
   	       	        	   ans=result;

   	       	         mlist=null;
   					 mlist = new MyCustomAdapter();

   		            JSONArray jsonArray = new JSONArray(result);
   		            item=	new String[jsonArray.length()];
   		            v_item=	new String [jsonArray.length()];
   		            QID =	new String[jsonArray.length()];
   		            isTeach=new String[jsonArray.length()];
   		            is_new=new String[jsonArray.length()];
   		            for(int i = 0; i < jsonArray.length(); i++) {
   		            JSONObject jsonData = jsonArray.getJSONObject(i);
   		            item[i]=	jsonData.getString("QID")+".  "+jsonData.getString("issue_text");
   		            v_item[i]=	jsonData.getString("issue_video");
   		            QID [i]=	jsonData.getString("QID");
   		            isTeach[i]=	jsonData.getString("name");
   		            is_new[i]=	jsonData.getString("is_new");
   		            mlist.addItem(String.valueOf(i));
   		            
   		            }
   		             Message msg = new Message();
   	       	      	  msg.what = 1;
   	       	      	  handler.sendMessage(msg);
   		            	}
   				 
   	       	        	   
   	       	         
   	       	         
   	       	            
   	       	            }
   	       	        } catch(Exception e) {
   	       	            // Log.e("log_tag", e.toString());
   	       	        }
   	       		    
                	
                	
                }  
            };  
                	
                }  
              
       
        if(tm != null && tmTask != null )  
            tm.schedule(tmTask, 1, 1000); 
        
    }
    private Handler handler = new Handler(){
 @Override
 public void handleMessage(Message msg) {
  // TODO Auto-generated method stub
  super.handleMessage(msg);
  switch (msg.what){
  case 1:
	  if(result.equals("null\n\n\n\n"))
      {}
     else
     //setListAdapter(adapter);Log.e("handle", "i get it!");
     //}
     {
    	 listview.setVisibility(View.VISIBLE); 
    	 mlist.notifyDataSetChanged();
    	 listview.setAdapter(mlist);
     }

	    
   break;
  default:
  }
     }};
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);

	       if(version >= 5) {
	    	   overridePendingTransition(R.anim.re11, R.anim.de11);
	    	   }
		super.onPause();
		
		 if (tm != null) {  
	            tm.cancel();  
	            tm = null;  
	        }  
	  
	        if (tmTask != null) {  
	           tmTask.cancel();  
	            tmTask = null;  
	        }     
	        Log.e("onPause", "stop");
	}


	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) { 
		// TODO Auto-generated method stub
		
		LayoutInflater factory=LayoutInflater.from(this);
		final View issue1=factory.inflate(R.layout.checktest,null);
		TextView Quest=(TextView) issue1.findViewById(R.id.Quest);
		youTubeView = (YouTubePlayerView) issue1.findViewById(R.id.youtube_viewTest);
	    youTubeView.initialize(Auth.KEY, this);
		Quest.setText(item[position]);
		play_videoid=v_item[position];
		thisQ =QID[position];
		thisQQ=item[position];
		new AlertDialog.Builder(this)
	    .setView(issue1)
	    .setPositiveButton("我要練習", new DialogInterface.OnClickListener() {
	    	
	        @Override
	        public void onClick(DialogInterface dialog, int which) {   
	        	if(player!=null)
	        	{
	        		player.pause();
	        		player.release();
	        	}
	        	if(youTubeView!=null)
	        	{
	        	youTubeView=null;
	        	}
	        Is_every_teach.thisQ="123";
	        startActivity(it1);
	        }
	})
		.setNeutralButton("這題我會怕", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {                               
	        	if(player!=null)
	        	{
	        		player.pause();
	        		player.release();
	        	}
	        	if(youTubeView!=null)
	        	{
	        	youTubeView=null;
	        	}
	        }
	    })
	    .show();	
	
	}
	
	
	private class MyCustomAdapter extends BaseAdapter {
		  
	    private ArrayList mData = new ArrayList();
	    private LayoutInflater mInflater;

	    public MyCustomAdapter() {
	        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    public void addItem(final String item) {
	        mData.add(item);
	        notifyDataSetChanged();
	    }

	    @Override
	    public int getCount() {
	        return mData.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return mData.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	     //   System.out.println("getView " + position + " " + convertView);
	        ViewHolder holder = null;
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.list_toy, null);
	            holder = new ViewHolder();
	          //  holder.img = (ImageView)convertView.findViewById(R.id.imageView1);
	            holder.text1 = (TextView)convertView.findViewById(R.id.isname);
	            holder.text2 = (TextView)convertView.findViewById(R.id.istext);
	            holder.a_new = (ProgressBar)convertView.findViewById(R.id.new_gogo);
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder)convertView.getTag();
	        }
	        holder.text1.setText(item[position]);
	        holder.text2.setText(isTeach[position]);
	        if(!is_new[position].equals("null"))
	        holder.a_new.setVisibility(View.VISIBLE);
	        else
	        	holder.a_new.setVisibility(View.GONE);
	      //  holder.img.setBackgroundColor(Color.rgb(255, 50, 50));
	     // blur(lay.getDrawingCache(),convertView);
	    
	        return convertView;
	    }

	}

	public static class ViewHolder {
	    public TextView text1,text2;
	    public ProgressBar a_new;
	  //  public ImageView img;
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
	this.player=arg1;
		// TODO Auto-generated method stub
	if(!play_videoid.equals(""))
		arg1.cueVideo(play_videoid);
	else
	{
		player.release();
		player=null;
	}
	}
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		
	}
	

	
	
	
}