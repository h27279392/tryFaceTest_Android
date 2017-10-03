package com.example.topic_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.ytdl.Auth;


public class Fragment1 extends ListFragment  {
	
	
	private static ListView mListView;
	private List<VideoInfo> videoList=new ArrayList<VideoInfo>();
	private VideoInfo videoinfo;
	private myAdapter adapter;
	private static final int ANIMATION_DURATION_MILLIS = 300;
	View view;
	String[] name,time,video,r_people,text,isnew;
	Youtube youtube_fragment;
	Timer tm;
	TimerTask tmTask;
	String result,ans,read;
	//Uri[] video_uri;
	//int x=0;
	/**SQL*/
	String query1,query2;
	
	String thisQ,Who,thisQQ;
	LinearLayout videoBox;
	ImageButton closeButton;
	String query_add,data_name,query_del;
	
	DisplayMetrics metrics;
	//HttpClient the_time = new DefaultHttpClient();
	//HttpPost R_time = new HttpPost("http://chentopic.no-ip.info:1993/server_time.php");
	//String Server_Time;
	private JSONArray jsonArray;
	

	@SuppressLint("NewApi")@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view= inflater.inflate(R.layout.fragment1, container, false);
		youtube_fragment = Youtube.newInstance();
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.youtube_fragment, youtube_fragment).commit();
		if(Is_every_teach.thisQ.indexOf('s')==-1)
		{
		Is_every_teach.thisQ="";
		thisQ =Is_every.thisQ;
		thisQQ=Is_every.thisQQ;
		Who  =Hello_login.Iam; 

		query1="select name,issue_text,issue_video,r_text,r_video,r_time,r_name,pig_read from test_quest,reply,member " +
				"where QID=r_quest AND r_name=UID AND QID=\""+thisQ+"\" AND r_who=\""+Who+"\"  order by(r_time)";
		read="update reply set pig_read='1' where r_name!=r_who AND r_who="+Hello_login.Iam+" AND r_quest='"+thisQ+"' AND pig_read='0'";
		}else
		{
			thisQ =Is_every_teach.thisQ.substring(1);
			thisQQ=Is_every_teach.thisQQ;
			Who  =Is_every_teach.Who;
			if(thisQ.indexOf('z')!=-1){
				query1="select name,issue_text,issue_video,r_text,r_video,r_time,r_name,pig_read" +
						" from test_quest,(select *,SUBSTR(r_quest, 2)as Rstr from reply where r_quest like 'z%' and r_quest='"+thisQ+"')as R1,member " +
						"where QID=Rstr AND r_name=UID AND r_who="+Who+" order by(r_time)";
				read="update reply set pig_read='1' where r_name=r_who AND r_who="+Who+" AND r_quest='"+thisQ+"' AND pig_read='0'";
			}else
			{
			
			query1="select name,issue_text,issue_video,r_text,r_video,r_time,r_name,pig_read from test_quest,reply,member " +
					"where QID=r_quest AND r_name=UID AND QID=\""+thisQ+"\" AND r_who=\""+Who+"\"  order by(r_time)";
			read="update reply set pig_read='1' where r_name=r_who AND r_who="+Who+" AND r_quest='"+thisQ+"' AND pig_read='0'";
			
			
			}
		}
		
		//Log.e("asd", Is_every_teach.thisQ);
		//Log.e("asd1", null);
		
		metrics = new DisplayMetrics();  
	    Fragment1.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
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
  
        mListView = (ListView) view.findViewById(android.R.id.list);
        videoBox=(LinearLayout)view.findViewById(R.id.video_box);
       closeButton=(ImageButton)view.findViewById(R.id.close_button);
       closeButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mListView.clearChoices();
    	    mListView.requestLayout();
    	    youtube_fragment.pause();
    	    ViewPropertyAnimator animator = videoBox.animate()
    	        .translationYBy(videoBox.getHeight())
    	        .setDuration(ANIMATION_DURATION_MILLIS);
    	    runOnAnimationEnd(animator, new Runnable() {
    	      @Override
    	      public void run() {
    	        videoBox.setVisibility(View.INVISIBLE);
    	      }
    	    });
		}
	});
        try {
            result = DBConnector.executeQuery(query1,view.getContext());
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
            	videoinfo=new VideoInfo(video[i],name[i],time[i],text[i],isnew[i],R.drawable.pic);
            	videoList.add(videoinfo);
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
		         
		       String ans =	DBConnector.executeQuery(query_del,getActivity());
		       Toast.makeText(getActivity(), "已刪除", Toast.LENGTH_LONG).show();
		        	Log.e("刪除回傳", query_del);
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
   
  
	return view;
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
	        
	        Reading();
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        if(Hello_login.Iam==null)
			 {
				 getActivity().finish();
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
	   	       	            result = DBConnector.executeQuery(query1,view.getContext());
	   	       	            
	   	       	      
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
	   	            	videoinfo=new VideoInfo(video[i],name[i],time[i],text[i],isnew[i],R.drawable.pic);
	   	            	videoList.add(videoinfo);
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
	            tm.schedule(tmTask, 1, 500); 
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
	 
	     public void Reading()
	     {
	    	 DBConnector.executeQuery(read,view.getContext());
	     }
	     
	     
	     class myAdapter extends BaseAdapter{
	    	// private LayoutInflater mInflater;
	    	 VideoInfo info;
	    	    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
	    	  
	    	    private final ThumbnailListener thumbnailListener;
	    	
	    	 public myAdapter() {
	    	//	 mInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		
	    	      thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
	    	     
	    	      thumbnailListener = new ThumbnailListener();
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
	    		    }
	    		@Override
	    		public View getView(int position, View convertView, ViewGroup parent) {
	    			final ViewHolder holder;
	    			final int mPosition=position;
	    			info = videoList.get(position);
	    			
	    			if(convertView==null){
	    				
	    				convertView=LayoutInflater.from(view.getContext()).inflate(R.layout.videolist_item,null);
	    				holder=new ViewHolder();
	    				holder.thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.video_image);
						holder.thumbnail.initialize(Auth.KEY, thumbnailListener);
	    				holder.isname=(TextView)convertView.findViewById(R.id.isname);
	    				holder.istime=(TextView)convertView.findViewById(R.id.istime);
	    				holder.istext=(TextView)convertView.findViewById(R.id.istext);
	    				holder.isnew=(ProgressBar)convertView.findViewById(R.id.is_new);
	    				convertView.setTag(holder);
	    		        holder.thumbnail.setTag(position);
	    		        holder.thumbnail.setId(position);
	    		        
	    			}else{
	    				
	    				holder=(ViewHolder) convertView.getTag();
	    				holder.thumbnail.setId(position);
	    		          Log.e("所以",String.valueOf(holder.thumbnail.getId()));
	    				YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.thumbnail);
	    		        if (loader == null) {
	    		          // 2) The view is already created, and is currently being initialized. We store the
	    		          //    current videoId in the tag.
	    		        	
	    		          holder.thumbnail.setTag(position);
	    		         
	    		        } else {
	    		          // 3) The view is already created and already initialized. Simply set the right videoId
	    		          //    on the loader.
	    		          holder.thumbnail.setImageResource(R.drawable.no_thumbnail);
	    		          
	    		          loader.setVideo(info.getUrl());
	    		        }
	    			}
	    			
	    			holder.isname.setText(videoList.get(position).getName());
	    			holder.istime.setText(videoList.get(position).getTime());
	    			holder.istext.setText(videoList.get(position).getText());
	    			holder.isname.setVisibility(View.VISIBLE);
	    			holder.istime.setVisibility(View.VISIBLE);
	    			holder.istext.setVisibility(View.VISIBLE);
	    			holder.thumbnail.setVisibility(View.VISIBLE);

	    			holder.thumbnail.setOnClickListener(new View.OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					
	    					youtube_fragment.setVideoId(videoList.get((Integer)v.getId()).getUrl());
	    					
	    					 if (videoBox.getVisibility() != View.VISIBLE) {
	    					        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
	    					          // Initially translate off the screen so that it can be animated in from below.
	    					          videoBox.setTranslationY(videoBox.getHeight());
	    					        }
	    					        videoBox.setVisibility(View.VISIBLE);
	    					      }

	    					      // If the fragment is off the screen, we animate it in.
	    					      if (videoBox.getTranslationY() > 0) {
	    					        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
	    					      }
	    					      
	    					}
	    			}	);
	    			
	    			if(videoList.get(mPosition).IsNew().equals("0") && !videoList.get(mPosition).getName().equals(Hello_login.name))
		    			holder.isnew.setVisibility(View.VISIBLE);
		    		else
		    			holder.isnew.setVisibility(View.GONE);
	    			
	    			if(videoList.get(mPosition).getUrl().equals("null"))
	    				holder.thumbnail.setVisibility(View.GONE);
	    			
	    			if(videoList.get(mPosition).getText().equals("null"))
	    				holder.istext.setVisibility(View.GONE);
	    		
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
	     
	    	static class ViewHolder{
	    		YouTubeThumbnailView thumbnail;
	    		TextView isname,istext,istime;
	    	//	ImageButton videoPlayBtn;
	    		ProgressBar isnew;
	    	//	VideoView videov;
	    	}
	    	static class VideoInfo {
	    		private String url;
	    		private String name,time,text,New;
	    		private int videoImage;
	    		public VideoInfo(String url,String name,String time,String text,String New,int path) {
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
		
	    	@Override
	        public void onDestroyView() {
	          super.onDestroyView();

	          adapter.releaseLoaders();
	          
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