package fr.dz.sherizi.service.share;

import java.util.Map;

import fr.dz.sherizi.common.message.Message;

/**
 * Transfer informations
 */
public class TransferInformations extends Message {

	private static final long serialVersionUID = -6319001553571345983L;

	// Constants
	public static final String SERVER_PARAMETER = "server";
	public static final String SHARE_MANAGER_PARAMETER = "shareManagerId";
	public static final String CONTENT_SIZE_PARAMETER = "contentSize";

	// The server address
	public String server;

	// The share manager used for transfer
	public String shareManagerId;

	// The data size
	public Long contentSize;


	/**
	 * Default constructor
	 */
	public TransferInformations() {
		super();
	}

	/**
	 * Constructor from a map
	 * @param parametersMapFromString
	 */
	private TransferInformations(Map<String, String> parametersMap) {
		super(parametersMap);

		// Reconstructs the transfer informations
		this.server = getParameter(SERVER_PARAMETER);
		this.shareManagerId = getParameter(SHARE_MANAGER_PARAMETER);
		this.contentSize = Long.valueOf(getParameter(CONTENT_SIZE_PARAMETER));
	}

	/**
	 * Constructs a transfer information object from the string message
	 * @param parametersString
	 * @return
	 */
	public static TransferInformations valueOf(String parametersString) {
		return new TransferInformations(getParametersMapFromString(parametersString));
	}


	/*
	 * GETTERS & SETTERS
	 */

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
		addParameter(SERVER_PARAMETER, server);
	}

	public String getShareManagerId() {
		return shareManagerId;
	}

	public void setShareManagerId(String shareManagerId) {
		this.shareManagerId = shareManagerId;
		addParameter(SHARE_MANAGER_PARAMETER, shareManagerId);
	}

	public Long getContentSize() {
		return contentSize;
	}

	public void setContentSize(Long contentSize) {
		this.contentSize = contentSize;
		addParameter(CONTENT_SIZE_PARAMETER, contentSize.toString());
	}
}
