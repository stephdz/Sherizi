package fr.dz.sherizi.service.bluetooth;

import android.bluetooth.BluetoothSocket;

/**
 * Action listener interface for bluetooth client thread
 */
public interface BluetoothClientListener {

	/**
	 * Called after connection established to the bluetooth server
	 * @param socket
	 */
	public void onConnect(BluetoothSocket socket);

	/**
	 * Called on errors
	 * @param t
	 */
	public void onError(Throwable t);
}
