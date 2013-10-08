package net.wszf.client.assistivetouch.utils;

import net.wszf.client.assistivetouch.R;
import net.wszf.client.assistivetouch.view.FloatActionView;
import net.wszf.client.assistivetouch.view.FloatTouchView;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MyWindowManager
	{

		private static FloatTouchView touchView;
		private static FloatActionView actionView;
		private static LayoutParams touchLayoutParams;
		private static LayoutParams actionLayoutParams;
		private static WindowManager mWindowManager;
		/**
		 * 创建touch view
		 * @param context
		 */
		public static void createTouchWindow(final Context context)
			{
				WindowManager windowManager = getWindowManager(context);
				int screenWidth = windowManager.getDefaultDisplay().getWidth();
				int screenHeight = windowManager.getDefaultDisplay().getHeight();
				if (touchView == null)
					{
						touchView = new FloatTouchView(context);
						touchView.setImageResource(R.drawable.ic_floater_iphone_hl);
						if (touchLayoutParams == null)
							{
								touchLayoutParams = new LayoutParams();
								touchLayoutParams.type = LayoutParams.TYPE_PHONE;
								touchLayoutParams.format = PixelFormat.RGBA_8888;
								touchLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
								touchLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
								// smallWindowParams.width = smallWindow.getWidth();
								// smallWindowParams.height = smallWindow.getHeight();
								touchLayoutParams.width = 100;
								touchLayoutParams.height = 100;
								touchLayoutParams.x = screenWidth;
								touchLayoutParams.y = screenHeight / 2;
							}
						touchView.setParams(touchLayoutParams);
						touchView.setOnClickListener(new OnClickListener()
							{
								@Override
								public void onClick(View v)
									{
										touchView.setClickable(false);
										removeTouchWindow(context);
										createActionWindow(context);
									}
							});
						windowManager.addView(touchView, touchLayoutParams);
					}
			}

		/**
		 * 移除touch view
		 * @param context
		 */
		public static void removeTouchWindow(Context context)
			{
				if (touchView != null)
					{
						WindowManager windowManager = getWindowManager(context);
						windowManager.removeView(touchView);
						touchView = null;
					}
			}

		/**
		 * 创建action view
		 * @param context
		 */
		public static void createActionWindow(final Context context)
			{
				WindowManager windowManager = getWindowManager(context);
				int screenWidth = windowManager.getDefaultDisplay().getWidth();
				int screenHeight = windowManager.getDefaultDisplay().getHeight();
				if (actionView == null)
					{
						actionView = new FloatActionView(context);
						if (actionLayoutParams == null)
							{
								actionLayoutParams = new LayoutParams();
								actionLayoutParams.x=screenWidth/6;
//								actionLayoutParams.x = screenWidth / 2 - FloatActionView.viewWidth / 2;
								actionLayoutParams.y = screenHeight / 2 - FloatActionView.viewHeight / 2;
								actionLayoutParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
								actionLayoutParams.format = PixelFormat.RGBA_8888;
								actionLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
								actionLayoutParams.width = FloatActionView.viewWidth;
								actionLayoutParams.height = FloatActionView.viewHeight;
								actionLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
								actionLayoutParams.flags = actionLayoutParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
								actionLayoutParams.flags = actionLayoutParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
							}
						actionView.setOnTouchListener(new OnTouchListener()
							{
								@Override
								public boolean onTouch(View v, MotionEvent event)
									{
										// TODO Auto-generated method stub
										if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
											{
												MyWindowManager.removeActionWindow(context);
												MyWindowManager.createTouchWindow(context);
											}
										return false;
									}
							});
						windowManager.addView(actionView, actionLayoutParams);
					}

			}

		public static void removeActionWindow(Context context)
			{
				if (actionView != null)
					{
						WindowManager windowManager = getWindowManager(context);
						windowManager.removeView(actionView);
						actionView = null;
					}
			}

		private static WindowManager getWindowManager(Context context)
			{
				if (mWindowManager == null)
					{
						mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
					}
				return mWindowManager;
			}

	}
