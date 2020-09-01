/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.advrogue;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.utils.OkhttpUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class App extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		OkhttpUtils.FUNC_TASK.shutdown();
		super.onTerminate();
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPoolSize(5);
		config.threadPriority(Thread.MAX_PRIORITY - 2);
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.FIFO);
		config.defaultDisplayImageOptions(getSimpleOptions());
		// config.writeDebugLogs(); // Remove for release app
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	private static DisplayImageOptions getSimpleOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// .showImageOnLoading(R.drawable.default_bg) // 设置图片在下载期间显示的图片
		// .showImageForEmptyUri(R.drawable.default_bg)// 设置图片Uri为空或是错误的时候显示的图片
		// .showImageOnFail(R.drawable.default_bg) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.build();// 构建完成
		return options;
	}
}
