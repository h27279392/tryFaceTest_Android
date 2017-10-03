package com.example.topic_test;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Class_job extends Activity {
public static int select_class,select_job;

String[] class_item,job_item=null,class_index,job_index;
ParentListView class_list;
MyCustomAdapter mAdapter;
ArrayAdapter mmAdapter;
private int mLcdWidth = 0,mLcdHeight=0,item_back_y=0,status_height=0;
private float mDensity = 0;
int pos2=-50;
ViewHolder holder;
RelativeLayout footer;
View list_lay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_class_job);
		
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
	getSystemInfo();
    class_list=(ParentListView)findViewById(R.id.list_class);
    mAdapter = new MyCustomAdapter();
    
     try {
         String result = DBConnector.executeQuery("SELECT * FROM class",this);
         JSONArray jsonArray = new JSONArray(result);
         class_item=new String[jsonArray.length()+1];
         class_index=new String[jsonArray.length()+1];
         class_item[0]="";
         class_index[0]="-1";
         mAdapter.addItem(String.valueOf(0));
         for(int i = 0; i < jsonArray.length(); i++) {
         	JSONObject jsonData = jsonArray.getJSONObject(i);
         	class_index[i+1]=jsonData.getString("class_index");
         	class_item[i+1]=jsonData.getString("class_name");
         	mAdapter.addItem(String.valueOf(i+1));
         }
     } catch(Exception e) {
         // Log.e("log_tag", e.toString());
     }
   	
    
     class_list.setAdapter(mAdapter);
     class_list.setOnItemClickListener(new OnItemClickListener(){
 		@Override
 		public void onItemClick(AdapterView<?> arg0, View v, int pos,
 				long arg3) {
 select_class=Integer.parseInt(class_index[pos]);
 if(pos!=0)
 {
	  
 			View footer = v.findViewById(R.id.footer);
 			if(pos2!=pos)
 			{
 				try {
 					
 		           String result = DBConnector.executeQuery("SELECT * FROM job where job_class='"+class_index[pos]+"'",Class_job.this);
 		          
 		           JSONArray jsonArray = new JSONArray(result);
 		           job_index=new String[jsonArray.length()]; 
 		           job_item=new String[jsonArray.length()];     
 		           for(int i = 0; i < jsonArray.length(); i++) {
 		           		JSONObject jsonData = jsonArray.getJSONObject(i);
 		           		job_index[i]=jsonData.getString("job_id");
 		           		job_item[i]=jsonData.getString("job_name");	
 		           		
 		           }
 		           	mmAdapter = new ArrayAdapter(Class_job.this,android.R.layout.simple_list_item_1,job_item);
 		           	holder=(ViewHolder) v.getTag();
 		 			holder.mmlist.setAdapter(mmAdapter);
 		 			holder.mmlist.setFocusable(false);
 		 			holder.mmlist.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							select_job=Integer.parseInt(job_index[position]);
							Intent it=new Intent(Class_job.this,Mod.class);
							startActivity(it);
						}
					});
 		 	}		
 		        catch(Exception e){
 		       }
 				
 				pos2=pos;
 				item_back_y=v.getTop();
 			}
 			class_list.setAnimation(new upAnimation(class_list,v,pos,500,1,item_back_y));
 			footer.startAnimation(new upAnimation(footer,500,0));
 			//mListView.setSelectionFromTop(pos, 0);
 			//Log.e("where", ""+v.getTop());
 			//mListView.setFocusable(false);
 			//mListView.setsc
 }
 		}
 	});
     
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

	private void getSystemInfo(){
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mLcdWidth = dm.widthPixels;
		mDensity = dm.density;
		mLcdHeight=dm.heightPixels;
		status_height= getStatusBarHeight();
	}
	public int getStatusBarHeight() {
		  int result = 0;
		  int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		  if (resourceId > 0) {
		      result = getResources().getDimensionPixelSize(resourceId);
		  }
		  return result;
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.class_job, menu);
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
	
	
	private class MyCustomAdapter extends BaseAdapter {
		  
	    private ArrayList mData = new ArrayList();
	    private LayoutInflater mInflater;

	    public MyCustomAdapter() {
	        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    public void addItem(final String item) {
	        mData.add(item);
	        notifyDataSetChanged();
	    }

	    @Override
	    public int getCount() {
	        return mData.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return mData.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	     //   System.out.println("getView " + position + " " + convertView);
	        holder = null;
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.expand_item, null);
	            list_lay=convertView;
	            holder = new ViewHolder();
	 
	            holder.txt_class_name = (TextView)convertView.findViewById(R.id.txt_class_name);
	            holder.mmlist=(ListView)convertView.findViewById(R.id.list_job);
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder)convertView.getTag();
	        }
	       
	 	       
	     
			holder.txt_class_name.setText(class_item[position]);
		
			RelativeLayout.LayoutParams lp = null;
			
			RelativeLayout header = (RelativeLayout) convertView.findViewById(R.id.header);
			int item_h=(int) header.getLayoutParams().height;
			RelativeLayout title = (RelativeLayout) findViewById(R.id.r_title);
			title.getLayoutParams().height=item_h+10;
			lp = (RelativeLayout.LayoutParams) holder.mmlist.getLayoutParams();
			lp.height =(mLcdHeight-item_h-status_height);
			//Log.e("tall", String.valueOf(mLcdHeight+","+item_h+","+status_height));
			holder.mmlist.setLayoutParams(lp);
			
			
			// get footer height
			footer = (RelativeLayout) convertView.findViewById(R.id.footer);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) footer.getLayoutParams();
			params.bottomMargin = -lp.height;
			footer.setVisibility(View.GONE);
	        
	        
	        return convertView;
	    }

	}

	public static class ViewHolder {
	    public TextView txt_class_name;
	    public ListView mmlist;
	  //  public ImageView img;
	}
	
	
}
