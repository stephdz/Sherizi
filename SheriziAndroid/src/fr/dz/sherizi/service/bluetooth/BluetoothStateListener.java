package fr.dz.sherizi.service.bluetooth;


/**
 * Listener for bluetooth state change event
 */
public interface BluetoothStateListener {
	void onTimeout();
	void onBluetoothStateChange(int newState);
}
