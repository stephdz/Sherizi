package fr.dz.sherizi.push.receiver;

import fr.dz.sherizi.app.SheriziApplication;
import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.push.PushMessageReceiver;
import fr.dz.sherizi.utils.Utils;

/**
 * Test push message receiver : sends a notification
 */
public class NotificationReceiver implements PushMessageReceiver {

	public static final String NOTIFICATION_TYPE = "notification";
	public static final String MESSAGE_PARAMETER = "message";

	public String getAcceptedMessageType() {
		return NOTIFICATION_TYPE;
	}

	public void onMessage(SheriziApplication application, PushMessage message) {
		Utils.showNotification(application, message.getParameter(MESSAGE_PARAMETER));
	}
}
