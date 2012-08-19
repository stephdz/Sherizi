package fr.dz.sherizi.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import fr.dz.sherizi.R;
import fr.dz.sherizi.gui.HomeActivity;

/**
 * Common utilities.
 */
public class Utils {

	// The current google account
	private static String googleAccount = null;

	// Constants
	public static final String GOOGLE_ACCOUNT_TYPE = "com.google";

	/**
	 * Shows an alert dialog with the given message.
	 * @param context context
	 * @param message message to show or {@code null} for none
	 */
	public static void showMessage(final Context context, String message) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Definition de la redirection au moment du clique sur la notification.
		// Dans notre cas la notification redirige vers notre application
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	new Intent(context, HomeActivity.class), 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		builder.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.sherizi)
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.sherizi))
				.setTicker(message)
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setContentTitle(context.getResources().getString(R.string.app_name))
				.setContentText(message);
		Notification notification = builder.getNotification();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		// Notification
		notificationManager.notify(1, notification);
	}

	/**
	 * Gets the main google account of the device
	 * @param context
	 * @return
	 */
	public static String getGoogleAccount(Context context) {
		if ( googleAccount == null ) {
			Account[] accounts = AccountManager.get(context).getAccounts();
			for (Account account : accounts) {
			    if ( GOOGLE_ACCOUNT_TYPE.equals(account.type) ) {
			    	googleAccount = account.name;
			    	break;
			    }
			}
		}
		return googleAccount;
	}

	/**
	 * Gets the device name
	 * @param context
	 * @return
	 */
	public static String getDeviceName(Context context) {
		return Build.MODEL;
	}
}
