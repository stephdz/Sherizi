package fr.dz.sherizi.service.server;

import com.appspot.api.services.sherizi.Sherizi;
import com.appspot.api.services.sherizi.Sherizi.Builder;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.services.GoogleClient;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

import fr.dz.sherizi.utils.Constants;


/**
 * Service for contact management
 */
public class SheriziServerService {

	// Contact Service instance
	private static final SheriziServerService instance = new SheriziServerService();

	// The service endpoint
	private final Sherizi sheriziServer;

	/**
	 * Private constructor : it's a singleton
	 */
	private SheriziServerService() {
		Builder endpointBuilder = new Sherizi.Builder (
			AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
			new HttpRequestInitializer() {
				public void initialize(HttpRequest httpRequest) {
				}
			});
		this.sheriziServer = updateBuilder(endpointBuilder).build();
	}

	/**
	 * @return the contact service instance
	 */
	public static Sherizi getInstance() {
		return instance.getSheriziEndpoint();
	}

	/**
	 * @return The Sherizi endpoint
	 */
	protected Sherizi getSheriziEndpoint() {
		return sheriziServer;
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
