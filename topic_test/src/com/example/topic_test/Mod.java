package com.example.topic_test;

import java.util.Timer;

import com.google.ytdl.ResumableUpload;
import com.google.ytdl.UploadService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Mod extends Activity {
Intent it;
Button mod1,mod2;
Timer tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mod);
		mod1=(Button)findViewById(R.id.btn_practice);
		mod2=(Button)findViewById(R.id.btn_godlike);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Hello_login.Iam==null)
		{
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mod, menu);
		return true;
	}

	public void goPractice(View v)
	{
		it=new Intent(Mod.this,Is_every.class);
		
    			startActivity(it);
    			int version = Integer.valueOf(android.os.Build.VERSION.SDK);

 		       if(version >= 5) {
 		    	   overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
 		    	   }
	}
	
	public void goGodLike(View v)
	{
		
		
				it=new Intent(Mod.this,GodLike.class);
    	
				Hello_login.isrun="T";
				startActivity(it);
		
		
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
}
