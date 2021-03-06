package fr.dz.sherizi.service.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.appspot.api.services.sherizi.model.User;

import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.common.message.Message;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.service.contact.ContactService;
import fr.dz.sherizi.service.server.SheriziServerService;


/**
 * Base class for sharing
 */
public abstract class ShareManager {

	// Constants
	public static final String DEFAULT_SHARE_MANAGER = BluetoothShareManager.class.getSimpleName();
	public static final int BUFFER_SIZE = 1024;

	// Share managers
	public static Map<String,Class<?>> managers = initShareManagers();

	// Context
	private Context context;

	// Share informations
	private SharedData data;
	private User user;
	private TransferInformations transferInformations;

	// State
	private boolean shareInProgess;

	/**
	 * Default constructor
	 */
	public ShareManager() {
		this(null);
	}

	/**
	 * Constructor
	 * @param listener
	 */
	public ShareManager(Context context) {
		this.context = context;
		this.shareInProgess = false;
	}

	/**
	 * Shares datas with given contact
	 * @param data
	 * @param contact
	 */
	public void share(SharedData data, User user, final SheriziActionListener listener) {
		if ( ! isShareInProgress() ) {
			this.shareInProgess = true;
			this.data = data;
			this.user = user;
			doPrepareServer(listener);
		} else {
			listener.onError(new SheriziException("Share is already in progress on this share manager"));
		}
	}

	/**
	 * Receive datats connecting through the given transferInformations
	 * @param data
	 * @param contact
	 */
	public void receive(final TransferInformations transferInformations, final SheriziActionListener listener) {
		if ( ! isShareInProgress() ) {
			this.shareInProgess = true;
			setTransferInformations(transferInformations);
			doPrepareClient(listener);
		} else {
			listener.onError(new SheriziException("Share is already in progress on this share manager"));
		}
	}

	/**
	 * Returns the default ShareManager (bluetooth for now)
	 * @param context
	 * @param listener
	 * @return
	 */
	public static ShareManager getDefaultShareManager(Context context) {
		return getShareManager(DEFAULT_SHARE_MANAGER, context);
	}

	/**
	 * Returns a ShareManager by its name
	 * @param name
	 * @param context
	 * @param listener
	 * @return
	 */
	public static ShareManager getShareManager(String name, Context context) {
		ShareManager manager = null;
		if ( managers.containsKey(name) ) {
			try {
				manager = (ShareManager) managers.get(name).newInstance();
				manager.setContext(context);
			} catch (Throwable t) {
				manager = null;
			}
		}
		return manager;
	}

	/**
	 * Prepares sharing
	 * @param listener
	 */
	protected void doPrepareServer(final SheriziActionListener listener) {
		prepare(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				startWaiting(listener);
			}
			@Override
			public void onError(Throwable t) {
				ShareManager.this.onError(listener, t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
			@Override
			public void onProgress(Integer percentage) {
				listener.onProgress(percentage);
			}
		});
	}

	/**
	 * Prepares sharing
	 * @param listener
	 */
	protected void doPrepareClient(final SheriziActionListener listener) {
		prepare(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				doConnectAndReceive(listener);
			}
			@Override
			public void onError(Throwable t) {
				ShareManager.this.onError(listener, t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
			@Override
			public void onProgress(Integer percentage) {
				listener.onProgress(percentage);
			}
		});
	}

