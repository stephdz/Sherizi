package fr.dz.sherizi.push.receiver;

import fr.dz.sherizi.app.SheriziApplication;
import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.push.PushMessageReceiver;
import fr.dz.sherizi.service.share.TransferInformations;

/**
 * ShareManagerReceiver : initiates the transfer using the appropriate share manager
 */
public class ShareManagerReceiver implements PushMessageReceiver {

	public static final String TRANSFER_INITIATING_TYPE = "initiateTransfer";

	public String getAcceptedMessageType() {
		return TRANSFER_INITIATING_TYPE;
	}

	/**
	 * When a message arrives, we treat it using the good share manager
	 * TODO Ask the user before starting incoming transfer
	 * @param context
	 * @param message
	 */
	public void onMessage(SheriziApplication application, PushMessage message) {
		final TransferInformations transferInformations = TransferInformations.valueOf(message.getParameter("transferInformations"));
		if ( transferInformations != null ) {
			application.addWaitingTransfer(transferInformations);
		}
	}
}
