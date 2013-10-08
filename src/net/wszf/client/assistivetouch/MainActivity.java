package net.wszf.client.assistivetouch;

import net.wszf.client.assistivetouch.services.TouchService;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity
	{
		private Button start,end,test;
		private ContentResolver resolver;

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				resolver = getContentResolver();
				setContentView(R.layout.activity_main);
				start=(Button) findViewById(R.id.start);
				end=(Button) findViewById(R.id.end);
				test=(Button) findViewById(R.id.test);
				test.setOnClickListener(new OnClickListener()
					{
						
						@Override
						public void onClick(View v)
							{
								Intent intent = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
					            intent.setClassName("com.android.systemui",
					                    "com.android.systemui.recent.RecentsActivity");
					            intent.putExtra("com.android.systemui.recent.WAITING_FOR_WINDOW_ANIMATION", true);
					            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					            startActivity(intent);
							}
					});
				start.setOnClickListener(onClickListener);
				end.setOnClickListener(onClickListener);
			}
		private OnClickListener onClickListener=new OnClickListener()
			{
				@Override
				public void onClick(View v)
					{
						if(v.getId()==start.getId())
							{
								startService(new Intent(MainActivity.this,TouchService.class));
								test(true);
							}
						else{
							stopService(new Intent(MainActivity.this,TouchService.class));
							test(false);
						}
						
					}
			};
		@Override
		public boolean onCreateOptionsMenu(Menu menu)
			{
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.main, menu);
				return true;
			}
		public void test(boolean isChecked)
			{
				boolean mDisabled = Settings.System.getInt(resolver,
		                Settings.System.EXPANDED_DESKTOP_STATE, 0) == 0;
				 boolean mStyleOff = Settings.System.getInt(resolver,
			                Settings.System.EXPANDED_DESKTOP_STYLE, 0) == 0;
			        boolean mPowerMenuOff = Settings.System.getInt(resolver,
			                Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 0) == 0;
			        if (mDisabled) {
			            if (mStyleOff) {
			                Settings.System.putInt(resolver,
			                    Settings.System.EXPANDED_DESKTOP_STYLE, 1);//Expanded Desktop Style default set to 2
			            }
			        }
				if(isChecked && mPowerMenuOff)
			        Settings.System.putInt(resolver,
			            Settings.System.POWER_MENU_EXPANDED_DESKTOP_ENABLED, 1);
			        Settings.System.putInt(resolver,
			            Settings.System.EXPANDED_DESKTOP_STATE, isChecked ? 1 : 0);
			}
	}
