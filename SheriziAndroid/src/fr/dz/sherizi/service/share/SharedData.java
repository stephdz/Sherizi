package fr.dz.sherizi.service.share;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.common.message.Message;

/**
 * Generic class for shared datas
 */
public class SharedData extends Message {

	private static final long serialVersionUID = -2253834426542461098L;

	// Constants
	public static final String TYPE_PARAMETER = "type";
	public static final String URI_PARAMETER = "uri";
	public static final String FILE_TYPE_PARAMETER = "fileType";
	public static final String FILE_CONTENT_PARAMETER = "fileContent";
	public static final String TEXT_PARAMETER = "text";

	// Data type
	public enum SharedDataType {
		uri,
		text
	};
	private SharedDataType type;

	// Datas to share : URI
	private Uri uri;
	private String fileType;
	private byte[] fileContent;

	// Datas to share : TEXT
	private String text;

	/**
	 * Constructor for URI datas
	 */
	public SharedData(Context context, Uri uri, String fileType) throws SheriziException {
		super();
		initFromUri(uri, fileType);
		loadFileContentFromUri(context, uri);
	}

	/**
	 * Constructor for String datas
	 */
	public SharedData(String text) {
		super();
		initFromText(text);
	}

	/**
	 * Constructor from a map
	 * @param parametersMapFromString
	 */
	private SharedData(Map<String, String> parametersMap) {
		super(parametersMap);

		// Reconstructs the shared datas
		String type = getParameter(TYPE_PARAMETER);
		if ( type != null && ! type.isEmpty() ) {
			setType(SharedDataType.valueOf(type));
			switch(getType()) {
				case uri:
					initFromUri(Uri.parse(getParameter(URI_PARAMETER)), getParameter(FILE_TYPE_PARAMETER));
					setFileContent(getParameter(FILE_CONTENT_PARAMETER));
					break;
				case text:
					initFromText(getParameter(TEXT_PARAMETER));
					break;
			}
		}
	}

	/**
	 * Constructs a shared data object from the string message
	 * @param parametersString
	 * @return
	 */
	public static SharedData valueOf(String parametersString) {
		return new SharedData(getParametersMapFromString(parametersString));
	}

	/**
	 * Initialization from uri
	 * @param uri
	 * @param fileType
	 */
	protected void initFromUri(Uri uri, String fileType) {
		setType(SharedDataType.uri);
		setUri(uri);
		setFileType(fileType);
	}

	/**
	 * Initialization from text
	 * @param text
	 */
	protected void initFromText(String text) {
		setType(SharedDataType.text);
		setText(text);
	}

	/**
	 * Loads the file content from an uri
	 */
	protected void loadFileContentFromUri(Context context, Uri uri) throws SheriziException {
		try {
			InputStream inputStream = context.getContentResolver().openInputStream(uri);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read;
			while ( (read = inputStream.read(buffer)) != -1 ) {
				baos.write(buffer, 0, read);
			}
			inputStream.close();
			setFileContent(baos.toByteArray());
			baos.close();
		} catch (Throwable t) {
			throw new SheriziException("Error while reading shared datas", t);
		}
	}

	/*
	 * GETTERS & SETTERS
	 */

	public SharedDataType getType() {
		return type;
	}

	public void setType(SharedDataType type) {
		this.type = type;
		addParameter(TYPE_PARAMETER, type.toString());
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
		addParameter(URI_PARAMETER, uri.toString());
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
		addParameter(FILE_TYPE_PARAMETER, fileType);
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
		addParameter(FILE_CONTENT_PARAMETER, Base64.encodeToString(fileContent, Base64.URL_SAFE));
	}

	public void setFileContent(String fileContentString) {
		setFileContent(Base64.decode(fileContentString, Base64.URL_SAFE));
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		addParameter(TEXT_PARAMETER, text);
	}
}
