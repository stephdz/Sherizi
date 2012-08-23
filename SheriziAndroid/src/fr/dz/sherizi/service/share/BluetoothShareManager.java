package fr.dz.sherizi.service.share;

import android.content.Context;
import fr.dz.sherizi.listener.SheriziActionListener;
import fr.dz.sherizi.service.bluetooth.BluetoothService;


/**
 * Bluetooth share manager
 */
public class BluetoothShareManager extends ShareManager {

	// True if we must disable bluetooth on release
	private boolean mustDisable;

	/**
	 * Constructor
	 * @param context
	 * @param listener
	 */
	public BluetoothShareManager(Context context, SheriziActionListener listener) {
		super(context, listener);
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
	protected void waitAndShare(SheriziActionListener listener) {
		// TODO Do sharing job
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			listener.onError(e);
		}
		listener.onInfo("Share done");
		listener.onSuccess();
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
}
