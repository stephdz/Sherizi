package fr.dz.sherizi.service.share;

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
	public static final String SHARE_MANAGER_PARAMETER = "shareManager";

	// Share managers
	public static Map<String,Class<?>> managers = initShareManagers();

	// Context
	private Context context;

	// Listener
	private SheriziActionListener listener;

	// Share informations
	private SharedData<?> data;
	private User user;

	// State
	private boolean shareInProgess;

	/**
	 * Default constructor
	 */
	public ShareManager() {
		this(null, null);
	}

	/**
	 * Constructor
	 * @param listener
	 */
	public ShareManager(Context context, SheriziActionListener listener) {
		this.context = context;
		this.listener = listener;
		this.shareInProgess = false;
	}

	/**
	 * Shares datas with given contact
	 * @param data
	 * @param contact
	 */
	public void share(SharedData<?> data, User user) {
		if ( ! isShareInProgress() ) {
			this.shareInProgess = true;
			this.data = data;
			this.user = user;
			doPrepare();
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
	public static ShareManager getDefaultShareManager(Context context, SheriziActionListener listener) {
		return getShareManager(DEFAULT_SHARE_MANAGER, context, listener);
	}

	/**
	 * Returns a ShareManager by its name
	 * @param name
	 * @param context
	 * @param listener
	 * @return
	 */
	public static ShareManager getShareManager(String name, Context context, SheriziActionListener listener) {
		ShareManager manager = null;
		if ( managers.containsKey(name) ) {
			try {
				manager = (ShareManager) managers.get(name).newInstance();
				manager.setContext(context);
				manager.setListener(listener);
			} catch (Throwable t) {
				manager = null;
			}
		}
		return manager;
	}

	/**
	 * Prepares sharing
	 * @param data
	 * @param contact
	 */
	protected void doPrepare() {
		prepare(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				onPrepareSuccess();
			}
			@Override
			public void onError(Throwable t) {
				onError(t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
		});
	}

	/**
	 * On prepare success, we initiate the transfer and start waiting
	 */
	protected void onPrepareSuccess() {
		try {
			// Adds the manager used to the transfer informations
			Message transferInformations = getTransferInformations();
			transferInformations.addParameter(SHARE_MANAGER_PARAMETER, getClass().getSimpleName());

			// Initiate the transfer
			SheriziServerService.getInstance().initiateTransfer(
				ContactService.getInstance().getConnectedUserEmailsCSV(context),
				user.getEmail(),
				user.getDeviceName(),
				transferInformations.toString());
			doWait();
		} catch (IOException e) {
			onError(e);
		}
	}

	/**
	 * Waits for sharing done
	 */
	protected void doWait() {
		waitAndShare(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				doRelease();
			}
			@Override
			public void onError(Throwable t) {
				onError(t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
		});
	}

	/**
	 * Releases after sharing is done
	 */
	protected void doRelease() {
		release(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				listener.onSuccess();
			}
			@Override
			public void onError(Throwable t) {
				listener.onError(t);
			}
			@Override
			public void onInfo(Object information) {
				listener.onInfo(information);
			}
		});
	}

	/**
	 * On error, we release and pass the error to the listener
	 * @param t
	 */
	protected void onError(Throwable t) {
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
	 * TODO Sends the datas via an output stream
	 * @param outputStream
	 */
	protected void sendDatas(OutputStream outputStream) {

	}

	/**
	 * TODO Reads the datas via an input stream
	 * @param inputStream
	 */
	protected void readDatas(InputStream inputStream) {

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
	protected abstract Message getTransferInformations();

	/**
	 * Does the client side transfer : must connect to the server with the given transfer informations and read through an InputStream via readDatas()
	 * @param listener Listener methods must be called in order to achieve sharing
	 */
	protected abstract void connectAndReceive(Message transferInformations, SheriziActionListener listener);

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

	public SharedData<?> getData() {
		return data;
	}

	public User getUser() {
		return user;
	}

	private void setListener(SheriziActionListener listener) {
		this.listener = listener;
	}
}
