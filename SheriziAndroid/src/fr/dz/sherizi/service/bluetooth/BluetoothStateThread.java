package fr.dz.sherizi.service.bluetooth;

import android.bluetooth.BluetoothAdapter;


/**
 * Thread waiting for the bluetooth adapter state to change
 */
public class BluetoothStateThread extends Thread {

	// Constants
	private static final long STATE_READ_INTERVAL = 100;
	private static final long TIMEOUT = 10000;

	private BluetoothAdapter adapter;
	private BluetoothStateListener listener;
	private int initialState;

	/**
	 * Constructor
	 * @param adapter
	 * @param listener
	 */
	public BluetoothStateThread(BluetoothAdapter adapter, BluetoothStateListener listener) {
		super();
		this.adapter = adapter;
		this.listener = listener;
		this.initialState = adapter.getState();
	}

	@Override
	public void run() {
		boolean breaked = false;
		boolean timeout = false;
		int currentState = adapter.getState();
		long startTime = System.currentTimeMillis();
		while ( currentState == initialState ) {
			try {
				sleep(STATE_READ_INTERVAL);
			} catch (InterruptedException e) {
				breaked = true;
				break;
			}
			currentState = adapter.getState();
			if ( System.currentTimeMillis() - startTime > TIMEOUT ) {
				timeout = true;
				break;
			}
		}
		if ( ! breaked ) {
			if ( timeout ) {
				listener.onTimeout();
			} else {
				listener.onBluetoothStateChange(currentState);
			}
		}
	}
}
