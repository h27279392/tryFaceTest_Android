package com.example.topic_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.topic_test.Fragment1.VideoInfo;
import com.example.topic_test.Fragment1.ViewHolder;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.ytdl.Auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class Combat_List extends ListActivity{
	
	
	
	private ListView mListView,resultlist;
	private List<VideoInfo> videoList=new ArrayList<VideoInfo>();
	private List<VideoInfo2> videoList2=new ArrayList<VideoInfo2>();
	private VideoInfo videoinfo;
	VideoInfo2 videoinfo2;
	private myAdapter adapter;
	myAdapter2 adapter2;
	
	View reply;
	String[] name,time,video,r_people,text,isnew,issue,Quest;
	
	Timer tm;
	TimerTask tmTask;
	String result,ans;
	int isclick;
	//Uri[] video_uri;
	//int x=0;
	/**SQL*/
	String query1,query2,read,update_read;
	
	ViewHolder2 holder2;
	String thisQ,Who,thisQQ;
	
	
	String query_add,data_name,query_del;
	LinearLayout videoBox;
	ImageButton closeButton;
	Youtube_of_Activity youTubeView;
	
	//HttpClient the_time = new DefaultHttpClient();
	//HttpPost R_time = new HttpPost("http://chentopic.no-ip.info:1993/server_time.php");
	//String Server_Time;
	private JSONArray jsonArray;
	
	
 		
     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_combat__list);
		
		Who  =Hello_login.Iam; 
			query1="select name,issue_text,issue_video,r_quest,r_text,r_video,r_time,r_name,pig_read,teach_when>r_time as have_new_msg" +
					" from test_quest,(select *,SUBSTR(r_quest, 2) as Rstr from reply where r_quest like'z%') as R1,member " +
					"where QID=Rstr AND r_name=UID AND r_who="+Who+" order by(r_index)";
			//select name,issue_text,issue_video,r_quest,r_text,r_video,r_time,r_name,pig_read,teach_when>r_time as new from test_quest,(select *,SUBSTR(r_quest, 2)as Rstr from reply where r_quest like 'z%')as R1,member where QID=Rstr AND r_name=UID AND r_who="+Who+" order by(r_index)
		Log.e("asd", query1);
		//Log.e("asd1", null);
	
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
  
        mListView = (ListView) findViewById(android.R.id.list);
      
			youTubeView = Youtube_of_Activity.newInstance();
	        getFragmentManager().beginTransaction().replace(R.id.youtube_viewCom, youTubeView).commit();
		
        try {
            result = DBConnector.executeQuery(query1,this);
            ans=result;
            //	HttpResponse responsePOST = the_time.execute(R_time);
           // 	HttpEntity resEntity = responsePOST.getEntity();
            //	Server_Time = EntityUtils.toString(resEntity);
            
            jsonArray = new JSONArray(result);
            name =new String[jsonArray.length()];
            text =new String[jsonArray.length()];
            video=new String[jsonArray.length()];
            time =new String[jsonArray.length()];
           r_people =new String[jsonArray.length()];
           isnew=new String[jsonArray.length()];
           issue=new String[jsonArray.length()];
           Quest=new String[jsonArray.length()];
           // video_uri=new Uri[video.length];
          // name[0]="題目";text[0]=thisQQ;
           
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonData = jsonArray.getJSONObject(i);
            	name [i]=jsonData.getString("name");
            	text [i]=jsonData.getString("r_text");
            	video[i]=jsonData.getString("r_video");
            	time [i]=jsonData.getString("r_time");
            	r_people[i]=jsonData.getString("r_name");
            	isnew[i]=jsonData.getString("pig_read");
            	issue[i]=jsonData.getString("issue_video");
            	Quest[i]=jsonData.getString("r_quest");
            	if(name[i].equals(Hello_login.name))
            	{
            	videoinfo=new VideoInfo(video[i],name[i],time[i],text[i],isnew[i],jsonData.getString("have_new_msg"),issue[i],Quest[i]);
            	videoList.add(videoinfo);
            	}
            	
            }
           
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }
        
    /* if(result.equals("null\n\n\n\n"))
      {
    	 
      }
     else
     {*/
    	 adapter = new myAdapter();
    	 adapter2=new myAdapter2();
 		 setListAdapter(adapter);
     //}

 		videoBox=(LinearLayout)findViewById(R.id.video_boxCom);
	       closeButton=(ImageButton)findViewById(R.id.close_buttonCom);
	      
	       closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListView.clearChoices();
	    	   mListView.requestLayout();
	    	    youTubeView.pause();
	    	    ViewPropertyAnimator animator = videoBox.animate()
	    	        .translationYBy(videoBox.getHeight())
	    	        .setDuration(300);
	    	    runOnAnimationEnd(animator, new Runnable() {
	    	      @Override
	    	      public void run() {
	    	        videoBox.setVisibility(View.INVISIBLE);
	    	      }
	    	    });
			}
		});
 	
 
  mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int p, long id) {
		
		query_del="DELETE FROM reply WHERE r_time='"+videoList.get(p).getTime()+"' AND r_name='"+Hello_login.Iam+"'";
		
	if(Hello_login.name.equals(videoList.get(p).getName())){	
		
			new AlertDialog.Builder(Combat_List.this)
		    .setMessage("請你注意！現在時間已經停止了")
		    .setPositiveButton("繼續啦白痴", new DialogInterface.OnClickListener() {
		    	
		        @Override
		        public void onClick(DialogInterface dialog, int which) {          
		         
		       String ans =	DBConnector.executeQuery(query_del,Combat_List.this);
		       Toast.makeText(Combat_List.this, "已刪除", Toast.LENGTH_LONG).show();
		        	Log.e("刪除回傳", ans);
		        }
		})
			.setNeutralButton("回到前上1秒", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {                               
		         
		        }
		    })
		    .show();	
	}
		
		return false;
	}
	  
	  
  });
 
  
	
	}
	
	
/****************************************************************************************/
	
	
	
	 @Override
	    public void onPause() {
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
	        youTubeView.release();
			adapter.releaseLoaders();
			adapter2.releaseLoaders();
			videoBox.setVisibility(View.GONE);
			if(youTubeView!=null)
				youTubeView=null;
	    }

	    @Override
public void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	
videoList2.clear();
	result=DBConnector.executeQuery("select * from reply,member where r_name=UID and r_name!=r_who and r_quest='"+videoList.get(position).getQID()+"' and r_who="+Hello_login.Iam, this);	
	read="update reply set pig_read='1' where r_name=r_who AND r_who="+Who+" AND r_quest='"+videoList.get(position).getQID()+"' AND pig_read='0'";
	update_read="update reply set teach_when=r_time where r_name=r_who AND r_who="+Who+" AND r_quest='"+videoList.get(position).getQID()+"' AND pig_read='1'";
	if(!result.equals("null\n\n\n\n"))
	{
		
		try {
			jsonArray = new JSONArray(result);
		//	text =new String[jsonArray.length()];
        //    video=new String[jsonArray.length()];
        //    time =new String[jsonArray.length()];
			for(int i=0 ; i < jsonArray.length(); i++) {
				JSONObject jsonData = jsonArray.getJSONObject(i);
				
				videoinfo2=new VideoInfo2(jsonData.getString("r_video"),jsonData.getString("name"),jsonData.getString("r_time"),jsonData.getString("r_text"),"1");
            	videoList2.add(videoinfo2);
            	Log.e("a", jsonData.getString("r_video")+","+jsonData.getString("name")+","+jsonData.getString("r_time")+","+jsonData.getString("r_text"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LayoutInflater factory=LayoutInflater.from(this);
		reply=factory.inflate(R.layout.teach_ans,null);
		resultlist=(ListView)reply.findViewById(R.id.list21);
		
		resultlist.setAdapter(adapter2);
		
		
		new AlertDialog.Builder(this).setView(reply).setTitle("面試結果")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
						DBConnector.executeQuery(read,Combat_List.this);
						DBConnector.executeQuery(update_read,Combat_List.this);
					}
				}).show();
		
	}
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
	                 
	                
	   	       		// query2="select name,issue_text,issue_video,r_text,r_video,r_time from test_quest,reply,member " +
	   	       		//			"where QID=r_quest AND r_name=UID AND QID=\""+thisQ+"\" AND r_who=\""+Who+"\" AND r_time>'"+Server_Time+"'";
	   	       		 try {
	   	       	            result = DBConnector.executeQuery(query1,Combat_List.this);
	   	       	            
	   	       	    
	   	       	            if(!result.equals(ans)){
	   	       	            	if(result.equals("null\n\n\n\n"))
	   	       	            	{
	   	       	            	mListView.setVisibility(View.INVISIBLE);
	   	       	            	
	   	       	            	}else{
	   	       	        	   ans=result;
	   	       	        	   // HttpResponse responsePOST = the_time.execute(R_time);
	   	       	            	//HttpEntity resEntity = responsePOST.getEntity();
	   	       	        	//Server_Time = EntityUtils.toString(resEntity);
	   	       	        	   videoList.clear();
	   	       	      jsonArray = new JSONArray(result);
	   	            name =new String[jsonArray.length()];
	   	            text =new String[jsonArray.length()];
	   	            video=new String[jsonArray.length()];
	   	            time =new String[jsonArray.length()];
	   	           r_people =new String[jsonArray.length()];
	   	           isnew=new String[jsonArray.length()];
	   	           issue=new String[jsonArray.length()];
	   	           Quest=new String[jsonArray.length()];
	   	           // video_uri=new Uri[video.length];
	   	          // name[0]="題目";text[0]=thisQQ;
	   	         
	   	            for(int i = 0; i < jsonArray.length(); i++) {
	   	            	JSONObject jsonData = jsonArray.getJSONObject(i);
	   	            	name [i]=jsonData.getString("name");
	   	            	text [i]=jsonData.getString("r_text");
	   	            	video[i]=jsonData.getString("r_video");
	   	            	time [i]=jsonData.getString("r_time");
	   	            	r_people[i]=jsonData.getString("r_name");
	   	            	issue[i]=jsonData.getString("issue_video");
	   	            	isnew[i]=jsonData.getString("pig_read");
	   	            	Quest[i]=jsonData.getString("r_quest");
	   	            	if(name[i].equals(Hello_login.name))
	   	            	{
	   	            	videoinfo=new VideoInfo(video[i],name[i],time[i],text[i],isnew[i],jsonData.getString("have_new_msg"),issue[i],Quest[i]);
	   	            	videoList.add(videoinfo);
	   	            	}else
	   	            	{
	   	            		
	   	            	}
	   	       	            	}
	   	       	         
	   	       	          Message msg = new Message();
	   	       	      	  msg.what = 1;
	   	       	      	  handler.sendMessage(msg);
	   	       	            
	   	       	            }}
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
		    	 mListView.setVisibility(View.VISIBLE); 
		  adapter.notifyDataSetChanged();
		  
		     }
	   break;
	  default:
	  }
	     }};
	 
	     
	     
	     
	     
	     class myAdapter extends BaseAdapter{
	    	// private LayoutInflater mInflater;
	    	 	
	    	    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMapQ,thumbnailViewToLoaderMapA;
	    	    private final ThumbnailListener1 thumbnailListenerQ;
	    	    private final ThumbnailListener2 thumbnailListenerA;
	    	 public myAdapter() {
	    		// mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		 thumbnailViewToLoaderMapQ = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
	    		 thumbnailViewToLoaderMapA = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
	    	      thumbnailListenerQ = new ThumbnailListener1();
	    	      thumbnailListenerA = new ThumbnailListener2();
	    	 }
	    		@Override
	    		public int getCount() {
	    			// TODO Auto-generated method stubs
	    			return videoList.size();
	    		}

	    		@Override
	    		public Object getItem(int position) {
	    			// TODO Auto-generated method stub
	    			return videoList.get(position);
	    		}

	    		@Override
	    		public long getItemId(int position) {
	    			// TODO Auto-generated method stub
	    			return position;
	    		}
	    		public void releaseLoaders() {
	    		      for (YouTubeThumbnailLoader loaderQ : thumbnailViewToLoaderMapQ.values()) {
	    		        loaderQ.release();
	    		      }
	    		      for (YouTubeThumbnailLoader loaderA : thumbnailViewToLoaderMapA.values()) {
		    		        loaderA.release();
		    		      }
	    		    }
	    		@Override
	    		public View getView(int position, View convertView, ViewGroup parent) {
	    			final ViewHolder holder;
	    			final int mPosition=position;
	    			
	    			
	    			if(convertView==null){
	    				convertView=LayoutInflater.from(Combat_List.this).inflate(R.layout.videolist_item_god, null);
	    				holder=new ViewHolder();
	    				holder.thumbnailQ = (YouTubeThumbnailView) convertView.findViewById(R.id.video_image_Q);
						holder.thumbnailQ.initialize(Auth.KEY, thumbnailListenerQ);
						holder.thumbnailA = (YouTubeThumbnailView) convertView.findViewById(R.id.video_image_A);
						holder.thumbnailA.initialize(Auth.KEY, thumbnailListenerA);
	    				holder.isname=(TextView)convertView.findViewById(R.id.isname);
	    				holder.isans=(TextView)convertView.findViewById(R.id.isans);
	    				holder.istime=(TextView)convertView.findViewById(R.id.istime);
	    				
	    			    holder.isnew=(ProgressBar)convertView.findViewById(R.id.is_new);
	    				convertView.setTag(holder);
	    				holder.thumbnailQ.setTag(position);
	    		        holder.thumbnailQ.setId(position);
	    		        holder.thumbnailA.setTag(position);
	    		        holder.thumbnailA.setId(position);
	    				
	    	
	    			}else{
	    				
	    				holder=(ViewHolder) convertView.getTag();
	    				holder.thumbnailQ.setId(position);
	    				holder.thumbnailA.setId(position);
	    		        
	    				YouTubeThumbnailLoader loaderQ = thumbnailViewToLoaderMapQ.get(holder.thumbnailQ);
	    				YouTubeThumbnailLoader loaderA = thumbnailViewToLoaderMapA.get(holder.thumbnailA);
	    		        if (loaderQ == null) {
	    		          holder.thumbnailQ.setTag(position);
	    		         } else {
	    		        holder.thumbnailQ.setImageResource(R.drawable.no_thumbnail);
	    		          loaderQ.setVideo(videoList.get(position).getissue());
	    		        }
	    		        if (loaderA == null) {
		    		          holder.thumbnailA.setTag(position);
		    		         
		    		        } else {
		    		          holder.thumbnailA.setImageResource(R.drawable.no_thumbnail);
		    		          
		    		          loaderA.setVideo(videoList.get(position).getUrl());
		    		        }
	    			}
	    			
	    			holder.istime.setText(videoList.get(position).getTime());
	    			
	    	
	    			holder.thumbnailQ.setOnClickListener(new View.OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					youTubeView.setVideoId(videoList.get((Integer)v.getId()).getissue());
	    					if (videoBox.getVisibility() != View.VISIBLE) {
	    					        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	    					           videoBox.setTranslationY(videoBox.getHeight());
	    					        }
	    					        videoBox.setVisibility(View.VISIBLE);
	    					      }
	    							if (videoBox.getTranslationY() > 0) {
	    					        videoBox.animate().translationY(0).setDuration(300);
	    					      } 
	    					}
	    			}	);
	    			
	    			holder.thumbnailA.setOnClickListener(new View.OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					youTubeView.setVideoId(videoList.get((Integer)v.getId()).getUrl());
	    					 if (videoBox.getVisibility() != View.VISIBLE) {
	    					        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	    					         videoBox.setTranslationY(videoBox.getHeight());
	    					        }
	    					        videoBox.setVisibility(View.VISIBLE);
	    					      }
	    					 		if (videoBox.getTranslationY() > 0) {
	    					        videoBox.animate().translationY(0).setDuration(300);
	    					      }
	    					      
	    					}
	    			}	);
	    		
	    			if(videoList.get(mPosition).IsRead().equals("1") && videoList.get(mPosition).IsNewMsg().equals("1"))
		    			holder.isnew.setVisibility(View.VISIBLE);
		    		else
		    			holder.isnew.setVisibility(View.GONE);
	    			
	    			return convertView;
	    		};
	    		 private final class ThumbnailListener1 implements
		         YouTubeThumbnailView.OnInitializedListener,
		         YouTubeThumbnailLoader.OnThumbnailLoadedListener {

		       @Override
		       public void onInitializationSuccess(
		           YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
		         loader.setOnThumbnailLoadedListener(this);
		         thumbnailViewToLoaderMapQ.put(view, loader);
		         
		         view.setImageResource(R.drawable.loading_thumbnail);
		         int x=(Integer)view.getId();
		         
		         loader.setVideo(videoList.get(x).getissue());
		       }

		       @Override
		       public void onInitializationFailure(
		           YouTubeThumbnailView view, YouTubeInitializationResult loader) {
		         view.setImageResource(R.drawable.no_thumbnail);
		       }

		       @Override
		       public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
		       }

		       @Override
		       public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
		         view.setImageResource(R.drawable.no_thumbnail);
		       }
		     }
	    		
	    		 private final class ThumbnailListener2 implements
		         YouTubeThumbnailView.OnInitializedListener,
		         YouTubeThumbnailLoader.OnThumbnailLoadedListener {

		       @Override
		       public void onInitializationSuccess(
		           YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
		         loader.setOnThumbnailLoadedListener(this);
		         thumbnailViewToLoaderMapA.put(view, loader);
		         
		         view.setImageResource(R.drawable.loading_thumbnail);
		         int x=(Integer)view.getId();
		         Log.e("look video", String.valueOf(x));
		         loader.setVideo(videoList.get(x).getUrl());
		       }

		       @Override
		       public void onInitializationFailure(
		           YouTubeThumbnailView view, YouTubeInitializationResult loader) {
		         view.setImageResource(R.drawable.no_thumbnail);
		       }

		       @Override
		       public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
		       }

		       @Override
		       public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
		         view.setImageResource(R.drawable.no_thumbnail);
		       }
		     }
	     
	    	}
	    	static class ViewHolder{
	    		YouTubeThumbnailView thumbnailQ,thumbnailA;
	    		TextView isname,isans,istime;
	    		ProgressBar mProgressBar,isnew;
	    		
	    	}
	    	static class VideoInfo {
	    		private String url;
	    		private String name,time,text,New,isnew,issue,QID;
	    		
	    		
	    		public VideoInfo(String url,String name,String time,String text,String New,String have_new_msg,String issue,String QID) {
	    			this.name=name;
	    			this.time=time;
	    			this.text=text;
	    			this.isnew=have_new_msg;
	    			this.url=url;
	    			this.New=New;
	    			this.issue=issue;
	    			this.QID=QID;
	    			
	    		}
	    		public String getName() {
	    			return name;
	    		}
	    		public String getTime() {
	    			return time;
	    		}
	    		public String getText() {
	    			return text;
	    		}
	    		public String IsRead() {
	    			return New;
	    		}
	    		public String IsNewMsg() {
	    			return isnew;
	    		}
	    		public String getissue(){
	    			return issue;
	    		}
	    		public String getQID(){
	    			return QID;
	    		}
	    		public void setVideoName(String videoName) {
	    			this.name = videoName;
	    		}
	    		
	    		public String getUrl() {
	    			return url;
	    		}
	    		public void setUrl(String url) {
	    			this.url = url;
	    		}
	    	}
		
	    	
	    	
	    	 class myAdapter2 extends BaseAdapter{
	 	    	// private LayoutInflater mInflater;
	 	    	 VideoInfo2 info;
	 	    	    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
	 	    	  
	 	    	    private final ThumbnailListener thumbnailListener;
	 	    	
	 	    	 public myAdapter2() {
	 	    	//	 mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 	    		
	 	    	      thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
	 	    	     
	 	    	      thumbnailListener = new ThumbnailListener();
	 	         }
	 	    		@Override
	 	    		public int getCount() {
	 	    			// TODO Auto-generated method stubs
	 	    			return videoList2.size();
	 	    		}
	 	    		
	 	    		@Override
	 	    		public Object getItem(int position) {
	 	    			// TODO Auto-generated method stub
	 	    			return videoList2.get(position);
	 	    		}

	 	    		@Override
	 	    		public long getItemId(int position) {
	 	    			// TODO Auto-generated method stub
	 	    			return position;
	 	    		}
	 	    		public void releaseLoaders() {
	 	    		      for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
	 	    		        loader.release();
	 	    		      }
	 	    		    }
	 	    		@Override
	 	    		public View getView(int position, View convertView, ViewGroup parent) {
	 	    			final ViewHolder holder;
	 	    			final int mPosition=position;
	 	    			info = videoList2.get(position);
	 	    			
	 	    			if(convertView==null){
	 	    				
	 	    				convertView=LayoutInflater.from(Combat_List.this).inflate(R.layout.videolist_item,null);
	 	    				holder2=new ViewHolder2();
	 	    				holder2.thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.video_image);
	 						holder2.thumbnail.initialize(Auth.KEY, thumbnailListener);
	 	    				holder2.isname=(TextView)convertView.findViewById(R.id.isname);
	 	    				holder2.istime=(TextView)convertView.findViewById(R.id.istime);
	 	    				holder2.istext=(TextView)convertView.findViewById(R.id.istext);
	 	    				holder2.isnew=(ProgressBar)convertView.findViewById(R.id.is_new);
	 	    				convertView.setTag(holder2);
	 	    		        holder2.thumbnail.setTag(position);
	 	    		        holder2.thumbnail.setId(position);
	 	    		        
	 	    			}else{
	 	    				
	 	    				holder2=(ViewHolder2) convertView.getTag();
	 	    				holder2.thumbnail.setId(position);
	 	    		          Log.e("所以",String.valueOf(holder2.thumbnail.getId()));
	 	    				YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder2.thumbnail);
	 	    		        if (loader == null) {
	 	    		          // 2) The view is already created, and is currently being initialized. We store the
	 	    		          //    current videoId in the tag.
	 	    		        	
	 	    		          holder2.thumbnail.setTag(position);
	 	    		         
	 	    		        } else {
	 	    		          // 3) The view is already created and already initialized. Simply set the right videoId
	 	    		          //    on the loader.
	 	    		          holder2.thumbnail.setImageResource(R.drawable.no_thumbnail);
	 	    		          
	 	    		          loader.setVideo(info.getUrl());
	 	    		        }
	 	    			}
	 	    			
	 	    			holder2.isname.setText(videoList2.get(position).getName());
	 	    			holder2.istime.setText(videoList2.get(position).getTime());
	 	    			holder2.istext.setText(videoList2.get(position).getText());
	 	    			holder2.isname.setVisibility(View.VISIBLE);
	 	    			holder2.istime.setVisibility(View.VISIBLE);
	 	    			holder2.istext.setVisibility(View.VISIBLE);
	 	    			holder2.thumbnail.setVisibility(View.VISIBLE);

	 	    			holder2.thumbnail.setOnClickListener(new View.OnClickListener() {
	 	    				@Override
	 	    				public void onClick(View v) {
	 	    					
	 	    					youTubeView.setVideoId(videoList2.get((Integer)v.getId()).getUrl());
	 	    					
	 	    					 if (videoBox.getVisibility() != View.VISIBLE) {
	 	    					        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	 	    					          // Initially translate off the screen so that it can be animated in from below.
	 	    					          videoBox.setTranslationY(videoBox.getHeight());
	 	    					        }
	 	    					        videoBox.setVisibility(View.VISIBLE);
	 	    					      }

	 	    					      // If the fragment is off the screen, we animate it in.
	 	    					      if (videoBox.getTranslationY() > 0) {
	 	    					        videoBox.animate().translationY(0).setDuration(300);
	 	    					      }
	 	    					      
	 	    					}
	 	    			}	);
	 	    			
	 	    			if(videoList2.get(mPosition).IsNew().equals("0") && !videoList2.get(mPosition).getName().equals(Hello_login.name))
	 		    			holder2.isnew.setVisibility(View.VISIBLE);
	 		    		else
	 		    			holder2.isnew.setVisibility(View.GONE);
	 	    			
	 	    			if(videoList2.get(mPosition).getUrl().equals("null"))
	 	    				holder2.thumbnail.setVisibility(View.GONE);
	 	    			
	 	    			if(videoList2.get(mPosition).getText().equals("null"))
	 	    				holder2.istext.setVisibility(View.GONE);
	 	    		
	 	    			return convertView;
	 	    		};
	 	    		
	 	    	 	 private final class ThumbnailListener implements
	 		         YouTubeThumbnailView.OnInitializedListener,
	 		         YouTubeThumbnailLoader.OnThumbnailLoadedListener {

	 		       @Override
	 		       public void onInitializationSuccess(
	 		           YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
	 		         loader.setOnThumbnailLoadedListener(this);
	 		         thumbnailViewToLoaderMap.put(view, loader);
	 		         view.setImageResource(R.drawable.loading_thumbnail);
	 		         int x=(Integer)view.getId();
	 		         Log.e("look video", String.valueOf(x));
	 		         
	 		         loader.setVideo(videoList.get(x).getUrl());
	 		       }

	 		       @Override
	 		       public void onInitializationFailure(
	 		           YouTubeThumbnailView view, YouTubeInitializationResult loader) {
	 		         view.setImageResource(R.drawable.no_thumbnail);
	 		       }

	 		       @Override
	 		       public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
	 		       }

	 		       @Override
	 		       public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
	 		         view.setImageResource(R.drawable.no_thumbnail);
	 		       }
	 		     }
	 	    		
	 	    	}
	 	     
	 	    	static class ViewHolder2{
	 	    		YouTubeThumbnailView thumbnail;
	 	    		TextView isname,istext,istime;
	 	    	//	ImageButton videoPlayBtn;
	 	    		ProgressBar isnew;
	 	    	//	VideoView videov;
	 	    	}
		    	static class VideoInfo2 {
		    		private String url;
		    		private String name,time,text,New;
		    		
		    		public VideoInfo2(String url,String name,String time,String text,String New) {
		    			this.name=name;
		    			this.time=time;
		    			this.text=text;
		    			
		    			this.url=url;
		    			this.New=New;
		    		}
		    		public String getName() {
		    			return name;
		    		}
		    		public String getTime() {
		    			return time;
		    		}
		    		public String getText() {
		    			return text;
		    		}
		    		public String IsNew() {
		    			return New;
		    		}
		    		public void setVideoName(String videoName) {
		    			this.name = videoName;
		    		}
		    		
		    		
		    		public String getUrl() {
		    			return url;
		    		}
		    		public void setUrl(String url) {
		    			this.url = url;
		    		}
		    	}

		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	 @TargetApi(16)
		      	  private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
		      	    if (Build.VERSION.SDK_INT >= 16) {
		      	      animator.withEndAction(runnable);
		      	    } else {
		      	      animator.setListener(new AnimatorListenerAdapter() {
		      	        @Override
		      	        public void onAnimationEnd(Animator animation) {
		      	          runnable.run();
		      	        }
		      	      });
		      	    }
		      	  }    	
		    	
}



/*,job_quest where UID=who AND job_num=num " +
"AND name=\""+sper2.getSelectedItem().toString()+"\" AND job=\""+sper1.getSelectedItem().toString()*/