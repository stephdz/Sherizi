package fr.dz.sherizi.gui.task;

import java.util.List;

import android.os.AsyncTask;
import fr.dz.sherizi.app.SheriziApplication;
import fr.dz.sherizi.service.contact.Contact;
import fr.dz.sherizi.service.contact.ContactService;

public class SheriziFriendsTask extends AsyncTask<SheriziApplication,Void,Void> {

	@Override
	protected Void doInBackground(SheriziApplication... params) {
		try {
			SheriziApplication application = params[0];
			List<Contact> friends = ContactService.getInstance().getSheriziContacts(application);
			application.setFriends(friends);
		} catch (Throwable e) {
			// TODO Manage exceptions
			e.printStackTrace();
		}
		return null;
	}
}
