package com.example.topic_test;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.ytdl.Auth;

public class Youtube_of_Activity extends YouTubePlayerFragment implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayer player;
    private String videoId;
   
    public static Youtube_of_Activity newInstance() {
    	
      return new Youtube_of_Activity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      initialize(Auth.KEY, this);
    }

    @Override
    public void onDestroy() {
    	release();
      super.onDestroy();
    }
    public void release(){
    	 if (player != null) {
    	        player.release();
    	      }
    }
    public void pause() {
      if (player != null) {
        player.pause();
      }
    }
    
    public void setVideoId(String videoId) {
    	
        if (videoId != null && !videoId.equals(this.videoId)) {
          this.videoId = videoId;
          if (player != null &&!videoId.equals("")) {
            player.cueVideo(videoId);
          }
        }
      }
    
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
    	this.player = player;
      //  player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
    	 
       
        if (!wasRestored && videoId != null &&!videoId.equals("")) {
          player.cueVideo(videoId);
    }
    }
}