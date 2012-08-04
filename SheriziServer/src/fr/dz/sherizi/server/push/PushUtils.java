package fr.dz.sherizi.server.push;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.server.exception.SheriziException;
import fr.dz.sherizi.server.model.User;
import fr.dz.sherizi.server.service.SheriziLocal;
import fr.dz.sherizi.server.service.SheriziRemote;
import fr.dz.sherizi.server.utils.Constants;

public class PushUtils {

	private static final SheriziRemote remote = new SheriziRemote();
	private static final SheriziLocal local = new SheriziLocal();

	/**
	 * Sends a message to a registered user.
	 * @param messageString the message to be sent.
	 * @param user the targetted user.
	 * @return Result the result of the push.
	 * @throws IOException
	 */
	public static Result sendMessage(PushMessage message, User user) throws SheriziException {

		// Message should not be greeter than 1000 bytes
		String messageString = message.toString();
		if (messageString.length() > 1000) {
			throw new IllegalArgumentException("");
		}

		// Sends the message to the user and maintains its registration status up-to-date
		try {
			Sender sender = new Sender(Constants.API_KEY);
			Message msg = new Message.Builder().addData("message", messageString).build();
			Result result = sender.send(msg, user.getRegistrationID(), 5);
			if (result.getMessageId() != null) {
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					remote.deleteUser(user.getRegistrationID());
					user.setRegistrationID(canonicalRegId);
					remote.saveOrUpdateUser(user);
				}
			} else {
				String error = result.getErrorCodeName();
				if ( error.equals(com.google.android.gcm.server.Constants.ERROR_NOT_REGISTERED) ) {
					remote.deleteUser(user.getRegistrationID());
				}
			}

			return result;
		} catch(IOException e) {
			throw new SheriziException("Unable to send message to the target user : "+e.getMessage());
		}
	}

	/**
	 * Sends a message to all registered users.
	 * @param message the message to be sent.
	 * @return Result the result of the push.
	 * @throws IOException
	 */
	public static void sendMessage(PushMessage message) throws SheriziException {
		for (User user : local.getAllUsers()) {
			sendMessage(message, user);
		}
	}
}
