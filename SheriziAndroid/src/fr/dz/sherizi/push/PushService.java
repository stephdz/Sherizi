package fr.dz.sherizi.push;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;

import com.appspot.api.services.sherizi.Sherizi;
import com.appspot.api.services.sherizi.Sherizi.Builder;
import com.appspot.api.services.sherizi.model.User;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.services.GoogleClient;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import fr.dz.sherizi.common.push.PushMessage;
import fr.dz.sherizi.utils.Constants;
import fr.dz.sherizi.utils.Utils;

/**
 * Push notifications listener for Sherizi
 */
public class PushService extends GCMBaseIntentService {

	private final Sherizi sheriziServer;

	/**
	 * Default constructor :
	 *  - creates the remote interface
	 *  - registers to GCM
	 */
	public PushService() {
		super(Constants.PROJECT_ID);
		Builder endpointBuilder = new Sherizi.Builder (
			AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
			new HttpRequestInitializer() {
				public void initialize(HttpRequest httpRequest) {
				}
			});
		this.sheriziServer = updateBuilder(endpointBuilder).build();
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
		// TODO Create an interface for MessageTreaters => the type give the object to use for treatment
		PushMessage message = PushMessage.valueOf(intent.getStringExtra("message"));
		Utils.showMessage(context, message.getParameter("message"));
	}

	/**
	 * Called when a registration token has been received.
	 * @param context the Context
	 */
	@Override
	public void onRegistered(Context context, String registrationId) {
		try {
			User user = new User()
				.setRegistrationID(registrationId)
				.setGoogleAccount(Utils.getGoogleAccount(context));
			sheriziServer.saveOrUpdateUser(user).execute();
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
			sheriziServer.deleteUser(registrationId).execute();
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

	/**
	 * Updates the Google client builder to connect the appropriate server based
	 * on whether LOCAL_ANDROID_RUN is true or false.
	 * @param builder Google client builder
	 * @return same Google client builder
	 */
	protected static <B extends GoogleClient.Builder> B updateBuilder(B builder) {
		if (Constants.LOCAL_ANDROID_RUN) {
			builder.setRootUrl(Constants.LOCAL_APP_ENGINE_SERVER_URL + "/_ah/api/");
		}
		// only enable GZip when connecting to remote server
		final boolean enableGZip = builder.getRootUrl().startsWith("https:");
		builder.setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
			public void initialize(JsonHttpRequest jsonHttpRequest) {
				jsonHttpRequest.setEnableGZipContent(enableGZip);
			}
		});
		return builder;
	}
}
