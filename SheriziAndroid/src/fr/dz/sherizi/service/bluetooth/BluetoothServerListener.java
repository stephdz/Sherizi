package fr.dz.sherizi.service.bluetooth;

import android.bluetooth.BluetoothSocket;

/**
 * Action listener interface for bluetooth server thread
 */
public interface BluetoothServerListener {
	/**
	 * Called when a client is connected
	 * @param clientSocket
	 */
	public void onAccept(BluetoothSocket clientSocket);

	/**
	 * Called when an error occurs
	 * @param t
	 */
	public void onError(Throwable t);
}
