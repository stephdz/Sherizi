package fr.dz.sherizi.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import fr.dz.sherizi.app.SheriziApplication;

/**
 * Utility Activity class for Sherizi
 */
public abstract class SheriziActivity extends Activity {

	// Toast handler
	private Handler toastHandler = new Handler() {
		@Override
        public void handleMessage(Message msg) {

        }
	};

	// Application
	private SheriziApplication application;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialization
        application = (SheriziApplication) getApplication();
    }

	/**
	 * Displays a toast from any thread
	 */
	public void makeToast(final String text) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(SheriziActivity.this, text, Toast.LENGTH_LONG).show();
			}
		});
	}

	/*
	 * GETTERS & SETTERS
	 */

	public SheriziApplication getSheriziApplication() {
		return application;
	}
}
