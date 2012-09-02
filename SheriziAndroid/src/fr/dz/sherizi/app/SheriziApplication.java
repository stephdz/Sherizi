package fr.dz.sherizi.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import fr.dz.sherizi.R;
import fr.dz.sherizi.gui.IncomingTransferActivity;
import fr.dz.sherizi.gui.task.SheriziFriendsTask;
import fr.dz.sherizi.push.PushService;
import fr.dz.sherizi.service.contact.Contact;
import fr.dz.sherizi.service.share.TransferInformations;
import fr.dz.sherizi.utils.Utils;

public class SheriziApplication extends Application {

	// Contacts list and it's handler
	private List<Contact> friends = null;
	private Handler friendsLoadedHandler = null;

	// Awaiting transfers
	private Map<Integer,TransferInformations> waitingTransfers = new HashMap<Integer,TransferInformations>();
	private Integer nextTransferId = 1;

	@Override
	public void onCreate() {
		super.onCreate();

		// GCM Registration
		PushService.register(this);
	}

	/**
	 * Updates friends list
	 */
	public void updateFriends() {
		new SheriziFriendsTask().execute(this);
	}

	/**
	 * Adds a new awaiting transfer
	 * @param transfer
	 */
	public synchronized Integer addWaitingTransfer(TransferInformations transfer) {

		// Add the transfer to the map
		waitingTransfers.put(nextTransferId, transfer);

		// Show a notification
		Utils.showNotification(this, getString(R.string.accept_transfer), IncomingTransferActivity.class, nextTransferId);

		// Next
		return nextTransferId++;
	}

	/**
	 * Gets the transfer informations and removes it from the map
	 * TODO Also remove the transfer when notification is closed
	 * @param transferId
	 */
	public TransferInformations getWaitingTransfer(Integer transferId) {
		TransferInformations result = waitingTransfers.get(transferId);
		waitingTransfers.remove(transferId);
		return result;
	}

	/**
	 * Fires the friends loaded event
	 */
	protected void fireFriendsLoaded() {
		if ( getFriendsLoadedHandler() != null ) {
	    	Message msg = new Message();
	    	msg.obj = friends;
	    	getFriendsLoadedHandler().sendMessage(msg);
		}
	}

	/*
	 * GETTERS & SETTERS
	 */

	public List<Contact> getFriends() {
		return friends;
	}

	public void setFriends(List<Contact> friends) {
		this.friends = friends;
		fireFriendsLoaded();
	}

	public Handler getFriendsLoadedHandler() {
		return friendsLoadedHandler;
	}

	public void setFriendsLoadedHandler(Handler friendsLoadedHandler) {
		this.friendsLoadedHandler = friendsLoadedHandler;
		fireFriendsLoaded();
	}
}
