����:Ŀǰ���ڵ�����:����״̬���ڿؼ������»����ᴥ��listview����


�໬ItemListViewʵ��ԭ��:

����getView��ÿһ��convertView����һ���Զ����SwipeMenuView;���Զ���view�����contentView��menuView;contentView��ʾ��������һ��,��menuView������Ļ��;
����listView��ˮƽ����ʱ,��λĳһ��Ȼ��scrollSwpieMenuView�������໬;MotionUp��ʱ��,ͨ��velocityTracker�������Ƿ���Ҫ��ɲ໬���߻ص�menu�ر�״̬.




ע��:

1.listView��onInterceptTouchEventΪʲôֻ��Ӧ��һ��down�¼�:
��Ϊ��onTouchEvent�������¼���,��������ͨ���ò㼶��onInterceptTouchEvent


Touch�¼���
�������,ð������

onDispatch return true: �¼�ֻ��dispatch������
		   return false: �¼����ص��ϼ�onTouch
		   return super: �¼��ַ���onInterceptTouch
		   
		   
		   
onIntercepTouchEvent  return true: �¼�����onTouch���Ѳ��Ҳ��پ���onIntercept
					  return false: �¼����·ַ�����view dispatch
					  return super: �¼�����onTouch���Ѳ��Ҳ��پ���onIntercept
					  
onTouch          return true:�����¼�,���������down����Ҳ���������
				 return false:���ص�����onTouch����
				 return super:ͬfalse
				 
				 
2.�������˵���ʱ,ӦmenuӦ�����Խ��յ�touch�¼���contentView����ղ����¼�,�رմ򿪵Ĳ˵�
