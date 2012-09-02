package fr.dz.sherizi.common.message;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes a push message
 */
public class Message implements Serializable {

	private static final long serialVersionUID = -5619739902326725183L;

	// Constants
	public static final String DEFAULT_ENCODING = "UTF-8";

	// Parameters map
	private Map<String,String> parameters = new HashMap<String,String>();

	// Message size and string cache
	private Long messageSize;
	private String value;

	/**
	 * Default constructor
	 * @param type
	 */
	public Message() {

	}

	/**
	 * Constructor by parameters
	 * @param parameters
	 */
	public Message(Map<String, String> parameters) {
		setParameters(parameters);
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

	/**
	 * Returns the message size (in bytes) using default encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public long getMessageSize() throws UnsupportedEncodingException {
		return getMessageSize(DEFAULT_ENCODING);
	}

	/**
	 * Returns the message size (in bytes)
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public long getMessageSize(String encoding) throws UnsupportedEncodingException {
		initMessageCache(encoding);
		return messageSize;
	}

	/* Same as toString but with DEFAULT_ENCODING
	 * (non-Javadoc)
	 * @see #toString(String)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return toString(DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// Cannot append
			return null;
		}
	}

	/**
	 * Serializes the message
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String toString(String encoding) throws UnsupportedEncodingException {
		initMessageCache(encoding);
		return value;
	}

	/**
	 * Constructs a push message object from the string message
	 * @param parametersString
	 * @return
	 */
	public static Message valueOf(String parametersString) {
		return new Message(getParametersMapFromString(parametersString));
	}

	/**
	 * Returns a parameters map from a string
	 * @param parameters
	 * @return
	 */
	protected static Map<String,String> getParametersMapFromString(String parameters) {
		Map<String,String> result = new HashMap<String,String>();
		String[] keyEqualsValueTab = parameters.split("&");
		if ( keyEqualsValueTab != null ) {
			for ( String keyEqualsValue : keyEqualsValueTab ) {
				String[] keyValueTab = keyEqualsValue.split("=");
				if ( keyValueTab != null && keyValueTab.length > 0 ) {
					String key = keyValueTab[0];
					String value = keyValueTab.length > 1 ? keyValueTab[1] : "";
					try {
						result.put(key, URLDecoder.decode(value, "UTF-8"));
					} catch ( UnsupportedEncodingException e ) {
						result.put(key, "");
					}
				}
			}
		}
		return result;
	}

	/**
	 * Internal serialization (using URL style)
	 */
	protected String internalToString() {
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
	 * Initializes the cache (serialized message and message size)
	 * @param encoding
	 * @throws UnsupportedEncodingException
	 */
	protected void initMessageCache(String encoding) throws UnsupportedEncodingException {
		if ( value == null ) {
			this.value = internalToString();
		}
		if ( messageSize == null ) {
			this.messageSize = (long) value.getBytes(encoding).length;
		}
	}
}
