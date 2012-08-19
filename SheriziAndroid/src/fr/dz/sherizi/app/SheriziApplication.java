package fr.dz.sherizi.app;

import java.util.List;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import fr.dz.sherizi.gui.task.SheriziFriendsTask;
import fr.dz.sherizi.push.PushService;
import fr.dz.sherizi.service.contact.Contact;

public class SheriziApplication extends Application {

	// Contacts list and it's handler
	private List<Contact> friends = null;
	private Handler friendsLoadedHandler = null;

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
