package com.example.curriculumtable;


import com.example.datamodle.UserData;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableFragment extends Fragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment, container,false);
		Bundle data = getArguments();
		int tag = data.getInt("tag");
		int length = UserData.shareUserDate().getClassByWeekIndex(tag).size();
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linear);
		for(int i=0; i<length; i++){
			LinearLayout lay =  new LinearLayout(getActivity());
			//创建设置闹钟的按钮
			final Button bn = new Button(getActivity());
			bn.setText("编辑闹铃");
			bn.setHeight(120);
			bn.setBackgroundColor(Color.parseColor("#00000000"));
			//创建课程信息
			TextView view = new TextView(getActivity());
			view.setText(UserData.shareUserDate().getClassByWeekIndex(tag).get(i).toString());
			view.setTextSize(18);
			LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(l);
			lay.setGravity(Gravity.CENTER_HORIZONTAL);
			lay.addView(bn);
			lay.addView(view);
			layout.addView(lay);
			final int id = UserData.shareUserDate().getClassByWeekIndex(tag).get(i).classid;
			//给按钮绑定事件
			final int tempTag = tag;
			final int tempI = i;
			bn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Bundle bundle = new Bundle();
					bundle.putInt("tag",tempTag);
					bundle.putInt("i",tempI);
					Intent intent = new Intent(getActivity(), ClockActivity.class);
					intent.putExtras(bundle);
				    startActivity(intent);
				}
					
			});
		}
		return rootView;
	}

}
