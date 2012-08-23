package fr.dz.sherizi.service.bluetooth;


/**
 * Listener for bluetooth activated event
 */
public interface BluetoothEnabledListener {
	public void onBluetoothEnabled();
	public void onError(Throwable t);
}
