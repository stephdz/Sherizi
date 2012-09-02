package fr.dz.sherizi.gui;

import android.app.Activity;
import android.os.Bundle;
import fr.dz.sherizi.app.SheriziApplication;
import fr.dz.sherizi.utils.Utils;

/**
 * Utility Activity class for Sherizi
 */
public abstract class SheriziActivity extends Activity {

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
				Utils.makeToast(SheriziActivity.this, text);
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
