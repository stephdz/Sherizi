package fr.dz.sherizi.push;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;

import com.appspot.api.services.sherizi.model.User;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.push.receiver.NotificationReceiver;
import fr.dz.sherizi.push.receiver.ShareManagerReceiver;
import fr.dz.sherizi.service.contact.ContactService;
import fr.dz.sherizi.service.contact.Email;
import fr.dz.sherizi.service.server.SheriziServerService;
import fr.dz.sherizi.utils.Constants;
import fr.dz.sherizi.utils.Utils;

/**
 * Push notifications listener for Sherizi
 */
public class PushService extends GCMBaseIntentService {

	// The push message receivers
	private static final List<PushMessageReceiver> RECEIVERS_LIST = Arrays.asList(
			new NotificationReceiver(),
			new ShareManagerReceiver()
		);
	private static final Map<String,PushMessageReceiver> RECEIVERS_MAP = createPushMessageReceivers();
	private static Map<String,PushMessageReceiver> createPushMessageReceivers() {
		Map<String,PushMessageReceiver> result = new HashMap<String,PushMessageReceiver>();
		for ( PushMessageReceiver receiver : RECEIVERS_LIST ) {
			result.put(receiver.getAcceptedMessageType(), receiver);
		}
		return result;
	}

	/**
	 * Default constructor :
	 *  - creates the remote interface
	 *  - registers to GCM
	 */
	public PushService() {
		super(Constants.PROJECT_ID);
	}

	/**
	 * Called on registration error. This is called in the context of a Service
	 * - no dialog or UI.
	 * @param context the Context
	 * @param errorId an error message
	 */
	@Override
	public void onError(Context context, String errorId) {
		// TODO To be completed
	}

	/**
	 * Called when a cloud message has been received.
	 */
	@Override
	public void onMessage(Context context, Intent intent) {
		PushMessage message = PushMessage.valueOf(intent.getStringExtra("message"));
		if ( RECEIVERS_MAP.containsKey(message.getType()) ) {
			RECEIVERS_MAP.get(message.getType()).onMessage(context, message);
		} else {
			// TODO Show a warning or log something
		}
	}

	/**
	 * Called when a registration token has been received.
	 * @param context the Context
	 */
	@Override
	public void onRegistered(Context context, String registrationId) {
		try {
			// Adds a user for each email address
			Set<Email> emails = ContactService.getInstance().getConnectedUserEmails(context);
			String deviceName = Utils.getDeviceName(context);
			for ( Email email : emails ) {
				User user = new User()
					.setRegistrationID(registrationId)
					.setEmail(email.getEmail())
					.setDeviceName(deviceName);
				SheriziServerService.getInstance().saveOrUpdateUser(user).execute();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the device has been unregistered.
	 * @param context the Context
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		try {
			Set<Email> emails = ContactService.getInstance().getConnectedUserEmails(context);
			String deviceName = Utils.getDeviceName(context);
			for ( Email email : emails ) {
				SheriziServerService.getInstance().deleteUser(email.getEmail(), deviceName).execute();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Register the device for GCM.
	 * @param context
	 */
	public static void register(Context context) {
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		//String regId = GCMRegistrar.getRegistrationId(context);
		// TODO Retester correctement avec ce "if"
		//if (regId.equals("")) {
			GCMRegistrar.register(context, Constants.PROJECT_ID);
		//}
	}
}
