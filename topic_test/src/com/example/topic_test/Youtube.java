package com.example.topic_test;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.ytdl.Auth;

public class Youtube extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayer player;
    private String videoId;
   
    public static Youtube newInstance() {
    	
      return new Youtube();
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
    	// && !videoId.equals(this.videoId)
        if (videoId != null) {
          this.videoId = videoId;
          if (player != null) {
            player.cueVideo(videoId);
            
          }
        }
      }
    
    public void load(){
    	if(player!=null)
    	{
    	player.loadVideo(videoId);
    	}
    }
    public void play(){
    	if(player!=null)
    	{
    	player.play();
    	}
    }
    public int getCurrentTime(){
    	return player.getCurrentTimeMillis();
    }
    public int getVideoSumTime(){
    	return player.getDurationMillis();
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
    	this.player = player;
      //  player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
       
       // player.setOnFullscreenListener();
       
        if (!wasRestored && videoId != null) {
          player.cueVideo(videoId);
    }
    }
}