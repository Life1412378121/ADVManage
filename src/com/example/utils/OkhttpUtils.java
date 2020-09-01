package com.example.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.example.advrogue.advBean;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class OkhttpUtils {
	private static String TAG = OkhttpUtils.class.getSimpleName();

	private static final BlockingQueue<Runnable> POOL_QUEUE_TASK = new SynchronousQueue<Runnable>();
	private static final ThreadFactory TASK_FACTORY = new ThreadFactory() {
		private final AtomicInteger COUNT = new AtomicInteger(1);

		public Thread newThread(Runnable runnable) {
			int count = COUNT.getAndIncrement();
			return new Thread(runnable, "Func #" + count);
		}
	};
	/**
	 * 线程池
	 */
	public static final ExecutorService FUNC_TASK = new ThreadPoolExecutor(8,
			Integer.MAX_VALUE, 3L, TimeUnit.SECONDS, POOL_QUEUE_TASK,
			TASK_FACTORY);

	public static String token = "db718add-59ff-4111-8d8e-e88029cbf55a",
			token_expire;

	/**
	 * get请求
	 * 
	 * @param url
	 */
	public static Call getCall(String url) {
		// TODO Auto-generated method stub
		DebugUtil.debug(TAG, "get请求url:" + url);
		OkHttpClient client = new OkHttpClient.Builder()
				.writeTimeout(20, TimeUnit.SECONDS)
				.connectTimeout(20, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS).build();

		Request request = new Request.Builder().url(url)
				.addHeader("content_type", "application/json").get().build();
		Call call = client.newCall(request);
		return call;
	}

	/**
	 * pos请求
	 * 
	 * @param url
	 */
	public static Call postCall(String url, String json) {
		// TODO Auto-generated method stub
		DebugUtil.debug(TAG, "get请求url:" + url);
		OkHttpClient client = new OkHttpClient.Builder()
				.writeTimeout(20, TimeUnit.SECONDS)
				.connectTimeout(20, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS).build();

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Call call = client.newCall(request);
		return call;
	}

	/**
	 * Response 转 String
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static String ResponseToString(Response response) throws IOException {
		if (response.isSuccessful()) {
			byte[] buffer = new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(response.body()
					.byteStream());
			int read = bis.read(buffer);

			byte[] mByte = new byte[1024 * 10];
			int index = 0;
			while (read > 0) {
				System.arraycopy(buffer, 0, mByte, index, read);
				index = index + read;
				read = bis.read(buffer);
			}

			String mRes = "";
			byte[] resp = new byte[index];
			if (0 < mByte.length) {
				System.arraycopy(mByte, 0, resp, 0, index);
				mRes = new String(resp);
			} else {
				mRes = "";
			}
			DebugUtil.debug(TAG, "get请求成功: " + mRes);
			return mRes;
		} else {
			DebugUtil.debug(TAG, "get请求异常: " + response);
			return "";
		}
	}

	public static advBean parseAdvBean(String implString) {
		advBean mAdvBean = null;
		try {
			Gson gson = new Gson();
			mAdvBean = gson.fromJson(implString, advBean.class);
		} catch (JsonIOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mAdvBean;
	}

	/**
	 * url转Bitmap图片
	 * 
	 * @param url
	 */
	public void getBitmapImg(String url) {
		// TODO Auto-generated method stub
		getCall(url).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				byte[] Picture_bt = response.body().bytes();
				Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0,
						Picture_bt.length);

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * url转Bitmap图片
	 * 
	 * @param url
	 */
	public static class MyPictask extends AsyncTask<String, Void, Bitmap> {
		private ImageView iv;
		private Context context;

		public MyPictask(ImageView iv, Context context) {
			this.iv = iv;
			this.context = context;
		}

		@Override
		protected Bitmap doInBackground(String... strings) {
			try {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					DebugUtil.debug(TAG, "once InterruptedException");
				}
				URL url = new URL(strings[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(5000);
				urlConnection.setReadTimeout(5000);
				int responseCode = urlConnection.getResponseCode();

				if (responseCode == 200) {
					InputStream inputStream = urlConnection.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					DebugUtil.debug(TAG, "url转bitmap成功");
					return bitmap;

				}
			} catch (Exception e) {
				e.printStackTrace();
				DebugUtil.debug(TAG, "url转bitmap失败");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (bitmap != null) {
				iv.setImageBitmap(ImageUtils.createReflectedImage(bitmap));
			} else {
				// iv.setImageBitmap(ImageUtils.createReflectedImage(ImageUtils
				// .drawableToBitmap(context, R.drawable.photo1)));
			}
		}
	}
	
	/**
	 * 根据key获取assault的信息ADD BY WU 180521
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getAssetsInfo(Context context, String fileName,
			String key) {
		InputStream inputStream;
		InputStreamReader inputStreamReader;
		BufferedReader bufferedReader;
		try {
			StringBuilder stringBuilder = new StringBuilder();
			inputStream = context.getAssets().open(fileName);
			if (inputStream != null) {
				inputStreamReader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(inputStreamReader);
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
			}
			if (!TextUtils.isEmpty(stringBuilder.toString())) {
				JSONObject jsonObject = new JSONObject(stringBuilder.toString());
				String result = jsonObject.optString(key, "");
				return result;
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return "";
	}
}
