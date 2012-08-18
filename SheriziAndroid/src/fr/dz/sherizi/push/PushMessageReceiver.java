package fr.dz.sherizi.push;

import android.content.Context;
import fr.dz.sherizi.common.push.PushMessage;

/**
 * Interface for push message treatment
 */
public interface PushMessageReceiver {

	/**
	 * Must return the message type accepted by this receiver
	 * @param context
	 * @param message
	 * @return
	 */
	public String getAcceptedMessageType();

	/**
	 * Called when an accepted message arrives
	 * @param context
	 * @param message
	 */
	public void onMessage(Context context, PushMessage message);
}
