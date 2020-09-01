package com.example.advrogue;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Toast;

@SuppressLint({ "InlinedApi", "NewApi" })
public class MainActivity extends Activity {
	// 要申请的权限
	private String[] permissions = { Manifest.permission.INTERNET,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.CHANGE_NETWORK_STATE,
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.SYSTEM_ALERT_WINDOW,
			Manifest.permission.RECEIVE_BOOT_COMPLETED };
	AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, mService.class));
		// 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// 检查该权限是否已经获取
			int i = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[0]);
			int l = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[1]);
			int m = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[2]);
			int n = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[3]);
			int n1 = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[4]);
			int n2 = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[5]);
			int n3 = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[6]);
			int n4 = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[7]);
			int n5 = ContextCompat.checkSelfPermission(getApplicationContext(),
					permissions[8]);
			// 权限是否已经 授权 GRANTED---授权 DINIED---拒绝
			if (i != PackageManager.PERMISSION_GRANTED
					|| l != PackageManager.PERMISSION_GRANTED
					|| m != PackageManager.PERMISSION_GRANTED
					|| n != PackageManager.PERMISSION_GRANTED
					|| n1 != PackageManager.PERMISSION_GRANTED
					|| n2 != PackageManager.PERMISSION_GRANTED
					|| n3 != PackageManager.PERMISSION_GRANTED
					|| n4 != PackageManager.PERMISSION_GRANTED
					|| n5 != PackageManager.PERMISSION_GRANTED) {
				// 如果没有授予该权限，就去提示用户请求
				startRequestPermission();
			}

			try {
				if (!Settings.canDrawOverlays(MainActivity.this)) {
					Intent intent = new Intent(
							Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
							Uri.parse("package:" + getPackageName()));
					startActivityForResult(intent, 10);
				}
			} catch (NoSuchMethodError e) {
				// TODO: handle exception
			}
		}

		MainActivity.this.finish();
	}

	/**
	 * 开始提交请求权限
	 */
	private void startRequestPermission() {
		ActivityCompat.requestPermissions(this, permissions, 321);
	}

	/**
	 * 用户权限 申请 的回调方法
	 * 
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 321) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
					// 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
					boolean b = shouldShowRequestPermissionRationale(permissions[0]);
					// 以前是!b
					if (b) {
						// 用户还是想用我的 APP 的
						// 提示用户去应用设置界面手动开启权限
						showDialogTipUserGoToAppSettting();
					} else {
						finish();
					}
				} else {
					// 获取权限成功提示，可以不要
					Toast toast = Toast.makeText(this, "获取权限成功",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		}
	}

	/**
	 * 提示用户去应用设置界面手动开启权限
	 */
	private void showDialogTipUserGoToAppSettting() {

		dialog = new AlertDialog.Builder(this)
				.setTitle("存储权限不可用")
				.setMessage("请在-应用设置-权限-中，允许应用使用存储权限来保存用户数据")
				.setPositiveButton("立即开启",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 跳转到应用设置界面
								goToAppSetting();
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// finish();
					}
				}).setCancelable(false).show();
	}

	/**
	 * 跳转到当前应用的设置界面
	 */
	private void goToAppSetting() {
		Intent intent = new Intent();

		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", getPackageName(), null);
		intent.setData(uri);

		startActivityForResult(intent, 123);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 权限管理
		if (requestCode == 123) {
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				// 检查该权限是否已经获取
				int i = ContextCompat.checkSelfPermission(this, permissions[0]);
				// 权限是否已经 授权 GRANTED---授权 DINIED---拒绝
				if (i != PackageManager.PERMISSION_GRANTED) {
					// 提示用户应该去应用设置界面手动开启权限
					showDialogTipUserGoToAppSettting();
				} else {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
				}
			}
		}

		if (requestCode == 1) {
			AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
			int mode = 0;
			mode = appOps.checkOpNoThrow("android:get_usage_stats",
					android.os.Process.myUid(), getPackageName());
			boolean granted = mode == AppOpsManager.MODE_ALLOWED;
			if (!granted) {
				Toast.makeText(this, "请开启该权限", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
