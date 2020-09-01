package com.example.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 * Created by Administrator on 2019-5-6.
 */

public class TxtUtil {
	final public static String TAG = "TxtUtil";
	public static Context context;

	/**
	 * 读取
	 * 
	 * @param path
	 * @return
	 */
	public static String readFileStr(String path) {
		byte Buffer[] = new byte[10240];
		// 得到文件输入流
		File file = new File(path);
		FileInputStream in = null;
		ByteArrayOutputStream outputStream = null;
		try {
			in = new FileInputStream(file);
			// 读出来的数据首先放入缓冲区，满了之后再写到字符输出流中
			int len = in.read(Buffer);

			if (len > 0) {
				// 创建一个字节数组输出流
				outputStream = new ByteArrayOutputStream();
				outputStream.write(Buffer, 0, len);
				// 把字节输出流转String
				DebugUtil.debug(TAG,
						"读取本地节目源：" + new String(outputStream.toByteArray()));
				return new String(outputStream.toByteArray());
			} else {
				return null;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		DebugUtil.debug(TAG, "获取文件节目信息为空：" + path);
		return null;
	}

	/**
	 * 缓存节目源
	 */
	public static void saveDataInfo(String channelType, String channelInfo,
			boolean isUP) {
		File file = checkLogFileIsExist(channelType, isUP);
		DebugUtil.debug(TAG, "开始缓存节目地址:" + file.getAbsolutePath());
		if (file == null)
			return;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			fos.write(channelInfo.getBytes());
			DebugUtil.debug(TAG, "缓存节目地址成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
			fos = null;
			file = null;
		}
	}

	/**
	 * 查文件是否存在 isUP==true删除原有重新建立
	 */
	public static File checkLogFileIsExist(String Name, boolean isUP) {
		File endfile = new File(Environment.getExternalStorageDirectory()
				.toString() + "/ADV/");
		if (isUP) {
			if (!endfile.exists()) {
				endfile.mkdirs();
			} else {
				endfile.delete();
				endfile.mkdirs();
			}
		} else {
			if (!endfile.exists()) {
				endfile.mkdirs();
			}
		}
		endfile = new File(Environment.getExternalStorageDirectory().toString()
				+ "/ADV/" + Name);
		if (isUP) {
			if (endfile.exists()) {
				try {
					endfile.delete();
					endfile.createNewFile();
				} catch (IOException e) {
				}
				return endfile;
			} else {
				try {
					endfile.createNewFile();
				} catch (IOException e) {
				}
				return endfile;
			}
		} else {
			if (!endfile.exists()) {
				try {
					endfile.createNewFile();
				} catch (IOException e) {
				}
				return endfile;
			}
		}
		return endfile;
	}

	/**
	 * 删除缓存文件
	 */

	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				deleteFile(f);
			}
			// file.delete();
		} else if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 获取Assets目录下的
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getAssetsServer(Context context, String fileName) {
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
				String result = jsonObject.optString("server", "");
				return result;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return "";
	}

	/**
	 * 判断是否有u盘插入
	 * 
	 * @return
	 */
	public static String getSDPath() {
		File file = new File("/cache/ADV/");
		File file1 = new File("/mnt/usbhost/Storage01/ADV/");
		File file2 = new File("/mnt/usbhost/Storage02/ADV/");
		File file3 = new File("/mnt/usbhost/Storage03/ADV/");
		File file4 = new File("/mnt/usbhost/Storage04/ADV/");
		if (file.exists()) {
			return "/cache/ADV/";
		} else if (file1.exists()) {
			return "/mnt/usbhost/Storage01/ADV/";
		} else if (file2.exists()) {
			return "/mnt/usbhost/Storage02/ADV/";
		} else if (file3.exists()) {
			return "/mnt/usbhost/Storage03/ADV/";
		} else if (file4.exists()) {
			return "/mnt/usbhost/Storage04/ADV/";
		} else {
			// SD是否挂载
			boolean hasSDCard = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
			if (!hasSDCard) {
				return Environment.getExternalStorageDirectory().toString()
						+ "/ADV/";
			} else {
				return Environment.getDownloadCacheDirectory().toString()
						+ "/ADV/";
			}
		}
	}
}
