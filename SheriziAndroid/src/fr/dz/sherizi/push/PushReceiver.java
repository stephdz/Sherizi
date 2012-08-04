package fr.dz.sherizi.push;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class PushReceiver extends GCMBroadcastReceiver {
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return PushService.class.getName();
	}
}
