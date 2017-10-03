package com.example.topic_test;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;

public class Testgo extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,OnFullscreenListener{
	
	
	private TabHost myTabHost;
	private ViewPager myViewPager;
	private PagerAdapter myPagerAdapter;
	private HashMap<String, TabInfo> myHashMapTabInfo = new HashMap<String, Testgo.TabInfo>();
	LinearLayout linear_updown;
	
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_testgo);
		linear_updown=(LinearLayout) findViewById(R.id.linear_updown);
/*********************************************************************************/		
		this.initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			myTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		this.intialiseViewPager();

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

/**********************************************************************************/
  
	}
public void go_create_ans(View v)
{
	Intent intent=new Intent(this,Create_Ans.class);
	startActivity(intent);
	int version = Integer.valueOf(android.os.Build.VERSION.SDK);

    if(version >= 5) {
 	   overridePendingTransition(R.anim.addq_in_up, R.anim.addq_in_nono);
 	   }
}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);

	       if(version >= 5) {
	    	   overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	    	   }
	}
	private void intialiseViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Fragment1.class.getName()));
		fragments.add(Fragment.instantiate(this, Fragment2.class.getName()));
		
		this.myPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		this.myViewPager = (ViewPager) super.findViewById(R.id.viewpager);
		this.myViewPager.setAdapter(this.myPagerAdapter);
		this.myViewPager.setOnPageChangeListener(this);
	}
	
	private void initialiseTabHost(Bundle args) {
		myTabHost = (TabHost) findViewById(android.R.id.tabhost);
		myTabHost.setup();
		TabInfo tabInfo = null;

		myTabHost = (TabHost) findViewById(android.R.id.tabhost);
		myTabHost.setup();
		

		Testgo.AddTab(this, this.myTabHost, this.myTabHost.newTabSpec("面試考題").setIndicator("面試紀錄"),
				(tabInfo = new TabInfo("面試考題", Fragment1.class, args)));
		this.myHashMapTabInfo.put(tabInfo.tag, tabInfo);
		
		Testgo.AddTab(this, this.myTabHost, this.myTabHost.newTabSpec("錄影室").setIndicator("錄影室"),
				(tabInfo = new TabInfo("錄影室", Fragment2.class, args)));
		this.myHashMapTabInfo.put(tabInfo.tag, tabInfo);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); // 先取得螢幕解析度
		int screenWidth = dm.widthPixels; // 取得螢幕的寬

		TabWidget tabWidget = myTabHost.getTabWidget(); // 取得tab的物件
		int count = tabWidget.getChildCount(); // 取得tab的分頁有幾個
		if (count > 4) { // 如果超過三個就來處理滑動
			for (int i = 0; i < count; i++) {
				tabWidget.getChildTabViewAt(i).setMinimumWidth((screenWidth) / 4);// 設定每一個分頁最小的寬度
			}
		}

		for (int i = 0; i < count; i++) {
			final View view = myTabHost.getTabWidget().getChildTabViewAt(i);
			if (view != null) {
				view.getLayoutParams().height *= 0.8;

				final View textView = view.findViewById(android.R.id.title);
				if (textView instanceof TextView) {
					((TextView) textView).setGravity(Gravity.CENTER);
					((TextView) textView).setSingleLine(false);
					((TextView) textView).setTextSize(20);
					((TextView) textView).setTextColor(Color.rgb(150, 150, 150));
					textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
				}
			}
		}
		myTabHost.setOnTabChangedListener(this);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		this.myTabHost.setCurrentTab(position);
		
		
		if(position==0)	
	     {
		linear_updown.setTranslationY(linear_updown.getHeight());
		linear_updown.setVisibility(View.VISIBLE);
		if (linear_updown.getTranslationY() > 0) 
		  {
	        linear_updown.animate().translationY(0).setDuration(500);
	      }
		      
 	     }
		else{
		ViewPropertyAnimator animator = linear_updown.animate()
    	        .translationYBy(linear_updown.getHeight())
    	        .setDuration(500);
    	    runOnAnimationEnd(animator, new Runnable() {
    	      @Override
    	      public void run() {
    	        linear_updown.setVisibility(View.GONE);
    	      }
    	    });
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
	@Override
	public void onPageScrollStateChanged(int state) {
	}
	
	@Override
	public void onTabChanged(String arg0) {
		int pos = this.myTabHost.getCurrentTab();
		this.myViewPager.setCurrentItem(pos);

	}

	private static void AddTab(Testgo activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	@Override
	public void onFullscreen(boolean arg0) {
		
		
	}
}