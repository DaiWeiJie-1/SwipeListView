package com.dwj.swipeListView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ListView;

import com.dwj.swipeListView.SwipeMenuView.onSwipeStateChangedListener;

public class SwipeMenuListView extends ListView implements onSwipeStateChangedListener{

	private final static int SCROLL_IDEL = 0;
	private final static int SCROLL_LIST_VIEW = 1;
	private final static int SCROLL_MENU = 2;
	private final static int DEFAULT_VELOCITY_COLLECTION_NUM = 3;
	
	private int mPointDownX = 0;
	private int mPointDownY = 0;
	private int mFinglerX = 0;
	private int mPointDownPosition = ListView.INVALID_POSITION;
	private int mState = SCROLL_IDEL;
	private VelocityTracker mVelocityTracker;
	private int mVelocityCollectionNum = 0;
	private SwipeMenuView mCurrentSwipeMenuView;
	private boolean isReceiveOnTouchDown = false;
	
	public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SwipeMenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SwipeMenuListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private void initVelocityTrackerIfNotExist(){
		if(mVelocityTracker == null){
			mVelocityTracker = VelocityTracker.obtain();
		}else{
			mVelocityTracker.clear();
		}
		mVelocityCollectionNum = 0;
	}
	
	private void unInitVelocityTracker(){
		if(mVelocityTracker != null){
			mVelocityTracker.clear();
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
		mVelocityCollectionNum = 0;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d("listview", "dispatchTouchEvent,ACTION_DOWN");
			if(mCurrentSwipeMenuView != null && (mCurrentSwipeMenuView.getSwipeState() == SwipeState.SWIPE_DOING || mCurrentSwipeMenuView.getSwipeState() == SwipeState.SWIPE_OPEN)){
				
				View menuView = mCurrentSwipeMenuView.getMenuView();
				if(menuView != null){
					Rect menuRect = new Rect();
					menuView.getHitRect(menuRect);
					
					//��ΪmenuView��õ�rect����Ը����ֵ�Ҳ����SwipeMenu��,��MotionEvent�����������listview��,����Ҫ����swipeMenu��λ��
					menuRect.left = menuRect.left + mCurrentSwipeMenuView.getLeft() - menuView.getWidth();
					menuRect.right = menuRect.right + mCurrentSwipeMenuView.getLeft() - menuView.getWidth();
					menuRect.top = menuRect.top + mCurrentSwipeMenuView.getTop();
					menuRect.bottom = menuRect.bottom + mCurrentSwipeMenuView.getTop();
					if(menuRect.contains((int)ev.getX(),(int)ev.getY())){
						return  super.dispatchTouchEvent(ev);
					}
				}
				
				mCurrentSwipeMenuView.closeSwipeView();
				setState(SCROLL_IDEL);
				mVelocityCollectionNum = 0;
				return true;
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			Log.d("listview", "dispatchTouchEvent,ACTION_MOVE");
			break;
			
		case MotionEvent.ACTION_UP:
			Log.d("listview", "dispatchTouchEvent,ACTION_UP");
			break;
			
		case MotionEvent.ACTION_CANCEL:
			Log.d("listview", "dispatchTouchEvent,ACTION_CANCEL");
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d("listview", "onInterceptTouchEvent:down");
			initVelocityTrackerIfNotExist();
			if(mState == SCROLL_IDEL){
				mPointDownX = (int) ev.getX();
				mPointDownY = (int) ev.getY();
				mFinglerX = mPointDownX;
				mPointDownPosition = pointToPosition(mPointDownX, mPointDownY);
				if(mPointDownPosition != INVALID_POSITION){
					mVelocityTracker.addMovement(ev);
					mCurrentSwipeMenuView = (SwipeMenuView) getChildAt(mPointDownPosition - getFirstVisiblePosition());
					mCurrentSwipeMenuView.setOnSwipeStateChangedListener(this);
				}
				
			}
			

			break;
			
		case MotionEvent.ACTION_MOVE:
			Log.d("listview", "onInterceptTouchEvent:move");
			break;
			
		case MotionEvent.ACTION_UP:
			Log.d("listview", "onInterceptTouchEvent:up");
			break;
			
		case MotionEvent.ACTION_CANCEL:
			Log.d("listview", "onInterceptTouchEvent:cancel");
			break;
		default:
			break;
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("listview", "onTouchEvent:" + ev.getAction());
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isReceiveOnTouchDown = true;
			Log.d("listview", "onTouchEvent:down");
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			Log.d("listview", "onTouchEvent:move");
			if(isReceiveOnTouchDown){
				if(mPointDownPosition != INVALID_POSITION && mState != SCROLL_LIST_VIEW ){
					
					//�ռ�2��Move����ȷ�ļ�����˶�����
					if(mVelocityCollectionNum < DEFAULT_VELOCITY_COLLECTION_NUM){
						mVelocityTracker.addMovement(ev);
						mVelocityCollectionNum++;
						return true;
					}
					if(mState == SCROLL_IDEL){
						mVelocityTracker.computeCurrentVelocity(1000);
						
						Log.d("velocity", "xveloc = " + mVelocityTracker.getXVelocity() + "; yveloc = " +mVelocityTracker.getYVelocity());
						if(Math.abs(mVelocityTracker.getXVelocity()) < Math.abs(mVelocityTracker.getYVelocity())){
							mVelocityTracker.clear();
							setState(SCROLL_LIST_VIEW);
							mCurrentSwipeMenuView = null;
							break;
						}else{
							setState(SCROLL_MENU);
						}
					}
					
					int currentX = (int) ev.getX();
					if(mCurrentSwipeMenuView != null){
						mCurrentSwipeMenuView.swipeViewByDiffX(mFinglerX - currentX);
						mFinglerX = currentX;
						return true;
					}
					
				}
			}else{
				setState(SCROLL_LIST_VIEW);
			}
		
			break;
			
		case MotionEvent.ACTION_UP:
			Log.d("listview", "onTouchEvent:up");
			if(isReceiveOnTouchDown){
				isReceiveOnTouchDown = false;
				if(mPointDownPosition != INVALID_POSITION && mState == SCROLL_MENU){
					
					if(mCurrentSwipeMenuView.getSwipeState() == SwipeState.SWIPE_CLOSE){
						setState(SCROLL_IDEL);
					}else{
						mVelocityTracker.computeCurrentVelocity(300);
						mCurrentSwipeMenuView.controlSwipeViewFinalStateByVelocity(mVelocityTracker);
					}
					unInitVelocityTracker();
					return true;
				}else{
					unInitVelocityTracker();
				}
				setState(SCROLL_IDEL);
			}else{
				setState(SCROLL_IDEL);
				return false;
			}
			break;
			
		case MotionEvent.ACTION_CANCEL:
			Log.d("listview", "onTouchEvent:cancel");
			setState(SCROLL_IDEL);
			mVelocityCollectionNum = 0;
			unInitVelocityTracker();
			isReceiveOnTouchDown = false;
			return true;
			
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	private void setState(int scrollState){
		this.mState = scrollState;
		Log.d("state", "" + scrollState);
	}
	
	public void closeOpenedMenu(){
		if(mCurrentSwipeMenuView != null && mState == SCROLL_MENU){
			if(mCurrentSwipeMenuView.getSwipeState() != SwipeState.SWIPE_CLOSE){
				mCurrentSwipeMenuView.closeSwipeView();
			}
		}
	}

	@Override
	public void onSwipeStateChanged(SwipeState oldState,SwipeState newState) {
		switch (newState) {
		case SWIPE_CLOSE:
			if(!isReceiveOnTouchDown){
				Log.d("state", "onSwipeChange");
				setState(SCROLL_IDEL);
			}
			break;
		case SWIPE_OPEN:
		case SWIPE_DOING:
			if(!isReceiveOnTouchDown){
				Log.d("state", "onSwipeChange");
				setState(SCROLL_MENU);
			}
			break;

		default:
			break;
		}
	}
	
	
}
