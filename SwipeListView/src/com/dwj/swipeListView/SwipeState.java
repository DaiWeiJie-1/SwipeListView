package com.dwj.swipeListView;

public enum SwipeState {
	SWIPE_OPEN(1),
	SWIPE_CLOSE(2),
	SWIPE_DOING(3);
	
	private int state;
	
	private SwipeState(int state){
		this.state = state;
	}
	
	public int getState(){
		return state;
	}
	
}
