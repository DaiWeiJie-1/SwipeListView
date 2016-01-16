package com.dwj.swipeListView;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class SwipeMenuAdapter extends BaseAdapter{

	public abstract int getMenuLayoutResourceId();
	
	public View createContentView(Context context,int contentViewLayoutResourceId){
		View contentView = new SwipeMenuView(context, contentViewLayoutResourceId, getMenuLayoutResourceId());
		return contentView;
	}
}
