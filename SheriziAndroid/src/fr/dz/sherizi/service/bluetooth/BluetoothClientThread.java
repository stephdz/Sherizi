package fr.dz.sherizi.service.bluetooth;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import fr.dz.sherizi.common.exception.SheriziException;


/**
 * Thread connecting to bluetooth server
 */
public class BluetoothClientThread extends Thread {

	private BluetoothSocket socket;
	private BluetoothClientListener listener;

	/**
	 * Constructor
	 * @param adapter
	 * @param listener
	 */
	public BluetoothClientThread(BluetoothAdapter adapter, BluetoothClientListener listener, BluetoothDevice server, UUID serviceUUID) {
		super();
		try {
			this.socket = server.createRfcommSocketToServiceRecord(serviceUUID);
		} catch ( Throwable t ) {
			listener.onError(new SheriziException("Unable to start bluetooth client", t));
		}
		this.listener = listener;
	}

	@Override
	public void run() {
		try {
            socket.connect();
            listener.onConnect(socket);
        } catch (Throwable t) {
        	listener.onError(new SheriziException("Error while connecting to bluetooth server", t));
            try {
            	socket.close();
            } catch (Throwable th) {}
        }
	}
}
