package fr.dz.sherizi.push;

import fr.dz.sherizi.app.SheriziApplication;
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
	 * @param application
	 * @param message
	 */
	public void onMessage(SheriziApplication application, PushMessage message);
}
