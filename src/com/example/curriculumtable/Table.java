package com.example.curriculumtable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Table extends FragmentActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);
		
		
		//DatabaseHelper dbHelper = new DatabaseHelper(this, "mydatabase.db3", 1);
		//Cursor cursor = dbHelper.getReadableDatabase().query(false,"class",new String[]{"className"},null,null,null,null,null,null);
		//data.putSerializable("data", converCursorToList(cursor));
		final ActionBar actionBar = getActionBar();
		//设置导航方式为：tab导航
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//一周有七天，所以我们添加七个tab页,并为tab标签添加事件监听器
		ActionBar.Tab tab1 = actionBar.newTab()  
				.setText("一") 
				.setTabListener(new TabListener<TableFragment>(  
						this, 1)); 
		actionBar.addTab(tab1);  
		ActionBar.Tab tab2 = actionBar.newTab()  
				.setText("二")  
				.setTabListener(new TabListener<TableFragment>(  
						this, 2));  
		actionBar.addTab(tab2); 
		ActionBar.Tab tab3 = actionBar.newTab()  
				.setText("三")  
				.setTabListener(new TabListener<TableFragment>(  
						this, 3)); 
		actionBar.addTab(tab3);
		ActionBar.Tab tab4 = actionBar.newTab()  
				.setText("四")  
				.setTabListener(new TabListener<TableFragment>(  
						this, 4)); 
		actionBar.addTab(tab4);
		ActionBar.Tab tab5 = actionBar.newTab()  
				.setText("五")  
				.setTabListener(new TabListener<TableFragment>(  
						this, 5)); 
		actionBar.addTab(tab5);
		ActionBar.Tab tab6 = actionBar.newTab()  
				.setText("六")  
				.setTabListener(new TabListener<TableFragment>(  
						this, 6)); 
		actionBar.addTab(tab6);
		ActionBar.Tab tab7 = actionBar.newTab()  
				.setText("七")  
				.setTabListener(new TabListener<TableFragment>(  
						this, 7)); 
		actionBar.addTab(tab7);
		/*
		tab7.select();
		tab6.select();
		tab5.select();
		tab4.select();
		tab3.select();
		tab2.select();
		tab1.select();
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, fm.findFragmentByTag("三"));
		ft.commit();
		*/
		/*
		actionBar.addTab(actionBar.newTab().setText("一").setTabListener(new ));
		actionBar.addTab(actionBar.newTab().setText("二").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("三").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("四").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("五").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("六").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("七").setTabListener(this));
		 */
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabSpec tab8 = tabHost.newTabSpec("tab8").setIndicator("课表").setContent(R.id.tab01);
		tabHost.addTab(tab8);

		TabSpec tab9 = tabHost.newTabSpec("tab9").setIndicator("聊天").setContent(R.id.tab02);
		tabHost.addTab(tab9);

	}
	public ArrayList<Map<String, String>> converCursorToList(Cursor cursor)
	{
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		while(cursor.moveToNext())
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("className",cursor.getString(2));
			map.put("classLocate", cursor.getString(3));
			map.put("classStartTime", cursor.getString(4));
			map.put("classWeek", cursor.getString(5));
			result.add(map);
		}
		return result;
	}
	
}
class TabListener<T extends Fragment> implements ActionBar.TabListener {
	private TableFragment mFragment;  
	private final FragmentActivity mActivity;  
	private final int mTag; 

	public TabListener(FragmentActivity activity, int tag) {  
		mActivity = activity;  
		mTag = tag;  
		mFragment = null;
	}  

	/* The following are each of the ActionBar.TabListener callbacks */  

	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {  
		// Check if the fragment is already initialized  
		if (mFragment == null) {  
			// If not, instantiate and add it to the activity 
			Bundle bundle = new Bundle();
			bundle.putInt("tag", mTag);
			mFragment = new TableFragment();
			mFragment.setArguments(bundle);
			ft.replace(R.id.container, mFragment,"mTag");  
		} else {
			// If it exists, simply attach it in order to show it  
			ft.attach(mFragment); 
		}  
	}  

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {  
		if (mFragment != null) {  
			// Detach the fragment, because another one is being attached  
			ft.detach(mFragment);  
		}  
	}  

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {  
		// User selected the already selected tab. Usually do nothing.  
	}  
}