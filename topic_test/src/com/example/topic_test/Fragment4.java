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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.ytdl.Auth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class Fragment4 extends ListActivity{
	
	
	
	private ListView mListView,resultlist;
	private List<VideoInfo> videoList=new ArrayList<VideoInfo>();
	private List<VideoInfo2> videoList2=new ArrayList<VideoInfo2>();
	private VideoInfo videoinfo;
	VideoInfo2 videoinfo2;
	private myAdapter adapter;
	myAdapter2 adapter2;
	private int currentIndex=-1,currentIndex2=-1;
	MediaController mMediaCtrl,mMediaCtrl2;
	private int playPosition=-1,playPosition2=-1;
	private boolean isPaused=false,isPaused2=false;
	private boolean isPlaying=false,isPlaying2=false;
	
	View view4,reply;
	String[] name,time,video,r_people,text,isnew,issue,Quest;
	
	Timer tm;
	TimerTask tmTask;
	String result,ans;
	int isclick;
	//Uri[] video_uri;
	//int x=0;
	/**SQL*/
	String query1,query2;
	
	ViewHolder2 holder2;
	String thisQ,Who,thisQQ;
	
	
	String query_add,data_name,query_del;
	
	
	//HttpClient the_time = new DefaultHttpClient();
	//HttpPost R_time = new HttpPost("http://chentopic.no-ip.info:1993/server_time.php");
	//String Server_Time;
	private JSONArray jsonArray;
	
	
 		
     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	setContentView(R.layout.fragment1);
		
		Who  =Hello_login.Iam; 
			query1="select name,issue_text,issue_video,r_quest,r_text,r_video,r_time,r_name,pig_read" +
					" from test_quest,(select *,SUBSTR(r_quest, 2)as Rstr from reply where r_quest like 'z%')as R1,member " +
					"where QID=Rstr AND r_name=UID AND r_who="+Who;
			
		//Log.e("asd", Is_every_teach.thisQ);
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
  
        mListView = (ListView) view4.findViewById(android.R.id.list);
       
        try {
            result = DBConnector.executeQuery(query1,view4.getContext());
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
            	videoinfo=new VideoInfo(video[i],name[i],time[i],text[i],isnew[i],issue[i],Quest[i],R.drawable.pic);
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

 		 
 	
 
  mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int p, long id) {
		
		query_del="DELETE FROM reply WHERE r_time='"+time[p]+"' AND r_name='"+Hello_login.Iam+"'";
		Log.e("刪除回傳12", Hello_login.Iam+r_people[p]);
	if(Hello_login.Iam.equals(r_people[p])){	
			new AlertDialog.Builder(view.getContext())
		    .setMessage("請你注意！現在時間已經停止了")
		    .setPositiveButton("繼續啦白痴", new DialogInterface.OnClickListener() {
		    	
		        @Override
		        public void onClick(DialogInterface dialog, int which) {          
		         
		       String ans =	DBConnector.executeQuery(query_del,Fragment4.this);
		       Toast.makeText(Fragment4.this, "已刪除", Toast.LENGTH_LONG).show();
		        	Log.e("刪除回傳", ans);
		        }
		})
			.setNeutralButton("回到前1秒", new DialogInterface.OnClickListener() {
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
	    }

	    @Override
public void onListItemClick(ListView l, View v, int position, long id) {
	// TODO Auto-generated method stub
	super.onListItemClick(l, v, position, id);
	
videoList2.clear();
	result=DBConnector.executeQuery("select * from reply,member where r_name=UID and r_name!=r_who and r_quest='"+videoList.get(position).getQID()+"' and r_who="+Hello_login.Iam, view4.getContext());	
	Log.e("a", result);
	if(!result.equals("null\n\n\n\n"))
	{
		
		String teach_say="";
		try {
			jsonArray = new JSONArray(result);
		//	text =new String[jsonArray.length()];
        //    video=new String[jsonArray.length()];
        //    time =new String[jsonArray.length()];
			for(int i=0 ; i < jsonArray.length(); i++) {
				JSONObject jsonData = jsonArray.getJSONObject(i);
				
				
				
				videoinfo2=new VideoInfo2(jsonData.getString("r_video"),jsonData.getString("name"),jsonData.getString("r_time"),jsonData.getString("r_text"),"1",R.drawable.pic);
            	videoList2.add(videoinfo2);
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LayoutInflater factory=LayoutInflater.from(view4.getContext());
		reply=factory.inflate(R.layout.teach_ans,null);
		resultlist=(ListView)reply.findViewById(R.id.list21);
		
		resultlist.setAdapter(adapter2);
		
		
		new AlertDialog.Builder(view4.getContext()).setView(reply).setTitle("面試結果")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
						
					}
				}).show();
		
	}
}


		@Override
	    public void onResume() {
	        super.onResume();
	        if (tm == null) {  
	            tm = new Timer();  
	        }  
	  
	        if (tmTask == null) {  
	            tmTask = new TimerTask() {  
	                @Override  
	                public void run() {  
	                 
	                	Log.e("run", "run..");
	                	
	   	       		// query2="select name,issue_text,issue_video,r_text,r_video,r_time from test_quest,reply,member " +
	   	       		//			"where QID=r_quest AND r_name=UID AND QID=\""+thisQ+"\" AND r_who=\""+Who+"\" AND r_time>'"+Server_Time+"'";
	   	       		 try {
	   	       	            result = DBConnector.executeQuery(query1,view4.getContext());
	   	       	            
	   	       	    
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
	   	            	isnew[i]=jsonData.getString("pig_read");
	   	            	issue[i]=jsonData.getString("issue_video");
	   	            	
	   	            	Quest[i]=jsonData.getString("r_quest");
	   	            	if(name[i].equals(Hello_login.name))
	   	            	{
	   	            	videoinfo=new VideoInfo(video[i],name[i],time[i],text[i],isnew[i],issue[i],Quest[i],R.drawable.pic);
	   	            	videoList.add(videoinfo);
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
	        Log.e("onResume", "go run..");
	        
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
	    	 	VideoInfo info;
	    	    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
	    	    private final ThumbnailListener thumbnailListenerA,thumbnailListenerQ;
	    	 public myAdapter() {
	    		// mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		 thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
	    		 
	    	      thumbnailListenerQ = new ThumbnailListener();
	    	      thumbnailListenerA = new ThumbnailListener();
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
	    		      for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
	    		        loader.release();
	    		      }
	    		      for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
		    		        loader.release();
		    		      }
	    		    }
	    		@Override
	    		public View getView(int position, View convertView, ViewGroup parent) {
	    			final ViewHolder holder;
	    			final int mPosition=position;
	    			info = videoList.get(position);
	    			
	    			if(convertView==null){
	    				convertView=LayoutInflater.from(view4.getContext()).inflate(R.layout.videolist_item_god, null);
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
	    		          Log.e("所以",String.valueOf(holder.thumbnailQ.getId()));
	    				YouTubeThumbnailLoader loaderQ = thumbnailViewToLoaderMap.get(holder.thumbnailQ);
	    				YouTubeThumbnailLoader loaderA = thumbnailViewToLoaderMap.get(holder.thumbnailA);
	    		        if (loaderQ == null) {
	    		          holder.thumbnailQ.setTag(position);
	    		         } else {
	    		        holder.thumbnailQ.setImageResource(R.drawable.no_thumbnail);
	    		          loaderQ.setVideo(info.getUrl());
	    		        }
	    		        if (loaderA == null) {
		    		          holder.thumbnailA.setTag(position);
		    		         
		    		        } else {
		    		          holder.thumbnailA.setImageResource(R.drawable.no_thumbnail);
		    		          
		    		          loaderA.setVideo(info.getUrl());
		    		        }
	    			}
	    			
	    			holder.istime.setText(videoList.get(position).getTime());
	    			
	    			
	    	/*		
	    			holder.thumbnailQ.setOnClickListener(new View.OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					youtube_fragment.setVideoId(videoList.get((Integer)v.getId()).getUrl());
	    					if (videoBox.getVisibility() != View.VISIBLE) {
	    					        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	    					           videoBox.setTranslationY(videoBox.getHeight());
	    					        }
	    					        videoBox.setVisibility(View.VISIBLE);
	    					      }
	    							if (videoBox.getTranslationY() > 0) {
	    					        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
	    					      } 
	    					}
	    			}	);
	    			
	    			holder.thumbnailA.setOnClickListener(new View.OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					youtube_fragment.setVideoId(videoList.get((Integer)v.getId()).getUrl());
	    					 if (videoBox.getVisibility() != View.VISIBLE) {
	    					        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	    					         videoBox.setTranslationY(videoBox.getHeight());
	    					        }
	    					        videoBox.setVisibility(View.VISIBLE);
	    					      }
	    					 		if (videoBox.getTranslationY() > 0) {
	    					        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
	    					      }
	    					      
	    					}
	    			}	);
	    	*/		
	    			if(videoList.get(mPosition).IsNew().equals("0") && !videoList.get(mPosition).getName().equals(Hello_login.name))
		    			holder.isnew.setVisibility(View.VISIBLE);
		    		else
		    			holder.isnew.setVisibility(View.GONE);
	    		
	    			return convertView;
	    		};
	    		 private final class ThumbnailListener implements
		         YouTubeThumbnailView.OnInitializedListener,
		         YouTubeThumbnailLoader.OnThumbnailLoadedListener {

		       @Override
		       public void onInitializationSuccess(
		           YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
		         loader.setOnThumbnailLoadedListener(this);
		         new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>().put(view, loader);
		         
		         view.setImageResource(R.drawable.loading_thumbnail);
		         int x=(Integer)view.getId();
		         Log.e("look video", String.valueOf(x));
		         
		         loader.setVideo("");
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
	    		private String name,time,text,New,issue,QID;
	    		private int videoImage;
	    		
	    		public VideoInfo(String url,String name,String time,String text,String New,String issue,String QID,int path) {
	    			this.name=name;
	    			this.time=time;
	    			this.text=text;
	    			this.videoImage=path;
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
	    		public String IsNew() {
	    			return New;
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
	    		
	    		public int getVideoImage() {
	    			return videoImage;
	    		}
	    		public void setVideoImage(int videoImage) {
	    			this.videoImage = videoImage;
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
		    	 public myAdapter2() {
		    	//	 mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		    		@Override
		    		public View getView(int position, View convertView2, ViewGroup parent) {
		    			
		    			final int mPosition=position;
		    			
		    			if(convertView2==null){
		    				convertView2=LayoutInflater.from(reply.getContext()).inflate(R.layout.videolist_item, null);
		    				holder2=new ViewHolder2();
		    				holder2.videoImage2=(ImageView) convertView2.findViewById(R.id.video_image);
		    				holder2.isname2=(TextView)convertView2.findViewById(R.id.isname);
		    				holder2.istime2=(TextView)convertView2.findViewById(R.id.istime);
		    				holder2.istext2=(TextView)convertView2.findViewById(R.id.istext);
		    				//holder2.videoPlayBtn2=(ImageButton)convertView2.findViewById(R.id.video_play_btn);
		    				
		    				holder2.isnew2=(ProgressBar)convertView2.findViewById(R.id.is_new);
		    				convertView2.setTag(holder2);
		    				
		    				
		    				
		    	
		    			}else{
		    				
		    				holder2=(ViewHolder2) convertView2.getTag();
		    				
		    			}
		    			
		    			holder2.videoImage2.setImageDrawable(getResources().getDrawable(videoList2.get(position).getVideoImage()));
		    			holder2.isname2.setText(videoList2.get(position).getName());
		    			holder2.istime2.setText(videoList2.get(position).getTime());
		    			holder2.istext2.setText(videoList2.get(position).getText());
		    			holder2.videoPlayBtn2.setVisibility(View.VISIBLE);
		    			holder2.videoImage2.setVisibility(View.VISIBLE);
		    			holder2.isname2.setVisibility(View.VISIBLE);
		    			holder2.istime2.setVisibility(View.VISIBLE);
		    			holder2.istext2.setVisibility(View.VISIBLE);
		    			mMediaCtrl2 = new MediaController(reply.getContext(),false);
		    			
		    			
		    			holder2.videoPlayBtn2.setOnClickListener(new View.OnClickListener() {
		    				@Override
		    				public void onClick(View v) {
		    				//	holder2.mProgressBar2.setVisibility(View.VISIBLE);
		    					currentIndex2=mPosition;
		    					playPosition2=-1;
		    					
		    					adapter2.notifyDataSetChanged();
		    					
		    				}
		    			});
		    			
		    			if(videoList2.get(mPosition).getUrl().equals("null"))
		    			{holder2.videoPlayBtn2.setVisibility(View.GONE);
		    			holder2.videoImage2.setVisibility(View.GONE);
		    			holder2.mProgressBar2.setVisibility(View.GONE);
		    			}
		    			
		    			//if(currentIndex!=position && )
		    			return convertView2;
		    		};
		    	}
		    	static class ViewHolder2{
		    		ImageView videoImage2;
		    		TextView isname2,istext2,istime2;
		    		ImageButton videoPlayBtn2;
		    		ProgressBar mProgressBar2,isnew2;
		    		VideoView videov2;
		    	}
		    	static class VideoInfo2 {
		    		private String url;
		    		private String name,time,text,New;
		    		private int videoImage;
		    		public VideoInfo2(String url,String name,String time,String text,String New,int path) {
		    			this.name=name;
		    			this.time=time;
		    			this.text=text;
		    			this.videoImage=path;
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
		    		
		    		public int getVideoImage() {
		    			return videoImage;
		    		}
		    		public void setVideoImage(int videoImage) {
		    			this.videoImage = videoImage;
		    		}
		    		public String getUrl() {
		    			return url;
		    		}
		    		public void setUrl(String url) {
		    			this.url = url;
		    		}
		    	}

		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
}



/*,job_quest where UID=who AND job_num=num " +
"AND name=\""+sper2.getSelectedItem().toString()+"\" AND job=\""+sper1.getSelectedItem().toString()*/