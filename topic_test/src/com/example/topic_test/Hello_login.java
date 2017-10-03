package com.example.topic_test;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Hello_login extends Activity {
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
		
	}









	public static String Iam,name,isrun="F";
	Intent it;
	Toast tos;
	String uname,str,job;
	EditText edt;
	EditText id,pwd;
	ImageView im1,im2;
	ImageButton ibtn1,ibtn2;
	LinearLayout lay1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_hello_login);
          
       
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
        
        id=(EditText)findViewById(R.id.edit1);
    	pwd=(EditText)findViewById(R.id.edit2);
    	lay1=(LinearLayout)findViewById(R.id.linearLayout1);
    	Button btn=(Button)findViewById(R.id.button1);
    	
    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
    	lay1.startAnimation(animation);
    	btn.startAnimation(animation);
        
     
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello_login, menu);
        return true;
    }
public void s_login(View v){
	uname="";
	
			 try {
		            String result = DBConnector.executeQuery("SELECT * FROM member " +
		            		"WHERE id='"+id.getText().toString().toLowerCase()+"' AND id='"+pwd.getText().toString().toLowerCase()+"'",this);
		            Log.e("r", result);
		            if(result.equals("null\n\n\n\n")){Toast.makeText(Hello_login.this, "登入失敗", Toast.LENGTH_SHORT).show();}
		           else{
		            JSONArray jsonArray = new JSONArray(result);
		        for(int i = 0; i < jsonArray.length(); i++) 
	{
		               JSONObject jsonData = jsonArray.getJSONObject(i);
		   Iam=jsonData.getString("UID"); name=jsonData.getString("name");job=jsonData.getString("cosplay");}
		       Toast.makeText(Hello_login.this,name+" 已登入", Toast.LENGTH_SHORT).show();	
		      // isrun="F";
	           id.setText("");
	           pwd.setText("");
	           
	           if(job.equals("學生"))
	           it=new Intent(this,Class_job.class);
	           else
	           it=new Intent(this,Teach_myQ.class);   
		       startActivity(it);
		       int version = Integer.valueOf(android.os.Build.VERSION.SDK);

		       if(version >= 5) {
		    	   overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		    	   }
		           }
		       
			 } catch(Exception e) {
		            // Log.e("log_tag", e.toString());
				 Toast.makeText(Hello_login.this, "與伺服器連接失敗", Toast.LENGTH_SHORT).show();
		        }
	           
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
	
	
	public void iamstu(View v)
	{
		Iam="2"; name="A先生";job="學生";
		if(job.equals("學生"))
	           it=new Intent(this,Class_job.class);
	           else
	           it=new Intent(this,Teach_myQ.class);   
		       startActivity(it);
		       int version = Integer.valueOf(android.os.Build.VERSION.SDK);

		       if(version >= 5) {
		    	   overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		    	   }
	}
	public void iamtea(View v)
	{
		Iam="4"; name="S老師";job="老師";
		if(job.equals("學生"))
	           it=new Intent(this,Class_job.class);
	           else
	           it=new Intent(this,Teach_myQ.class);   
		       startActivity(it);
		       int version = Integer.valueOf(android.os.Build.VERSION.SDK);

		       if(version >= 5) {
		    	   overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		    	   }
	}
	
}
