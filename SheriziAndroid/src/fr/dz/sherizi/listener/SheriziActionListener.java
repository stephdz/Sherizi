package fr.dz.sherizi.listener;

/**
 * Interface for action listener
 */
public abstract class SheriziActionListener {

	/**
	 * Called on operation success
	 */
	public abstract void onSuccess();

	/**
	 * Called on error
	 * @param t
	 */
	public abstract void onError(Throwable t);

	/**
	 * Called to provide status information to caller
	 * @param information
	 */
	public void onInfo(Object information) {

	}

	/**
	 * Called when action progress changes
	 * @param percentage
	 */
	public void onProgress(Integer percentage) {

	}
}