package fr.dz.sherizi.boot;

import fr.dz.sherizi.push.PushService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// Registers the PushService
		PushService.register(context);
	}
}
