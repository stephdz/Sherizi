package fr.dz.sherizi.common.push;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes a push message
 */
public class PushMessage {

	public static final String TYPE_KEY = "type";

	private Map<String,String> parameters = new HashMap<String,String>();

	/**
	 * Constructor by type
	 * @param type
	 */
	public PushMessage(String type) {
		setType(type);
	}

	/**
	 * Constructor by parameters
	 * @param parameters
	 */
	public PushMessage(Map<String, String> parameters) {
		setParameters(parameters);
	}

	/**
	 * Gets the message type
	 * @return
	 */
	public String getType() {
		return getParameter(TYPE_KEY);
	}

	/**
	 * Gets the message type
	 * @param type
	 */
	public void setType(String type) {
		addParameter(TYPE_KEY, type);
	}

	/**
	 * Adds a parameter to the message
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}

	/**
	 * Gets a parameter from the message
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		return parameters.get(key);
	}

	/**
	 * Gets the parameters map
	 * @return
	 */
	public Map<String,String> getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters map
	 * @param parameters
	 */
	public void setParameters(Map<String,String> parameters) {
		this.parameters = parameters;
	}

	/* Returns the PushMessage string
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			StringBuffer buffer = new StringBuffer();
			if ( parameters != null ) {
				boolean first = true;
				for ( String key : parameters.keySet() ) {
					buffer.append((first?"":"&")+key+"="+URLEncoder.encode(parameters.get(key), "UTF-8"));
					first = false;
				}
			}
			return buffer.toString();
		} catch ( UnsupportedEncodingException e ) {
			return null;
		}
	}

	/**
	 * Constructs a push message object from the string message
	 * @param stringExtra
	 * @return
	 */
	public static PushMessage valueOf(String stringExtra) {
		Map<String,String> parameters = new HashMap<String,String>();
		String[] keyEqualsValueTab = stringExtra.split("&");
		if ( keyEqualsValueTab != null ) {
			for ( String keyEqualsValue : keyEqualsValueTab ) {
				String[] keyValueTab = keyEqualsValue.split("=");
				if ( keyValueTab != null && keyValueTab.length > 0 ) {
					String key = keyValueTab[0];
					String value = keyValueTab.length > 1 ? keyValueTab[1] : "";
					try {
						parameters.put(key, URLDecoder.decode(value, "UTF-8"));
					} catch ( UnsupportedEncodingException e ) {
						parameters.put(key, "");
					}
				}
			}
		}
		return new PushMessage(parameters);
	}
}