	/**
	 * Does the transfer on the client side
	 * @param listener
	 */
	protected void doConnectAndReceive(final SheriziActionListener listener) {
		connectAndReceive(getTransferInformations(), new SheriziActionListener() {
			@Override
			public void onSuccess() {
				release(listener);
			}
			@Override
			public void onError(Throwable t) {
				ShareManager.this.onError(listener, t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
			@Override
			public void onProgress(Integer percentage) {
				listener.onProgress(percentage);
			}
		});
	}

	/**
	 * On prepare success, we initiate the transfer and start waiting
	 * @param listener
	 */
	protected void startWaiting(final SheriziActionListener listener) {
		try {
			listener.onInfo("Initiating transfer");

			// Adds the manager used to the transfer informations
			TransferInformations transferInformations = getTransferInformationsForClient();
			transferInformations.setShareManagerId(getClass().getSimpleName());
			transferInformations.setContentSize(getData().getMessageSize());
			setTransferInformations(transferInformations);

			// Initiate the transfer
			SheriziServerService.getInstance().initiateTransfer(
				ContactService.getInstance().getConnectedUserEmailsCSV(context),
				user.getEmail(),
				user.getDeviceName(),
				transferInformations.toString()).execute();
			doWait(listener);
		} catch (IOException e) {
			onError(listener, e);
		}
	}

	/**
	 * Waits for sharing done
	 * @param listener
	 */
	protected void doWait(final SheriziActionListener listener) {
		listener.onInfo("Waiting for connection");
		waitAndShare(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				release(listener);
			}
			@Override
			public void onError(Throwable t) {
				ShareManager.this.onError(listener, t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
			@Override
			public void onProgress(Integer percentage) {
				listener.onProgress(percentage);
			}
		});
	}

	/**
	 * On error, we release and pass the error to the listener
	 * @param t
	 */
	protected void onError(final SheriziActionListener listener, Throwable t) {
		listener.onError(t);
		release(new SheriziActionListener() {
			@Override
			public void onSuccess(){}
			@Override
			public void onError(Throwable t) {
				listener.onError(t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
			@Override
			public void onProgress(Integer percentage) {
				listener.onProgress(percentage);
			}
		});
	}

	/**
	 * Initializes the share managers list
	 * @return
	 */
	protected static Map<String,Class<?>> initShareManagers() {
		Map<String, Class<?>> result = new HashMap<String, Class<?>>();
		result.put(BluetoothShareManager.class.getSimpleName(), BluetoothShareManager.class);
		return result;
	}

	/**
	 * Sends the datas via an output stream
	 * /!\ This method does not close the stream
	 * TODO Give progress feedback
	 * @param outputStream
	 */
	protected void sendDatas(OutputStream outputStream, SheriziActionListener listener) throws SheriziException {
		try {
			listener.onInfo("Sending datas");
			String datas = getData().toString();
			byte[] bytes = datas.getBytes(Message.DEFAULT_ENCODING);
			int offset = 0;
			while ( offset < bytes.length ) {
				outputStream.write(bytes, offset, Math.min(BUFFER_SIZE, bytes.length-offset));
				offset += BUFFER_SIZE;
				listener.onProgress(offset * 100 / bytes.length);
			}
			listener.onInfo("Waiting the end of transfer");
		} catch (Throwable t) {
			throw new SheriziException("Error while sending datas", t);
		}
	}

	/**
	 * Reads the datas via an input stream
	 * /!\ This method does not close the stream
	 * TODO Give progress feedback
	 * @param inputStream
	 */
	protected void readDatas(InputStream inputStream, SheriziActionListener listener) throws SheriziException {
		try {
			listener.onInfo("Receiving datas");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			long totalRead = 0;
			long toBeRead = Long.valueOf(getTransferInformations().getContentSize());
			int read;
			while ( totalRead < toBeRead && (read = inputStream.read(buffer)) != -1 ) {
				baos.write(buffer, 0, read);
				totalRead += read;
				listener.onProgress((int)(totalRead * 100 / toBeRead));
			}
			this.data = SharedData.valueOf(new String(baos.toByteArray(), Message.DEFAULT_ENCODING));
			baos.close();
		} catch (Throwable t) {
			throw new SheriziException("Error while reading datas", t);
		}
	}

	/**
	 * Share initialization
	 * @param listener Listener methods must be called in order to achieve sharing
	 */
	protected abstract void prepare(SheriziActionListener listener);

	/**
	 * Wait for sharing. When sharing is done, must call listener.onSuccess
	 * @param listener Listener methods must be called in order to achieve sharing
	 */
	protected abstract void waitAndShare(SheriziActionListener listener);

	/**
	 * Share post job (release resources or restore initial state, for exemple)
	 * @param listener Listener methods must be called in order to achieve sharing
	 */
	protected abstract void release(SheriziActionListener listener);

	/**
	 * Returns share specific informations to provide from the server to the client to initiate the transfer
	 * @return
	 */
	protected abstract TransferInformations getTransferInformationsForClient();

	/**
	 * Does the client side transfer : must connect to the server with the given transfer informations and read through an InputStream via readDatas()
	 * @param listener Listener methods must be called in order to achieve sharing
	 */
	protected abstract void connectAndReceive(TransferInformations transferInformations, SheriziActionListener listener);


	/*
	 * GETTERS & SETTERS
	 */

	public Context getContext() {
		return context;
	}

	private void setContext(Context context) {
		this.context = context;
	}

	public boolean isShareInProgress() {
		return shareInProgess;
	}

	public SharedData getData() {
		return data;
	}

	public User getUser() {
		return user;
	}

	public TransferInformations getTransferInformations() {
		return transferInformations;
	}

	private void setTransferInformations(TransferInformations transferInformations) {
		this.transferInformations = transferInformations;
	}
}
