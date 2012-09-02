package fr.dz.sherizi.push.receiver;

import java.io.OutputStream;

import android.content.Context;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.common.message.Message;
import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.push.PushMessageReceiver;
import fr.dz.sherizi.service.share.ShareManager;
import fr.dz.sherizi.utils.Utils;

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
	public void onMessage(final Context context, PushMessage message) {
		Message transferInformations = Message.valueOf(message.getParameter("transferInformations"));
		String shareManagerId = transferInformations != null ? transferInformations.getParameter(ShareManager.SHARE_MANAGER_PARAMETER) : null;
		if ( shareManagerId != null && ! shareManagerId.isEmpty() ) {

			// Gets the share manager
			final ShareManager shareManager = ShareManager.getShareManager(shareManagerId, context);

			// Receive datas
			shareManager.receive(transferInformations, new SheriziActionListener() {
				@Override
				public void onSuccess() {
					try {
						OutputStream outputStream = Utils.openFileOutput("test.jpg");
						outputStream.write(shareManager.getData().getFileContent());
						outputStream.close();
					} catch(Throwable t) {
						onError(new SheriziException("Error while saving received file", t));
					}
				}

				@Override
				public void onError(Throwable t) {
					// TODO Log errors
				}
			});
		}
	}
}
