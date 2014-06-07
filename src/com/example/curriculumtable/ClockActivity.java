package com.example.curriculumtable;

import java.util.Calendar;

import com.example.dataBase.DBManager;
import com.example.datamodle.UserData;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ClockActivity extends Activity
{
	AlarmManager am;
	Calendar calendar = Calendar.getInstance();
	TextView tempView;	
	public final int h = 1;
	public final int m = 1;
	private int classid;
	private int tag;
	private int i;
	DBManager dbManager;
	@Override
	protected 
	void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);
		tempView = (TextView) new TextView(this);
		dbManager = new DBManager(this);
		/*获取闹钟管理的实例 */
		am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		tag = b.getInt("tag");
		i = b.getInt("i");
		if(UserData.shareUserDate().getClassByWeekIndex(tag).get(i).isSetClock){
			TextView view = (TextView) new TextView(ClockActivity.this);
			view.setText(UserData.shareUserDate().getClassByWeekIndex(tag).get(i).clockHour+":"+UserData.shareUserDate().getClassByWeekIndex(tag).get(i).clockMinute);
			TableRow tablerow = (TableRow) new TableRow(ClockActivity.this);
			Button button = (Button) new Button(ClockActivity.this);
			button.setText("删除");
			button.setTextSize(18);
			view.setTextSize(18);
			tablerow.addView(button);
			tablerow.addView(view);
			TableLayout layout = (TableLayout) findViewById(R.id.clock);
			layout.addView(tablerow);
		}
	}

	public void addClock(View v)
	{

		calendar = Calendar.getInstance();
		Calendar c= Calendar.getInstance();

		new TimePickerDialog(ClockActivity.this,0,
				new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker tp,
					int hourOfDay, int minute) {
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				/* 建立Intent和PendingIntent，来调用目标组件 */
				Intent intent = new Intent(ClockActivity.this,MyAlarmReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(ClockActivity.this, UserData.shareUserDate().getClassByWeekIndex(tag).get(i).classid,intent, 0);
				AlarmManager am;
				/* 获取闹钟管理的实例 */
				am = (AlarmManager) getSystemService(ALARM_SERVICE);

				/* 设置闹钟 */
				am.set(AlarmManager.RTC_WAKEUP, calendar
						.getTimeInMillis(), pendingIntent);
				/* 设置周期闹 */

				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ (10 * 1000), (24 * 60 * 60 * 1000),pendingIntent);
				UserData.shareUserDate().getClassByWeekIndex(tag).get(i).clockHour = hourOfDay;
				UserData.shareUserDate().getClassByWeekIndex(tag).get(i).clockMinute = minute;
				
				Toast.makeText(ClockActivity.this,hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
			}
		}
		, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();

	}

}
