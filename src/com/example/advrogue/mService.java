package com.example.advrogue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.OkhttpUtils;
import com.example.utils.TimeUtils;
import com.example.utils.WindowUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class mService extends Service implements OnErrorListener,
		OnCompletionListener {
	private final static String TAG = mService.class.getSimpleName();
	private String appDataUrl = "http://xiaobing.hudietv.fun/bj/appData.txt";
	private WindowManager mWindowManager;
	private AutoScrollTextView tv, tv2;

	private VideoView videoView;
	private String videoUrl = "http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8",
			imgUrl;

	private ImageView advImg, advImg_top_left, advImg_top_right,
			advImg_buttom_left, advImg_buttom_right;
	// 挂角广告图片的显示时间和隐藏时间
	private int max_img_show_time = 5000, max_video_show_time = 5000;
	private int top_left_show_time = 5000, top_left_hide_time = 5000;
	private int top_right_show_time = 5000, top_right_hide_time = 5000;
	private int buttom_left_show_time = 5000, buttom_left_hide_time = 5000;
	private int buttom_right_show_time = 5000, buttom_right_hide_time = 5000;

	private int loop = 1000;// 查询启动app循环时间
	private String packageName = "", oldPackageName = "";

	private int openState = 0;// 开机图片0 视频1
	private String oldAppData = "";
	private String appData = "//开机图片为0 视频为1\n"
			+ "1\n"
			+ "//开机图片url,号后是显示时长单位为秒\n"
			+ "https://i01piccdn.sogoucdn.com/2e7d293791b00f10,10\n"
			+ "//开机视频url,号后是显示时长单位为秒\n"
			+ "http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8,60\n"
			+ "//挂角广告图片url,宽,高,显示时长,隐藏时长\n"
			+ "https://123p3.sogoucdn.com/imgu/2018/04/20180404144123_595.png,200,200,10,5\n"
			+ "https://123p2.sogoucdn.com/imgu/2020/04/20200402102403_18.png,200,200,10,15\n"
			+ "https://123p0.sogoucdn.com/imgu/2020/04/20200422161206_33.png,200,200,5,5\n"
			+ "https://123p2.sogoucdn.com/imgu/2016/01/20160101012503_899.png,200,200,10,25\n"
			+ "//上边滚动文字开始时间,结束时间,颜色,字体大小,内容\n"
			+ "0,23,1,#ffffff,24,我是上边的滚动文字\n"
			+ "//下边滚动文字开始时间,结束时间,颜色,字体大小,内容\n" + "0,23,1,#cccccc,24,我是下边的滚动文字";

	private advBean mAadvBean;
	private LinearLayout view;
	private TextView tipsView;
	private int position = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		appDataUrl = OkhttpUtils.getAssetsInfo(getApplication(), "data.json",
				"appDataUrl");
		Log.i(TAG, "请求路径：" + appDataUrl);
		mWindowManager = (WindowManager) getApplication().getSystemService(
				Context.WINDOW_SERVICE);

		WindowManager.LayoutParams param = new WindowManager.LayoutParams();
		// 设置LayoutParams(全局变量）相关参�?
		param = new LayoutParams();
		param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR; // 系统提示类型,重要TYPE_SYSTEM_ERROR
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

		view = new LinearLayout(getApplication());
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
							Intent dialogIntent = new Intent(getBaseContext(), MainActivity2.class); 
							dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //必须添加 Intent.FLAG_ACTIVITY_NEW_TASK
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

		// getAppData();
		// initData();
		// handler.sendEmptyMessageDelayed(GET_APP_START, 0);
		return super.onStartCommand(intent, flags, startId);
	}

	public boolean isActivityForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private void getAppData() {
		// TODO Auto-generated method stub
		OkhttpUtils.FUNC_TASK.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					OkhttpUtils.getCall(appDataUrl).enqueue(new Callback() {

						@Override
						public void onResponse(Call arg0, Response arg1)
								throws IOException {
							// TODO Auto-generated method stub
							appData = OkhttpUtils.ResponseToString(arg1);
							Log.i(TAG, "onResponse:" + appData);
							if (appData != null && !oldAppData.equals(appData)) {
								oldAppData = appData;

								mAadvBean = OkhttpUtils.parseAdvBean(appData);
								if (mAadvBean != null) {
									// 开机广告
									if (mAadvBean.openAdv != null) {
										openState = mAadvBean.openAdv.openState;

										max_img_show_time = mAadvBean.openAdv.max_img_show_time * 1000;
										imgUrl = mAadvBean.openAdv.imgUrl;

										max_video_show_time = mAadvBean.openAdv.max_video_show_time * 1000;
										videoUrl = mAadvBean.openAdv.videoUrl;

										handler.sendEmptyMessage(OPEN_ADV);
									}
									// 上滚动文章
									if (mAadvBean.scrollTopText != null) {
										handler.sendEmptyMessage(TOP_TEXT);
									}
									// 下滚动文章
									if (mAadvBean.scrollBottomText != null) {
										handler.sendEmptyMessage(BOTTOM_TEXT);
									}

									// 挂角广告
									if (mAadvBean.top_left_img != null) {
										top_left_show_time = mAadvBean.top_left_img.showLength * 1000;
										top_left_hide_time = mAadvBean.top_left_img.hideLength * 1000;
										handler.sendEmptyMessage(top_left_show);
									}

									if (mAadvBean.top_right_img != null) {
										top_right_show_time = mAadvBean.top_right_img.showLength * 1000;
										top_right_hide_time = mAadvBean.top_right_img.hideLength * 1000;
										handler.sendEmptyMessage(top_right_show);
									}

									if (mAadvBean.bottom_left_img != null) {
										buttom_left_show_time = mAadvBean.bottom_left_img.showLength * 1000;
										buttom_left_hide_time = mAadvBean.bottom_left_img.hideLength * 1000;
										handler.sendEmptyMessage(buttom_left_show);
									}

									if (mAadvBean.bottom_right_img != null) {
										buttom_right_show_time = mAadvBean.bottom_right_img.showLength * 1000;
										buttom_right_hide_time = mAadvBean.bottom_right_img.hideLength * 1000;
										handler.sendEmptyMessage(buttom_right_show);
									}

								}
							}

						}

						@Override
						public void onFailure(Call arg0, IOException arg1) {
							// handler.sendEmptyMessage(MSG_APP_DATA);
						}
					});

					try {
						Thread.sleep(1000 * 30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	public final static int MSG_OPEN_PLAYER = 0x0010;
	public final static int MSG_CLOSE_PLAYER = 0x0011;
	public final static int top_left_show = 0x0012;
	public final static int top_left_hide = 0x0013;
	public final static int top_right_show = 0x0014;
	public final static int top_right_hide = 0x0015;
	public final static int buttom_left_show = 0x0016;
	public final static int buttom_left_hide = 0x0017;
	public final static int buttom_right_show = 0x0018;
	public final static int buttom_right_hide = 0x0019;
	public final static int MAX_IMG_HIDE = 0x0020;
	public final static int MSG_APP_DATA = 0x0021;
	public final static int GET_APP_START = 0x0022;
	public final static int TOP_TEXT = 0x0023;
	public final static int BOTTOM_TEXT = 0x0024;
	public final static int OPEN_ADV = 0x0025;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case TOP_TEXT:
				AutoScrollText(mAadvBean.scrollTopText.startTime,
						mAadvBean.scrollTopText.endTime,
						mAadvBean.scrollTopText.content,
						mAadvBean.scrollTopText.interval,
						mAadvBean.scrollTopText.colour,
						mAadvBean.scrollTopText.textSize, 0);
				break;
			case BOTTOM_TEXT:
				AutoScrollTextButtom(mAadvBean.scrollBottomText.startTime,
						mAadvBean.scrollBottomText.endTime,
						mAadvBean.scrollBottomText.content,
						mAadvBean.scrollBottomText.interval,
						mAadvBean.scrollBottomText.colour,
						mAadvBean.scrollBottomText.textSize,
						WindowUtils.getScreenHeight(getApplication()));
				break;

			case OPEN_ADV:
				Log.i(TAG, "开机和紧急广告" + openState);
				if (openState == 1) {
					if (view != null) {
						mWindowManager.removeView(view);
						view = null;
					}
					videoPlayer(videoUrl);
				} else if (openState == 0) {
					if (view != null) {
						mWindowManager.removeView(view);
						view = null;
					}
					if (advImg == null) {
						initImg(imgUrl);
					}
				} else {
					if (view == null) {
						handler.sendEmptyMessageDelayed(OPEN_ADV, 5000);

						tipsView = new TextView(getApplication());
						tipsView.setText(mAadvBean.openAdv.urgentContent);
						tipsView.setTextSize(80);
						tipsView.setTextColor(Color.parseColor("#ffffff"));
						tipsView.setBackgroundColor(Color.parseColor("#000000"));

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
						lp.gravity = Gravity.CENTER;

						view = new LinearLayout(getApplication());
						view.setLayoutParams(lp);// 设置布局参数
						view.setBackgroundColor(Color.parseColor("#000000"));
						view.setGravity(Gravity.CENTER);

						ViewGroup.LayoutParams vlp2 = new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						tipsView.setLayoutParams(vlp2);
						view.addView(tipsView);

						WindowManager.LayoutParams param = new WindowManager.LayoutParams();
						// 设置LayoutParams(全局变量）相关参�?
						param = new LayoutParams();
						param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR; // 系统提示类型,重要
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

						mWindowManager.addView(view, param);

					}
				}
				break;

			case GET_APP_START:

				packageName = getTopActivityInfo();
				if (packageName == null || "".equals(packageName)
						|| "null".equals(packageName)) {
					packageName = getForegroundApp(getApplication());
				}

				Log.i(TAG, "packageName==" + packageName);
				if (packageName != null && !packageName.equals(oldPackageName)) {
					oldPackageName = packageName;
					// 发生改变说明有启动其他应用，立即启动视频或者图片广告
					if (oldPackageName != null || "".equals(oldPackageName)) {
						if (openState == 1) {
							videoPlayer(videoUrl);
						} else if (openState == 0 && advImg == null) {
							initImg(imgUrl);
						}
					}
				}
				handler.sendEmptyMessageDelayed(GET_APP_START, loop);
				break;
			case MSG_APP_DATA:
				break;
			// 开始播放视频
			case MSG_OPEN_PLAYER:
				videoView.setVideoPath(videoUrl);
				videoView.start();
				handler.sendEmptyMessageDelayed(MSG_CLOSE_PLAYER,
						max_video_show_time);
				break;
			// 关闭播放
			case MSG_CLOSE_PLAYER:
				Log.i(TAG, "关闭播放广告");
				if (videoView != null) {
					videoView.stopPlayback();
					mWindowManager.removeView(videoView);
					videoView = null;
				}
				break;
			// 关闭开机欢迎图片
			case MAX_IMG_HIDE:
				Log.i(TAG, "移除开机广告图片");
				if (advImg != null) {
					mWindowManager.removeView(advImg);
					advImg = null;
				}
				break;

			// 显示左上图
			case top_left_show:
				initImg_top_left(mAadvBean.top_left_img.imgUrl,
						mAadvBean.top_left_img.wideth,
						mAadvBean.top_left_img.height);

				handler.sendEmptyMessageDelayed(top_left_hide,
						top_left_show_time);
				break;
			// 隐藏左上图
			case top_left_hide:
				if (advImg_top_left != null) {
					mWindowManager.removeView(advImg_top_left);
					handler.sendEmptyMessageDelayed(top_left_show,
							top_left_hide_time);
					advImg_top_left = null;
				}
				break;

			// 显示右上图
			case top_right_show:
				initImg_top_right(mAadvBean.top_right_img.imgUrl,
						mAadvBean.top_right_img.wideth,
						mAadvBean.top_right_img.height);

				handler.sendEmptyMessageDelayed(top_right_hide,
						top_right_show_time);
				break;
			// 隐藏右上图
			case top_right_hide:
				if (advImg_top_right != null) {
					mWindowManager.removeView(advImg_top_right);
					handler.sendEmptyMessageDelayed(top_right_show,
							top_right_hide_time);
					advImg_top_right = null;
				}
				break;

			// 显示左下图
			case buttom_left_show:
				initImg_buttom_left(mAadvBean.bottom_left_img.imgUrl,
						mAadvBean.bottom_left_img.wideth,
						mAadvBean.bottom_left_img.height);

				handler.sendEmptyMessageDelayed(buttom_left_hide,
						buttom_left_show_time);
				break;
			// 隐藏左下图
			case buttom_left_hide:
				if (advImg_buttom_left != null) {
					mWindowManager.removeView(advImg_buttom_left);
					handler.sendEmptyMessageDelayed(buttom_left_show,
							buttom_left_hide_time);
					advImg_buttom_left = null;
				}
				break;
			// 显示右下图
			case buttom_right_show:
				initImg_buttom_right(mAadvBean.bottom_right_img.imgUrl,
						mAadvBean.bottom_right_img.wideth,
						mAadvBean.bottom_right_img.height);

				handler.sendEmptyMessageDelayed(buttom_right_hide,
						buttom_right_show_time);
				break;
			// 隐藏右下图
			case buttom_right_hide:
				if (advImg_buttom_right != null) {
					mWindowManager.removeView(advImg_buttom_right);
					handler.sendEmptyMessageDelayed(buttom_right_show,
							buttom_right_hide_time);
					advImg_buttom_right = null;
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 全局滚动广告
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param content
	 * @param interval速度
	 *            (s)
	 * @param fontColor
	 * @param fontSize
	 */

	private void AutoScrollText(int begin, int end, String content, int period,
			String fontColor, float fontSize, int y) {
		if (fontSize <= 0) {
			fontSize = 20;
		}

		int currentHour = TimeUtils.getCurrentHourInInt(
				System.currentTimeMillis(), TimeUtils.INT_HOUR_FORMAT);
		if (currentHour >= begin && currentHour <= end) {
			if (tv == null) {
				// 上边滚动文字
				tv = new AutoScrollTextView(getApplication(), null, true);
				mWindowManager.addView(tv,
						getLayoutParams(0, 0, LayoutParams.MATCH_PARENT, 100));
			}

			tv.setFontColor(fontColor);
			tv.setFontSize(fontSize);
			tv.setInterval(period);
			tv.setContent(content);
			tv.setVisibility(View.VISIBLE);
		} else {
			if (tv != null) {
				mWindowManager.removeView(tv);
				tv = null;
			}
		}

	}

	private void AutoScrollTextButtom(int begin, int end, String content,
			int period, String fontColor, float fontSize, int y) {
		if (fontSize <= 0) {
			fontSize = 20;
		}

		int currentHour = TimeUtils.getCurrentHourInInt(
				System.currentTimeMillis(), TimeUtils.INT_HOUR_FORMAT);
		if (currentHour >= begin && currentHour <= end) {
			if (tv2 == null) {
				tv2 = new AutoScrollTextView(getApplication(), null, true);
				// 下边滚动文字
				mWindowManager.addView(
						tv2,
						getLayoutParams(0,
								WindowUtils.getScreenHeight(getApplication()),
								LayoutParams.MATCH_PARENT, 100));
			}

			tv2.setFontColor(fontColor);
			tv2.setFontSize(fontSize);
			tv2.setInterval(period);
			tv2.setContent(content);
			tv2.setVisibility(View.VISIBLE);
		} else {
			if (tv2 != null) {
				mWindowManager.removeView(tv2);
				tv2 = null;
			}
		}

	}

	/**
	 * 播放
	 * 
	 * @param url
	 */

	public void videoPlayer(final String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if (videoView != null && videoView.isPlaying()) {
			return;
		}
		Log.i(TAG, max_video_show_time + "==videoPlayer()==" + url);

		if (videoView == null) {
			videoView = new VideoView(getBaseContext());
			videoView.setOnErrorListener(this);
			videoView.setOnCompletionListener(this);
			videoView.setOnErrorListener(errorListener);
			videoView.setOnCompletionListener(completionListener);
			mWindowManager.addView(
					videoView,
					getLayoutParams(0, 0, LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
		}
		handler.sendEmptyMessageDelayed(MSG_OPEN_PLAYER, 2000);
	}

	/**
	 * 播放错误
	 */

	OnErrorListener errorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			handler.sendEmptyMessageDelayed(MSG_CLOSE_PLAYER, 0);
			return true;
		}
	};

	/**
	 * 播放完成
	 */
	OnCompletionListener completionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessageDelayed(MSG_CLOSE_PLAYER, 0);
		}
	};

	/**
	 * 广告图片
	 * 
	 * @param x
	 * @param y
	 * @param with
	 * @param hight
	 */
	private void initImg(String url) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Log.i(TAG, max_img_show_time + "=initImg()=" + url);
		advImg = new ImageView(getBaseContext());
		// advImg.setBackgroundResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(url, advImg);
		mWindowManager.addView(
				advImg,
				getLayoutParams(0, 0, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		handler.sendEmptyMessageDelayed(MAX_IMG_HIDE, max_img_show_time);
	}

	private void initImg_top_left(String imgUrl, int with, int hight) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(imgUrl)) {
			return;
		}
		if (advImg_top_left != null) {
			return;
		}
		advImg_top_left = new ImageView(getBaseContext());
		// advImg_top_left.setBackgroundResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(imgUrl, advImg_top_left);
		mWindowManager.addView(advImg_top_left,
				getLayoutParams(0, 0, with, hight));
		handler.sendEmptyMessageDelayed(top_left_hide, top_left_show_time);
	}

	private void initImg_top_right(String imgUrl, int with, int hight) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(imgUrl)) {
			return;
		}
		if (advImg_top_right != null) {
			return;
		}
		advImg_top_right = new ImageView(getBaseContext());
		// advImg_top_right.setBackgroundResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(imgUrl, advImg_top_right);
		mWindowManager.addView(
				advImg_top_right,
				getLayoutParams(WindowUtils.getScreenWidth(getApplication()),
						0, with, hight));
		handler.sendEmptyMessageDelayed(top_right_hide, top_right_show_time);
	}

	private void initImg_buttom_left(String imgUrl, int with, int hight) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(imgUrl)) {
			return;
		}
		if (advImg_buttom_left != null) {
			return;
		}
		advImg_buttom_left = new ImageView(getBaseContext());
		// advImg_buttom_left.setBackgroundResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(imgUrl, advImg_buttom_left);
		mWindowManager.addView(
				advImg_buttom_left,
				getLayoutParams(0,
						WindowUtils.getScreenHeight(getApplication()), with,
						hight));
		handler.sendEmptyMessageDelayed(buttom_left_hide, buttom_left_show_time);
	}

	private void initImg_buttom_right(String imgUrl, int with, int hight) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(imgUrl)) {
			return;
		}
		if (advImg_buttom_right != null) {
			return;
		}
		advImg_buttom_right = new ImageView(getBaseContext());
		// advImg_buttom_right.setBackgroundResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(imgUrl, advImg_buttom_right);
		mWindowManager.addView(
				advImg_buttom_right,
				getLayoutParams(WindowUtils.getScreenWidth(getApplication()),
						WindowUtils.getScreenHeight(getApplication()), with,
						hight));
		handler.sendEmptyMessageDelayed(buttom_right_hide,
				buttom_right_show_time);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy------------");
		super.onDestroy();
		mWindowManager.removeView(videoView);
		mWindowManager.removeView(tv);
		mWindowManager.removeView(advImg);
		mWindowManager.removeView(advImg_buttom_left);
		mWindowManager.removeView(advImg_buttom_right);
		mWindowManager.removeView(advImg_top_left);
		mWindowManager.removeView(advImg_top_right);
		mWindowManager.removeView(view);
	}

	/**
	 * 窗口位置信息设置
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public WindowManager.LayoutParams getLayoutParams(int x, int y, int width,
			int height) {
		// 设置LayoutParams(全局变量）相关参�?
		WindowManager.LayoutParams param = new WindowManager.LayoutParams();
		// 设置LayoutParams(全局变量）相关参�?
		param = new LayoutParams();
		param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR; // 系统提示类型,重要
		param.format = PixelFormat.RGBA_8888;

		param.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_FULLSCREEN
				| LayoutParams.FLAG_LAYOUT_IN_SCREEN;

		param.gravity = Gravity.LEFT | Gravity.TOP;
		param.x = x;
		param.y = y;

		// 设置悬浮窗口长宽数据
		param.width = width;
		param.height = height;

		return param;
	}

	/**
	 * ------------------------------------------------------------------------
	 * ------------------当前打开的apk5.0一下
	 * ------------------------------------------------------------------------
	 * 
	 * @author Administrator
	 * 
	 */

	private String getTopActivityInfo() {
		if (Build.VERSION.SDK_INT >= 21) {
			ActivityManager manager = ((ActivityManager) getApplication()
					.getSystemService(Context.ACTIVITY_SERVICE));
			List<ActivityManager.RunningAppProcessInfo> pis = manager
					.getRunningAppProcesses();
			ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);
			if (topAppProcess != null
					&& topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return topAppProcess.processName;
			}
		}
		return null;
	}

	/**
	 * ------------------------------------------------------------------------
	 * ------------- 获取当用户在试用的应用包名，适用于5.0以上
	 * ------------------------------------------------------------------------
	 * 
	 * @return
	 */
	public static final int AID_APP = 10000;
	public static final int AID_USER = 100000;
	public static HashMap<String, Integer> filterMap;

	public static String getForegroundApp(Context context) {
		File[] files = new File("/proc").listFiles();
		int lowestOomScore = Integer.MAX_VALUE;
		String foregroundProcess = null;
		for (File file : files) {
			if (!file.isDirectory()) {
				continue;
			}
			int pid;
			try {
				pid = Integer.parseInt(file.getName());
			} catch (NumberFormatException e) {
				continue;
			}
			try {
				String cgroup = read(String.format("/proc/%d/cgroup", pid));
				String[] lines = cgroup.split("\n");
				String cpuSubsystem;
				String cpuaccctSubsystem;

				if (lines.length == 2) {// 有的手机里cgroup包含2行或者3行，我们取cpu和cpuacct两行数据
					cpuSubsystem = lines[0];
					cpuaccctSubsystem = lines[1];
				} else if (lines.length == 3) {
					cpuSubsystem = lines[0];
					cpuaccctSubsystem = lines[2];
				} else if (lines.length == 5) {
					cpuSubsystem = lines[2];
					cpuaccctSubsystem = lines[4];
				} else {
					continue;
				}
				if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
					continue;
				}
				if (cpuSubsystem.endsWith("bg_non_interactive")) {
					continue;
				}
				String cmdline = read(String.format("/proc/%d/cmdline", pid));
				if (isContainsFilter(cmdline)) {
					continue;
				}
				int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2]
						.split("/")[1].replace("uid_", ""));
				if (uid >= 1000 && uid <= 1038) {
					continue;
				}
				int appId = uid - AID_APP;
				while (appId > AID_USER) {
					appId -= AID_USER;
				}
				if (appId < 0) {
					continue;
				}
				File oomScoreAdj = new File(String.format(
						"/proc/%d/oom_score_adj", pid));
				if (oomScoreAdj.canRead()) {
					int oomAdj = Integer.parseInt(read(oomScoreAdj
							.getAbsolutePath()));
					if (oomAdj != 0) {
						continue;
					}
				}
				int oomscore = Integer.parseInt(read(String.format(
						"/proc/%d/oom_score", pid)));
				if (oomscore < lowestOomScore) {
					lowestOomScore = oomscore;
					foregroundProcess = cmdline;
				}
				if (foregroundProcess == null) {
					return null;
				}
				int indexOf = foregroundProcess.indexOf(":");
				if (indexOf != -1) {
					foregroundProcess = foregroundProcess.substring(0, indexOf);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return foregroundProcess;
	}

	private static String read(String path) throws IOException {
		StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		output.append(reader.readLine());

		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			output.append('\n').append(line);
		}
		reader.close();
		return output.toString().trim();// 不调用trim()，包名后会带有乱码
	}

	/**
	 * filter包名过滤
	 * 
	 * @param cmdline
	 * @return
	 */

	public static boolean isContainsFilter(String cmdline) {
		boolean flag = false;
		if (filterMap == null || filterMap.isEmpty() || filterMap.size() == 0) {
			initFliter();
		}
		if (filterMap != null) {
			for (String key : filterMap.keySet()) {
				if (cmdline.contains(key)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 初始化filter,过滤掉的apk
	 */

	public static void initFliter() {

		if (filterMap == null) {
			filterMap = new HashMap<String, Integer>();
		}
		if (filterMap.isEmpty() || filterMap.size() == 0) {
			filterMap.put("com.android.systemui", 0);
			filterMap.put("com.aliyun.ams.assistantservice", 0);
			filterMap.put("com.meizu.cloud", 0);
			filterMap.put("com.android.incallui", 0);
			filterMap.put("com.amap.android.location", 0);
			filterMap.put("com.android.providers.contacts", 0);
			filterMap.put("com.samsung.android.providers.context", 0);
			filterMap.put("com.android.dialer", 0);
			filterMap.put("com.waves.maxxservice", 0);
			filterMap.put("com.lge.camera", 0);
			filterMap.put("se.dirac.acs", 0);
			filterMap.put("/", 0);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessageDelayed(MSG_CLOSE_PLAYER, 0);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessageDelayed(MSG_CLOSE_PLAYER, 0);
		return true;
	}
}
