package net.wszf.client.assistivetouch.view;

import java.lang.reflect.Field;

import net.wszf.client.assistivetouch.R;
import net.wszf.client.assistivetouch.R.id;
import net.wszf.client.assistivetouch.R.layout;
import net.wszf.client.assistivetouch.utils.MyWindowManager;

import android.content.Context;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatTouchView extends ImageView
	{

		public static int viewWidth;
		public static int viewHeight;
		private static int statusBarHeight;
		private WindowManager windowManager;
		private WindowManager.LayoutParams mParams;
		private float xInScreen;
		private float yInScreen;
		private float xInView;
		private float yInView;
		private float mStartX;
		private float mStartY;
		private OnClickListener onClickListener;

		public FloatTouchView(Context context)
			{
				super(context);
				windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

			}

		@Override
		public boolean onTouchEvent(MotionEvent event)
			{
				xInScreen = event.getRawX();
				yInScreen = event.getRawY() - getStatusBarHeight();
				switch (event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
						xInView = event.getX();
						yInView = event.getY();
						mStartX=xInScreen;
						mStartY=yInScreen;
						break;
					case MotionEvent.ACTION_MOVE:
						// 手指移动的时候更新小悬浮窗的位置
						updateViewPosition();
						break;
					case MotionEvent.ACTION_UP:
						if (spacing(xInScreen, yInScreen, mStartX, mStartY)<5)
							{
								if (onClickListener != null)
									{
										onClickListener.onClick(this);
									}
							}

						break;
					}
				return true;
			}

		/**
		 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
		 * 
		 * @param params
		 *            小悬浮窗的参数
		 */
		public void setParams(WindowManager.LayoutParams params)
			{
				mParams = params;
			}

		/**
		 * 更新小悬浮窗在屏幕中的位置。
		 */
		private void updateViewPosition()
			{
				mParams.x = (int) (xInScreen - xInView);
				mParams.y = (int) (yInScreen - yInView);
				windowManager.updateViewLayout(this, mParams);
			}

		/**
		 * 用于获取状态栏的高度。
		 * 
		 * @return 返回状态栏高度的像素值。
		 */
		private int getStatusBarHeight()
			{
				if (statusBarHeight == 0)
					{
						try
							{
								Class<?> c = Class.forName("com.android.internal.R$dimen");
								Object o = c.newInstance();
								Field field = c.getField("status_bar_height");
								int x = (Integer) field.get(o);
								statusBarHeight = getResources().getDimensionPixelSize(x);
							} catch (Exception e)
							{
								e.printStackTrace();
							}
					}
				return statusBarHeight;
			}

		@Override
		public void setOnClickListener(OnClickListener l)
			{
				this.onClickListener = l;
			}
		/**
		 * 计算距离
		 * @param currentX
		 * @param currentY
		 * @param startX
		 * @param startY
		 * @return
		 */
		private float spacing(float currentX,float currentY,float startX,float startY){
		    float x =currentX-startX;
		    float y =currentY-startY;
		    return FloatMath.sqrt(x * x + y * y);
		} 
	}
