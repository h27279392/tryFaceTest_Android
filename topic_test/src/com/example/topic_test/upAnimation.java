package com.example.topic_test;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class upAnimation extends Animation {

	private View mAnimationView = null,list_item=null;
	ListView mlist=null;
	private LayoutParams mViewLayoutParams = null;
	private int mStart = 0;
	private int mEnd = 0;
	int who_call_me,item_x,item_pos;
	
	int back;
	public upAnimation(View view){
		animationSettings(view, 500);
		
	}

	public upAnimation(View view, int duration,int who){
		who_call_me=who;
		animationSettings(view, duration);
		
	}
	public upAnimation(ListView list,View view,int pos, int duration,int who, int back_item){
		
		
		item_x=view.getTop();
		back=back_item;
		who_call_me=who;
		item_pos=pos;
		list_animationSettings(list,view,duration);
	}
	private void animationSettings(View view, int duration){
		setDuration(duration);
		
		mAnimationView = view;
		mViewLayoutParams = (LayoutParams) view.getLayoutParams();
		
		mStart = mViewLayoutParams.bottomMargin;
		
		mEnd = (mStart == 0 ? (0-view.getHeight()) : 0);
		
		
		view.setVisibility(View.VISIBLE);
		
		}
	private void list_animationSettings(ListView list,View view, int duration){
		setDuration(duration);
		list_item = view;
		mlist=(ListView)list;
			mStart = list_item.getTop();
			mEnd = (mStart==0 ? back:0);	
			
		//Log.e("s;e", mStart+","+mEnd);
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
	if(who_call_me==0)
	{
		if(interpolatedTime < 1.0f){
			mViewLayoutParams.bottomMargin = mStart+ (int) ((mEnd - mStart) * interpolatedTime);
			// invalidate
			
			mAnimationView.requestLayout();
		}else{
			mViewLayoutParams.bottomMargin = mEnd;
			mAnimationView.requestLayout();
			if(mEnd != 0){
				mAnimationView.setVisibility(View.GONE);
			}
		}
	}
	
	if(who_call_me==1)
	{
		if(interpolatedTime < 1.0f){
			
			mlist.setSelectionFromTop(item_pos,(int) (mStart-(mStart-mEnd)*interpolatedTime));
		//	Log.e("d list",""+(int) (mStart-(mStart-mEnd)*interpolatedTime));
		}else
			mlist.setSelectionFromTop(item_pos,mEnd);
		}
	}

}
