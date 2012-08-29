package fr.dz.sherizi.service.share;

import java.util.UUID;

import android.bluetooth.BluetoothSocket;
import fr.dz.sherizi.common.exception.SheriziException;
import fr.dz.sherizi.common.message.Message;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.service.bluetooth.BluetoothServerListener;
import fr.dz.sherizi.service.bluetooth.BluetoothService;


/**
 * Bluetooth share manager
 */
public class BluetoothShareManager extends ShareManager {

	// Constants
	public static final String SERVER_PARAMETER = "server";
	public static final String SERVICE_NAME = "Sherizi Share";
	public static final String SERVICE_UUID = new UUID(85321578123648764l, 1256874535484594l).toString();

	// True if we must disable bluetooth on release
	private boolean mustDisable;

	/**
	 * Constructor
	 */
	public BluetoothShareManager() {

	}

	@Override
	protected void prepare(final SheriziActionListener listener) {
		this.mustDisable = BluetoothService.getInstance().enableBluetooth(new SheriziActionListener() {
			@Override
			public void onSuccess() {
				listener.onInfo("Bluetooth enabled");
				listener.onSuccess();
			}

			@Override
			public void onError(Throwable t) {
				listener.onError(t);
			}
		});
	}

	@Override
	protected void waitAndShare(final SheriziActionListener listener) {
		// Starts the server
		BluetoothService.getInstance().startServer(SERVICE_NAME, SERVICE_UUID, new BluetoothServerListener() {

			// When a client is connected, we send it datas and close the connection
			public void onAccept(BluetoothSocket clientSocket) {
				boolean sent = false;
				try {
					try {
						sendDatas(clientSocket.getOutputStream());
						sent = true;
					} catch (Throwable t) {
						onError(new SheriziException("Error while sending datas : "+t.getMessage()));
					}
					clientSocket.close();
					if ( sent ) {
						listener.onInfo("Datas sent");
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
					listener.onInfo("Bluetooth disabled");
					listener.onSuccess();
				}

				@Override
				public void onError(Throwable t) {
					listener.onError(t);
				}
			});
		}
	}

	@Override
	protected Message getTransferInformations() {
		Message message = new Message();
		message.addParameter(SERVER_PARAMETER, BluetoothService.getInstance().getAddress());
		return message;
	}

	@Override
	protected void connectAndReceive(Message transferInformations, SheriziActionListener listener) {
		// TODO Connect to the server and receive the datas
	}
}
