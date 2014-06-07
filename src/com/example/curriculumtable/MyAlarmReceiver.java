package com.example.curriculumtable;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;
public class MyAlarmReceiver extends BroadcastReceiver {
	Vibrator vibrator;  
	@Override
	public void onReceive(Context context, Intent intent) {
		 Toast.makeText(context, "你设置的闹钟时间到了", Toast.LENGTH_LONG).show();
		 vibrator  = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
		 vibrator.vibrate(7000);
	}
}
