package com.example.topic_test;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class RCamera extends Thread{  
    private MediaRecorder mediarecorder;
    private SurfaceHolder surfaceHolder;  
    private long recordTime;  
    private SurfaceView surfaceview;
    public Camera mCamera;  
    private int video_index;
    private String thisQ; 
    private String name=Hello_login.name;
    static boolean start_r=false;
    static String godlike_path;
    public RCamera(long recordTime, SurfaceView surfaceview,SurfaceHolder surfaceHolder,String QQ,int v) {  
        this.recordTime = recordTime;  
        this.surfaceview = surfaceview;  
        this.surfaceHolder = surfaceHolder; 
        this.video_index=v;
        this.thisQ=QQ;
    }  
  
    @Override  
    public void run() {  
   
        startRecord();  
 
        Timer timer = new Timer();  
  
        timer.schedule(new TimerThread(), recordTime);  
    }  
  
   
    public Camera getCameraInstance() {  
        Camera c = null;  
        try {  
            c = Camera.open();  
        } catch (Exception e) {  
           
            Log.i("info", "錯誤");  
        }  
        return c;  
    }  
  
    public void startRecord() {  
          
        mCamera = getCameraInstance();  
       
        int cameraCount = 0;  
        CameraInfo cameraInfo = new CameraInfo();  
        cameraCount = Camera.getNumberOfCameras();
        int cameraPosition=1;  
        for(int i = 0; i < cameraCount; i++) {  
            Camera.getCameraInfo(i, cameraInfo);
            Log.e("後相機", "後相機"+cameraCount);
            if(cameraPosition == 1) {  
             
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {    
                    if (mCamera!=null) {  
                        mCamera.stopPreview();
                        mCamera.release();
                        mCamera = null;
                    }  
                    mCamera = Camera.open(i);
                    try {  
                        mCamera.setPreviewDisplay(surfaceHolder);  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                    mCamera.startPreview();
                    cameraPosition = 0;  
                    break;  
                }  
             else {  
                
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCamera.stopPreview();  
                    mCamera.release();  
                    mCamera = null;
                    mCamera = Camera.open(i);  
                    try {  
                        mCamera.setPreviewDisplay(surfaceHolder);  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                    mCamera.startPreview();
                    cameraPosition = 1;  
                
                }  
            }  
            }
        }  
          
        mCamera.setDisplayOrientation(90);  
        Camera.Parameters params=mCamera.getParameters();  
        params.setPictureSize(640,480);  
        mCamera.setParameters(params);  
          
        
        
               
                
           
        mCamera.unlock(); 
        
        mediarecorder = new MediaRecorder();   

        mediarecorder.setCamera(mCamera);    
        mediarecorder.setOrientationHint(270);    
        
        mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);    
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);    
            
       
        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);      
        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);     
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);    
        
       if(Hello_login.isrun.indexOf('T')!=-1){
			mediarecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/面試App/實戰_"+name+"_"+video_index+".mp4"); 
			godlike_path=Environment.getExternalStorageDirectory().getPath()+"/面試App/實戰_"+name+"_"+video_index+".mp4";
		}else
        if(Is_every_teach.thisQ.indexOf('s')==-1)
		{
        	mediarecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/面試App/面試_"+name+"_"+thisQ+"-"+video_index+".mp4"); 
		}
		else 
		{
			thisQ =Is_every_teach.thisQ.substring(1);
			mediarecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath()+"/面試App/R面試For_"+Is_every_teach.R_for_x+"_"+thisQ+"-"+video_index+".mp4"); 
		}
          
        mediarecorder.setVideoSize(640,480);  

        mediarecorder.setVideoEncodingBitRate(10*1024*1024);
        
        mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());    
        try {  
            
            mediarecorder.prepare();  
           
            mediarecorder.start();  
            
        } catch (IllegalStateException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
       start_r=true;
    }  
  
    public void stopRecord() {  
        System.out.print("stopRecord()");  
          
        if (mediarecorder != null) {  
       
        	mediarecorder.reset();
            if (mCamera != null) {  
                mCamera.release();  
                mCamera = null;  
            }  
            
            // mediarecorder.stop();  
              
           
            mediarecorder.release();  
            mediarecorder = null;  
  
            
        } 
        surfaceview = null;  
        surfaceHolder = null;
        start_r=false;
    }  
  
    
    class TimerThread extends TimerTask {  
  
     
        @Override  
        public void run() {  
            stopRecord();  
            this.cancel(); 
            
        }  
    }


	

  
}