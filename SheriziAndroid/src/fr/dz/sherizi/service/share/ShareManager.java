package fr.dz.sherizi.service.share;

import android.content.Context;

import com.appspot.api.services.sherizi.model.User;

import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.listener.SheriziActionListener;


/**
 * Base class for sharing
 */
public abstract class ShareManager {

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
		return new BluetoothShareManager(context, listener);
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
		/*try {*/
			doWait();
			// TODO Initiate transfer
			/*
			SheriziServerService.getInstance().initiateTransfer(
				ContactService.getInstance().getConnectedUserEmailsCSV(context),
				user.getEmail(),
				user.getDeviceName(),
				"");

		} catch (IOException e) {
			onError(e);
		}*/
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

	/*
	 * GETTERS & SETTERS
	 */

	public Context getContext() {
		return context;
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
}
