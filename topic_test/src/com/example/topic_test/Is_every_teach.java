package com.example.topic_test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class Is_every_teach extends Activity implements OnItemClickListener,OnItemSelectedListener{
	public static String thisQ,thisQQ,Iam,Who,R_for_x;
	Spinner sper1,sper;
	View view;
	ListView listview;
	String[] item,QID,issue,who,QQID,item_num,name;
	private ArrayAdapter<String> listAdapter;
	int q_index;
	//SharedPreferences.Editor add_q_index = getPreferences(MODE_PRIVATE).edit();
	SharedPreferences add_q_index ;
	HttpClient the_time = new DefaultHttpClient();
	HttpPost R_time = new HttpPost("http://chentopic.no-ip.info/server_time.php");
	String Server_Time;
	Intent it;
	EditText edt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Iam=Hello_login.Iam;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 //      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	 //              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_is_every_teach);
		add_q_index = getPreferences(MODE_PRIVATE);
		
		
		sper1=(Spinner)findViewById(R.id.spier1);
		listview=(ListView)findViewById(R.id.teach_list);
        
		/* StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	        .detectDiskReads()  
	        .detectDiskWrites()  
	        .detectNetwork()  
	        .penaltyLog()  
	        .build());  
	        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
	        .detectLeakedSqlLiteObjects()   
	        .penaltyLog()  
	        .penaltyDeath()  
	        .build());  */
       
        
        try {
            String result = DBConnector.executeQuery("SELECT * FROM test_quest,member where UID=who AND UID="+Iam,this);
            JSONArray jsonArray = new JSONArray(result);
            item=new String[jsonArray.length()+2];
            QQID =new String[jsonArray.length()+2];
            item[0]="挑戰新訊息";item[1]="新訊息";
            for(int i = 0; i < jsonArray.length(); i++) {
            	JSONObject jsonData = jsonArray.getJSONObject(i);
            	item[i+2]=jsonData.getString("issue_text");
            	QQID [i+2]=jsonData.getString("QID");
            }
            listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,item);
            sper1.setAdapter(listAdapter);
        } catch(Exception e) {
            // Log.e("log_tag", e.toString());
        }
       
		try {
			 HttpResponse responsePOST;
			responsePOST = the_time.execute(R_time);
			HttpEntity resEntity = responsePOST.getEntity();
			Server_Time = EntityUtils.toString(resEntity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       
		listview.setOnItemClickListener(this);
		sper1.setOnItemSelectedListener(this);
		
		it=getIntent();
		int a= it.getIntExtra("QID",0);
		sper1.setSelection(a+2);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.is_every_teach, menu);
		return true;
	}
	@Override
    public void onResume() {
        super.onResume();
        if(Hello_login.Iam==null)
		 {
			 finish();
		 }
       q_index = add_q_index.getInt("題目索引", 1);
	}
	@Override
    public void onPause() {
        super.onPause();
        
        add_q_index.edit().putInt("題目索引",q_index).commit();
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//
		try { 
			 String result;
			 if(sper1.getSelectedItem().equals("挑戰新訊息"))
			 {
			 	 result = DBConnector.executeQuery("select *,count(UID) FROM test_quest,(select *,SUBSTR(r_quest, 2)as Rstr from reply where r_quest like 'z%')as R1,member where Rstr=QID AND r_name=r_who AND r_name=UID AND who='"+Iam+"' AND teach_when<r_time GROUP BY QID,UID",this);	
			 	}
			 else  if(sper1.getSelectedItem().equals("新訊息"))
			 {
				 result = DBConnector.executeQuery("select *,count(UID) FROM test_quest,reply,member where r_quest=QID AND r_name=r_who AND r_name=UID AND teach_when<r_time  AND who="+Iam +" GROUP BY UID",this);
			 }
			  else
			  {
				  result = DBConnector.executeQuery("select *,count(UID) from test_quest,reply,member where r_quest=QID AND r_name=UID AND r_name=r_who AND QID="+QQID[position] +" GROUP BY UID",this);
				  }
			
			 if(result.equals("null\n\n\n\n")){
				 listAdapter=null;
			 }else{
	            
	            JSONArray jsonArray = new JSONArray(result);
	            item=new String[jsonArray.length()];
	            name=new String[jsonArray.length()];
	            QID =new String[jsonArray.length()];
	            issue=new String[jsonArray.length()];
	            who=new String[jsonArray.length()];
	            for(int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonData = jsonArray.getJSONObject(i);
	            item[i]=jsonData.getString("name")+"   ("+jsonData.getString("count(UID)")+")";
	            name[i]=jsonData.getString("name");
	            QID [i]=jsonData.getString("QID");
	            issue[i]=jsonData.getString("issue_text");
	            who[i]=jsonData.getString("r_who");
	            }
	            if(item[0].equals("null   (0)"))
	            	{listAdapter=null;	}else
	            listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,item);
	            	}
	            listview.setAdapter(listAdapter);
			 
	        } catch(Exception e) {
	            // Log.e("log_tag", e.toString());
	        }
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(sper1.getSelectedItem().equals("挑戰新訊息"))
		{thisQ="sz"+QID[position];}else
		thisQ="s"+QID[position];
		
		thisQQ=issue[position];
		Who=who[position];
		R_for_x=name[position];
		
		it=new Intent(this,Testgo.class);
		 
		startActivity(it);
		
	}
}
