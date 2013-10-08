package net.wszf.client.assistivetouch.view;

import net.wszf.client.assistivetouch.R;
import net.wszf.client.assistivetouch.utils.MyWindowManager;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore.Action;
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.HapticFeedbackConstants;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatActionView extends LinearLayout
	{
		public static int viewWidth;
		public static int viewHeight;
		private int mCode ;
		private long mDownTime;
		private ImageView home, back, menu, recent_apps;
		public FloatActionView(final Context context)
			{
				super(context);
				LayoutInflater.from(context).inflate(R.layout.action_touch, this);
				View view = findViewById(R.id.linearLayout);
				viewWidth = view.getLayoutParams().width;
				viewHeight = view.getLayoutParams().height;
				home=(ImageView) findViewById(R.id.home);
				back=(ImageView) findViewById(R.id.back);
				menu=(ImageView) findViewById(R.id.menu);
				recent_apps=(ImageView) findViewById(R.id.recent_apps);
				home.setOnTouchListener(onTouchListener);
				back.setOnTouchListener(onTouchListener);
				menu.setOnTouchListener(onTouchListener);
				recent_apps.setOnClickListener(new OnClickListener()//打开最近运行APp列表
					{
						@Override
						public void onClick(View v)
							{
								Intent intent = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
					            intent.setClassName("com.android.systemui","com.android.systemui.recent.RecentsActivity");
					            intent.putExtra("com.android.systemui.recent.WAITING_FOR_WINDOW_ANIMATION", true);
					            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					            context.startActivity(intent);
							}
					});
				
			}

		void sendEvent(int action, int flags)
			{
				sendEvent(action, flags, SystemClock.uptimeMillis());
			}

		void sendEvent(int action, int flags, long when)
			{
				System.out.println(action);
				final int repeatCount = (flags & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
				final KeyEvent ev = new KeyEvent(mDownTime, when, action, mCode, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0, flags | KeyEvent.FLAG_FROM_SYSTEM
						| KeyEvent.FLAG_VIRTUAL_HARD_KEY, InputDevice.SOURCE_KEYBOARD);
				InputManager.getInstance().injectInputEvent(ev, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
			}
		private OnTouchListener onTouchListener=new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
					{
						mCode=Integer.valueOf(v.getTag().toString());
					        switch (event.getAction()) {
					            case MotionEvent.ACTION_DOWN:
					                //Slog.d("KeyButtonView", "press");
					                mDownTime = SystemClock.uptimeMillis();
					                setPressed(true);
					                if (mCode != 0) {
					                    sendEvent(KeyEvent.ACTION_DOWN, 0, mDownTime);
					                }
					                else{
					                	performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
					                }
					                break;
					            case MotionEvent.ACTION_MOVE:
					               
					                break;
					            case MotionEvent.ACTION_CANCEL:
					                setPressed(false);
					                if (mCode != 0) {
					                    sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
					                }
					                break;
					            case MotionEvent.ACTION_UP:
					                final boolean doIt = isPressed();
					                setPressed(false);
					                if (mCode != 0) {
					                    if (doIt) {
					                        sendEvent(KeyEvent.ACTION_UP, 0);
					                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
					                        playSoundEffect(SoundEffectConstants.CLICK);
					                    } else {
					                        sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
					                    }
					                } else {
					                    // no key code, just a regular ImageView
					                    if (doIt) {
					                        performClick();
					                    }
					                }
					                break;
					        }
						return true;
					}
			};
	}
