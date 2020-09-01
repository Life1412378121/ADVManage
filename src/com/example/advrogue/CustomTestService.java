package com.example.advrogue;

import java.util.List;

import com.example.utils.OkhttpUtils;

import okhttp3.internal.http.OkHeaders;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CustomTestService extends Service {

	public static final int NOTIFICATION_ID = 1234;

	private static final String TAG = "CustomTestService";

	int position = 0;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		if (Build.VERSION.SDK_INT < 18) {
			// 18以前空通知栏即可
			startForeground(NOTIFICATION_ID, new Notification());
		} else {
			Intent innerIntent = new Intent(this, CustomTestInnerService.class);
			startService(innerIntent);
			startForeground(NOTIFICATION_ID, new Notification());
		}

		final WindowManager mWindowManager = (WindowManager) getApplication()
				.getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams param = new WindowManager.LayoutParams();
		// 设置LayoutParams(全局变量）相关参�?
		param = new LayoutParams();
		param.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 系统提示类型,重要TYPE_SYSTEM_ERROR
		param.format = PixelFormat.RGBA_8888;

		param.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_FULLSCREEN
				| LayoutParams.FLAG_LAYOUT_IN_SCREEN;

		param.gravity = Gravity.CENTER;
		param.x = LayoutParams.MATCH_PARENT;
		param.y = LayoutParams.MATCH_PARENT;

		// 设置悬浮窗口长宽数据
		param.width = LayoutParams.MATCH_PARENT;
		param.height = LayoutParams.MATCH_PARENT;

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;

		LinearLayout view = new LinearLayout(getApplication());
		view.setLayoutParams(lp);// 设置布局参数
		// view.setBackgroundColor(Color.parseColor("#000000"));
		view.setGravity(Gravity.CENTER);
		view.setLayoutParams(lp);

		final ImageView i = new ImageView(getApplication());
		i.setBackgroundResource(R.drawable.img_1);
		view.addView(i);
		i.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				mWindowManager.removeView(i);
				return false;
			}
		});

		i.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				position++;
				if (position == 1) {
					i.setBackgroundResource(R.drawable.img_1);
				} else if (position == 2) {
					i.setBackgroundResource(R.drawable.img_2);
				} else if (position == 3) {
					i.setBackgroundResource(R.drawable.img_3);
					position = 0;
				}
				Toast.makeText(getApplication(), "你再点一个试试", Toast.LENGTH_SHORT)
						.show();
			}
		});
		mWindowManager.addView(view, param);

		OkhttpUtils.FUNC_TASK.execute(new Runnable() {

			@SuppressLint("NewApi")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					Log.i(TAG, "2----------");
					if (!Settings.canDrawOverlays(getApplication())) {
						Log.i(TAG, "1----------");

						if (!isActivityForeground(getBaseContext(),
								"com.android.settings.Settings$AppDrawOverlaySettingsActivity")) {
							Log.i(TAG, "3----------");
							Intent dialogIntent = new Intent(getBaseContext(),
									MainActivity.class);
							dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP); // 必须添加
							getApplication().startActivity(dialogIntent);
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		return super.onStartCommand(intent, flags, startId);
	}

	private static class CustomTestInnerService extends Service {

		@Nullable
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			startForeground(NOTIFICATION_ID, new Notification());
			stopForeground(true);
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}
	}

	public static boolean isActivityForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			Log.i(TAG, "className===" + cpn.getClassName());
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}
		return false;
	}

}