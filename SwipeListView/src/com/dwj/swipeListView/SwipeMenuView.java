package com.dwj.swipeListView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class SwipeMenuView extends RelativeLayout{
	
	private final static int CONTENT_VIEW_ID = 1001;
	
	private Context mContext;
	private View mContentView;
	private View mMenuView;
	private SwipeState mSwipeState = SwipeState.SWIPE_CLOSE;
	private Scroller mScroller;
	
	//�ٶȼ�����
	private VelocityTracker mVelocityTracker;
	
	private onSwipeStateChangedListener mSwipeStateChangedListener;

	public SwipeMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SwipeMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SwipeMenuView(Context context) {
		super(context);
		mContext = context;
	}
	
	public SwipeMenuView(Context context,int contentViewResourceId,int menuViewResourcId){
		this(context);
		//because menuview may under contentView
		
		setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		addMenuView(menuViewResourcId);
		addContentView(contentViewResourceId);
		initScroller();
	}
	
	
	public interface onSwipeStateChangedListener{
		public void onSwipeStateChanged(SwipeState oldState,SwipeState newState);
	}
	
	public void setOnSwipeStateChangedListener(onSwipeStateChangedListener stateChangedListener){
		mSwipeStateChangedListener = stateChangedListener;
	}
	
	private void initScroller(){
		mScroller = new Scroller(mContext,new LinearInterpolator());
	}

	private void addContentView(int contentViewResourceId) {
		mContentView = LayoutInflater.from(mContext).inflate(contentViewResourceId, null);
		if(mContentView == null){
			throw new NullPointerException("ContentView can't be null!");
		}
		
		RelativeLayout.LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		if(lp == null){
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
		
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		mContentView.setId(CONTENT_VIEW_ID);
		addView(mContentView, lp);
	}
	
	private void addMenuView(int menuViewResourceId){
		mMenuView = LayoutInflater.from(mContext).inflate(menuViewResourceId, null);
		if(mMenuView == null){
			return;
		}
		
		RelativeLayout.LayoutParams lp = (LayoutParams) mMenuView.getLayoutParams();
		if(lp == null){
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}
		
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		
		lp.addRule(RelativeLayout.RIGHT_OF,CONTENT_VIEW_ID);
		
		addView(mMenuView, lp);
	}
	
	public View getContentView(){
		return mContentView;
	}
	
	public View getMenuView(){
		return mMenuView;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(mMenuView != null){
			//����menuview�Ѿ�����Ļ��,������Ҫlayout
			mMenuView.layout(mContentView.getRight(), mMenuView.getTop(), mMenuView.getMeasuredWidth() + mContentView.getRight(), mMenuView.getBottom());
		}
	
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int parentWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
		int parentHeightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
		if (mMenuView != null) {
			LayoutParams params = (LayoutParams) mMenuView.getLayoutParams();
			int widthSpec = ViewGroup.getChildMeasureSpec(parentWidthSpec, getPaddingLeft() + getPaddingRight() + params.leftMargin
					+ params.rightMargin, params.width);
			int heightSpec = ViewGroup.getChildMeasureSpec(parentHeightSpec, getPaddingTop() + getPaddingBottom() + params.topMargin
					+ params.bottomMargin, params.height);
			//���Ӹ÷���menuview�еĿؼ�����ʾ
			mMenuView.measure(widthSpec, heightSpec);
		}
	}
	
	public void swipeViewByDiffX(int diffX){
		
		int scrollX = 0;
		int currentScrollX = getScrollX();
		//���ܴ���menu�Ŀ�ʱ,����С��0
		if(currentScrollX + diffX > getMaxLimitScrollX()){
			scrollX = getMaxLimitScrollX() - currentScrollX;
		}else if(currentScrollX + diffX < getMinLimitScrollX()){
			scrollX = getMinLimitScrollX() - currentScrollX;
		}else{
			scrollX = diffX;
		}
		
		scrollBy(scrollX, 0);
	}
	
	
	public SwipeState getSwipeState(){
		return mSwipeState;
	}
	
	/**
	 * ���ɻ�����Xֵ
	 * @return
	 */
	private int getMaxLimitScrollX(){
		return mMenuView.getWidth();
	}
	
	/**
	 * ��С�ɻ�����Xֵ
	 * @return
	 */
	private int getMinLimitScrollX(){
		return 0;
	}
	
	public void closeSwipeView(){
		if(getScrollX() != getMinLimitScrollX() && mSwipeState != SwipeState.SWIPE_CLOSE){
			mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), 0, 250);
			invalidate();
		}
	}
	
	//�����ٶ�������view������״̬
	public void controlSwipeViewFinalStateByVelocity(VelocityTracker velocityTracker){
		int currentScrollX = getScrollX();
		int velocityX = (int) velocityTracker.getXVelocity();
		//��velocityXΪ����ʱ,scrollX��Ҫ����,����ȡ-velocityX
		
		//���������ٶ����յ����λ�õ��ǲ�ȥִ��
		mScroller.fling(currentScrollX,getScrollY(), -velocityX, 0, getMinLimitScrollX(), getMaxLimitScrollX(), 0,0);
		Log.d("computeScroll","velocityX = " + (-velocityX));
		int finalX = mScroller.getFinalX();
		mScroller.forceFinished(true);
		
		//��������λ�����ж�swipeMenu������״̬
		if(finalX > (getMaxLimitScrollX() * 2 / 3)){
			mScroller.startScroll(currentScrollX, getScrollY(), getMaxLimitScrollX() - currentScrollX, 0, 250);
		}else{
			mScroller.startScroll(currentScrollX, getScrollY(), -currentScrollX, 0, 250);
		}
		invalidate();
	}
	
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		SwipeState oldState = mSwipeState;
		
		if(l == getMinLimitScrollX()){
			if(mSwipeState != SwipeState.SWIPE_CLOSE){
				mSwipeState = SwipeState.SWIPE_CLOSE;
				mSwipeStateChangedListener.onSwipeStateChanged(oldState,mSwipeState);
			}
		}else if(l == getMaxLimitScrollX()){
			if(mSwipeState != SwipeState.SWIPE_OPEN){
				mSwipeState = SwipeState.SWIPE_OPEN;
				mSwipeStateChangedListener.onSwipeStateChanged(oldState,mSwipeState);
			}
		}else{
			
			if(mSwipeState != SwipeState.SWIPE_DOING){
				mSwipeState = SwipeState.SWIPE_DOING;
				mSwipeStateChangedListener.onSwipeStateChanged(oldState,mSwipeState);
			}
		}
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			Log.d("computeScroll","currentX = " + mScroller.getCurrX() + " ; currentY = " + mScroller.getCurrY());
			postInvalidate();
		}
		
	}
	
}
