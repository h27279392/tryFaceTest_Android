package com.example.topic_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.topic_test.Fragment1.ViewHolder;



import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.api.services.youtube.YouTube;
import com.google.ytdl.Auth;

public class Teach_myQ  extends Activity {

	private List<VideoInfo> videoList=new ArrayList<VideoInfo>();
	private VideoInfo videoinfo;
	private myAdapter adapter;

SwipeMenuListView list;
String Iam;
String[] QID,Q_text,Q_video;
Intent it;
int image_path;
String v_id;
LinearLayout videoBox;
ImageButton closeButton;
Youtube_of_Activity youTubeView;

//com.baoyz.swipemenulistview.SwipeMenu
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teach_my_q);
		
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
	        
		Iam=Hello_login.Iam;
		it=new Intent();
		
		list=(SwipeMenuListView)findViewById(R.id.teach_list);
		  videoBox=(LinearLayout)findViewById(R.id.video_boxT);
	       closeButton=(ImageButton)findViewById(R.id.close_buttonT);
	      
	       closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.clearChoices();
	    	    list.requestLayout();
	    	    youTubeView.pause();
	    	    ViewPropertyAnimator animator = videoBox.animate()
	    	        .translationYBy(videoBox.getHeight())
	    	        .setDuration(600);
	    	    runOnAnimationEnd(animator, new Runnable() {
	    	      @Override
	    	      public void run() {
	    	        videoBox.setVisibility(View.INVISIBLE);
	    	      }
	    	    });
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				it.setClass(Teach_myQ.this,Is_every_teach.class);
				it.putExtra("QID", position);
				youTubeView.release();
				adapter.releaseLoaders();
				videoBox.setVisibility(View.GONE);
				if(youTubeView!=null)
					youTubeView=null;
				startActivity(it);
			}
		});
		
		SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        list.setMenuCreator(creator);

        // step 2. listener item click event
        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                
                switch (index) {
                    
                    case 0:
                        delete(position);

                        break;
                }
                return false;
            }
        });

	}
	private void delete(int position) {
        // delete app
		
		DBConnector.executeQuery("DELETE FROM test_quest WHERE QID="+QID[position]+" AND who="+Iam,this);
		videoList.clear();
		try {
            String result = DBConnector.executeQuery("SELECT * FROM test_quest,member where UID=who AND UID="+Iam,this);
            JSONArray jsonArray = new JSONArray(result);
            Q_text=new String[jsonArray.length()];
            QID =new String[jsonArray.length()];
            Q_video=new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonData = jsonArray.getJSONObject(i);
            	Q_text[i]=jsonData.getString("issue_text");
            	Q_video[i]=jsonData.getString("issue_video");
            	QID [i]=jsonData.getString("QID");
            	videoinfo=new VideoInfo(Q_video[i],Q_text[i],"1",image_path);
            	videoList.add(videoinfo);
            } } catch(Exception e) {
                // Log.e("log_tag", e.toString());
            }
		adapter.notifyDataSetChanged();
		Toast.makeText(this, "¤w§R°£", Toast.LENGTH_LONG).show();
    }
	 private int dp2px(int dp) {
	        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
	                getResources().getDisplayMetrics());
	    }
	 
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 if(Hello_login.Iam==null)
		 {
			 finish();
		 }
		if(adapter!=null)
			adapter.releaseLoaders();
		if(youTubeView==null)
		{

			youTubeView = Youtube_of_Activity.newInstance();
	        getFragmentManager().beginTransaction().replace(R.id.youtube_viewT, youTubeView).commit();
		}
		videoList.clear();
		try {
            String result = DBConnector.executeQuery("SELECT * FROM test_quest,member where UID=who AND UID="+Iam+" order by(QID)",this);
            JSONArray jsonArray = new JSONArray(result);
            Q_text=new String[jsonArray.length()];
            QID =new String[jsonArray.length()];
            Q_video=new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonData = jsonArray.getJSONObject(i);
            	Q_text[i]=jsonData.getString("issue_text");
            	Q_video[i]=jsonData.getString("issue_video");
            	QID [i]=jsonData.getString("QID");
            	videoinfo=new VideoInfo(Q_video[i],Q_text[i],"1",image_path);
            	videoList.add(videoinfo);
            }
           adapter=new myAdapter();
        list.setAdapter(adapter);
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }
	
	}

	public void add_Q(View v)
	{
		it.setClass(Teach_myQ.this,Add_Q.class);
		startActivity(it);
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);

	       if(version >= 5) {
	    	   overridePendingTransition(R.anim.addq_in_up, R.anim.addq_in_nono);
	    	   }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teach_my_q, menu);
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
	
	
	
	
	class myAdapter extends BaseAdapter{
		private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
  	  	private final ThumbnailListener thumbnailListener;
    	// private LayoutInflater mInflater;
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
    			
    			if(convertView==null){
    				convertView=LayoutInflater.from(Teach_myQ.this).inflate(R.layout.videolist_teach_myq, null);
    				holder=new ViewHolder();
    				
    				holder.thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.video_imageT);
					holder.thumbnail.initialize(Auth.KEY, thumbnailListener);
    				holder.istext=(TextView)convertView.findViewById(R.id.istext);
    				
    				holder.isnew=(ProgressBar)convertView.findViewById(R.id.is_new);
    				convertView.setTag(holder);
    				 holder.thumbnail.setTag(position);
	    		        holder.thumbnail.setId(position);
    			}else{
    				
    				holder=(ViewHolder) convertView.getTag();
    				holder.thumbnail.setId(position);
    		         
    				YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.thumbnail);
    		        if (loader == null) {
    		          // 2) The view is already created, and is currently being initialized. We store the
    		          //    current videoId in the tag.
    		        	
    		          holder.thumbnail.setTag(position);
    		         
    		        } else {
    		          // 3) The view is already created and already initialized. Simply set the right videoId
    		          //    on the loader.
    		          holder.thumbnail.setImageResource(R.drawable.no_thumbnail);
    		          
    		          loader.setVideo(videoList.get(mPosition).getUrl());
    		        }
    			}
    			
    			holder.istext.setText(videoList.get(position).getText());
    			holder.istext.setVisibility(View.VISIBLE);
    			
    		
    			holder.thumbnail.setVisibility(View.VISIBLE);

    			holder.thumbnail.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					 v_id= videoList.get((Integer)v.getId()).getUrl();
    					youTubeView.setVideoId(v_id);
    				    
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
    			
    			
    			if(videoList.get(mPosition).IsNew().equals("0") && !videoList.get(mPosition).getName().equals(Hello_login.name))
	    			holder.isnew.setVisibility(View.VISIBLE);
	    				else
	    			holder.isnew.setVisibility(View.GONE);
    			if(videoList.get(mPosition).getUrl().equals("null"))
    			{}
    			
    			//if(currentIndex!=position && )
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
    // Log.e("look video", String.valueOf(x));
     
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
    		
    		TextView istext;
    		YouTubeThumbnailView thumbnail;
    		ProgressBar isnew;
    		
    	}
    	static class VideoInfo {
    		private String url;
    		private String name,time,text,New;
    		private int videoImage;
    		public VideoInfo(String url,String text,String New,int path) {
    		
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
