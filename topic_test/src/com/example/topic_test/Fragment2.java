package com.example.topic_test;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment2 extends Fragment implements SurfaceHolder.Callback,OnClickListener{
	
	View view;
	private Boolean btn_switch=false;
	private SurfaceView surfaceview;  
    private RelativeLayout lay;   
    private ImageButton btn; //   
     MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;  
    SurfaceHolder holder;
     RCamera mthread; 
    SharedPreferences add_video_index ;
    int video_index;
    String thisQ,name;
   
   
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	view = inflater.inflate(R.layout.pick_myans, container, false);
	/*
	 start = (Button) view.findViewById(R.id.start);  
     stop = (Button) view.findViewById(R.id.stop);  
     start.setOnClickListener(new TestVideoListener());  
     stop.setOnClickListener(new TestVideoListener());  
     surfaceview = (SurfaceView) view.findViewById(R.id.surfaceView1);  
     SurfaceHolder holder = surfaceview.getHolder(); 
     holder.addCallback(this); 
     holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
*/
	
	name=Hello_login.name;
	
	
	 add_video_index = getActivity().getSharedPreferences("vv",0);
     if(Is_every_teach.thisQ.indexOf('s')==-1)
     {   	
    	 thisQ =Is_every.thisQ;
    	 video_index = add_video_index.getInt(thisQ, 0);
	 }
     else
     {
    	 thisQ =Is_every_teach.thisQ.substring(1);
    	 video_index = add_video_index.getInt(Is_every_teach.R_for_x+"-"+thisQ, 0);	
     }
    
	
    return view;
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
	   @Override
	    public void onResume() {
	        super.onResume();
	       
	    	File dir=Environment.getExternalStorageDirectory(); 
		       String path=dir.getPath()+"/面試App"; 
		       File file=new File(path); 
		       if(!file.exists()) 
		        file.mkdir(); 
	     
		       surfaceview = (SurfaceView) view.findViewById(R.id.surfaceView1); 
		       surfaceview.setOnClickListener(this);
		  
		       lay = (RelativeLayout) view.findViewById(R.id.lay);  
		       btn=(ImageButton)view.findViewById(R.id.video_image__btn);  
		       
		       holder = this.surfaceview.getHolder();
		       holder.addCallback(this);
		       mediaPlayer = new MediaPlayer();
		       holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
		       btn.setOnClickListener(new OnClickListener() {  
		    	        
		           @Override  
		           public void onClick(View arg0)
	  {  
		               // TODO Auto-generated method stub  
		       if(btn_switch==false)
		       {mediaPlayer.reset();
		               if (mthread==null)
		               {  
		            	   video_index++;
		            	   mthread = new RCamera(10*60*1000, surfaceview, surfaceHolder,thisQ,video_index);
		            	  mthread.start();  
		            	   while(mthread.start_r==false)
		            	   { btn.setEnabled(false);}
		            		  
		            	   btn_switch=true;
			               btn.setImageResource(R.drawable.image_stop);
			               btn.setEnabled(true);
			              
		               }else {  
		                   Toast.makeText(getActivity(), "正在錄影", Toast.LENGTH_SHORT).show();  
		               }  
		               
		        
		            
		       } 
		       else
		       {
		    	   if (mthread!=null) {  
		    	
		    		   mthread.stopRecord();  
	                  
	                   mthread=null;  
	                  
	                   Intent mediaScanIntent = new Intent(
	                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	                   Uri contentUri;
	                   if(Is_every_teach.thisQ.indexOf('s')==-1)
	                   {
	                    contentUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/面試App/面試_"+name+"_"+thisQ+"-"+video_index+".mp4"));
	                   }else{
	                	contentUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/面試App/R面試For_"+Is_every_teach.R_for_x+"_"+thisQ+"-"+video_index+".mp4"));
	                   }
	                	mediaScanIntent.setData(contentUri);
	                    view.getContext().sendBroadcast(mediaScanIntent);
	                	  
	                    if(Is_every_teach.thisQ.indexOf('s')==-1)
						{
				        	
				        	Toast.makeText(view.getContext(),Environment.getExternalStorageDirectory().getPath()+"/面試App/面試_"+name+"_"+thisQ+"-"+video_index+".mp4" , Toast.LENGTH_SHORT).show(); 
						}
						else
						{
							thisQ =Is_every_teach.thisQ.substring(1);
							
							Toast.makeText(view.getContext(),Environment.getExternalStorageDirectory().getPath()+"/面試App/R面試For_"+Is_every_teach.R_for_x+"_"+thisQ+"-"+video_index+".mp4", Toast.LENGTH_SHORT).show(); 
						}
	                   
	               }else {  
	                   Toast.makeText(getActivity(), "還沒開始錄呢", Toast.LENGTH_SHORT).show();  
	               }
	               btn_switch=false;
	               btn.setImageResource(R.drawable.image_btn);
		       }
	  }    
		       });  
		      
		   }  
		 
	   @Override
		public void onDestroy() {
			super.onDestroy();
			
			
			add_video_index = getActivity().getSharedPreferences("vv",0);
			if(Is_every_teach.thisQ.indexOf('s')==-1)
			{
	        add_video_index.edit().putInt(thisQ,video_index).commit();
			}
	        else
	        {
	        add_video_index.edit().putInt(Is_every_teach.R_for_x+"-"+thisQ,video_index).commit();
	        }
		}
		@Override
	    public void onPause() {
	        super.onPause();
	        mediaPlayer.stop();

		}
		@Override
		public void onClick(View v) {
			
			 try {
				 if(Is_every_teach.thisQ.indexOf('s')==-1)
					{
			        	mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/面試App/面試_"+name+"_"+thisQ+"-"+video_index+".mp4");
			        	Toast.makeText(view.getContext(),"正在播放"+Environment.getExternalStorageDirectory().getPath()+"/面試App/面試_"+name+"_"+thisQ+"-"+video_index+".mp4" , Toast.LENGTH_SHORT).show(); 
					}
					else
					{
						thisQ =Is_every_teach.thisQ.substring(1);
						mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/面試App/R面試For_"+Is_every_teach.R_for_x+"_"+thisQ+"-"+video_index+".mp4");
						Toast.makeText(view.getContext(),"正在播放"+Environment.getExternalStorageDirectory().getPath()+"/面試App/R面試For_"+Is_every_teach.R_for_x+"_"+thisQ+"-"+video_index+".mp4", Toast.LENGTH_SHORT).show(); 
					}
		            
		            mediaPlayer.prepare();
		            mediaPlayer.setDisplay(surfaceHolder);
		            mediaPlayer.start();
		            mediaPlayer.setOnCompletionListener(
		                    new MediaPlayer.OnCompletionListener() {
		                        @Override
		                        public void onCompletion(MediaPlayer mp) {
		                            try {
		                                mp.reset();
		                               
		                            } catch (Exception e) {
		                            }
		                            // repeat play 只要下 start()
		                            mp.start();
		                        }
		                    });
		        } catch (Exception e) {
		        }
			 
		}
	}