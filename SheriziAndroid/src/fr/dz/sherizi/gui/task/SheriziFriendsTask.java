package fr.dz.sherizi.gui.task;

import java.util.List;

import android.os.AsyncTask;
import fr.dz.sherizi.gui.FriendsActivity;
import fr.dz.sherizi.service.contact.Contact;
import fr.dz.sherizi.service.contact.ContactService;

public class SheriziFriendsTask extends AsyncTask<FriendsActivity,Void,Void> {

	@Override
	protected Void doInBackground(FriendsActivity... params) {
		try {
			FriendsActivity activity = params[0];
			List<Contact> friends = ContactService.getInstance().getSheriziContacts(activity);
			activity.setFriendsList(friends);
		} catch (Throwable e) {
			// TODO Manage exceptions
			e.printStackTrace();
		}
		return null;
	}
}
