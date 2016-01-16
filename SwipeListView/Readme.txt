问题:目前存在的问题:正常状态下在控件上上下滑动会触发listview滑动


侧滑ItemListView实现原理:

对于getView中每一项convertView返回一个自定义的SwipeMenuView;该自定义view添加了contentView和menuView;contentView显示于完整的一项,而menuView则在屏幕外;
当在listView上水平滑动时,定位某一项然后scrollSwpieMenuView来触发侧滑;MotionUp的时候,通过velocityTracker来计算是否需要完成侧滑或者回到menu关闭状态.




注意:

1.listView中onInterceptTouchEvent为什么只响应了一次down事件:
因为当onTouchEvent消费了事件后,并不会在通过该层级的onInterceptTouchEvent


Touch事件：
隧道传递,冒泡消费

onDispatch return true: 事件只在dispatch里消费
		   return false: 事件返回到上级onTouch
		   return super: 事件分发到onInterceptTouch
		   
		   
		   
onIntercepTouchEvent  return true: 事件交给onTouch消费并且不再经过onIntercept
					  return false: 事件向下分发给子view dispatch
					  return super: 事件交给onTouch消费并且不再经过onIntercept
					  
onTouch          return true:消费事件,如果消费了down后续也会继续传入
				 return false:返回到父的onTouch处理
				 return super:同false
				 
				 
2.当滑动菜单打开时,应menu应当可以接收到touch事件而contentView则接收不到事件,关闭打开的菜单
