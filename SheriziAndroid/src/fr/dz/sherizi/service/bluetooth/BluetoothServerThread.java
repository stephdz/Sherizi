package fr.dz.sherizi.service.bluetooth;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import fr.dz.sherizi.common.exception.SheriziException;


/**
 * Thread waiting for the bluetooth client connexions
 */
public class BluetoothServerThread extends Thread {

	private BluetoothServerSocket serverSocket;
	private BluetoothServerListener listener;
	private Boolean ended;

	/**
	 * Constructor
	 * @param adapter
	 * @param listener
	 */
	public BluetoothServerThread(BluetoothAdapter adapter, BluetoothServerListener listener, String serviceName, UUID serviceUUID) {
		super();
		try {
			this.serverSocket = adapter.listenUsingRfcommWithServiceRecord(serviceName, serviceUUID);
		} catch ( Throwable t ) {
			listener.onError(new SheriziException("Unable to start bluetooth server : "+t.getMessage()));
		}
		this.listener = listener;
		this.ended = Boolean.FALSE;
	}

	@Override
	public void run() {
		// Waits for connexions
        while ( ! isEnded() ) {
            try {
            	BluetoothSocket clientSocket = serverSocket.accept();
            	if (clientSocket != null) {
            		// If there is a connexion, we notify the listener
                    listener.onAccept(clientSocket);
                }
            } catch (Throwable t) {
            	if ( ! isEnded() ) {
	                listener.onError(new SheriziException("Error while waiting for bluetooth client : "+t.getMessage()));
	                end();
            	}
            }
        }
	}

	/**
	 * Ends the server thread
	 */
	public void end() {
		synchronized(this.ended) {
			this.ended = true;
			try {
				serverSocket.close();
			} catch (Throwable t) {
				listener.onError(new SheriziException("Error while ending bluetooth server : "+t.getMessage()));
			}
		}
	}

	/**
	 * Returns the waiting state of the thread
	 * @return
	 */
	protected boolean isEnded() {
		synchronized(this.ended) {
			return this.ended;
		}
	}
}
