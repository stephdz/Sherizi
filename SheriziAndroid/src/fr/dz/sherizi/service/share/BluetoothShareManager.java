package fr.dz.sherizi.service.share;

import android.bluetooth.BluetoothSocket;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.service.bluetooth.BluetoothClientListener;
import fr.dz.sherizi.service.bluetooth.BluetoothServerListener;
import fr.dz.sherizi.service.bluetooth.BluetoothService;


/**
 * Bluetooth share manager
 * TODO Localization
 */
public class BluetoothShareManager extends ShareManager {

	// Constants
	public static final String SERVER_PARAMETER = "server";
	public static final String SERVICE_NAME = "Sherizi Share";
	public static final String SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB";

	// True if we must disable bluetooth on release
	private boolean mustDisable;

	/**
	 * Constructor
	 */
	public BluetoothShareManager() {

	}

	@Override
	protected void prepare(final SheriziActionListener listener) {
		this.mustDisable = BluetoothService.getInstance().enableBluetooth(listener);
		if ( mustDisable ) {
			listener.onInfo("Enabling bluetooth");
		}
	}

	@Override
	protected void waitAndShare(final SheriziActionListener listener) {
		// Starts the server
		BluetoothService.getInstance().startServer(SERVICE_NAME, SERVICE_UUID, new BluetoothServerListener() {

			// When a client is connected, we send it datas and close the connection
			public void onAccept(BluetoothSocket clientSocket) {
				boolean sent = false;
				try {
					// Sends the datas
					try {
						sendDatas(clientSocket.getOutputStream(), listener);
						sent = true;
					} catch (Throwable t) {
						onError(new SheriziException("Error while sending datas", t));
					}

					// Waits for the "all is read" signal from the client
					clientSocket.getInputStream().read();

					// Closes the connection
					clientSocket.close();
					if ( sent ) {
						listener.onSuccess();
					}
				} catch (Throwable t) {
					onError(t);
				}
			}

			public void onError(Throwable t) {
				listener.onError(t);
			}
		});
	}

	@Override
	protected void release(final SheriziActionListener listener) {
		if ( mustDisable ) {
			BluetoothService.getInstance().disableBluetooth(new SheriziActionListener() {
				@Override
				public void onSuccess() {
					listener.onSuccess();
				}

				@Override
				public void onError(Throwable t) {
					listener.onError(t);
				}
			});
			listener.onInfo("Disabling bluetooth");
		} else {
			listener.onSuccess();
		}
	}

	@Override
	protected TransferInformations getTransferInformationsForClient() {
		TransferInformations result = new TransferInformations();
		result.setServer(BluetoothService.getInstance().getAddress());
		return result;
	}

	@Override
	protected void connectAndReceive(TransferInformations transferInformations, final SheriziActionListener listener) {
		String serverAddress = transferInformations.getServer();
		if ( serverAddress != null && ! serverAddress.isEmpty() ) {
			BluetoothService.getInstance().startClient(serverAddress, SERVICE_UUID, new BluetoothClientListener() {
				public void onConnect(BluetoothSocket socket) {
					try {
						// Reads the datas
						boolean received = false;
						try {
							readDatas(socket.getInputStream(), listener);
							received = true;
						} catch (Throwable t) {
							onError(new SheriziException("Error while receiving datas", t));
						}

						// Sends the "all is read" signal to the server
						socket.getOutputStream().write(1);

						// Closes the connection
						socket.close();
						if ( received ) {
							listener.onSuccess();
						}
					} catch (Throwable t ) {
						onError(t);
					}
				}

				public void onError(Throwable t) {
					listener.onError(t);
				}
			});
		} else {
			listener.onError(new SheriziException("Unable to get bluetooth server address"));
		}
	}
}
