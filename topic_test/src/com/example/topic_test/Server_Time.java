package com.example.topic_test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;



public class Server_Time {
String Server_Time;
HttpClient the_time;
HttpPost R_time;
	public void Server_Time(){
		
	}
	public String getServerTime(){
		try {
			the_time = new DefaultHttpClient();
			R_time = new HttpPost("http://chentopic.no-ip.info/server_time.php");
			HttpResponse responsePOST = new DefaultHttpClient().execute(R_time);
			HttpEntity resEntity = responsePOST.getEntity();
			Server_Time = EntityUtils.toString(resEntity);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		return Server_Time;
	}
}
