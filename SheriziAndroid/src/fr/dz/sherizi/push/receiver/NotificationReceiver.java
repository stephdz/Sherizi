package fr.dz.sherizi.push.receiver;

import android.content.Context;
import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.push.PushMessageReceiver;
import fr.dz.sherizi.utils.Utils;

/**
 * Test push message receiver : sends a notification
 */
public class NotificationReceiver implements PushMessageReceiver {

	public static final String NOTIFICATION_TYPE = "notification";

	public String getAcceptedMessageType() {
		return NOTIFICATION_TYPE;
	}

	public void onMessage(Context context, PushMessage message) {
		Utils.showMessage(context, message.getParameter("message"));
	}
}
