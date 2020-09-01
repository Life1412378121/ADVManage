/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.advrogue;

import com.example.utils.ScreenUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 字幕区
 */
public class AutoScrollTextView extends SurfaceView implements Callback,
		Runnable {

	public volatile static AutoScrollTextView mAutoScrollTextView;

	public static AutoScrollTextView getInstance(Context context) {
		if (mAutoScrollTextView == null) {
			synchronized (AutoScrollTextView.class) {
				if (mAutoScrollTextView == null) {
					mAutoScrollTextView = new AutoScrollTextView(context, null,
							true);
				}
			}
		}
		return mAutoScrollTextView;
	}

	public static final String TAG = "AutoScrollTextView";

	public Context mContext;

	/**
	 * 是否滚动
	 */
	private boolean isMove = false;
	/**
	 * 移动方向
	 */
	private int orientation = 0;
	/**
	 * 向左移动
	 */
	public final static int MOVE_LEFT = 0;
	/**
	 * 向右移动
	 */
	public final static int MOVE_RIGHT = 1;
	/**
	 * 移动速度 ms　移动一次
	 */
	private long speed = 50;
	/**
	 * 字幕内容
	 */
	private String content = "";

	/**
	 * 字幕背景色
	 * */
	private String bgColor = "#E7E7E7";

	/**
	 * 字幕透明度　默认：60
	 */
	private int bgalpha = 00;

	/**
	 * 字体颜色 　默认：白色 (#FFFFFF)
	 */
	private String fontColor = "#FFFFFF";

	/**
	 * 字体透明度　默认：不透明(255)
	 */
	private int fontAlpha = 255;

	/**
	 * 字体大小 　默认：20
	 */
	private float fontSize = 32.0f;
	/**
	 * 容器
	 */
	private SurfaceHolder mSurfaceHolder;
	/**
	 * 线程控制
	 */
	private boolean loop = true;
	/**
	 * 内容滚动位置起始坐标
	 */
	private float x = 0f;

	/*   *//**
	 * 滚动位置坐标
	 */
	/*
	 * private float positon=0;
	 */
	/**
	 * @param context
	 *            <see>默认滚动</see>
	 */

	public Thread mThread;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 123455:
				getBackground().setAlpha(0);
				break;
			case 123456:
				getBackground().setAlpha(bgalpha);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		};
	};

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		fontSize = ScreenUtils.dpToPx(context, fontSize);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		// 设置画布背景不为黑色　继承Sureface时这样处理才能透明
		setZOrderOnTop(true);
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
		// 背景色
		setBackgroundColor(Color.parseColor(bgColor));
		// 设置透明
		getBackground().setAlpha(bgalpha);
	}

	/**
	 * @param context
	 * @param move
	 *            <see>是否滚动</see>
	 */
	public AutoScrollTextView(Context context, AttributeSet attrs, boolean move) {
		this(context, attrs);
		mContext = context;
		this.isMove = move;
		setLoop(isMove());
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Log.e(TAG, "surfaceChanged");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// Log.e(TAG, "surfaceCreated");
		Log.d("WIDTH:", "" + getWidth());
		Log.i(TAG, "surfaceCreated-----");
		if (isMove) {// 滚动效果
			if (orientation == MOVE_LEFT) {
				x = getWidth();
			} else {
				x = -(content.length() * 10);
			}
			mThread = new Thread(this);
			mThread.start();
		} else {// 不滚动只画一次
			draw();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// loop = false;
		Log.e(TAG, "surfaceDestroyed");
	}

	/**
	 * 画图
	 */
	private void draw() {
		try {
			if (mSurfaceHolder == null) {
				return;
			}
			// 锁定画布
			Canvas canvas = mSurfaceHolder.lockCanvas();

			Paint paint = new Paint();
			// 清屏
			canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
			// 锯齿
			paint.setAntiAlias(true);
			// 字体
			paint.setTypeface(Typeface.SANS_SERIF);
			// 字体大小
			paint.setTextSize(fontSize);
			// 字体颜色
			paint.setColor(Color.parseColor(fontColor));
			// 字体透明度
			paint.setAlpha(fontAlpha);

			// 画文字
			canvas.drawText(content, x,
					(getHeight() / 2 + ScreenUtils.dpToPx(getContext(), 10)),
					paint);
			// 解锁显示
			mSurfaceHolder.unlockCanvasAndPost(canvas);
			// 滚动效果
			if (isMove) {
				// 内容所占像素
				float conlen = paint.measureText(content);
				// 组件宽度
				int w = getWidth();
				// 方向
				if (orientation == MOVE_LEFT) {// 向左
					if (x < -conlen) {
						isFirstScroll = false;
						x = w;
					} else {
						x -= 2;
					}
				} else if (orientation == MOVE_RIGHT) {// 向右
					if (x >= w) {
						x = -conlen;
					} else {
						x += 2;
					}
				}
			}
		} catch (IllegalStateException e) {
			// TODO: handle exception
		} catch (Exception e) {

		}
	}

	public boolean isFirstScroll = true;

	public void run() {
		while (loop) {
			synchronized (mSurfaceHolder) {
				if (x == getWidth() && !isFirstScroll) {
					try {
						mHandler.removeMessages(123455);
						mHandler.sendEmptyMessage(123455);
						Thread.sleep(interval * 1000);
					} catch (InterruptedException ex) {
						Log.e("TextSurfaceView", ex.getMessage() + "\n" + ex);
					}
				}
				mHandler.removeMessages(123456);
				mHandler.sendEmptyMessage(123456);
				draw();
				try {
					Thread.sleep(speed);
				} catch (InterruptedException ex) {
					Log.e("TextSurfaceView", ex.getMessage() + "\n" + ex);
				}
			}

		}
		content = null;
	}

	/****************************** set get method ***********************************/

	/*
	 * @Override protected Parcelable onSaveInstanceState() { // TODO
	 * Auto-generated method stub Parcelable superState =
	 * super.onSaveInstanceState(); SavedState savedState= new
	 * SavedState(superState); savedState.speed = speed; return savedState; }
	 * 
	 * @Override protected void onRestoreInstanceState(Parcelable state) { //
	 * TODO Auto-generated method stub if (!(state instanceof SavedState)) {
	 * super.onRestoreInstanceState(state); return; } SavedState savedState =
	 * (SavedState) state;
	 * super.onRestoreInstanceState(savedState.getSuperState()); speed=
	 * savedState.speed; }
	 * 
	 * public static class SavedState extends BaseSavedState { public long
	 * speed= 0L;
	 * 
	 * SavedState(Parcelable superState) { super(superState); }
	 * 
	 * @Override public void writeToParcel(Parcel out, int flags) {
	 * super.writeToParcel(out, flags); out.writeFloat(speed); }
	 * 
	 * public static final Parcelable.Creator<SavedState> CREATOR = new
	 * Parcelable.Creator<SavedState>() {
	 * 
	 * public SavedState[] newArray(int size) { return new SavedState[size]; }
	 * 
	 * @Override public SavedState createFromParcel(Parcel in) { return new
	 * SavedState(in); } };
	 * 
	 * private SavedState(Parcel in) { super(in); speed= in.readLong(); } }
	 */

	private int getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation
	 *            <li>可以选择类静态变量</li> <li>1.MOVE_RIGHT 向右 (默认)</li> <li>
	 *            2.MOVE_LEFT 向左</li>
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	private long getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            <li>速度以毫秒计算两次移动之间的时间间隔</li>
	 */
	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public boolean isMove() {
		return isMove;
	}

	/**
	 * @param isMove
	 *            <see>默认滚动</see>
	 */
	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public void setBgalpha(int bgalpha) {
		this.bgalpha = bgalpha;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public void setFontAlpha(int fontAlpha) {
		this.fontAlpha = fontAlpha;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = ScreenUtils.dpToPx(mContext, fontSize);
	}

	public long interval = 0;

	public void setInterval(long interval) {
		this.interval = interval;
	}
}