package com.example.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwj.swipeListView.SwipeMenuAdapter;
import com.dwj.swipeListView.SwipeMenuListView;
import com.example.slidelistview.R;

public class MainActivity extends Activity {

	private List<String> mDatas = new ArrayList<String>();
	private SwipeMenuListView mSwipeMenuListView;
	private MySwipeMenuAdapter mSwipeMenuAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipe_list_view);
        initDatas();
        mSwipeMenuAdapter = new MySwipeMenuAdapter();
        mSwipeMenuListView.setAdapter(mSwipeMenuAdapter);
        mSwipeMenuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("click","onItemClick");
			}
		});
    }
    
    private void initDatas(){
    	mDatas.add("第1行");
    	mDatas.add("第2行");
    	mDatas.add("第3行");
    	mDatas.add("第4行");
    	mDatas.add("第5行");
    	mDatas.add("第6行");
    	mDatas.add("第7行");
    	mDatas.add("第8行");
    	mDatas.add("第9行");
    	mDatas.add("第1行");
    	mDatas.add("第2行");
    	mDatas.add("第3行");
    	mDatas.add("第4行");
    	mDatas.add("第5行");
    	mDatas.add("第6行");
    	mDatas.add("第7行");
    	mDatas.add("第8行");
    	mDatas.add("第9行");
    	mDatas.add("第1行");
    	mDatas.add("第2行");
    	mDatas.add("第3行");
    	mDatas.add("第4行");
    	mDatas.add("第5行");
    	mDatas.add("第6行");
    	mDatas.add("第7行");
    	mDatas.add("第8行");
    	mDatas.add("第9行");
    	mDatas.add("第1行");
    	mDatas.add("第2行");
    	mDatas.add("第3行");
    	mDatas.add("第4行");
    	mDatas.add("第5行");
    	mDatas.add("第6行");
    	mDatas.add("第7行");
    	mDatas.add("第8行");
    	mDatas.add("第9行");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class MySwipeMenuAdapter extends SwipeMenuAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = createContentView(MainActivity.this,R.layout.item_view);
				holder.mContentTv = (TextView) convertView.findViewById(R.id.content_tv);
				holder.mDelTextView = (Button) convertView.findViewById(R.id.del_btn);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.mContentTv.setText(mDatas.get(position));
			holder.mDelTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("click","mDelTextView click");
//					mSwipeMenuListView.closeOpenedMenu();
				}
			});
			
			holder.mContentTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("click","mContentTv click");
				}
			});
			return convertView;
		}

		@Override
		public int getMenuLayoutResourceId() {
			return R.layout.menu_view;
		}
    	
    }
    
    private final class ViewHolder{
    	TextView mContentTv;
    	Button mDelTextView;
    }

    
}
