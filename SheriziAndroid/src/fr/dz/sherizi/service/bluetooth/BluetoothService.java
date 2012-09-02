package fr.dz.sherizi.service.bluetooth;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.listener.SheriziActionListener;


/**
 * Service for contact management
 */
public class BluetoothService {

	// Contact Service instance
	private static final BluetoothService instance = new BluetoothService();

	// The bluetooth default adapter must be retrived from the main thread...
	private BluetoothAdapter manager;

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
	 * Starts a bluetooth server with the given parameters
	 * @param serviceName
	 * @param serviceUUID
	 * @param listener
	 * @return true if it has really started, false if not
	 */
	public boolean startServer(String serviceName, String serviceUUID, final BluetoothServerListener listener) {
		if ( manager != null ) {
			if ( manager.isEnabled() ) {
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
	 * Starts a bluetooth client with the given parameters
	 * @param serverAddress
	 * @param serviceUUID
	 * @param listener
	 * @return true if it has really started, false if not
	 */
	public boolean startClient(String serverAddress, String serviceUUID, final BluetoothClientListener listener) {
		BluetoothDevice server = manager != null ? manager.getRemoteDevice(serverAddress) : null;
		if ( manager == null ) {
			listener.onError(new SheriziException("No bluetooth adapter available"));
			return false;
		} else if ( server == null ) {
			listener.onError(new SheriziException("Unknown bluetooth server address : "+serverAddress));
			return false;
		} else {
			if ( manager.isEnabled() ) {
				BluetoothClientThread clientThread = new BluetoothClientThread(manager, listener, server, UUID.fromString(serviceUUID));
				clientThread.start();
			} else {
				listener.onError(new SheriziException("Enable bluetooth before starting a client"));
				return false;
			}
			return true;
		}
	}

	/**
	 * Returns the device bluetooth adress
	 * @return
	 */
	public String getAddress() {
		return manager.getAddress();
	}

	/**
	 * Initializes the default adapter
	 * @return
	 */
	public void initDefaultAdapter() {
		this.manager = BluetoothAdapter.getDefaultAdapter();
	}
}
