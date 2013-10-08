package net.wszf.client.assistivetouch.services;

import net.wszf.client.assistivetouch.utils.MyWindowManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TouchService extends Service
	{

		@Override
		public IBinder onBind(Intent intent)
			{
				return null;
			}

		@Override
		public void onCreate()
			{
				MyWindowManager.createTouchWindow(getApplicationContext());
				super.onCreate();
			}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId)
			{
				return START_STICKY;//被K自动重启服务
			}

		@Override
		public void onDestroy()
			{
				super.onDestroy();
			}
	}
