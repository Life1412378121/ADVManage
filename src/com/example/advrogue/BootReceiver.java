package com.example.advrogue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent mIntent) {
		context.startService(new Intent(context, mService.class));
	}
}
