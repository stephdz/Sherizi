package fr.dz.sherizi.service.bluetooth;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.listener.SheriziActionListener;


/**
 * Service for contact management
 */
public class BluetoothService {

	// Contact Service instance
	private static final BluetoothService instance = new BluetoothService();

	/**
	 * Private constructor : it's a singleton
	 */
	private BluetoothService() {

	}

	/**
	 * @return the contact service instance
	 */
	public static BluetoothService getInstance() {
		return instance;
	}

	/**
	 * Activates the bluetooth and when it's activated, calls the SheriziActionListener
	 * @param listener
	 * @return true if it has really been enabled, false if bluetooth was already enabled
	 */
	public boolean enableBluetooth(final SheriziActionListener listener) {
		BluetoothAdapter manager = BluetoothAdapter.getDefaultAdapter();
		if ( manager != null ) {
			boolean bluetoothEnabled = manager.isEnabled();
			if ( ! bluetoothEnabled ) {
				manager.enable();
				BluetoothStateThread stateThread = new BluetoothStateThread(manager, new BluetoothStateListener() {
					public void onTimeout() {
						listener.onError(new SheriziException("Timeout during bluetooth activation"));
					}
					public void onBluetoothStateChange(int newState) {
						if ( newState == BluetoothAdapter.STATE_ON ) {
							listener.onSuccess();
						} else {
							listener.onError(new SheriziException("Bluetooth adapter in unexpected state"));
						}
					}
			    });
				stateThread.start();
			} else {
				listener.onSuccess();
			}
			return ! bluetoothEnabled;
		} else {
			listener.onError(new SheriziException("No bluetooth adapter available"));
			return false;
		}
	}

	/**
	 * Disables the bluetooth and when it's disabled, calls the SheriziActionListener
	 * @param listener
	 * @return true if it has really been disabled, false if bluetooth was already disabled
	 */
	public boolean disableBluetooth(final SheriziActionListener listener) {
		BluetoothAdapter manager = BluetoothAdapter.getDefaultAdapter();
		if ( manager != null ) {
			boolean bluetoothEnabled = manager.isEnabled();
			if ( bluetoothEnabled ) {
				manager.disable();
				BluetoothStateThread stateThread = new BluetoothStateThread(manager, new BluetoothStateListener() {
					public void onTimeout() {
						listener.onError(new SheriziException("Timeout during bluetooth unactivation"));
					}
					public void onBluetoothStateChange(int newState) {
						if ( newState == BluetoothAdapter.STATE_OFF ) {
							listener.onSuccess();
						} else {
							listener.onError(new SheriziException("Bluetooth adapter in unexpected state"));
						}
					}
			    });
				stateThread.start();
			} else {
				listener.onSuccess();
			}
			return bluetoothEnabled;
		} else {
			listener.onError(new SheriziException("No bluetooth adapter available"));
			return false;
		}
	}

	/**
	 * Disables the bluetooth and when it's disabled, calls the SheriziActionListener
	 * @param listener
	 * @return true if it has really been disabled, false if bluetooth was already disabled
	 */
	public boolean startServer(String serviceName, String serviceUUID, final BluetoothServerListener listener) {
		BluetoothAdapter manager = BluetoothAdapter.getDefaultAdapter();
		if ( manager != null ) {
			if ( manager.isEnabled() ) {
				manager.disable();
				BluetoothServerThread serverThread = new BluetoothServerThread(manager, listener, serviceName, UUID.fromString(serviceUUID));
				serverThread.start();
			} else {
				listener.onError(new SheriziException("Enable bluetooth before starting a server"));
				return false;
			}
			return true;
		} else {
			listener.onError(new SheriziException("No bluetooth adapter available"));
			return false;
		}
	}

	/**
	 * Returns the device bluetooth adress
	 * @return
	 */
	public String getAddress() {
		return BluetoothAdapter.getDefaultAdapter().getAddress();
	}
}
