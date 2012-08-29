package fr.dz.sherizi.common.push;

import java.util.Map;

import fr.dz.sherizi.common.message.Message;

/**
 * Describes a push message
 */
public class PushMessage extends Message {

	private static final long serialVersionUID = 975635731211943070L;

	// Constants
	public static final String TYPE_KEY = "type";


	/**
	 * Constructor by type
	 * @param type
	 */
	public PushMessage(String type) {
		super();
		setType(type);
	}

	/**
	 * Constructor by parameters
	 * @param parameters
	 */
	public PushMessage(Map<String, String> parameters) {
		super(parameters);
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
	 * Constructs a push message object from the string message
	 * @param parametersString
	 * @return
	 */
	public static PushMessage valueOf(String parametersString) {
		return new PushMessage(getParametersMapFromString(parametersString));
	}
}
